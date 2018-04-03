package complain.module;

import java.io.IOException;

import com.gjzg.bean.ToComplainBean;
import com.gjzg.listener.JsonListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ComplainModule implements IComplainModule {

    private OkHttpClient okHttpClient;
    private Call call, userInfoCall, submitCall;

    public ComplainModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void userInfo(String url, final JsonListener jsonListener) {
        Request userInfoRequest = new Request.Builder().url(url).get().build();
        userInfoCall = okHttpClient.newCall(userInfoRequest);
        userInfoCall.enqueue(new Callback() {
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
    public void userIssue(String url, final JsonListener jsonListener) {
        Request request = new Request.Builder().url(url).get().build();
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
    public void submit(String url, ToComplainBean toComplainBean, final JsonListener jsonListener) {
        RequestBody submitBody = new FormBody.Builder()
                .add("c_author", toComplainBean.getAuthorId())
                .add("c_against", toComplainBean.getAgainstId())
                .add("ct_id", toComplainBean.getCtId())
                .add("c_desc", toComplainBean.getContent())
                .build();
        Request submitRequest = new Request.Builder()
                .url(url)
                .post(submitBody)
                .build();
        submitCall = okHttpClient.newCall(submitRequest);
        submitCall.enqueue(new Callback() {
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
        if (userInfoCall != null) {
            userInfoCall.cancel();
            userInfoCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
