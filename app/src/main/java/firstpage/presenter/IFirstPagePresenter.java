package firstpage.presenter;


public interface IFirstPagePresenter {

    void loadHotCity(String hotUrl);

    void loadComCity(String comUrl);

    void getLocId(String[] letter, String locCity, String comJson);

    void changePosition(String url);

    void destroy();
}
