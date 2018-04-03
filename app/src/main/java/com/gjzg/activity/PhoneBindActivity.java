package com.gjzg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import com.gjzg.config.VarConfig;
import com.gjzg.service.CodeTimerService;
import com.gjzg.utils.Utils;

public class PhoneBindActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private EditText phoneEt, pwdEt;
    private TextView getPwdTv, tipTv, sureTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_phone_bind, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_phone_bind_return);
        phoneEt = (EditText) rootView.findViewById(R.id.et_phone_bind_phone);
        pwdEt = (EditText) rootView.findViewById(R.id.et_phone_bind_pwd);
        getPwdTv = (TextView) rootView.findViewById(R.id.tv_phone_bind_get_pwd);
        tipTv = (TextView) rootView.findViewById(R.id.tv_phone_bind_tip);
        sureTv = (TextView) rootView.findViewById(R.id.tv_phone_bind_sure);
    }

    private void initData() {
        tipTv.setText("手机号码便于登录和接收雇主来电，重新绑定后，请使用新的手机号码登录");
        getPwdTv.setText(VarConfig.getPwdTip);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        getPwdTv.setOnClickListener(this);
        sureTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_phone_bind_return:
                finish();
                break;
            case R.id.tv_phone_bind_get_pwd:
                if (Utils.isPhonenumber(phoneEt.getText().toString())) {
                    startService(new Intent(PhoneBindActivity.this, CodeTimerService.class));
                } else {
                    Utils.toast(this, VarConfig.phoneErrorTip);
                }
                break;
            case R.id.tv_phone_bind_sure:
                sure();
                break;
            default:
                break;
        }
    }

    private void sure() {
        Utils.toast(PhoneBindActivity.this, phoneEt.getText().toString() + "\n" + pwdEt.getText().toString());
    }

    private static IntentFilter updateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CodeTimerService.IN_RUNNING);
        intentFilter.addAction(CodeTimerService.END_RUNNING);
        return intentFilter;
    }

    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case CodeTimerService.IN_RUNNING:
                    if (getPwdTv.isEnabled())
                        getPwdTv.setEnabled(false);
                    getPwdTv.setText(intent.getStringExtra("time") + "秒重新获取");
                    break;
                case CodeTimerService.END_RUNNING:
                    getPwdTv.setEnabled(true);
                    getPwdTv.setText(VarConfig.getPwdTip);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mUpdateReceiver, updateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mUpdateReceiver);
    }
}
