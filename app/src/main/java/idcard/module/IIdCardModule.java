package idcard.module;

import idcard.listener.IdCardListener;


public interface IIdCardModule {

    void verify(String mobile, String idcard, IdCardListener idCardListener);

    void cancelTask();
}
