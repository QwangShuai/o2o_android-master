package publishjob.module;

import publishjob.listener.GetSkillListener;
import publishjob.listener.TaskTypeListener;


public interface IPublishJobModule {

    void getTaskType(String url, TaskTypeListener taskTypeListener);

    void getSkill(String url, GetSkillListener getSkillListener);

    void cancelTask();
}
