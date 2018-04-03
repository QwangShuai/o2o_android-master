package city.module;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.bean.CityBean;
import com.gjzg.bean.CityBigBean;
import city.listener.CityListener;

public class CityModule implements ICityModule {

    @Override
    public void load(String[] letter, CityBigBean cityBigBean, CityListener cityListener) {
        List<CityBean> cityBeanList = new ArrayList<>();
        cityBeanList.add(cityBigBean.getCityBean());
        try {
            JSONObject hotObj = new JSONObject(cityBigBean.getHotJson());
            if (hotObj.optInt("code") == 200) {
                JSONArray dataArr = hotObj.optJSONArray("data");
                if (dataArr != null) {
                    CityBean cb = new CityBean();
                    cb.setId("-1");
                    cb.setName("热门城市");
                    cityBeanList.add(cb);
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONObject o = dataArr.optJSONObject(i);
                        if (o != null) {
                            CityBean c = new CityBean();
                            c.setId(o.optString("r_id"));
                            c.setName(o.optString("r_name"));
                            cityBeanList.add(c);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject comObj = new JSONObject(cityBigBean.getComJson());
            if (comObj.optInt("code") == 200) {
                JSONObject dataObj = comObj.optJSONObject("data");
                if (dataObj != null) {
                    for (int i = 0; i < letter.length; i++) {
                        JSONArray arr = dataObj.optJSONArray(letter[i]);
                        if (arr != null) {
                            CityBean c = new CityBean();
                            c.setId("-1");
                            c.setName(letter[i].toUpperCase());
                            cityBeanList.add(c);
                            for (int j = 0; j < arr.length(); j++) {
                                JSONObject o = arr.optJSONObject(j);
                                if (o != null) {
                                    CityBean cityBean = new CityBean();
                                    cityBean.setId(o.optString("r_id"));
                                    cityBean.setName(o.optString("r_name"));
                                    cityBeanList.add(cityBean);
                                }
                            }
                        }
                    }
                    cityListener.loadSuccess(cityBeanList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
