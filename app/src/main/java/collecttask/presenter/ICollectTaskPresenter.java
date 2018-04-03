package collecttask.presenter;


public interface ICollectTaskPresenter {

    void load(String url);

    void cancelCollect(String url);

    void destroy();
}
