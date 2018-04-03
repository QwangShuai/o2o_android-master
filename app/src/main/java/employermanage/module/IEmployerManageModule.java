package employermanage.module;

import com.gjzg.listener.JsonListener;


public interface IEmployerManageModule {

    void load(String url, JsonListener jsonListener);

    void cancel(String url,JsonListener jsonListener);

    void cancelTask();
}
