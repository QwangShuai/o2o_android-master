package voucher.module;


import com.gjzg.listener.JsonListener;

public interface IVoucherModule {

    void load(String url, JsonListener jsonListener);

    void cancelTask();
}
