package city.module;

import com.gjzg.bean.CityBigBean;
import city.listener.CityListener;

public interface ICityModule {

    void load(String[] letter,CityBigBean cityBigBean, CityListener cityListener);
}
