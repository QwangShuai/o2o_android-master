package talkworker.presenter;

import android.os.Handler;

import com.gjzg.listener.JsonListener;
import talkworker.module.ITalkWorkerModule;
import talkworker.module.TalkWorkerModule;
import talkworker.view.ITalkWorkerActivity;


public class TalkWorkerPresenter implements ITalkWorkerPresenter {

    private ITalkWorkerActivity talkWorkerActivity;
    private ITalkWorkerModule talkWorkerModule;
    private Handler handler;

    public TalkWorkerPresenter(ITalkWorkerActivity talkWorkerActivity) {
        this.talkWorkerActivity = talkWorkerActivity;
        talkWorkerModule = new TalkWorkerModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        talkWorkerModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        talkWorkerActivity.loadSuccess(json);
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
                                talkWorkerActivity.loadFailure(failure);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void check(String url) {
        talkWorkerModule.check(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        talkWorkerActivity.checkSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        talkWorkerActivity.checkFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (talkWorkerModule != null) {
            talkWorkerModule.cancelTask();
            talkWorkerModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (talkWorkerActivity != null) {
            talkWorkerActivity = null;
        }
    }
}
