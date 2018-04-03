package idcard.module;

import android.text.TextUtils;

import com.gjzg.config.NetConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import idcard.listener.IdCardListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class IdCardModule implements IIdCardModule {

    private OkHttpClient okHttpClient;
    private Call verifyCall;

    public IdCardModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void verify(String mobile, String idcard, final IdCardListener idCardListener) {
        RequestBody verifyBody = new FormBody.Builder()
                .add("u_mobile", mobile)
                .add("u_idcard", idcard)
                .build();
        Request verifyRequest = new Request.Builder()
                .url(NetConfig.proveIdcardUrl)
                .post(verifyBody)
                .build();
        verifyCall = okHttpClient.newCall(verifyRequest);
        verifyCall.enqueue(new Callback() {
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
                                        idCardListener.failure(msg);
                                        break;
                                    case 1:
                                        idCardListener.success(msg);
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
        if (verifyCall != null) {
            verifyCall.cancel();
            verifyCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
