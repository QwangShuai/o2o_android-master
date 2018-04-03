package phoneprove.module;

import android.text.TextUtils;

import com.gjzg.config.NetConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import phoneprove.listener.ForgetPwdCodeListener;
import phoneprove.listener.ProveMobileCodeListener;


public class PhoneProveModule implements IPhoneProveModule {

    private OkHttpClient okHttpClient;
    private Call verifyCodeCall, proveMobileCodeCall;

    public PhoneProveModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void getVerifyCode(String mobile, final ForgetPwdCodeListener forgetPwdCodeListener) {
        Request verifyCodeRequest = new Request.Builder()
                .url(NetConfig.fgtPwdCodeUrl + mobile)
                .get()
                .build();
        verifyCodeCall = okHttpClient.newCall(verifyCodeRequest);
        verifyCodeCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                forgetPwdCodeListener.failure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject beanObj = new JSONObject(json);
                            int code = beanObj.optInt("code");
                            JSONObject dataObj = beanObj.optJSONObject("data");
                            if (dataObj != null) {
                                String msg = dataObj.optString("msg");
                                switch (code) {
                                    case 0:
                                        forgetPwdCodeListener.failure(msg);
                                        break;
                                    case 1:
                                        forgetPwdCodeListener.success(msg);
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void proveMobileCode(String mobile, String code, final ProveMobileCodeListener proveMobileCodeListener) {
        RequestBody proveMobileCodeBody = new FormBody.Builder()
                .add("u_mobile", mobile)
                .add("verify_code", code)
                .build();
        Request proveMobileCodeRequest = new Request.Builder()
                .url(NetConfig.provePhoneUrl)
                .post(proveMobileCodeBody)
                .build();
        proveMobileCodeCall = okHttpClient.newCall(proveMobileCodeRequest);
        proveMobileCodeCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                proveMobileCodeListener.failure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject beanObj = new JSONObject(json);
                            int code = beanObj.optInt("code");
                            JSONObject dataObj = beanObj.optJSONObject("data");
                            if (dataObj != null) {
                                String msg = dataObj.optString("msg");
                                switch (code) {
                                    case 0:
                                        proveMobileCodeListener.failure(msg);
                                        break;
                                    case 1:
                                        proveMobileCodeListener.success(msg);
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void cancelTask() {
        if (verifyCodeCall != null) {
            verifyCodeCall.cancel();
            verifyCodeCall = null;
        }
        if (proveMobileCodeCall != null) {
            proveMobileCodeCall.cancel();
            proveMobileCodeCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
