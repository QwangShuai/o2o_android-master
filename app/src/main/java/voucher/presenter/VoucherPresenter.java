package voucher.presenter;


import android.os.Handler;

import com.gjzg.listener.JsonListener;
import voucher.module.IVoucherModule;
import voucher.module.VoucherModule;
import voucher.view.IVoucherActivity;

public class VoucherPresenter implements IVoucherPresenter {

    private IVoucherActivity voucherActivity;
    private IVoucherModule voucherModule;
    private Handler handler;

    public VoucherPresenter(IVoucherActivity voucherActivity) {
        this.voucherActivity = voucherActivity;
        voucherModule = new VoucherModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        voucherModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        voucherActivity.loadSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        voucherActivity.loadFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (voucherModule != null) {
            voucherModule.cancelTask();
            voucherModule = null;
        }
        if (voucherActivity != null) {
            voucherActivity = null;
        }
        if (handler != null) {
            handler = null;
        }
    }
}
