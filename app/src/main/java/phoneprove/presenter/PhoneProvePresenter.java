package phoneprove.presenter;

import android.os.Handler;

import phoneprove.listener.ForgetPwdCodeListener;
import phoneprove.listener.ProveMobileCodeListener;
import phoneprove.module.IPhoneProveModule;
import phoneprove.module.PhoneProveModule;
import phoneprove.view.IPhoneProveActivity;


public class PhoneProvePresenter implements IPhoneProvePresenter {

    private IPhoneProveActivity phoneProveActivity;
    private IPhoneProveModule phoneProveModule;
    private Handler handler;

    public PhoneProvePresenter(IPhoneProveActivity phoneProveActivity) {
        this.phoneProveActivity = phoneProveActivity;
        phoneProveModule = new PhoneProveModule();
        handler = new Handler();
    }

    @Override
    public void getVerifyCode(String mobile) {
        phoneProveModule.getVerifyCode(mobile, new ForgetPwdCodeListener() {
            @Override
            public void success(final String success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        phoneProveActivity.showVerifyCodeSuccess(success);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        phoneProveActivity.showVerifyCodeFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void proveMobileCode(String mobile, String code) {
        phoneProveModule.proveMobileCode(mobile, code, new ProveMobileCodeListener() {
            @Override
            public void success(final String success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        phoneProveActivity.showProveMobileCodeSuccess(success);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        phoneProveActivity.showProveMobileCodeFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (phoneProveModule != null) {
            phoneProveModule.cancelTask();
            phoneProveModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (phoneProveActivity != null) {
            phoneProveActivity = null;
        }
    }
}
