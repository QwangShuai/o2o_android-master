package com.gjzg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gjzg.R;

import com.gjzg.activity.EmployerManageActivity;
import com.gjzg.activity.LoginActivity;
import com.gjzg.activity.UserManageActivity;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.activity.WalletActivity;
import com.gjzg.activity.WorkerManageActivity;
import com.gjzg.activity.PublishJobActivity;

/**
 * 工作管理
 */
public class ManageFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private RelativeLayout rechargeRl, publishRl, workerRl, employerRl, userRl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_manage, null);
        initView();
        setListener();
        return rootView;
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        rechargeRl = (RelativeLayout) rootView.findViewById(R.id.rl_manage_recharge);
        publishRl = (RelativeLayout) rootView.findViewById(R.id.rl_manage_publish);
        workerRl = (RelativeLayout) rootView.findViewById(R.id.rl_manage_worker);
        employerRl = (RelativeLayout) rootView.findViewById(R.id.rl_manage_employer);
//        userRl = (RelativeLayout) rootView.findViewById(R.id.rl_manage_user);
    }

    private void setListener() {
//        rechargeRl.setOnClickListener(this);
        publishRl.setOnClickListener(this);
        workerRl.setOnClickListener(this);
        employerRl.setOnClickListener(this);
//        userRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (UserUtils.isUserLogin(getActivity())) {
            switch (v.getId()) {
//                case R.id.rl_manage_recharge://充值
//                    String idcard0 = UserUtils.readUserData(getActivity()).getIdcard();
//                    if (idcard0 == null || TextUtils.isEmpty(idcard0) || idcard0.equals("null")) {
//                        Utils.toast(getActivity(), "请在工作管理中完善个人信息");
//                    } else {
//                        startActivity(new Intent(getActivity(), WalletActivity.class));
//                    }
//                    break;
                case R.id.rl_manage_publish:
                    String idcard1 = UserUtils.readUserData(getActivity()).getIdcard();
                    String pass = UserUtils.readUserData(getActivity()).getPass();
                    if (idcard1 == null || TextUtils.isEmpty(idcard1) || idcard1.equals("null")) {
                        Utils.toast(getActivity(), "请完善个人信息");
//                    } else if (pass == null || TextUtils.isEmpty(pass) || pass.equals("null")) {
//                        Utils.toast(getActivity(), "请设置交易密码");
                    } else {
                        startActivity(new Intent(getActivity(), PublishJobActivity.class));
                    }
                    break;
                case R.id.rl_manage_worker:
                    String idcard2 = UserUtils.readUserData(getActivity()).getIdcard();
                    if (idcard2 == null || TextUtils.isEmpty(idcard2) || idcard2.equals("null")) {
                        Utils.toast(getActivity(), "请完善个人信息");
                    } else {
                        startActivity(new Intent(getActivity(), WorkerManageActivity.class));
                    }
                    break;
                case R.id.rl_manage_employer:
                    String idcard3 = UserUtils.readUserData(getActivity()).getIdcard();
                    if (idcard3 == null || TextUtils.isEmpty(idcard3) || idcard3.equals("null")) {
                        Utils.toast(getActivity(), "请完善个人信息");
                    } else {
                        startActivity(new Intent(getActivity(), EmployerManageActivity.class));
                    }
                    break;
//                case R.id.rl_manage_user:
//                    startActivity(new Intent(getActivity(), UserManageActivity.class));
//                    break;
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

}