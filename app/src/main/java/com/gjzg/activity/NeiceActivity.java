package com.gjzg.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class NeiceActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private EditText pwdEt;
    private Button pwdBtn;
    private CProgressDialog cProgressDialog;
    private String pwd;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        cProgressDialog.dismiss();
                        Utils.toast(NeiceActivity.this, "密码不正确");
                        break;
                    case 1:
                        cProgressDialog.dismiss();
                        startActivity(new Intent(NeiceActivity.this, GuideActivity.class));
                        finish();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_neice, null);
        setContentView(rootView);
        initView();
        setListener();
    }

    private void initView() {
        pwdEt = (EditText) rootView.findViewById(R.id.et_neice_pwd);
        pwdBtn = (Button) rootView.findViewById(R.id.btn_neice_pwd);
        cProgressDialog = Utils.initProgressDialog(NeiceActivity.this, cProgressDialog);
    }

    private void setListener() {
        pwdEt.addTextChangedListener(textWatcher);
        pwdBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_neice_pwd:
                if (pwd == null || TextUtils.isEmpty(pwd)) {
                    Utils.toast(NeiceActivity.this, "请输入内测版本密码");
                } else {
                    cProgressDialog.show();
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
                                if (json == null || json.equals("null") || TextUtils.isEmpty(json)) {
                                } else {
                                    try {
                                        JSONObject beanObj = new JSONObject(json);
                                        if (beanObj.optInt("code") == 200) {
                                            String data = beanObj.optString("data");
                                            if (data == null || data.equals("null") || TextUtils.isEmpty(data)) {
                                            } else {
                                                if (data.equals(pwd)) {
                                                    handler.sendEmptyMessage(1);
                                                } else {
                                                    handler.sendEmptyMessage(0);
                                                }
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
                break;
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            pwd = s.toString();
        }
    };
}
