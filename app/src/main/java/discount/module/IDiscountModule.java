package discount.module;


import discount.listener.OnLoadDiscountListener;

public interface IDiscountModule {

    void load(OnLoadDiscountListener onLoadDiscountListener);

    void cancelTask();
}
