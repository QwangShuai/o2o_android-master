package selectaddress.presenter;


import android.os.Handler;

import java.util.List;

import com.gjzg.bean.SelectAddressBean;
import selectaddress.listener.OnLoadSelAddListener;
import selectaddress.module.ISelectAddressModule;
import selectaddress.module.SelectAddressModule;
import selectaddress.view.ISelectAddressActivity;

public class SelectAddressPresenter implements ISelectAddressPresenter {

    private ISelectAddressActivity iSelectAddressActivity;
    private ISelectAddressModule iSelectAddressModule;
    private Handler handler;

    public SelectAddressPresenter(ISelectAddressActivity iSelectAddressActivity) {
        this.iSelectAddressActivity = iSelectAddressActivity;
        iSelectAddressModule = new SelectAddressModule();
        handler = new Handler();
    }

    @Override
    public void load(String id) {
        iSelectAddressActivity.showLoading();
        iSelectAddressModule.load(id, new OnLoadSelAddListener() {
            @Override
            public void success(final List<SelectAddressBean> selectAddressBeanList) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iSelectAddressActivity.showSuccess(selectAddressBeanList);
                        iSelectAddressActivity.hideLoading();
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iSelectAddressActivity.showFailure(failure);
                        iSelectAddressActivity.hideLoading();
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (iSelectAddressModule != null) {
            iSelectAddressModule.cancelTask();
            iSelectAddressModule = null;
        }
        if (iSelectAddressActivity != null) {
            iSelectAddressActivity = null;
        }
        if (handler != null) {
            handler = null;
        }
    }
}
