package mine.view;

import com.gjzg.bean.UserInfoBean;


public interface IMineFragment {

    void showUserInfoSuccess(UserInfoBean userInfoBean);

    void showUserInfoFailure(String failure);

    void showPostOnlineSuccess(String success);

    void showPostOnlineFailure(String failure);
}
