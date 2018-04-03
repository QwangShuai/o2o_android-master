package password.module;

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
import password.listener.EditPwdListener;
import password.listener.ForgetPwdListener;
import password.listener.ProveOriPwdListener;
import password.listener.SetPwdListener;


public class PwdModule implements IPwdModule {

    private OkHttpClient okHttpClient;
    private Call setPwdCall, proveOriPwdCall, editPwdCall, forgetPwdCall;

    public PwdModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void setPwd(String id, String pass, final SetPwdListener setPwdListener) {
        RequestBody setPwdBody = new FormBody.Builder()
                .add("u_id", id)
                .add("u_pass", pass)
                .build();
        Request setPwdRequest = new Request.Builder()
                .url(NetConfig.setPwdUrl)
                .post(setPwdBody)
                .build();
        setPwdCall = okHttpClient.newCall(setPwdRequest);
        setPwdCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setPwdListener.failure(e.getMessage());
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
                                        setPwdListener.failure(msg);
                                        break;
                                    case 1:
                                        setPwdListener.success(msg);
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
    public void proveOriPwd(String id, String pass, final ProveOriPwdListener proveOriPwdListener) {
        RequestBody proveOriPwdBody = new FormBody.Builder()
                .add("u_id", id)
                .add("u_pass", pass)
                .build();
        Request proveOriPwdRequest = new Request.Builder()
                .url(NetConfig.proveOriPwdUrl)
                .post(proveOriPwdBody)
                .build();
        proveOriPwdCall = okHttpClient.newCall(proveOriPwdRequest);
        proveOriPwdCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                proveOriPwdListener.failure(e.getMessage());
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
                                        proveOriPwdListener.failure(msg);
                                        break;
                                    case 1:
                                        proveOriPwdListener.success(msg);
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
    public void editPwd(String id, String oldPass, String newPass, final EditPwdListener editPwdListener) {
        RequestBody editPwdBody = new FormBody.Builder()
                .add("u_id", id)
                .add("u_pass", oldPass)
                .add("new_pass", newPass)
                .build();
        Request editPwdRequest = new Request.Builder()
                .url(NetConfig.editPwdUrl)
                .post(editPwdBody)
                .build();
        editPwdCall = okHttpClient.newCall(editPwdRequest);
        editPwdCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                editPwdListener.failure(e.getMessage());
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
                                        editPwdListener.failure(msg);
                                        break;
                                    case 1:
                                        editPwdListener.success(msg);
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
    public void forgetPwd(String mobile, String verifycode, String newPwd, String idcard, final ForgetPwdListener forgetPwdListener) {
        RequestBody forgetPwdBody = new FormBody.Builder()
                .add("u_mobile", mobile)
                .add("verify_code", verifycode)
                .add("new_pass", newPwd)
                .add("u_idcard", idcard)
                .build();
        Request forgetPwdRequest = new Request.Builder()
                .url(NetConfig.fgtPwdUrl)
                .post(forgetPwdBody)
                .build();
        forgetPwdCall = okHttpClient.newCall(forgetPwdRequest);
        forgetPwdCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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
                                        forgetPwdListener.failure(msg);
                                        break;
                                    case 1:
                                        forgetPwdListener.success(msg);
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
        if (setPwdCall != null) {
            setPwdCall.cancel();
            setPwdCall = null;
        }
        if (proveOriPwdCall != null) {
            proveOriPwdCall.cancel();
            proveOriPwdCall = null;
        }
        if (editPwdCall != null) {
            editPwdCall.cancel();
            editPwdCall = null;
        }
        if (forgetPwdCall != null) {
            forgetPwdCall.cancel();
            forgetPwdCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
