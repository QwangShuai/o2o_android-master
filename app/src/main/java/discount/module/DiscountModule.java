package discount.module;


import discount.listener.OnLoadDiscountListener;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class DiscountModule implements IDiscountModule {

    private OkHttpClient okHttpClient;
    private Call call;

    public DiscountModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void load(OnLoadDiscountListener onLoadDiscountListener) {

    }

    @Override
    public void cancelTask() {
        if (call != null) {
            call.cancel();
            call = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
