package com.gjzg.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.gjzg.bean.ToChangePriceBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class ChangePriceActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl, submitRl;
    private TextView skillTv, workerNumTv, timeTv;
    private EditText amountEt;
    private CProgressDialog cProgressDialog;
    private ToChangePriceBean toChangePriceBean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        cProgressDialog.dismiss();
                        startActivity(new Intent(ChangePriceActivity.this, EmployerManageActivity.class));
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(ChangePriceActivity.this).inflate(R.layout.activity_change_price, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_change_price_return);
        submitRl = (RelativeLayout) rootView.findViewById(R.id.rl_change_price_submit);
        skillTv = (TextView) rootView.findViewById(R.id.tv_change_price_skill);
        workerNumTv = (TextView) rootView.findViewById(R.id.tv_change_price_worker_num);
        timeTv = (TextView) rootView.findViewById(R.id.tv_change_price_time);
        amountEt = (EditText) rootView.findViewById(R.id.et_change_price_amount);
        cProgressDialog = Utils.initProgressDialog(ChangePriceActivity.this, cProgressDialog);
    }

    private void initData() {
        toChangePriceBean = (ToChangePriceBean) getIntent().getSerializableExtra(IntentConfig.toChangePrice);
        skillTv.setText(toChangePriceBean.getSkill());
        workerNumTv.setText(toChangePriceBean.getWorker_num());
        amountEt.setText(toChangePriceBean.getAmount());
        amountEt.setSelection(amountEt.getText().length());
        timeTv.setText(toChangePriceBean.getStart_time() + "——" + toChangePriceBean.getEnd_time());
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        submitRl.setOnClickListener(this);
        amountEt.addTextChangedListener(amountTw);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_change_price_return:
                finish();
                break;
            case R.id.rl_change_price_submit:
                if (toChangePriceBean.getAmount().equals("0")) {
                    Utils.toast(ChangePriceActivity.this, "工资不能为0!");
                } else {
                    String url = NetConfig.orderUrl +
                            "?action=price" +
                            "&tew_id=" + toChangePriceBean.getTew_id() +
                            "&t_id=" + toChangePriceBean.getT_id() +
                            "&t_author=" + toChangePriceBean.getT_author() +
                            "&amount=" + toChangePriceBean.getAmount() +
                            "&worker_num=" + toChangePriceBean.getWorker_num() +
                            "&start_time=" + toChangePriceBean.getStart_time() +
                            "&end_time=" + toChangePriceBean.getEnd_time() +
                            "&o_worker=" + toChangePriceBean.getO_worker();
                    change(url);
                }
                break;
        }
    }

    TextWatcher amountTw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            toChangePriceBean.setAmount(s.toString());
        }
    };

    private void change(String url) {
        cProgressDialog.show();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
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
                            if (beanObj.optString("data").equals("success")) {
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
