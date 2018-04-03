package city.presenter;

import com.gjzg.bean.CityBigBean;

public interface ICityPresenter {

    void load(String[] letter, CityBigBean cityBigBean);

    void destroy();
}
