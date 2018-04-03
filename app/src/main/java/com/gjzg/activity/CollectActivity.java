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

import com.gjzg.fragment.CollectTaskFragment;
import com.gjzg.fragment.CollectWorkerFragment;
import com.gjzg.config.ColorConfig;

/**
 * 收藏页面
 */
public class CollectActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl, jobRl, workerRl;
    private TextView jobTv, workerTv;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private int curState = 0, tarState = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(CollectActivity.this).inflate(R.layout.activity_collect, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_collect_return);
        jobRl = (RelativeLayout) rootView.findViewById(R.id.rl_collect_job);
        workerRl = (RelativeLayout) rootView.findViewById(R.id.rl_collect_worker);
        jobTv = (TextView) rootView.findViewById(R.id.tv_collect_job);
        workerTv = (TextView) rootView.findViewById(R.id.tv_collect_worker);
    }

    private void initData() {
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new CollectTaskFragment());
        fragmentList.add(new CollectWorkerFragment());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_collect_sit, fragmentList.get(curState));
        transaction.commit();
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        jobRl.setOnClickListener(this);
        workerRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_collect_return:
                finish();
                break;
            case R.id.rl_collect_job:
                tarState = 0;
                changeState();
                break;
            case R.id.rl_collect_worker:
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
                    jobTv.setTextColor(ColorConfig.red_ff3e50);
                    workerTv.setTextColor(ColorConfig.gray_a0a0a0);
                    break;
                case 1:
                    jobTv.setTextColor(ColorConfig.gray_a0a0a0);
                    workerTv.setTextColor(ColorConfig.red_ff3e50);
                    break;
                default:
                    break;
            }
            Fragment curFragment = fragmentList.get(curState);
            Fragment tarFragment = fragmentList.get(tarState);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_collect_sit, tarFragment);
            }
            transaction.commit();
            curState = tarState;
        }
    }
}
