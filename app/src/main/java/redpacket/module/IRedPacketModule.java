package redpacket.module;

import com.gjzg.listener.JsonListener;

public interface IRedPacketModule {

    void load(String url, JsonListener jsonListener);

    void cancelTask();
}
