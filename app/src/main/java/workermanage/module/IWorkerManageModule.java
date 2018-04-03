package workermanage.module;


import com.gjzg.listener.JsonListener;

public interface IWorkerManageModule {

    void load(String url, JsonListener jsonListener);

    void cancelTask();
}
