package collecttask.presenter;


import android.os.Handler;

import collecttask.module.CollectTaskModule;
import collecttask.module.ICollectTaskModule;
import collecttask.view.ICollectTaskFragment;
import com.gjzg.listener.JsonListener;

public class CollectTaskPresenter implements ICollectTaskPresenter {

    private ICollectTaskFragment collectTaskFragment;
    private ICollectTaskModule collectTaskModule;
    private Handler handler;

    public CollectTaskPresenter(ICollectTaskFragment collectTaskFragment) {
        this.collectTaskFragment = collectTaskFragment;
        collectTaskModule = new CollectTaskModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        collectTaskModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        collectTaskFragment.loadSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        collectTaskFragment.loadFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void cancelCollect(String url) {
        collectTaskModule.cancelCollect(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        collectTaskFragment.cancelCollectSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        collectTaskFragment.cancelCollectFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (collectTaskModule != null) {
            collectTaskModule.cancelTask();
            collectTaskModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (collectTaskFragment != null) {
            collectTaskFragment = null;
        }
    }
}
