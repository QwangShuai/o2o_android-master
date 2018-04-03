package com.gjzg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.PhoneBean;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChangePwdActivity extends AppCompatActivity {
    @BindView(R.id.rl_change_pwd_return)
    RelativeLayout returnRl;
    @BindView(R.id.et_change_pwd_pwd1)
    EditText pwdEt1;
    @BindView(R.id.et_change_pwd_pwd2)
    EditText pwdEt2;
    @BindView(R.id.tv_change_pwd_log)
    TextView logTv;
    @BindView(R.id.iv_eyes1)
    ImageView eyesIv1;
    @BindView(R.id.iv_eyes2)
    ImageView eyesIv2;
    String TAG = "ChangePwdActivity";
    private Unbinder unbinder;
    String phone = "", code = "";
    PhoneBean phoneBean = new PhoneBean();
    boolean eyes1_boolean = true, eyes2_boolean = true;
    String activity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        phoneBean = UserUtils.readPhoneData(ChangePwdActivity.this);
        activity = getIntent().getStringExtra("activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @OnClick({R.id.rl_change_pwd_return, R.id.iv_eyes1, R.id.iv_eyes2, R.id.tv_change_pwd_log})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_change_pwd_return:
                finish();
                break;
            case R.id.iv_eyes1:
                if (eyes1_boolean) {
                    eyesIv1.setImageResource(R.mipmap.open_eyes);
                    pwdEt1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyes1_boolean = false;
                } else {
                    eyesIv1.setImageResource(R.mipmap.close_eyes);
                    pwdEt1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyes1_boolean = true;
                }
                break;
            case R.id.iv_eyes2:
                if (eyes2_boolean) {
                    eyesIv2.setImageResource(R.mipmap.open_eyes);
                    pwdEt2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyes2_boolean = false;
                } else {
                    eyesIv2.setImageResource(R.mipmap.close_eyes);
                    pwdEt2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyes2_boolean = true;
                }
                break;
            case R.id.tv_change_pwd_log:
                if (activity.equals("forget")) {//忘记密码重置
                    changePwd(phoneBean.getNumber(), phoneBean.getVerify_code(), pwdEt1.getText().toString(), pwdEt2.getText().toString());
                } else if (activity.equals("main")) {//我的中修改密码
                    setPwd(UserUtils.readUserData(ChangePwdActivity.this).getId(),
                            pwdEt1.getText().toString(),pwdEt2.getText().toString());
                }

                break;
        }
    }

    private void changePwd(final String phone_number, String verify_code, String userpass, String password_confirm) {
        if (Utils.isMatcherFinded(userpass)) {
            if (userpass.equals(password_confirm)) {
                OkHttpUtils
                        .get()
                        .tag(this)
//                        .url(NetConfig.testforgetPwdUrl)
                        .addParams("phone_number", phone_number)
                        .addParams("verify_code", verify_code)
                        .addParams("userpass", userpass)
                        .addParams("password_confirm", password_confirm)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Utils.toast(ChangePwdActivity.this, VarConfig.noNetTip);
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
                                                    Utils.toast(ChangePwdActivity.this, msg);
                                                break;
                                            case 1:
                                                finish();
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            } else {
                Utils.toast(ChangePwdActivity.this, "两次输入的密码不一致");
            }
        } else {
            Utils.toast(ChangePwdActivity.this, "请输入8-16的数字字母混合");
        }

    }

    private void setPwd(String u_id, String u_password, String password_confirm) {
        if (Utils.isMatcherFinded(u_password)) {
            if (u_password.equals(password_confirm)) {
                OkHttpUtils
                        .get()
                        .tag(this)
//                        .url(NetConfig.testSetLoginPwdUrl)
                        .addParams("u_id", u_id)
                        .addParams("u_password", u_password)
                        .addParams("password_confirm", password_confirm)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Utils.toast(ChangePwdActivity.this, VarConfig.noNetTip);
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
                                                    Utils.toast(ChangePwdActivity.this, msg);
                                                break;
                                            case 1:
                                                finish();
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            } else {
                Utils.toast(ChangePwdActivity.this, "两次输入的密码不一致");
            }
        } else {
            Utils.toast(ChangePwdActivity.this, "请输入8-16的数字字母混合");
        }

    }
}
