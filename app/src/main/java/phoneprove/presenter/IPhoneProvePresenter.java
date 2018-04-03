package phoneprove.presenter;


public interface IPhoneProvePresenter {

    void getVerifyCode(String mobile);

    void proveMobileCode(String mobile,String code);

    void destroy();
}
