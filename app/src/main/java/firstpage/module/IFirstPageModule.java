package firstpage.module;

import firstpage.listener.ComCityListener;
import firstpage.listener.HotCityListener;
import firstpage.listener.LocIdListener;
import com.gjzg.listener.JsonListener;

public interface IFirstPageModule {

    void loadHotCity(String hotUrl, HotCityListener hotCityListener);

    void loadComCity(String comUrl, ComCityListener comCityListener);

    void getLocId(String[] letter, String locCity, String comJson, LocIdListener locIdListener);

    void changePosition(String url, JsonListener jsonListener);

    void cancelTask();
}
