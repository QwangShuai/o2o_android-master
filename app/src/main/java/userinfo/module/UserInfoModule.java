package userinfo.module;

import com.gjzg.listener.JsonListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UserInfoModule implements IUserInfoModule {

    private OkHttpClient okHttpClient;
    private Call infoCall, skillCall;

    public UserInfoModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void info(String url, final JsonListener jsonListener) {
        Request infoRequest = new Request.Builder().url(url).get().build();
        infoCall = okHttpClient.newCall(infoRequest);
        infoCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                jsonListener.failure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    jsonListener.success(response.body().string());
                }
            }
        });
    }

    @Override
    public void skill(String url, final JsonListener jsonListener) {
        Request skillRequest = new Request.Builder().url(url).get().build();
        skillCall = okHttpClient.newCall(skillRequest);
        skillCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                jsonListener.failure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    jsonListener.success(response.body().string());
                }
            }
        });
    }

    @Override
    public void cancelTask() {
        if (infoCall != null) {
            infoCall.cancel();
            infoCall = null;
        }
        if (skillCall != null) {
            skillCall.cancel();
            skillCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
