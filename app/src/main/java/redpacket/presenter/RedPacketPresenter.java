package redpacket.presenter;

import android.os.Handler;

import com.gjzg.listener.JsonListener;
import redpacket.module.IRedPacketModule;
import redpacket.module.RedPacketModule;
import redpacket.view.IRedPacketActivity;

public class RedPacketPresenter implements IRedPacketPresenter {

    private IRedPacketActivity redPacketActivity;
    private IRedPacketModule redPacketModule;
    private Handler handler;

    public RedPacketPresenter(IRedPacketActivity redPacketActivity) {
        this.redPacketActivity = redPacketActivity;
        redPacketModule = new RedPacketModule();
        handler = new Handler();
    }

    @Override
    public void load(String url) {
        redPacketModule.load(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        redPacketActivity.loadSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        redPacketActivity.loadFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (redPacketModule != null) {
            redPacketModule.cancelTask();
            redPacketModule = null;
        }
        if (redPacketActivity != null) {
            redPacketActivity = null;
        }
        if (handler != null) {
            handler = null;
        }
    }
}
