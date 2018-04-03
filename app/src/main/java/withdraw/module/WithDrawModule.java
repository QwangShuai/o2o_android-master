package withdraw.module;

import com.gjzg.bean.WithDrawBean;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.listener.JsonListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WithDrawModule implements IWithDrawModule {

    private OkHttpClient okHttpClient;
    private Call withDrawCall;

    public WithDrawModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void withdraw(WithDrawBean withDrawBean, final JsonListener jsonListener) {
        RequestBody requestBody = new FormBody.Builder()
                .add("u_id", withDrawBean.getU_id())
                .add("uwl_amount", withDrawBean.getUwl_amount())
                .add("p_id", withDrawBean.getP_id())
                .add("uwl_card", withDrawBean.getUwl_card())
                .add("uwl_truename", withDrawBean.getUwl_truename())
                .add("u_pass", withDrawBean.getPassword())
                .build();
        Request request = new Request.Builder().url(NetConfig.applyWithdrawUrl).post(requestBody).build();
        withDrawCall = okHttpClient.newCall(request);
        withDrawCall.enqueue(new Callback() {
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
        if (withDrawCall != null) {
            withDrawCall.cancel();
            withDrawCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
