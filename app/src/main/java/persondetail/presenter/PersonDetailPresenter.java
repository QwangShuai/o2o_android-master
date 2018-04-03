package persondetail.presenter;

import android.os.Handler;

import com.gjzg.listener.JsonListener;
import persondetail.module.IPersonDetailModule;
import persondetail.module.PersonDetailModule;
import persondetail.view.IPersonDetailActivity;


public class PersonDetailPresenter implements IPersonDetailPresenter {

    private IPersonDetailActivity personDetailActivity;
    private IPersonDetailModule personDetailModule;
    private Handler handler;

    public PersonDetailPresenter(IPersonDetailActivity personDetailActivity) {
        this.personDetailActivity = personDetailActivity;
        personDetailModule = new PersonDetailModule();
        handler = new Handler();
    }

    @Override
    public void info(String url) {
        personDetailModule.info(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personDetailActivity.infoSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personDetailActivity.infoFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void getSkill(String url) {
        personDetailModule.getSkill(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personDetailActivity.skillSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personDetailActivity.skillFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void evaluate(String url) {
        personDetailModule.evaluate(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personDetailActivity.evaluateSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personDetailActivity.evaluateFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (personDetailModule != null) {
            personDetailModule.cancelTask();
            personDetailModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (personDetailActivity != null) {
            personDetailActivity = null;
        }
    }
}
