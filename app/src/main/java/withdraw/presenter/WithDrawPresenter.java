package withdraw.presenter;

import android.os.Handler;

import com.gjzg.bean.WithDrawBean;
import com.gjzg.listener.JsonListener;
import withdraw.module.IWithDrawModule;
import withdraw.module.WithDrawModule;
import withdraw.view.IWithDrawActivity;


public class WithDrawPresenter implements IWithDrawPresenter {

    private IWithDrawActivity withDrawActivity;
    private IWithDrawModule withDrawModule;
    private Handler handler;

    public WithDrawPresenter(IWithDrawActivity withDrawActivity) {
        this.withDrawActivity = withDrawActivity;
        withDrawModule = new WithDrawModule();
        handler = new Handler();
    }

    @Override
    public void withdraw(WithDrawBean withDrawBean) {
        withDrawModule.withdraw(withDrawBean, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        withDrawActivity.loadSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        withDrawActivity.loadFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (withDrawModule != null) {
            withDrawModule.cancelTask();
            withDrawModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (withDrawActivity != null) {
            withDrawActivity = null;
        }
    }
}
