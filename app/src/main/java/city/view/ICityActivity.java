package city.view;


import java.util.List;

import com.gjzg.bean.CityBean;

public interface ICityActivity {

    void showSuccess(List<CityBean> cityBeanList);

    void showFailure(String failure);

}
