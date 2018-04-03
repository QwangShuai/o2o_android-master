package getevaluate.presenter;

import android.os.Handler;

import com.gjzg.listener.JsonListener;

import getevaluate.module.GetEvaluateModule;
import getevaluate.module.IGetEvaluateModule;
import getevaluate.view.IGetEvaluateFragment;


public class GetEvaluatePresenter implements IGetEvaluatePresenter {

    private IGetEvaluateFragment getEvaluateFragment;
    private IGetEvaluateModule getEvaluateModule;
    private Handler handler;

    public GetEvaluatePresenter(IGetEvaluateFragment getEvaluateFragment) {
        this.getEvaluateFragment = getEvaluateFragment;
        getEvaluateModule = new GetEvaluateModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        getEvaluateModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getEvaluateFragment.loadSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getEvaluateFragment.loadFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (getEvaluateModule != null) {
            getEvaluateModule.cancelTask();
            getEvaluateModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (getEvaluateFragment != null) {
            getEvaluateFragment = null;
        }
    }
}
