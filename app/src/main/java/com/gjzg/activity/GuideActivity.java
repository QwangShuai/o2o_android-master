package com.gjzg.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.gjzg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gjzg.config.NetConfig;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;

public class GuideActivity extends AppCompatActivity {

    private View rootView;
    private ViewPager viewPager;
    private View view1, view2, view3;
    private List<View> viewList = new ArrayList<>();

    private View popView;
    private PopupWindow pop;
    private EditText editText;
    private Button button;
    private boolean correct = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        Utils.toast(GuideActivity.this, "密码不正确");
                        break;
                    case 1:
                        correct = true;
                        if (pop.isShowing()) {
                            pop.dismiss();
                        }
                        break;
                    case 2:
                        if (!pop.isShowing()) {
                            pop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                        }
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(GuideActivity.this).inflate(R.layout.activity_guide, null);
        setContentView(rootView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(0);
            handler.removeMessages(1);
            handler.removeMessages(2);
            handler = null;
        }
    }

    private void initView() {
        initRootView();
        initPopView();
    }

    private void initRootView() {
        viewPager = (ViewPager) findViewById(R.id.vp_guide);
        view1 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.guide_1, null);
        view2 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.guide_2, null);
        view3 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.guide_3, null);
        viewList = new ArrayList<>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewPager.setAdapter(new ViewPagerAdatper(viewList));
    }

    private void initPopView() {
        popView = LayoutInflater.from(GuideActivity.this).inflate(R.layout.pop_guide, null);
        editText = (EditText) popView.findViewById(R.id.et_guide_pwd);
        button = (Button) popView.findViewById(R.id.btn_guide_pwd);
        pop = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        pop.setFocusable(true);
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (correct) {
                    editText.setText("");
                } else {
                    editText.setText("");
                    finish();
                }
            }
        });
    }

    private void setListener() {
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUtils.saveUse(GuideActivity.this);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Utils.toast(GuideActivity.this, "请输入密码");
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
                                if (json == null || TextUtils.isEmpty(json)) {
                                } else {
                                    try {
                                        JSONObject beanObj = new JSONObject(json);
                                        if (beanObj.optInt("code") == 200) {
                                            if (beanObj.optString("data").equals(editText.getText().toString())) {
                                                handler.sendEmptyMessage(1);
                                            } else {
                                                handler.sendEmptyMessage(0);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
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
                                case 2:
                                    handler.sendEmptyMessageDelayed(2, 100);
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

    public class ViewPagerAdatper extends PagerAdapter {
        private List<View> mViewList;

        public ViewPagerAdatper(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }
}
