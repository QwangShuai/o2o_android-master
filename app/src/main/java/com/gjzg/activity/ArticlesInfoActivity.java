package com.gjzg.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.gjzg.config.NetConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class ArticlesInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private TextView infoTv;
    private CProgressDialog cProgressDialog;
    private String a_id;
    private String a_desc;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        cProgressDialog.dismiss();
                        if (a_desc == null || a_desc.equals("null") || TextUtils.isEmpty(a_desc)) {
                        } else {
                            infoTv.setText(a_desc);
                        }
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(ArticlesInfoActivity.this).inflate(R.layout.activity_articles_info, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(1);
            handler = null;
        }
    }

    private void initView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_articles_info_return);
        infoTv = (TextView) rootView.findViewById(R.id.tv_articles_info);
        cProgressDialog = Utils.initProgressDialog(ArticlesInfoActivity.this, cProgressDialog);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            a_id = intent.getStringExtra("a_id");
        }
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_articles_info_return:
                finish();
                break;
        }
    }

    private void loadData() {
        if (a_id == null || a_id.equals("null") || TextUtils.isEmpty(a_id)) {
        } else {
            cProgressDialog.show();
            String url = NetConfig.articlesInfoUrl + a_id;
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
                            if (beanObj.optInt("code") == 1) {
                                JSONObject dataObj = beanObj.optJSONObject("data");
                                if (dataObj != null) {
                                    a_desc = dataObj.optString("a_desc");
                                    handler.sendEmptyMessage(1);
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
}
