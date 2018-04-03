package talkworker.module;

import com.gjzg.listener.JsonListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TalkWorkerModule implements ITalkWorkerModule {

    private OkHttpClient okHttpClient;
    private Call call, checkCall;

    public TalkWorkerModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void load(String url, final JsonListener jsonListener) {
        Request request = new Request
                .Builder()
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
    public void check(String url, final JsonListener jsonListener) {
        Request checkRequest = new Request.Builder().url(url).get().build();
        checkCall = okHttpClient.newCall(checkRequest);
        checkCall.enqueue(new Callback() {
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
        if (checkCall != null) {
            checkCall.cancel();
            checkCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
