package selecttask.view;


public interface ISelectTaskActivity {

    void loadSuccess(String json);

    void loadFailure(String failure);

    void inviteSuccess(String json);

    void inviteFailure(String failure);
}
