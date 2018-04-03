package employermanage.presenter;

import android.os.Handler;

import com.gjzg.listener.JsonListener;

import employermanage.module.EmployerManageModule;
import employermanage.module.IEmployerManageModule;
import employermanage.view.IEmployerManageActivity;


public class EmployerManagePresenter implements IEmployerManagePresenter {

    private IEmployerManageActivity employerManageActivity;
    private IEmployerManageModule employerManageModule;
    private Handler handler;

    public EmployerManagePresenter(IEmployerManageActivity employerManageActivity) {
        this.employerManageActivity = employerManageActivity;
        employerManageModule = new EmployerManageModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        employerManageModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        employerManageActivity.loadSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        employerManageActivity.loadFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void cancel(String url) {
        employerManageModule.cancel(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        employerManageActivity.cancelSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                employerManageActivity.cancelFailure(failure);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (employerManageModule != null) {
            employerManageModule.cancelTask();
            employerManageModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (employerManageActivity != null) {
            employerManageActivity = null;
        }
    }
}
