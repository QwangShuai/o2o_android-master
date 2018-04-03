package talkemployer.module;

import com.gjzg.listener.JsonListener;


public interface ITalkEmployerModule {

    void load(String url, JsonListener jsonListener);

    void loadSkill(String url, JsonListener jsonListener);

    void invite(String url,JsonListener jsonListener);

    void cancelTask();
}
