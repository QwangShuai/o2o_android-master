package com.gjzg.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gjzg.cache.LruJsonCache;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;

public class SetActivity extends AppCompatActivity {

    @BindView(R.id.rl_set_return)
    RelativeLayout rlSetReturn;
    @BindView(R.id.rl_set_cache_clear)
    RelativeLayout rlSetCacheClear;
    @BindView(R.id.rl_set_cache_message_tip)
    RelativeLayout rlSetCacheMessageTip;
    @BindView(R.id.rl_set_about_us)
    RelativeLayout rlSetAboutUs;
    @BindView(R.id.rl_set_quit)
    RelativeLayout rlSetQuit;

    private View rootView, cachePopView, quitPopView;
    private PopupWindow cachePop, quitPop;
    private LruJsonCache lruJsonCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_set, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        initPopView();
        initData();
    }

    @OnClick({R.id.rl_set_return, R.id.rl_set_cache_clear, R.id.rl_set_cache_message_tip, R.id.rl_set_about_us, R.id.rl_set_quit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_set_return:
                startActivity(new Intent(SetActivity.this,MainActivity.class));
                break;
            case R.id.rl_set_cache_clear:
                if (!cachePop.isShowing()) {
                    backgroundAlpha(0.5f);
                    cachePop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.rl_set_cache_message_tip:
                startActivity(new Intent(SetActivity.this, MessageTipActivity.class));
                break;
            case R.id.rl_set_about_us:
                startActivity(new Intent(SetActivity.this, AboutUsActivity.class));
                break;
            case R.id.rl_set_quit:
                if (!quitPop.isShowing()) {
                    backgroundAlpha(0.5f);
                    quitPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
        }
    }

    private void initPopView() {
        cachePopView = LayoutInflater.from(SetActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) cachePopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("是否清理缓存？");
        ((TextView) cachePopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) cachePopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("清理");
        cachePopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cachePop.isShowing()) {
                    cachePop.dismiss();
                }
            }
        });
        cachePopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cachePop.isShowing()) {
                    cachePop.dismiss();
                    lruJsonCache.clear();
                    Utils.toast(SetActivity.this, "缓存已清理");
                }
            }
        });
        cachePop = new PopupWindow(cachePopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        cachePop.setFocusable(true);
        cachePop.setTouchable(true);
        cachePop.setOutsideTouchable(true);
        cachePop.setBackgroundDrawable(new BitmapDrawable());
        cachePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        quitPopView = LayoutInflater.from(SetActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) quitPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("是否退出？");
        ((TextView) quitPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) quitPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确定");
        quitPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quitPop.isShowing()) {
                    quitPop.dismiss();
                }
            }
        });
        quitPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quitPop.isShowing()) {
                    quitPop.dismiss();
                    quit();
                }
            }
        });
        quitPop = new PopupWindow(quitPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        quitPop.setFocusable(true);
        quitPop.setTouchable(true);
        quitPop.setOutsideTouchable(true);
        quitPop.setBackgroundDrawable(new BitmapDrawable());
        quitPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    private void initData() {
        lruJsonCache = LruJsonCache.get(this);
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);
    }

    private void quit() {
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetConfig.userInfoEditUrl)
                .addParams("u_id", UserUtils.readUserData(SetActivity.this).getId())
                .addParams("u_online", "0")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Utils.toast(SetActivity.this, VarConfig.noNetTip);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.optInt("code") == 1) {
                                    UserUtils.clearUserData(SetActivity.this);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
