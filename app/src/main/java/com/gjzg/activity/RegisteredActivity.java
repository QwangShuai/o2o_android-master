package com.gjzg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gjzg.R;
import com.gjzg.bean.UserBean;
import com.gjzg.config.ColorConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.custom.CProgressDialog;
import com.gjzg.fragment.MobieLoginFragment;
import com.gjzg.fragment.PwdRegisteredFragment;
import com.gjzg.service.GetLoginCodeTimerService;
import com.gjzg.utils.MD5;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.squareup.okhttp.Request;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class RegisteredActivity extends AppCompatActivity {
    @BindView(R.id.rl_registered_return)
    RelativeLayout returnRl;
    @BindView(R.id.tv_login)
    TextView logintv;
    @BindView(R.id.et_registered_phone_number)
    EditText numberEt;
    @BindView(R.id.et_registered_move_pwd)
    EditText pwdEt;
    @BindView(R.id.iv_eyes)
    ImageView eyesIv;
    @BindView(R.id.et_registered_get_code)
    EditText codeEt;
    @BindView(R.id.tv_registered_get_code)
    TextView codeTv;
    @BindView(R.id.tv_registered_log)
    TextView registeredTv;
    @BindView(R.id.tv_login_agreement)
    TextView dealTv;
    @BindView(R.id.iv_wx_registered)
    ImageView wxRegisteredIv;
    private Unbinder unbinder;
    boolean eyesState = true;
    UMShareAPI umShareAPI = null;
    private SHARE_MEDIA platform = null;
    private CProgressDialog cpd;
    String phone = "";//手机号MD5加密后的
    String uid="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    public void initView(){
        umShareAPI = UMShareAPI.get(RegisteredActivity.this);
//        registeredTv.setEnabled(false);
        cpd = Utils.initProgressDialog(RegisteredActivity.this, cpd);
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
//                    registeredTv.setEnabled(false);
//                } else {
//                    registeredTv.setEnabled(true);
//                }
//            }
//        });
    }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkHttpUtils.getInstance().cancelTag(this);
    }


    @OnClick({R.id.rl_registered_return,R.id.tv_login,R.id.iv_eyes,R.id.tv_registered_get_code,
            R.id.tv_registered_log,R.id.tv_login_agreement,R.id.iv_wx_registered})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_registered_return:
                finish();
                break;
            case R.id.tv_login:
                startActivity(new Intent(RegisteredActivity.this, LoginActivity.class));
                break;
            case R.id.iv_eyes:
                if(eyesState){
                    eyesIv.setImageResource(R.mipmap.open_eyes);
                    pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyesState = false;
                }else {
                    eyesIv.setImageResource(R.mipmap.close_eyes);
                    pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyesState = true;
                }
                break;
            case R.id.tv_registered_get_code:
                getCode(numberEt.getText().toString());
                break;
            case R.id.tv_registered_log:
                if(codeEt.length()==6)
                    register(numberEt.getText().toString(),codeEt.getText().toString(),pwdEt.getText().toString());
                else
                    Utils.toast(RegisteredActivity.this,"验证码长度不正确");
                break;
            case R.id.tv_login_agreement:
                startActivity(new Intent(RegisteredActivity.this, AgreementActivity.class));
                break;
            case R.id.iv_wx_registered:
                platform = SHARE_MEDIA.WEIXIN;
                umShareAPI.doOauthVerify(RegisteredActivity.this, platform,
                        umAuthListener);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
                            Utils.toast(RegisteredActivity.this, VarConfig.noNetTip);
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
                                        Utils.toast(RegisteredActivity.this, msg);
                                    if (code == 1) {
                                        startService(new Intent(RegisteredActivity.this, GetLoginCodeTimerService.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {
            Utils.toast(RegisteredActivity.this, "手机号格式不正确");
        }
    }
    private void register(final String number, String code,String userpass) {
        Log.i("number=",number);
        if(Utils.isMatcherFinded(userpass)){
            if (Utils.isPhonenumber(number)) {
                if (!TextUtils.isEmpty(code)) {
                    cpd.show();
                    OkHttpUtils
                            .get()
                            .tag(this)
//                            .url(NetConfig.registeredUrl)
                            .addParams("phone_number", number)
                            .addParams("verify_code", code)
                            .addParams("userpass",userpass)
                            .addParams("type","pass")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    cpd.dismiss();
                                    Utils.toast(RegisteredActivity.this, VarConfig.noNetTip);
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
                                                        Utils.toast(RegisteredActivity.this, msg);
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
//                                                    postOnline(userBean);
                                                    UserUtils.saveUserData(RegisteredActivity.this,userBean);
                                                    startActivity(new Intent(RegisteredActivity.this, MainActivity.class));
                                                    finish();
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
                Utils.toast(RegisteredActivity.this, "手机号格式不正确");
            }
        } else {
            Utils.toast(RegisteredActivity.this,"请输入8-16位数字字母混合");
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
                                    UserUtils.saveUserData(RegisteredActivity.this, userBean);
                                    startActivity(new Intent(RegisteredActivity.this, MainActivity.class));
                                    finish();
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
                    codeTv.setText(intent.getStringExtra("time") + "秒后重新发送");
                    break;
                case GetLoginCodeTimerService.END_RUNNING:
                    codeTv.setText(VarConfig.getPwdTip);
                    break;
            }
        }
    };
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if(platform == SHARE_MEDIA.WEIXIN){
                //unionid:（6.2以前用unionid）uid
                uid = map.get("unionid");
            }else{
                uid = map.get("uid");
            }
            if(!uid.equals("")||uid.length()==0){
                //如果uid不为空即回调授权成功，则可以调接口告诉后台此时的第三方uid，后台判断此唯一标识值是否存在即判断用户是否用
                //第三方登录过，如果登录过直接进入主界面， 没有登录过则后台存储该值并进入注册界面进行手机号绑定注册
            }else{
                Toast.makeText(RegisteredActivity.this, "暂无法使用该登录方式",
                        Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(RegisteredActivity.this, "授权成功",
                    Toast.LENGTH_SHORT).show();
            Log.d("user info", "user info:" + map.toString());
            Utils.toast(RegisteredActivity.this,uid+"");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };
}
