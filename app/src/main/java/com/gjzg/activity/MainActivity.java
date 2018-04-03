package com.gjzg.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.AppConfigBean;
import com.gjzg.config.ColorConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.fragment.DiscountFragment;
import com.gjzg.fragment.FirstPageFragment;
import com.gjzg.fragment.ManageFragment;
import com.gjzg.fragment.MineFragment;
import com.gjzg.singleton.SingleGson;
import com.gjzg.utils.Utils;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout fpRl, magRl, dcRl, meRl;
    private ImageView fpIv, magIv, dcIv, meIv;
    private TextView fpTv, magTv, dcTv, meTv;
    private FirstPageFragment fpFrag;
    private ManageFragment magFrag;
    private DiscountFragment dcFrag;
    private MineFragment meFrag;
    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;
    private int curPos, tarPos;

    private View lockPopView;
    private PopupWindow lockPop;
    private EditText lockEt;
    private Button lockBtn;
    private String lockPwd;
    private boolean correct = false;

    private View updatePopView;
    private PopupWindow updatePop;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        if (!lockPop.isShowing()) {
                            lockPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                        }
                        break;
                    case 2:
                        correct = true;
                        if (lockPop.isShowing()) {
                            lockPop.dismiss();
                        }
                        break;
                    case 3:
                        Utils.toast(MainActivity.this, "密码不正确");
                        break;
                    case 4:
                        if (!updatePop.isShowing()) {
                            Utils.dark(MainActivity.this);
                            updatePop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                        }
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData();
        getUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(1);
            handler.removeMessages(2);
            handler.removeMessages(3);
            handler.removeMessages(4);
            handler = null;
        }
        OkHttpUtils.getInstance().cancelTag(this);
    }

    private void initView() {
        initRootView();
        initPopView();
    }

    private void initRootView() {
        fpRl = (RelativeLayout) rootView.findViewById(R.id.rl_main_fp);
        magRl = (RelativeLayout) rootView.findViewById(R.id.rl_main_mag);
        dcRl = (RelativeLayout) rootView.findViewById(R.id.rl_main_dc);
        meRl = (RelativeLayout) rootView.findViewById(R.id.rl_main_me);
        fpIv = (ImageView) rootView.findViewById(R.id.iv_main_fp);
        magIv = (ImageView) rootView.findViewById(R.id.iv_main_mag);
        dcIv = (ImageView) rootView.findViewById(R.id.iv_main_dc);
        meIv = (ImageView) rootView.findViewById(R.id.iv_main_me);
        fpTv = (TextView) rootView.findViewById(R.id.tv_main_fp);
        magTv = (TextView) rootView.findViewById(R.id.tv_main_mag);
        dcTv = (TextView) rootView.findViewById(R.id.tv_main_dc);
        meTv = (TextView) rootView.findViewById(R.id.tv_main_me);
    }

    private void initPopView() {
        lockPopView = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_guide, null);
        lockEt = (EditText) lockPopView.findViewById(R.id.et_guide_pwd);
        lockEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lockPwd = s.toString();
            }
        });
        lockBtn = (Button) lockPopView.findViewById(R.id.btn_guide_pwd);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lockPwd == null || TextUtils.isEmpty(lockPwd)) {
                    Utils.toast(MainActivity.this, "请输入密码");
                } else {
                    String url = NetConfig.guidePwdUrl;
                    Request request = new Request.Builder().url(url).get().build();
                    OkHttpClient okHttpClient = new OkHttpClient();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String json = response.body().string();
                                try {
                                    JSONObject beanObj = new JSONObject(json);
                                    if (beanObj.optInt("code") == 200) {
                                        String data = beanObj.optString("data");
                                        if (data == null || TextUtils.isEmpty(data)) {
                                        } else {
                                            if (data.equals(lockPwd)) {
                                                handler.sendEmptyMessage(2);
                                            } else {
                                                handler.sendEmptyMessage(3);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });
        lockPop = new PopupWindow(lockPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        lockPop.setFocusable(true);
        lockPop.setTouchable(true);
        lockPop.setOutsideTouchable(true);
        lockPop.setBackgroundDrawable(new BitmapDrawable());
        lockPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (correct) {
                    lockEt.setText("");
                } else {
                    lockEt.setText("");
                    finish();
                }
            }
        });

        updatePopView = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) updatePopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("检测到有新版本，是否更新?");
        ((TextView) updatePopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) updatePopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        updatePopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updatePop.isShowing()) {
                    updatePop.dismiss();
                }
            }
        });
        updatePopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updatePop.isShowing()) {
                    updatePop.dismiss();
                    toBrowser();
                }
            }
        });
        updatePop = new PopupWindow(updatePopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        updatePop.setFocusable(true);
        updatePop.setTouchable(true);
        updatePop.setOutsideTouchable(true);
        updatePop.setBackgroundDrawable(new BitmapDrawable());
        updatePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.light(MainActivity.this);
            }
        });
    }

    private void toBrowser() {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(NetConfig.updateUrl);
            intent.setDataAndType(uri, "text/html");
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        fpFrag = new FirstPageFragment();
        magFrag = new ManageFragment();
        dcFrag = new DiscountFragment();
        meFrag = new MineFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(fpFrag);
        fragmentList.add(magFrag);
        fragmentList.add(dcFrag);
        fragmentList.add(meFrag);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_main_sit, fragmentList.get(curPos));
        transaction.commit();
    }

    private void setListener() {
        fpRl.setOnClickListener(this);
        magRl.setOnClickListener(this);
        dcRl.setOnClickListener(this);
        meRl.setOnClickListener(this);
    }

    private void loadData() {
        String url = NetConfig.lockUrl;
        Request request = new Request.Builder().url(url).get().build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject beanObj = new JSONObject(json);
                        if (beanObj.optInt("code") == 200) {
                            int data = beanObj.optInt("data");
                            switch (data) {
                                case 1:
                                    handler.sendEmptyMessageDelayed(1, 100);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getUpdate() {
        OkHttpUtils
                .get()
                .tag(this)
                .url(NetConfig.appConfigUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            AppConfigBean appConfigBean = SingleGson.getInstance().fromJson(response, AppConfigBean.class);
                            if (appConfigBean != null) {
                                if (appConfigBean.getData() != null) {
                                    if (appConfigBean.getData().getData() != null) {
                                        if (!TextUtils.isEmpty(appConfigBean.getData().getData().getAndroid_version())) {
                                            String version = getAppInfo();
                                            if (!TextUtils.isEmpty(version)) {
                                                if (!appConfigBean.getData().getData().getAndroid_version().equals(version)) {
                                                    handler.sendEmptyMessageDelayed(4, 2000);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            return versionName;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_main_fp:
                tarPos = 0;
                break;
            case R.id.rl_main_mag:
                tarPos = 1;
                break;
            case R.id.rl_main_dc:
                tarPos = 2;
                break;
            case R.id.rl_main_me:
                tarPos = 3;
                break;
        }
        changeBlock();
    }

    private void changeBlock() {
        if (tarPos != curPos) {
            changeButton();
            changeFrag();
            curPos = tarPos;
        }
    }

    private void changeFrag() {
        Fragment curFragment = fragmentList.get(curPos);
        Fragment tarFragment = fragmentList.get(tarPos);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(curFragment);
        if (tarFragment.isAdded()) {
            transaction.show(tarFragment);
        } else {
            transaction.add(R.id.ll_main_sit, tarFragment);
        }
        transaction.commit();
    }

    private void changeButton() {
        switch (curPos) {
            case 0:
                fpIv.setImageResource(R.mipmap.fp_default);
                fpTv.setTextColor(ColorConfig.blue_2681fc);
                break;
            case 1:
                magIv.setImageResource(R.mipmap.mag_default);
                magTv.setTextColor(ColorConfig.blue_2681fc);
                break;
            case 2:
                dcIv.setImageResource(R.mipmap.dc_default);
                dcTv.setTextColor(ColorConfig.blue_2681fc);
                break;
            case 3:
                meIv.setImageResource(R.mipmap.me_default);
                meTv.setTextColor(ColorConfig.blue_2681fc);
                break;
        }
        switch (tarPos) {
            case 0:
                fpIv.setImageResource(R.mipmap.fp_choosed);
                fpTv.setTextColor(ColorConfig.red_ff3e50);
                break;
            case 1:
                magIv.setImageResource(R.mipmap.mag_choosed);
                magTv.setTextColor(ColorConfig.red_ff3e50);
                break;
            case 2:
                dcIv.setImageResource(R.mipmap.dc_choosed);
                dcTv.setTextColor(ColorConfig.red_ff3e50);
                break;
            case 3:
                meIv.setImageResource(R.mipmap.me_choosed);
                meTv.setTextColor(ColorConfig.red_ff3e50);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (curPos == 2) {
                if (dcFrag != null) {
                    if (dcFrag.webView != null) {
                        if (dcFrag.webView.canGoBack()) {
                            dcFrag.webView.goBack();
                        } else {
                            moveTaskToBack(true);
                        }
                    } else {
                        moveTaskToBack(true);
                    }
                } else {
                    moveTaskToBack(true);
                }
            } else {
                moveTaskToBack(true);
            }
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}