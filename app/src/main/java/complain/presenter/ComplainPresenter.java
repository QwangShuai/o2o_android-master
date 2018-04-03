package complain.presenter;


import android.os.Handler;

import com.gjzg.bean.ToComplainBean;
import complain.module.ComplainModule;
import complain.module.IComplainModule;
import complain.view.IComplainActivity;
import com.gjzg.listener.JsonListener;

public class ComplainPresenter implements IComplainPresenter {

    private IComplainActivity complainActivity;
    private IComplainModule complainModule;
    private Handler handler;

    public ComplainPresenter(IComplainActivity complainActivity) {
        this.complainActivity = complainActivity;
        complainModule = new ComplainModule();
        handler = new Handler();
    }

    @Override
    public void userInfo(String url) {
        complainModule.userInfo(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        complainActivity.userInfoSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        complainActivity.userInfoFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void userIssue(String url) {
        complainModule.userIssue(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        complainActivity.userIssueSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        complainActivity.userIssueFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void submit(String url, ToComplainBean toComplainBean) {
        complainModule.submit(url, toComplainBean, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        complainActivity.submitSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        complainActivity.submitFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destory() {
        if (complainModule != null) {
            complainModule.cancelTask();
            complainModule = null;
        }
        if (complainActivity != null) {
            complainActivity = null;
        }
        if (handler != null) {
            handler = null;
        }
    }
}
