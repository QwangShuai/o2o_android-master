package com.gjzg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForgetPwdActivity extends AppCompatActivity {
    @BindView(R.id.rl_forget_pwd_return)
    RelativeLayout returnRl;
    @BindView(R.id.et_forget_pwd_number)
    EditText numberEt;//手机号输入框
    @BindView(R.id.et_forget_pwd)
    EditText codeEt;//验证码输入框
    @BindView(R.id.tv_forget_pwd_submit)
    TextView logTv;
    @BindView(R.id.tv_forget_pwd)
    TextView getTv;//获取验证码
    private Unbinder unbinder;
    boolean eyes1_boolean = true,eyes2_boolean = true;
    private CProgressDialog cpd;
    String phone = "";//手机号MD5加密后的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        unbinder = ButterKnife.bind(this);
        cpd = Utils.initProgressDialog(ForgetPwdActivity.this, cpd);
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
    @OnClick({R.id.rl_forget_pwd_return,R.id.tv_forget_pwd,R.id.tv_forget_pwd_submit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_forget_pwd_return:
                finish();
                break;
            case R.id.tv_forget_pwd:
                    getCode(numberEt.getText().toString());
                break;
            case R.id.tv_forget_pwd_submit:
                jumpChangePwd();
                break;
        }
    }

    private void jumpChangePwd(){
        PhoneBean phoneBean = new PhoneBean();
        phoneBean.setVerify_code(codeEt.getText().toString());
        phoneBean.setNumber(numberEt.getText().toString());
        UserUtils.savePhoneData(ForgetPwdActivity.this,phoneBean);
        Intent it = new Intent(ForgetPwdActivity.this,ChangePwdActivity.class);
        it.putExtra("activity","forget");
        startActivity(it);
        finish();
    }
    private void getCode(final String number) {
        if (Utils.isPhonenumber(number)) {
            cpd.show();
            phone = MD5.md5(numberEt.getText().toString());
            OkHttpUtils
                    .get()
                    .tag(this)
//                    .url(NetConfig.testGetCode)
                    .addParams("phone_number", number)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            cpd.dismiss();
                            Utils.toast(ForgetPwdActivity.this, VarConfig.noNetTip);
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
                                        Utils.toast(ForgetPwdActivity.this, msg);
                                    if (code == 1) {
                                        startService(new Intent(ForgetPwdActivity.this, GetLoginCodeTimerService.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {
            Utils.toast(ForgetPwdActivity.this, "手机号格式不正确");
        }
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
