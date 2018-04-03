package withdraw.module;

import com.gjzg.bean.WithDrawBean;
import com.gjzg.listener.JsonListener;


public interface IWithDrawModule {

    void withdraw(WithDrawBean withDrawBean, JsonListener jsonListener);

    void cancelTask();
}
