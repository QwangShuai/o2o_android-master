package persondetail.module;

import com.gjzg.listener.JsonListener;


public interface IPersonDetailModule {

    void info(String url, JsonListener jsonListener);

    void getSkill(String url,JsonListener jsonListener);

    void evaluate(String url,JsonListener jsonListener);

    void cancelTask();
}
