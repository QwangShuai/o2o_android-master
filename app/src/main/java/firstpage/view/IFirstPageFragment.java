package firstpage.view;


public interface IFirstPageFragment {

    void showHotSuccess(String json);

    void showHotFailure(String failure);

    void showComSuccess(String json);

    void showComFailure(String failure);

    void showLocIdSuccess(String id);

    void showLocIdFailure(String failure);

    void changePositionSuccess(String json);

    void changePositionFailure(String failure);
}
