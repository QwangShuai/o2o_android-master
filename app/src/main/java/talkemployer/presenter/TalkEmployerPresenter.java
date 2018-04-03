package talkemployer.presenter;

import android.os.Handler;

import com.gjzg.listener.JsonListener;
import talkemployer.module.ITalkEmployerModule;
import talkemployer.module.TalkEmployerModule;
import talkemployer.view.ITalkEmployerActivity;


public class TalkEmployerPresenter implements ITalkEmployerPresenter {

    private ITalkEmployerActivity talkEmployerActivity;
    private ITalkEmployerModule talkEmployerModule;
    private Handler handler;

    public TalkEmployerPresenter(ITalkEmployerActivity talkEmployerActivity) {
        this.talkEmployerActivity = talkEmployerActivity;
        talkEmployerModule = new TalkEmployerModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        talkEmployerModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        talkEmployerActivity.success(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        talkEmployerActivity.failure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void loadSkill(String url) {
        talkEmployerModule.loadSkill(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        talkEmployerActivity.skillSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        talkEmployerActivity.skillFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void invite(String url) {
        talkEmployerModule.invite(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        talkEmployerActivity.inviteSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        talkEmployerActivity.inviteFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (talkEmployerModule != null) {
            talkEmployerModule.cancelTask();
            talkEmployerModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (talkEmployerActivity != null) {
            talkEmployerActivity = null;
        }
    }
}
