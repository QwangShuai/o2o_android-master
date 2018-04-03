package userinfo.presenter;

import android.os.Handler;

import com.gjzg.listener.JsonListener;
import userinfo.module.IUserInfoModule;
import userinfo.module.UserInfoModule;
import userinfo.view.IUserInfoFragment;


public class UserInfoPresenter implements IUserInfoPresenter {

    private IUserInfoFragment userInfoFragment;
    private IUserInfoModule userInfoModule;
    private Handler handler;

    public UserInfoPresenter(IUserInfoFragment userInfoFragment) {
        this.userInfoFragment = userInfoFragment;
        userInfoModule = new UserInfoModule();
        handler = new Handler();
    }

    @Override
    public void info(String url) {
        userInfoModule.info(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userInfoFragment.infoSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userInfoFragment.infoFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void skill(String url) {
        userInfoModule.skill(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userInfoFragment.skillSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userInfoFragment.skillFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (userInfoModule != null) {
            userInfoModule.cancelTask();
            userInfoModule = null;
        }
    }
}
