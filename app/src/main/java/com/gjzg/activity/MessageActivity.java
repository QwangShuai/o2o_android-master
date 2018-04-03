package com.gjzg.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.gjzg.fragment.JobOfferFragment;
import com.gjzg.fragment.SysMsgFragment;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl, offerRl, systemRl;
    private TextView offerTv, systemTv;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private final int LEFT = 0, RIGHT = 1;
    private int curState = LEFT, tarState = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_message, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_message_return);
        offerRl = (RelativeLayout) rootView.findViewById(R.id.rl_message_offer);
        offerTv = (TextView) rootView.findViewById(R.id.tv_message_offer);
        systemRl = (RelativeLayout) rootView.findViewById(R.id.rl_message_system);
        systemTv = (TextView) rootView.findViewById(R.id.tv_message_system);
    }

    private void initData() {
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        Fragment jobOfferFragment = new JobOfferFragment();
        Fragment sysMsgFragment = new SysMsgFragment();
        fragmentList.add(jobOfferFragment);
        fragmentList.add(sysMsgFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_message_sit, fragmentList.get(curState));
        transaction.commit();
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        offerRl.setOnClickListener(this);
        systemRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_message_return:
                finish();
                break;
            case R.id.rl_message_offer:
                tarState = LEFT;
                changeFragment();
                break;
            case R.id.rl_message_system:
                tarState = RIGHT;
                changeFragment();
                break;
        }
    }

    private void changeFragment() {
        if (tarState != curState) {
            switch (tarState) {
                case LEFT:
                    offerTv.setTextColor(ColorConfig.red_ff3e50);
                    systemTv.setTextColor(ColorConfig.gray_a0a0a0);
                    break;
                case RIGHT:
                    offerTv.setTextColor(ColorConfig.gray_a0a0a0);
                    systemTv.setTextColor(ColorConfig.red_ff3e50);
                    break;
            }
            Fragment curFragment = fragmentList.get(curState);
            Fragment tarFragment = fragmentList.get(tarState);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_message_sit, tarFragment);
            }
            transaction.commit();
            curState = tarState;
        }
    }
}
