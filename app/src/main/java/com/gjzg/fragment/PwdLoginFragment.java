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
import android.widget.Toast;

import com.gjzg.R;
import com.gjzg.activity.ForgetPwdActivity;
import com.gjzg.activity.LoginActivity;
import com.gjzg.activity.MainActivity;
import com.gjzg.bean.UserBean;
import com.gjzg.config.AppConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PwdLoginFragment extends Fragment implements View.OnClickListener{
    View rootView;
    ImageView eyesIv,wxLoginIv;
    TextView loginTv,getPwdTv;
    EditText mobieEt,pwdEt;
    boolean eyes_boolean = true;
    UMShareAPI umShareAPI = null;
    private SHARE_MEDIA platform = null;
    String uid = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_pwd_login, null);
        initView();
        setListener();
        return rootView;
    }
    public void initView(){
        initRootView();
        umShareAPI = UMShareAPI.get(getActivity());
    }
    public void initRootView(){
        wxLoginIv = (ImageView) rootView.findViewById(R.id.iv_wx_login);
        eyesIv = (ImageView) rootView.findViewById(R.id.iv_eyes);
        loginTv = (TextView) rootView.findViewById(R.id.tv_login_log);
        getPwdTv = (TextView) rootView.findViewById(R.id.tv_get_pwd);
        mobieEt = (EditText) rootView.findViewById(R.id.et_login_phone_number);
        pwdEt = (EditText) rootView.findViewById(R.id.et_login_move_pwd);
    }
    public void setListener(){
        wxLoginIv.setOnClickListener(this);
        eyesIv.setOnClickListener(this);
        loginTv.setOnClickListener(this);
        getPwdTv.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_eyes:
                if(eyes_boolean){
                    eyesIv.setImageResource(R.mipmap.open_eyes);
                    pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyes_boolean = false;
                }else {
                    eyesIv.setImageResource(R.mipmap.close_eyes);
                    pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyes_boolean = true;
                }
                break;
            case R.id.tv_login_log:
                login(mobieEt.getText().toString(),pwdEt.getText().toString());
                break;
            case R.id.tv_get_pwd:
                    startActivity(new Intent(getActivity(), ForgetPwdActivity.class));
                break;
            case R.id.iv_wx_login:
                platform = SHARE_MEDIA.WEIXIN;
                umShareAPI.doOauthVerify(getActivity(), platform,
                        umAuthListener);
                break;
        }
    }
    private void login(final String username, String userpass) {
        if(Utils.isMatcherFinded(userpass)){
            OkHttpUtils
                    .get()
                    .tag(this)
                    .url(NetConfig.loginUrl)
                    .addParams("phone_number", username)
                    .addParams("userpass", userpass)
                    .addParams("type","pass")
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
                                            userBean.setIdcard(dataObj.optString("u_idcard"));
//                                            postOnline(userBean);
                                            UserUtils.saveUserData(getActivity(), userBean);
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
        } else {
            Utils.toast(getActivity(),"请输入8-16位的字母数字混合");
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
               Toast.makeText(getActivity(), "暂无法使用该登录方式",
                       Toast.LENGTH_SHORT).show();
           }
           Toast.makeText(getActivity(), "授权成功",
                   Toast.LENGTH_SHORT).show();
           Log.d("user info", "user info:" + map.toString());
           Utils.toast(getActivity(),uid+"");
       }

       @Override
       public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

       }

       @Override
       public void onCancel(SHARE_MEDIA share_media, int i) {

       }
   };
}
