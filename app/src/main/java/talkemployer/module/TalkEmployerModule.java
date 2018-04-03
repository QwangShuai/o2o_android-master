package talkemployer.module;

import com.gjzg.listener.JsonListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TalkEmployerModule implements ITalkEmployerModule {

    private OkHttpClient okHttpClient;
    private Call call, skillCall, inviteCall;

    public TalkEmployerModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void load(String url, final JsonListener jsonListener) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
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
    public void loadSkill(String url, final JsonListener jsonListener) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        skillCall = okHttpClient.newCall(request);
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
    public void invite(String url, final JsonListener jsonListener) {
        Request inviteRequest = new Request.Builder().url(url).get().build();
        inviteCall = okHttpClient.newCall(inviteRequest);
        inviteCall.enqueue(new Callback() {
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
        if (call != null) {
            call.cancel();
            call = null;
        }
        if (skillCall != null) {
            skillCall.cancel();
            skillCall = null;
        }
        if (inviteCall != null) {
            inviteCall.cancel();
            inviteCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
