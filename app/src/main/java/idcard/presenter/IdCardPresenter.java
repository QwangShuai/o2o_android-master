package idcard.presenter;

import android.os.Handler;

import idcard.listener.IdCardListener;
import idcard.module.IIdCardModule;
import idcard.module.IdCardModule;
import idcard.view.IIdCardActivity;


public class IdCardPresenter implements IIdCardPresenter {

    private IIdCardActivity idCardActivity;
    private IIdCardModule idCardModule;
    private Handler handler;

    public IdCardPresenter(IIdCardActivity idCardActivity) {
        this.idCardActivity = idCardActivity;
        idCardModule = new IdCardModule();
        handler = new Handler();
    }

    @Override
    public void verify(String mobile, String idcard) {
        idCardModule.verify(mobile, idcard, new IdCardListener() {
            @Override
            public void success(final String success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        idCardActivity.showVerifySuccess(success);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        idCardActivity.showVerifyFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (idCardModule != null) {
            idCardModule.cancelTask();
            idCardModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (idCardActivity != null) {
            idCardActivity = null;
        }
    }
}
