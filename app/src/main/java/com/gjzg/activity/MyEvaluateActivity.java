package com.gjzg.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.config.ColorConfig;
import com.gjzg.fragment.GetEvaluateFragment;
import com.gjzg.fragment.GiveEvaluateFragment;

public class MyEvaluateActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl, getRl, giveRl;
    private TextView getTv, giveTv;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private int curState = 0, tarState = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(MyEvaluateActivity.this).inflate(R.layout.activity_my_evaluate, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_my_evaluate_return);
        getRl = (RelativeLayout) rootView.findViewById(R.id.rl_my_evaluate_get);
        giveRl = (RelativeLayout) rootView.findViewById(R.id.rl_my_evaluate_give);
        getTv = (TextView) rootView.findViewById(R.id.tv_my_evaluate_get);
        giveTv = (TextView) rootView.findViewById(R.id.tv_my_evaluate_give);
    }

    private void initData() {
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new GetEvaluateFragment());
        fragmentList.add(new GiveEvaluateFragment());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_my_evaluate_sit, fragmentList.get(curState));
        transaction.commit();
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        getRl.setOnClickListener(this);
        giveRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_evaluate_return:
                finish();
                break;
            case R.id.rl_my_evaluate_get:
                tarState = 0;
                changeState();
                break;
            case R.id.rl_my_evaluate_give:
                tarState = 1;
                changeState();
                break;
            default:
                break;
        }
    }

    private void changeState() {
        if (tarState != curState) {
            switch (tarState) {
                case 0:
                    getTv.setTextColor(ColorConfig.red_ff3e50);
                    giveTv.setTextColor(ColorConfig.gray_a0a0a0);
                    break;
                case 1:
                    getTv.setTextColor(ColorConfig.gray_a0a0a0);
                    giveTv.setTextColor(ColorConfig.red_ff3e50);
                    break;
            }
            Fragment curFragment = fragmentList.get(curState);
            Fragment tarFragment = fragmentList.get(tarState);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_my_evaluate_sit, tarFragment);
            }
            transaction.commit();
            curState = tarState;
        }

    }
}
