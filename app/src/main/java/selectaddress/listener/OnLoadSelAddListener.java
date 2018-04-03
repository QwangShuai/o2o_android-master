package selectaddress.listener;


import java.util.List;

import com.gjzg.bean.SelectAddressBean;

public interface OnLoadSelAddListener {

    void success(List<SelectAddressBean> selectAddressBeanList);

    void failure(String failure);
}
