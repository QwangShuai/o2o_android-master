package userinfo.view;


public interface IUserInfoFragment {

    void infoSuccess(String json);

    void infoFailure(String failure);

    void skillSuccess(String json);

    void skillFailure(String failure);
}
