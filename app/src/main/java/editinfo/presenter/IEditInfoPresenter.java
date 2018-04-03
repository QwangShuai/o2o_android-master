package editinfo.presenter;


import com.gjzg.bean.UserInfoBean;

public interface IEditInfoPresenter {

    void skill(String url);

    void load(String url);

    void submit(UserInfoBean userInfoBean);

    void destroy();
}
