package com.gjzg.bean;

import java.io.Serializable;


public class CityBigBean implements Serializable {

    private CityBean cityBean;
    private String hotJson;
    private String comJson;

    public CityBean getCityBean() {
        return cityBean;
    }

    public void setCityBean(CityBean cityBean) {
        this.cityBean = cityBean;
    }

    public String getHotJson() {
        return hotJson;
    }

    public void setHotJson(String hotJson) {
        this.hotJson = hotJson;
    }

    public String getComJson() {
        return comJson;
    }

    public void setComJson(String comJson) {
        this.comJson = comJson;
    }

    @Override
    public String toString() {
        return "CityBigBean{" +
                "cityBean=" + cityBean +
                ", hotJson='" + hotJson + '\'' +
                ", comJson='" + comJson + '\'' +
                '}';
    }
}
