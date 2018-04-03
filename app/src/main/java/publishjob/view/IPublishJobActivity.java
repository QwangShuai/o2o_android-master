package publishjob.view;

import java.util.List;


public interface IPublishJobActivity {

    void taskTypeSuccess(List<String> taskTypeList);

    void taskTypeFailure(String failure);

    void skillSuccess(String json);

    void skillFailure(String failure);
}
