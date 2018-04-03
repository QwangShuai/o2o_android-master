package getevaluate.module;

import com.gjzg.listener.JsonListener;


public interface IGetEvaluateModule {

    void load(String url, JsonListener jsonListener);

    void cancelTask();
}
