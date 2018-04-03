package userinfo.module;

import com.gjzg.listener.JsonListener;


public interface IUserInfoModule {

    void info(String url, JsonListener jsonListener);

    void skill(String url,JsonListener jsonListener);

    void cancelTask();
}
