package selecttask.presenter;

import android.os.Handler;

import com.gjzg.listener.JsonListener;
import selecttask.module.ISelectTaskModule;
import selecttask.module.SelectTaskModule;
import selecttask.view.ISelectTaskActivity;


public class SelectTaskPresenter implements ISelectTaskPresenter {

    private ISelectTaskActivity selectTaskActivity;
    private ISelectTaskModule selectTaskModule;
    private Handler handler;

    public SelectTaskPresenter(ISelectTaskActivity selectTaskActivity) {
        this.selectTaskActivity = selectTaskActivity;
        selectTaskModule = new SelectTaskModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        selectTaskModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        selectTaskActivity.loadSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        selectTaskActivity.loadFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void invite(String url) {
        selectTaskModule.invite(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        selectTaskActivity.inviteSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        selectTaskActivity.inviteFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (selectTaskModule != null) {
            selectTaskModule.cancelTask();
            selectTaskModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (selectTaskActivity != null) {
            selectTaskActivity = null;
        }
    }
}
