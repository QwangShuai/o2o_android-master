package workermanage.module;


import java.io.IOException;

import com.gjzg.config.VarConfig;
import com.gjzg.listener.JsonListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WorkerManageModule implements IWorkerManageModule {

    private OkHttpClient okHttpClient;
    private Call workerManageCall;

    public WorkerManageModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void load(String url, final JsonListener jsonListener) {
        Request request = new Request.Builder().url(url).get().build();
        workerManageCall = okHttpClient.newCall(request);
        workerManageCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                jsonListener.failure(VarConfig.noNet);
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
        if (workerManageCall != null) {
            workerManageCall.cancel();
            workerManageCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
