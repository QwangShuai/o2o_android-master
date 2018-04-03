package collecttask.module;


import com.gjzg.listener.JsonListener;

public interface ICollectTaskModule {

    void load(String url, JsonListener jsonListener);

    void cancelCollect(String url,JsonListener jsonListener);

    void cancelTask();
}
