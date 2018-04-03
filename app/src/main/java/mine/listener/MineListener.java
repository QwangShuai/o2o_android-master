package mine.listener;

import com.gjzg.bean.UserInfoBean;


public interface MineListener {

    void success(UserInfoBean userInfoBean);

    void failure(String failure);
}
