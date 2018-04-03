package talkworker.view;


public interface ITalkWorkerActivity {

    void loadSuccess(String json);

    void loadFailure(String failure);

    void checkSuccess(String json);

    void checkFailure(String failure);

}
