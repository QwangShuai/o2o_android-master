package mine.module;

import android.text.TextUtils;

import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.utils.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mine.listener.MineListener;
import mine.listener.OnLineListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MineModule implements IMineModule {

    private OkHttpClient okHttpClient;
    private Call userInfoCall, postOnlineCall;

    public MineModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void load(String id, final MineListener mineListener) {
        Request request = new Request.Builder().url(NetConfig.userInfoUrl + id).get().build();
        userInfoCall = okHttpClient.newCall(request);
        userInfoCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mineListener.failure(VarConfig.noNet);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    mineListener.success(DataUtils.getUserInfoBean(json));
                }
            }
        });
    }

    @Override
    public void postOnline(String id, String online, final OnLineListener onLineListener) {
        RequestBody requestBody = new FormBody.Builder()
                .add("u_id", id)
                .add("u_online", online)
                .build();
        Request request = new Request.Builder().url(NetConfig.userInfoEditUrl).post(requestBody).build();
        postOnlineCall = okHttpClient.newCall(request);
        postOnlineCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLineListener.failure(e.getMessage());
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
                                        onLineListener.failure(msg);
                                        break;
                                    case 1:
                                        onLineListener.success(msg);
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
        if (userInfoCall != null) {
            userInfoCall.cancel();
            userInfoCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
