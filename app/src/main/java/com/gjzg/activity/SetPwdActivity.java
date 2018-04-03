package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gjzg.R;

public class SetPwdActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private LinearLayout cashLl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_set_pwd, null);
        setContentView(rootView);
        initView();
        setListener();
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_set_pwd_return);
        cashLl = (LinearLayout) rootView.findViewById(R.id.ll_set_pwd_cash);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        cashLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_set_pwd_return:
                finish();
                break;
            case R.id.ll_set_pwd_cash:
                startActivity(new Intent(this, PwdActivity.class));
                break;
        }
    }
}
