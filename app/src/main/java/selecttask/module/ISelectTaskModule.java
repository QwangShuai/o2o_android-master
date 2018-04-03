package selecttask.module;

import com.gjzg.listener.JsonListener;


public interface ISelectTaskModule {

    void load(String url, JsonListener jsonListener);

    void invite(String url,JsonListener jsonListener);

    void cancelTask();
}
