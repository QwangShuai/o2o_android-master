package talkworker.presenter;


public interface ITalkWorkerPresenter {

    void load(String url);

    void check(String url);

    void destroy();
}
