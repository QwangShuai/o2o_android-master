package com.gjzg.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.activity.MainActivity;
import com.gjzg.activity.RegisteredActivity;
import com.gjzg.bean.UserBean;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.custom.CProgressDialog;
import com.gjzg.service.GetLoginCodeTimerService;
import com.gjzg.utils.MD5;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MobieLoginFragment extends Fragment implements View.OnClickListener{
    private View rootView;
    private TextView getTv,loginTv;
    private EditText numberEt,codeEt;

    private CProgressDialog cpd;
    String phone = "";//手机号MD5加密后的

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mobie_login,null);
        initView();
        setListener();
        return rootView;
    }
    public void initView(){
        initRootView();
//        loginTv.setEnabled(false);
        cpd = Utils.initProgressDialog(getActivity(), cpd);
//        codeEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() != 6) {
//                    loginTv.setEnabled(false);
//                } else {
//                    loginTv.setEnabled(true);
//                }
//            }
//        });
    }

    public void initRootView(){
        getTv = (TextView) rootView.findViewById(R.id.tv_login_get_move_pwd);
        loginTv = (TextView) rootView.findViewById(R.id.tv_login_log);
        numberEt = (EditText) rootView.findViewById(R.id.et_login_phone_number);
        codeEt = (EditText) rootView.findViewById(R.id.et_login_move_pwd);
    }
    public void setListener(){
        getTv.setOnClickListener(this);
        loginTv.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_get_move_pwd:
                getCode(numberEt.getText().toString());
                break;
            case R.id.tv_login_log:
                if(codeEt.length()==6)
                    login(numberEt.getText().toString(),codeEt.getText().toString());
                else
                    Utils.toast(getActivity(),"验证码长度不正确");
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mUpdateReceiver, updateIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mUpdateReceiver);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
    private void getCode(final String number) {
        if (Utils.isPhonenumber(number)) {
            cpd.show();
            phone = MD5.md5(numberEt.getText().toString());
            OkHttpUtils
                    .get()
                    .tag(this)
                    .url(NetConfig.codeUrl)
                    .addParams("phone_number", number)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            cpd.dismiss();
                            Utils.toast(getActivity(), VarConfig.noNetTip);
                        }

                        @Override
                        public void onResponse(String response) {
                            cpd.dismiss();
                            if (!TextUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int code = jsonObject.optInt("code");
                                    String msg = null;
                                    JSONObject dataObj = jsonObject.optJSONObject("data");
                                    if (dataObj != null) {
                                        msg = dataObj.optString("msg");
                                    }
                                    if (!TextUtils.isEmpty(msg))
                                        Utils.toast(getActivity(), msg);
                                    if (code == 1) {
                                        getActivity().startService(new Intent(getActivity(), GetLoginCodeTimerService.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {
            Utils.toast(getActivity(), "手机号格式不正确");
        }
    }

    private void login(final String number, String code) {
        if (Utils.isPhonenumber(number)) {
            if (!TextUtils.isEmpty(code)) {
                cpd.show();
                OkHttpUtils
                        .get()
                        .tag(this)
                        .url(NetConfig.loginUrl)
                        .addParams("phone_number", number)
                        .addParams("verify_code", code)
                        .addParams("type","verify")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                cpd.dismiss();
                                Utils.toast(getActivity(), VarConfig.noNetTip);
                            }

                            @Override
                            public void onResponse(String response) {
                                cpd.dismiss();
                                Log.e("TAG", response);
                                if (!TextUtils.isEmpty(response)) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        int code = jsonObject.optInt("code");
                                        String msg = null;
                                        JSONObject dataObj = jsonObject.optJSONObject("data");
                                        switch (code) {
                                            case 0:
                                                msg = dataObj.optString("msg");
                                                if (!TextUtils.isEmpty(msg))
                                                    Utils.toast(getActivity(), msg);
                                                break;
                                            case 1:
                                                UserBean userBean = new UserBean();
                                                userBean.setId(dataObj.optString("u_id"));
                                                userBean.setName(dataObj.optString("u_name"));
                                                userBean.setSex(dataObj.optString("u_sex"));
                                                userBean.setOnline(dataObj.optString("u_online"));
                                                userBean.setIcon(dataObj.optString("u_img"));
                                                userBean.setToken(dataObj.optString("token"));
                                                userBean.setPass(dataObj.optString("u_pass"));
                                                userBean.setIdcard(dataObj.optString("u_idcard"));
//                                                postOnline(userBean);
                                                UserUtils.saveUserData(getActivity(),userBean);
                                                startActivity(new Intent(getActivity(), MainActivity.class));
                                                getActivity().finish();
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        } else {
            Utils.toast(getActivity(), "手机号格式不正确");
        }
    }

    private void postOnline(final UserBean userBean) {
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetConfig.userInfoEditUrl)
                .addParams("u_id", userBean.getId())
                .addParams("u_online", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.optInt("code") == 1) {
                                    cpd.dismiss();
                                    userBean.setOnline("1");
                                    UserUtils.saveUserData(getActivity(), userBean);
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private static IntentFilter updateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GetLoginCodeTimerService.IN_RUNNING);
        intentFilter.addAction(GetLoginCodeTimerService.END_RUNNING);
        return intentFilter;
    }

    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case GetLoginCodeTimerService.IN_RUNNING:
                    getTv.setText(intent.getStringExtra("time") + "秒后重新发送");
                    break;
                case GetLoginCodeTimerService.END_RUNNING:
                    getTv.setText(VarConfig.getPwdTip);
                    break;
            }
        }
    };
}
