package city.presenter;


import android.os.Handler;

import java.util.List;

import com.gjzg.bean.CityBean;
import com.gjzg.bean.CityBigBean;
import city.listener.CityListener;
import city.module.CityModule;
import city.module.ICityModule;
import city.view.ICityActivity;

public class CityPresenter implements ICityPresenter {

    private ICityActivity cityActivity;
    private ICityModule cityModule;
    private Handler handler;

    public CityPresenter(ICityActivity iCityActivity) {
        this.cityActivity = iCityActivity;
        cityModule = new CityModule();
        handler = new Handler();
    }

    @Override
    public void load(String[] letter, CityBigBean cityBigBean) {
        cityModule.load(letter, cityBigBean, new CityListener() {
            @Override
            public void loadSuccess(final List<CityBean> cityBeanList) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cityActivity.showSuccess(cityBeanList);
                    }
                });
            }

            @Override
            public void loadFailure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cityActivity.showFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (cityModule != null) {
            cityModule = null;
        }
        if (cityActivity != null) {
            cityActivity = null;
        }
        if (handler != null) {
            handler = null;
        }
    }
}
