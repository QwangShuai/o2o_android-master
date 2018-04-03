package giveevaluate.module;

import com.gjzg.listener.JsonListener;


public interface IGiveEvaluateModule {

    void load(String url, JsonListener jsonListener);

    void cancelTask();
}
