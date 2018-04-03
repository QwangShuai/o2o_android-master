package selectaddress.module;


import selectaddress.listener.OnLoadSelAddListener;

public interface ISelectAddressModule {

    void load(String id, OnLoadSelAddListener onLoadSelAddListener);

    void cancelTask();
}
