package mine.presenter;


public interface IMinePresenter {

    void load(String id);

    void postOnline(String id, String online);

    void destroy();
}
