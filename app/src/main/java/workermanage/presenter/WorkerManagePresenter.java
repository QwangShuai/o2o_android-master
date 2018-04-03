package workermanage.presenter;


import android.os.Handler;

import com.gjzg.listener.JsonListener;
import workermanage.module.IWorkerManageModule;
import workermanage.module.WorkerManageModule;
import workermanage.view.IWorkerManageActivity;

public class WorkerManagePresenter implements IWorkerManagePresenter {

    private IWorkerManageActivity workerManageActivity;
    private IWorkerManageModule workerManageModule;
    private Handler handler;

    public WorkerManagePresenter(IWorkerManageActivity workerManageActivity) {
        this.workerManageActivity = workerManageActivity;
        workerManageModule = new WorkerManageModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        workerManageModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        workerManageActivity.loadSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        workerManageActivity.loadFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (workerManageModule != null) {
            workerManageModule.cancelTask();
            workerManageModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (workerManageActivity != null) {
            workerManageActivity = null;
        }
    }
}
