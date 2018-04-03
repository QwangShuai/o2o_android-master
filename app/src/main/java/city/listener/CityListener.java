package city.listener;


import java.util.List;

import com.gjzg.bean.CityBean;

public interface CityListener {

    void loadSuccess(List<CityBean> cityBeanList);

    void loadFailure(String failure);
}
