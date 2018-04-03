package password.presenter;


public interface IPwdPresenter {

    void setPwd(String id, String pass);

    void proveOriPwd(String id, String pass);

    void editPwd(String id, String oldPass, String newPass);

    void forgetPwd(String mobile,String verifycode,String newPwd,String idcard);

    void destroy();
}
