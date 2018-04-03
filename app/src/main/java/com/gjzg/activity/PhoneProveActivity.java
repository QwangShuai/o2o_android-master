package com.gjzg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.config.VarConfig;
import phoneprove.presenter.IPhoneProvePresenter;
import phoneprove.presenter.PhoneProvePresenter;
import phoneprove.view.IPhoneProveActivity;
import com.gjzg.service.CodeTimerService;
import com.gjzg.adapter.InputPasswordAdapter;
import com.gjzg.bean.InputPasswordBean;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class PhoneProveActivity extends AppCompatActivity implements IPhoneProveActivity, View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private TextView phoneProveMobileTv, tv0, tv1, tv2, tv3, tv4, tv5, resendTv;
    private CProgressDialog cpd;
    private GridView inputPasswordGv;
    private List<InputPasswordBean> inputPasswordBeanList;
    private InputPasswordAdapter inputPasswordAdapter;
    private StringBuilder phoneproveSb;
    private IPhoneProvePresenter phoneProvePresenter;

    private final int VERIFY_CODE_SUCCESS = 0;
    private final int VERIFY_CODE_FAILURE = 1;
    private final int PROVE_MOBILE_SUCCESS = 2;
    private final int PROVE_MOBILE_FAILURE = 3;
    private String verifyTip;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case VERIFY_CODE_SUCCESS:
                        Utils.toast(PhoneProveActivity.this, verifyTip);
                        break;
                    case VERIFY_CODE_FAILURE:
                        Utils.toast(PhoneProveActivity.this, verifyTip);
                        break;
                    case PROVE_MOBILE_SUCCESS:
                        cpd.dismiss();
                        Utils.toast(PhoneProveActivity.this, verifyTip);
                        Intent intent = new Intent(PhoneProveActivity.this, IdCardActivity.class);
                        intent.putExtra("verifycode", phoneproveSb.toString());
                        startActivity(intent);
                        break;
                    case PROVE_MOBILE_FAILURE:
                        cpd.dismiss();
                        Utils.toast(PhoneProveActivity.this, verifyTip);
                        phoneproveSb.delete(0, phoneproveSb.length());
                        refreshView(phoneproveSb.toString());
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_phone_prove, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData();
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_phone_prove_return);
        phoneProveMobileTv = (TextView) rootView.findViewById(R.id.tv_phone_prove_mobile);
        phoneProveMobileTv.setText(cutMobile(UserUtils.readUserData(PhoneProveActivity.this).getMobile()));
        tv0 = (TextView) rootView.findViewById(R.id.tv_phone_prove_0);
        tv1 = (TextView) rootView.findViewById(R.id.tv_phone_prove_1);
        tv2 = (TextView) rootView.findViewById(R.id.tv_phone_prove_2);
        tv3 = (TextView) rootView.findViewById(R.id.tv_phone_prove_3);
        tv4 = (TextView) rootView.findViewById(R.id.tv_phone_prove_4);
        tv5 = (TextView) rootView.findViewById(R.id.tv_phone_prove_5);
        resendTv = (TextView) rootView.findViewById(R.id.tv_phone_prove_resend);
        inputPasswordGv = (GridView) rootView.findViewById(R.id.gv_phone_prove);
        cpd = Utils.initProgressDialog(PhoneProveActivity.this, cpd);
    }

    private void initData() {
        phoneProvePresenter = new PhoneProvePresenter(this);
        phoneproveSb = new StringBuilder();
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
        inputPasswordAdapter = new InputPasswordAdapter(PhoneProveActivity.this, inputPasswordBeanList);
        inputPasswordGv.setAdapter(inputPasswordAdapter);
        Utils.setGridViewHeight(inputPasswordGv, 3);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        resendTv.setOnClickListener(this);
        inputPasswordGv.setOnItemClickListener(this);
    }

    private void loadData() {
        phoneProvePresenter.getVerifyCode(UserUtils.readUserData(PhoneProveActivity.this).getMobile());
        startService(new Intent(this, CodeTimerService.class));
    }

    private void refreshView(String str) {
        int length = str.length();
        switch (length) {
            case 0:
                tv0.setText("");
                tv1.setText("");
                tv2.setText("");
                tv3.setText("");
                tv4.setText("");
                tv5.setText("");
                break;
            case 1:
                tv0.setText(str.substring(0, 1));
                tv1.setText("");
                tv2.setText("");
                tv3.setText("");
                tv4.setText("");
                tv5.setText("");
                break;
            case 2:
                tv0.setText(str.substring(0, 1));
                tv1.setText(str.substring(1, 2));
                tv2.setText("");
                tv3.setText("");
                tv4.setText("");
                tv5.setText("");
                break;
            case 3:
                tv0.setText(str.substring(0, 1));
                tv1.setText(str.substring(1, 2));
                tv2.setText(str.substring(2, 3));
                tv3.setText("");
                tv4.setText("");
                tv5.setText("");
                break;
            case 4:
                tv0.setText(str.substring(0, 1));
                tv1.setText(str.substring(1, 2));
                tv2.setText(str.substring(2, 3));
                tv3.setText(str.substring(3, 4));
                tv4.setText("");
                tv5.setText("");
                break;
            case 5:
                tv0.setText(str.substring(0, 1));
                tv1.setText(str.substring(1, 2));
                tv2.setText(str.substring(2, 3));
                tv3.setText(str.substring(3, 4));
                tv4.setText(str.substring(4, 5));
                tv5.setText("");
                break;
            case 6:
                tv0.setText(str.substring(0, 1));
                tv1.setText(str.substring(1, 2));
                tv2.setText(str.substring(2, 3));
                tv3.setText(str.substring(3, 4));
                tv4.setText(str.substring(4, 5));
                tv5.setText(str.substring(5, 6));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_phone_prove_return:
                finish();
                break;
            case R.id.tv_phone_prove_resend:
                loadData();
                break;
        }
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
                    if (resendTv.isEnabled())
                        resendTv.setEnabled(false);
                    resendTv.setText(intent.getStringExtra("time") + "秒后可重新发送");
                    break;
                case CodeTimerService.END_RUNNING:
                    resendTv.setEnabled(true);
                    resendTv.setText(VarConfig.pwdResendTip);
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

    private String cutMobile(String mobile) {
        String a = mobile.substring(0, 3);
        String b = mobile.substring(7, 11);
        return a + "****" + b;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.gv_phone_prove:
                InputPasswordBean inputPasswordBean = inputPasswordBeanList.get(position);
                if (inputPasswordBean != null) {
                    switch (inputPasswordBean.getType()) {
                        case 0:
                            if (phoneproveSb.length() < 6) {
                                phoneproveSb.append(inputPasswordBean.getNumber());
                            }
                            if (phoneproveSb.length() == 6) {
                                inputDone();
                            }
                            break;
                        case 2:
                            if (phoneproveSb.length() != 0) {
                                phoneproveSb.deleteCharAt(phoneproveSb.length() - 1);
                            }
                            break;
                    }
                    refreshView(phoneproveSb.toString());
                }
                break;
        }
    }

    private void inputDone() {
        cpd.show();
        phoneProvePresenter.proveMobileCode(UserUtils.readUserData(PhoneProveActivity.this).getMobile(), phoneproveSb.toString());
    }

    @Override
    public void showVerifyCodeSuccess(String success) {
        verifyTip = success;
        handler.sendEmptyMessage(VERIFY_CODE_SUCCESS);
    }

    @Override
    public void showVerifyCodeFailure(String failure) {
        verifyTip = failure;
        handler.sendEmptyMessage(VERIFY_CODE_FAILURE);
    }

    @Override
    public void showProveMobileCodeSuccess(String success) {
        verifyTip = success;
        handler.sendEmptyMessage(PROVE_MOBILE_SUCCESS);
    }

    @Override
    public void showProveMobileCodeFailure(String failure) {
        verifyTip = failure;
        handler.sendEmptyMessage(PROVE_MOBILE_FAILURE);
    }
}
