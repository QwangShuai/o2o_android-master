package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.bean.UserBean;

import password.presenter.IPwdPresenter;
import password.presenter.PwdPresenter;
import password.view.IPwdActivity;

import com.gjzg.adapter.InputPasswordAdapter;
import com.gjzg.bean.InputPasswordBean;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class PwdActivity extends AppCompatActivity implements IPwdActivity, View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private TextView textView;
    private ImageView pIv0, pIv1, pIv2, pIv3, pIv4, pIv5;
    private TextView fgtTv;
    private CProgressDialog cpd;

    private String firstPwd, againPwd, originPwd;

    private final int FIRST = 0, AGAIN = 1, ORIGIN = 2, NEW = 3, NEW_AGAIN = 4;
    private int state;

    private IPwdPresenter pwdPresenter;

    private GridView inputPasswordGv;
    private List<InputPasswordBean> inputPasswordBeanList;
    private InputPasswordAdapter inputPasswordAdapter;
    private StringBuilder passwordSb;

    private final int SET_PWD_SUCCESS = 0;
    private final int SET_PWD_FAILURE = 1;
    private final int ORI_PWD_SUCCESS = 2;
    private final int ORI_PWD_FAILURE = 3;
    private final int EDIT_PWD_SUCCESS = 4;
    private final int EDIT_PWD_FAILURE = 5;
    private final int FORGET_PWD_SUCCESS = 6;
    private final int FORGET_PWD_FAILURE = 7;
    private String pwdTip;
    private boolean fromIdCard = false;
    private String verifycode;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case SET_PWD_SUCCESS:
                        cpd.dismiss();
                        Utils.toast(PwdActivity.this, pwdTip);
                        UserBean userBean = UserUtils.readUserData(PwdActivity.this);
                        userBean.setPass("1");
                        UserUtils.saveUserData(PwdActivity.this, userBean);
                        startActivity(new Intent(PwdActivity.this, MainActivity.class));
                        break;
                    case SET_PWD_FAILURE:
                        cpd.dismiss();
                        Utils.toast(PwdActivity.this, pwdTip);
                        passwordSb.delete(0, passwordSb.length());
                        refreshView();
                        break;
                    case ORI_PWD_SUCCESS:
                        cpd.dismiss();
                        passwordSb.delete(0, passwordSb.length());
                        state = NEW;
                        refreshView();
                        break;
                    case ORI_PWD_FAILURE:
                        cpd.dismiss();
                        Utils.toast(PwdActivity.this, pwdTip);
                        passwordSb.delete(0, passwordSb.length());
                        refreshView();
                        break;
                    case EDIT_PWD_SUCCESS:
                        cpd.dismiss();
                        Utils.toast(PwdActivity.this, pwdTip);
                        startActivity(new Intent(PwdActivity.this, MainActivity.class));
                        break;
                    case EDIT_PWD_FAILURE:
                        cpd.dismiss();
                        Utils.toast(PwdActivity.this, pwdTip);
                        passwordSb.delete(0, passwordSb.length());
                        refreshView();
                        break;
                    case FORGET_PWD_SUCCESS:
                        cpd.dismiss();
                        Utils.toast(PwdActivity.this, pwdTip);
                        startActivity(new Intent(PwdActivity.this, MainActivity.class));
                        break;
                    case FORGET_PWD_FAILURE:
                        cpd.dismiss();
                        Utils.toast(PwdActivity.this, pwdTip);
                        passwordSb.delete(0, passwordSb.length());
                        refreshView();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_pwd, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_pwd_return);
        textView = (TextView) rootView.findViewById(R.id.tv_pwd_tip);
        pIv0 = (ImageView) rootView.findViewById(R.id.iv_pwd_point_0);
        pIv1 = (ImageView) rootView.findViewById(R.id.iv_pwd_point_1);
        pIv2 = (ImageView) rootView.findViewById(R.id.iv_pwd_point_2);
        pIv3 = (ImageView) rootView.findViewById(R.id.iv_pwd_point_3);
        pIv4 = (ImageView) rootView.findViewById(R.id.iv_pwd_point_4);
        pIv5 = (ImageView) rootView.findViewById(R.id.iv_pwd_point_5);
        fgtTv = (TextView) rootView.findViewById(R.id.tv_pwd_fgt);
        cpd = Utils.initProgressDialog(PwdActivity.this, cpd);
        inputPasswordGv = (GridView) rootView.findViewById(R.id.gv_input_password);
    }

    private void initData() {
        Intent intent = getIntent();
        verifycode = intent.getStringExtra("verifycode");
        if (!TextUtils.isEmpty(verifycode)) {
            state = NEW;
            fromIdCard = true;
        } else {
            if (TextUtils.isEmpty(UserUtils.readUserData(PwdActivity.this).getPass())) {
                state = FIRST;
            } else {
                state = ORIGIN;
            }
        }
        passwordSb = new StringBuilder();
        pwdPresenter = new PwdPresenter(this);
        refreshView();
        InputPasswordBean inputPasswordBean0 = new InputPasswordBean(0, 1, "");
        InputPasswordBean inputPasswordBean1 = new InputPasswordBean(0, 2, "ABC");
        InputPasswordBean inputPasswordBean2 = new InputPasswordBean(0, 3, "DEF");
        InputPasswordBean inputPasswordBean3 = new InputPasswordBean(0, 4, "GHI");
        InputPasswordBean inputPasswordBean4 = new InputPasswordBean(0, 5, "JKL");
        InputPasswordBean inputPasswordBean5 = new InputPasswordBean(0, 6, "MNO");
        InputPasswordBean inputPasswordBean6 = new InputPasswordBean(0, 7, "PQRS");
        InputPasswordBean inputPasswordBean7 = new InputPasswordBean(0, 8, "TUV");
        InputPasswordBean inputPasswordBean8 = new InputPasswordBean(0, 9, "WXYZ");
        InputPasswordBean inputPasswordBean9 = new InputPasswordBean(1, 0, "");
        InputPasswordBean inputPasswordBean10 = new InputPasswordBean(0, 0, "");
        InputPasswordBean inputPasswordBean11 = new InputPasswordBean(2, 0, "");
        inputPasswordBeanList = new ArrayList<>();
        inputPasswordBeanList.add(inputPasswordBean0);
        inputPasswordBeanList.add(inputPasswordBean1);
        inputPasswordBeanList.add(inputPasswordBean2);
        inputPasswordBeanList.add(inputPasswordBean3);
        inputPasswordBeanList.add(inputPasswordBean4);
        inputPasswordBeanList.add(inputPasswordBean5);
        inputPasswordBeanList.add(inputPasswordBean6);
        inputPasswordBeanList.add(inputPasswordBean7);
        inputPasswordBeanList.add(inputPasswordBean8);
        inputPasswordBeanList.add(inputPasswordBean9);
        inputPasswordBeanList.add(inputPasswordBean10);
        inputPasswordBeanList.add(inputPasswordBean11);
        inputPasswordAdapter = new InputPasswordAdapter(PwdActivity.this, inputPasswordBeanList);
        inputPasswordGv.setAdapter(inputPasswordAdapter);
        Utils.setGridViewHeight(inputPasswordGv, 3);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        fgtTv.setOnClickListener(this);
        inputPasswordGv.setOnItemClickListener(this);
    }

    private void refreshView() {
        switch (state) {
            case FIRST:
                textView.setText("设置六位交易密码");
                fgtTv.setVisibility(View.INVISIBLE);
                break;
            case AGAIN:
                textView.setText("请再输入一次");
                fgtTv.setVisibility(View.INVISIBLE);
                break;
            case ORIGIN:
                textView.setText("请输入原密码");
                fgtTv.setVisibility(View.VISIBLE);
                break;
            case NEW:
                textView.setText("请输入新密码");
                fgtTv.setVisibility(View.INVISIBLE);
                break;
            case NEW_AGAIN:
                textView.setText("再次输入新密码");
                fgtTv.setVisibility(View.INVISIBLE);
                break;
        }
        switch (passwordSb.length()) {
            case 0:
                pIv0.setVisibility(View.GONE);
                pIv1.setVisibility(View.GONE);
                pIv2.setVisibility(View.GONE);
                pIv3.setVisibility(View.GONE);
                pIv4.setVisibility(View.GONE);
                pIv5.setVisibility(View.GONE);
                break;
            case 1:
                pIv0.setVisibility(View.VISIBLE);
                pIv1.setVisibility(View.GONE);
                pIv2.setVisibility(View.GONE);
                pIv3.setVisibility(View.GONE);
                pIv4.setVisibility(View.GONE);
                pIv5.setVisibility(View.GONE);
                break;
            case 2:
                pIv0.setVisibility(View.VISIBLE);
                pIv1.setVisibility(View.VISIBLE);
                pIv2.setVisibility(View.GONE);
                pIv3.setVisibility(View.GONE);
                pIv4.setVisibility(View.GONE);
                pIv5.setVisibility(View.GONE);
                break;
            case 3:
                pIv0.setVisibility(View.VISIBLE);
                pIv1.setVisibility(View.VISIBLE);
                pIv2.setVisibility(View.VISIBLE);
                pIv3.setVisibility(View.GONE);
                pIv4.setVisibility(View.GONE);
                pIv5.setVisibility(View.GONE);
                break;
            case 4:
                pIv0.setVisibility(View.VISIBLE);
                pIv1.setVisibility(View.VISIBLE);
                pIv2.setVisibility(View.VISIBLE);
                pIv3.setVisibility(View.VISIBLE);
                pIv4.setVisibility(View.GONE);
                pIv5.setVisibility(View.GONE);
                break;
            case 5:
                pIv0.setVisibility(View.VISIBLE);
                pIv1.setVisibility(View.VISIBLE);
                pIv2.setVisibility(View.VISIBLE);
                pIv3.setVisibility(View.VISIBLE);
                pIv4.setVisibility(View.VISIBLE);
                pIv5.setVisibility(View.GONE);
                break;
            case 6:
                pIv0.setVisibility(View.VISIBLE);
                pIv1.setVisibility(View.VISIBLE);
                pIv2.setVisibility(View.VISIBLE);
                pIv3.setVisibility(View.VISIBLE);
                pIv4.setVisibility(View.VISIBLE);
                pIv5.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pwd_return:
                finish();
                break;
            case R.id.tv_pwd_fgt:
                startActivity(new Intent(this, PhoneProveActivity.class));
                break;
        }
    }

    @Override
    public void showSetPwdSuccess(String success) {
        pwdTip = success;
        handler.sendEmptyMessage(SET_PWD_SUCCESS);
    }

    @Override
    public void showSetPwdFailure(String failure) {
        pwdTip = failure;
        handler.sendEmptyMessage(SET_PWD_FAILURE);
    }

    @Override
    public void showProveOriPwdSuccess(String success) {
        handler.sendEmptyMessage(ORI_PWD_SUCCESS);
    }

    @Override
    public void showProveOriPwdFailure(String failure) {
        pwdTip = failure;
        handler.sendEmptyMessage(ORI_PWD_FAILURE);
    }

    @Override
    public void showEditPwdSuccess(String success) {
        pwdTip = success;
        handler.sendEmptyMessage(EDIT_PWD_SUCCESS);
    }

    @Override
    public void showEditPwdFailure(String failure) {
        pwdTip = failure;
        handler.sendEmptyMessage(EDIT_PWD_FAILURE);
    }

    @Override
    public void showForgetPwdSuccess(String success) {
        pwdTip = success;
        handler.sendEmptyMessage(FORGET_PWD_SUCCESS);
    }

    @Override
    public void showForgetPwdFailure(String failure) {
        pwdTip = failure;
        handler.sendEmptyMessage(FORGET_PWD_FAILURE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.gv_input_password:
                InputPasswordBean inputPasswordBean = inputPasswordBeanList.get(position);
                if (inputPasswordBean != null) {
                    switch (inputPasswordBean.getType()) {
                        case 0:
                            if (passwordSb.length() < 6) {
                                passwordSb.append(inputPasswordBean.getNumber());
                            }
                            if (passwordSb.length() == 6) {
                                inputDone();
                            }
                            break;
                        case 2:
                            if (passwordSb.length() != 0) {
                                passwordSb.deleteCharAt(passwordSb.length() - 1);
                            }
                            break;
                    }
                    refreshView();
                }
                break;
        }
    }

    private void inputDone() {
        switch (state) {
            case FIRST:
                firstPwd = passwordSb.toString();
                state = AGAIN;
                passwordSb.delete(0, passwordSb.length());
                refreshView();
                break;
            case AGAIN:
                againPwd = passwordSb.toString();
                if (firstPwd.equals(againPwd)) {
                    cpd.show();
                    pwdPresenter.setPwd(UserUtils.readUserData(PwdActivity.this).getId(), againPwd);
                } else {
                    Utils.toast(this, "两次密码不正确");
                    passwordSb.delete(0, passwordSb.length());
                    refreshView();
                }
                break;
            case ORIGIN:
                originPwd = passwordSb.toString();
                cpd.show();
                pwdPresenter.proveOriPwd(UserUtils.readUserData(PwdActivity.this).getId(), originPwd);
                break;
            case NEW:
                firstPwd = passwordSb.toString();
                state = NEW_AGAIN;
                passwordSb.delete(0, passwordSb.length());
                refreshView();
                break;
            case NEW_AGAIN:
                againPwd = passwordSb.toString();
                if (firstPwd.equals(againPwd)) {
                    cpd.show();
                    if (fromIdCard) {
                        UserBean userBean = UserUtils.readUserData(PwdActivity.this);
                        pwdPresenter.forgetPwd(userBean.getMobile(), verifycode, againPwd, userBean.getIdcard());
                    } else {
                        pwdPresenter.editPwd(UserUtils.readUserData(PwdActivity.this).getId(), originPwd, againPwd);
                    }
                } else {
                    Utils.toast(this, "两次密码不正确");
                    passwordSb.delete(0, passwordSb.length());
                    refreshView();
                }
                break;
        }
    }
}
