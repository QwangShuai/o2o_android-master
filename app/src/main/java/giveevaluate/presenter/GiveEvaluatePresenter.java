package giveevaluate.presenter;

import android.os.Handler;

import com.gjzg.listener.JsonListener;

import giveevaluate.module.GiveEvaluateModule;
import giveevaluate.module.IGiveEvaluateModule;
import giveevaluate.view.IGiveEvaluateFragment;


public class GiveEvaluatePresenter implements IGiveEvaluatePresenter {

    private IGiveEvaluateFragment giveEvaluateFragment;
    private IGiveEvaluateModule giveEvaluateModule;
    private Handler handler;

    public GiveEvaluatePresenter(IGiveEvaluateFragment giveEvaluateFragment) {
        this.giveEvaluateFragment = giveEvaluateFragment;
        giveEvaluateModule = new GiveEvaluateModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        giveEvaluateModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        giveEvaluateFragment.loadSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        giveEvaluateFragment.loadFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (giveEvaluateModule != null) {
            giveEvaluateModule.cancelTask();
            giveEvaluateModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (giveEvaluateFragment != null) {
            giveEvaluateFragment = null;
        }
    }
}
