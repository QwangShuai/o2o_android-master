package idcard.presenter;


public interface IIdCardPresenter {

    void verify(String mobile,String idcard);

    void destroy();
}
