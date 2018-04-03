package publishjob.listener;

import java.util.List;


public interface TaskTypeListener {

    void success(List<String> taskTypeList);

    void failure(String failure);
}
