package publishjob.presenter;

import android.os.Handler;

import java.util.List;

import publishjob.listener.GetSkillListener;
import publishjob.listener.TaskTypeListener;
import publishjob.module.IPublishJobModule;
import publishjob.module.PublishJobModule;
import publishjob.view.IPublishJobActivity;


public class PublishJobPresenter implements IPublishJobPresenter {

    private IPublishJobActivity publishJobActivity;
    private IPublishJobModule publishJobModule;
    private Handler handler;

    public PublishJobPresenter(IPublishJobActivity publishJobActivity) {
        this.publishJobActivity = publishJobActivity;
        publishJobModule = new PublishJobModule();
        handler = new Handler();
    }

    @Override
    public void getTaskType(String url) {
        publishJobModule.getTaskType(url, new TaskTypeListener() {
            @Override
            public void success(final List<String> taskTypeList) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        publishJobActivity.taskTypeSuccess(taskTypeList);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        publishJobActivity.taskTypeFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void getSkill(String url) {
        publishJobModule.getSkill(url, new GetSkillListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        publishJobActivity.skillSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        publishJobActivity.skillFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (publishJobModule != null) {
            publishJobModule.cancelTask();
            publishJobModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (publishJobActivity != null) {
            publishJobActivity = null;
        }
    }
}
