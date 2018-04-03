package discount.listener;


import java.util.List;

import com.gjzg.bean.DiscountBean;

public interface OnLoadDiscountListener {

    void success(List<DiscountBean> discountBeanList);

    void failure(String failure);
}
