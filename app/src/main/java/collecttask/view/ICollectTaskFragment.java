package collecttask.view;

public interface ICollectTaskFragment {

    void loadSuccess(String json);

    void loadFailure(String failure);

    void cancelCollectSuccess(String json);

    void cancelCollectFailure(String failure);
}
