package com.gjzg.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.AppConfigBean;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.singleton.SingleGson;
import com.gjzg.utils.Utils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends AppCompatActivity {

    @BindView(R.id.rl_about_us_return)
    RelativeLayout rlAboutUsReturn;
    @BindView(R.id.tv_about_us_official_website)
    TextView tvAboutUsOfficialWebsite;
    @BindView(R.id.tv_about_us_service_telephone)
    TextView tvAboutUsServiceTelephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @OnClick(R.id.rl_about_us_return)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_about_us_return:
                finish();
                break;
        }
    }

    private void loadData() {
        OkHttpUtils.get().tag(this).url(NetConfig.appConfigUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                Utils.toast(AboutUsActivity.this, VarConfig.noNetTip);
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    AppConfigBean appConfigBean = SingleGson.getInstance().fromJson(response, AppConfigBean.class);
                    if (appConfigBean != null) {
                        if (appConfigBean.getCode() == 1) {
                            if (appConfigBean.getData() != null) {
                                if (appConfigBean.getData().getData() != null) {
                                    if (!TextUtils.isEmpty(appConfigBean.getData().getData().getOfficial_website()))
                                        tvAboutUsOfficialWebsite.setText(appConfigBean.getData().getData().getOfficial_website());
                                    if (!TextUtils.isEmpty(appConfigBean.getData().getData().getService_telephone()))
                                        tvAboutUsServiceTelephone.setText(appConfigBean.getData().getData().getService_telephone());
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
