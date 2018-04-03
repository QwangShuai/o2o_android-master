package com.gjzg.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

public class IdActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private TextView tipTv, nextTv;
    private EditText numberEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_id, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_id_return);
        tipTv = (TextView) rootView.findViewById(R.id.tv_id_tip);
        nextTv = (TextView) rootView.findViewById(R.id.tv_id_next);
        numberEt = (EditText) rootView.findViewById(R.id.et_id_number);
    }

    private void initData() {
        tipTv.setText("请填写王小二的身份证号");
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        nextTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_id_return:
                finish();
                break;
            case R.id.tv_id_next:
                break;
            default:
                break;
        }
    }
}
