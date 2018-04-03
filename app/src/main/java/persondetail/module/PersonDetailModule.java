package persondetail.module;

import com.gjzg.listener.JsonListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PersonDetailModule implements IPersonDetailModule {

    private OkHttpClient okHttpClient;
    private Call infoCall, evaluateCall, getSkillCall;

    public PersonDetailModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void info(String url, final JsonListener jsonListener) {
        Request infoRequest = new Request
                .Builder()
                .url(url)
                .get()
                .build();
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
    public void getSkill(String url, final JsonListener jsonListener) {
        Request skillRequest = new Request
                .Builder()
                .url(url)
                .get()
                .build();
        getSkillCall = okHttpClient.newCall(skillRequest);
        getSkillCall.enqueue(new Callback() {
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
    public void evaluate(String url, final JsonListener jsonListener) {
        Request evaluateRequest = new Request
                .Builder()
                .url(url)
                .get()
                .build();
        evaluateCall = okHttpClient.newCall(evaluateRequest);
        evaluateCall.enqueue(new Callback() {
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
        if (getSkillCall != null) {
            getSkillCall.cancel();
            getSkillCall = null;
        }
        if (evaluateCall != null) {
            evaluateCall.cancel();
            evaluateCall = null;
        }
    }
}
