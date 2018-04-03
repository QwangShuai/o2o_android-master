package employermanage.view;


public interface IEmployerManageActivity {

    void loadSuccess(String json);

    void loadFailure(String failure);

    void cancelSuccess(String json);

    void cancelFailure(String failure);
}
