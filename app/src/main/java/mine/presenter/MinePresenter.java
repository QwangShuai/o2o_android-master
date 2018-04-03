package mine.presenter;

import android.os.Handler;

import com.gjzg.bean.UserInfoBean;

import mine.listener.MineListener;
import mine.listener.OnLineListener;
import mine.module.IMineModule;
import mine.module.MineModule;
import mine.view.IMineFragment;


public class MinePresenter implements IMinePresenter {

    private IMineFragment mineFragment;
    private IMineModule mineModule;
    private Handler handler;

    public MinePresenter(IMineFragment mineFragment) {
        this.mineFragment = mineFragment;
        mineModule = new MineModule();
        handler = new Handler();
    }

    @Override
    public void load(String id) {
        mineModule.load(id, new MineListener() {
            @Override
            public void success(final UserInfoBean userInfoBean) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mineFragment.showUserInfoSuccess(userInfoBean);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mineFragment.showUserInfoFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void postOnline(String id, String online) {
        mineModule.postOnline(id, online, new OnLineListener() {
            @Override
            public void success(final String success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mineFragment.showPostOnlineSuccess(success);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mineFragment.showPostOnlineFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (mineModule != null) {
            mineModule.cancelTask();
            mineModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (mineFragment != null) {
            mineFragment = null;
        }
    }
}
