package selectaddress.view;


import java.util.List;

import com.gjzg.bean.SelectAddressBean;

public interface ISelectAddressActivity {

    void showLoading();

    void hideLoading();

    void showSuccess(List<SelectAddressBean> selectAddressBeanList);

    void showFailure(String failure);
}
