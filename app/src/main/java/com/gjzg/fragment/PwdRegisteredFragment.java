package com.gjzg.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.activity.MainActivity;
import com.gjzg.bean.UserBean;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class PwdRegisteredFragment extends Fragment implements View.OnClickListener {
    View rootView;
    ImageView eyesIv1,eyesIv2;
    TextView registeredTv;
    EditText mobieEt,pwdEt1,pwdEt2;
    boolean eyes1_boolean = true,eyes2_boolean = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_pwd_registered, null);
        initView();
        setListener();
        return rootView;
    }
    public void initView(){
        initRootView();
    }
    public void initRootView(){
        eyesIv1 = (ImageView) rootView.findViewById(R.id.iv_eyes1);
        registeredTv = (TextView) rootView.findViewById(R.id.tv_registered_log);
        mobieEt = (EditText) rootView.findViewById(R.id.et_registered_phone_number);
        pwdEt1 = (EditText) rootView.findViewById(R.id.et_registered_move_pwd1);
        eyesIv2 = (ImageView) rootView.findViewById(R.id.iv_eyes2);
        pwdEt2 = (EditText) rootView.findViewById(R.id.et_registered_move_pwd2);
    }

    public void setListener(){
        registeredTv.setOnClickListener(this);
        eyesIv1.setOnClickListener(this);
        eyesIv2.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_eyes1:
                    if(eyes1_boolean){
                        eyesIv1.setImageResource(R.mipmap.open_eyes);
                        pwdEt1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        eyes1_boolean = false;
                    }else {
                        eyesIv1.setImageResource(R.mipmap.close_eyes);
                        pwdEt1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        eyes1_boolean = true;
                    }
                break;
            case R.id.iv_eyes2:
                if(eyes2_boolean){
                    eyesIv2.setImageResource(R.mipmap.open_eyes);
                    pwdEt2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyes2_boolean = false;
                }else {
                    eyesIv2.setImageResource(R.mipmap.close_eyes);
                    pwdEt2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyes2_boolean = true;
                }
                break;
            case R.id.tv_registered_log:
                login(mobieEt.getText().toString(),pwdEt1.getText().toString(),pwdEt2.getText().toString());
                break;
        }

    }
    private void login(final String username, String userpass,String password_confirm) {
        if(Utils.isMatcherFinded(userpass)){
            if(userpass.equals(password_confirm)){
                OkHttpUtils
                        .get()
                        .tag(this)
//                        .url(NetConfig.testRegisteredUrl)
                        .addParams("username", username)
                        .addParams("userpass", userpass)
                        .addParams("password_confirm",password_confirm)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Utils.toast(getActivity(), VarConfig.noNetTip);
                            }

                            @Override
                            public void onResponse(String response) {
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
//                                                userBean.setIdcard(dataObj.optString("u_idcard"));
                                                postOnline(userBean);
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            } else {
                Utils.toast(getActivity(),"两次输入的密码不一致");
            }
        } else {
            Utils.toast(getActivity(),"请输入8-16位的数字字母混合");
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
}
