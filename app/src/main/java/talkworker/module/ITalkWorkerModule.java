package talkworker.module;

import com.gjzg.listener.JsonListener;


public interface ITalkWorkerModule {

    void load(String url, JsonListener jsonListener);

    void check(String url,JsonListener jsonListener);

    void cancelTask();
}
