package phoneprove.view;


public interface IPhoneProveActivity {

    void showVerifyCodeSuccess(String success);

    void showVerifyCodeFailure(String failure);

    void showProveMobileCodeSuccess(String success);

    void showProveMobileCodeFailure(String failure);
}
