package com.gjzg.bean;

import java.io.Serializable;


public class LonLatBean implements Serializable {

    private String lon;
    private String lat;

    public LonLatBean() {
    }

    public LonLatBean(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "LonLatBean{" +
                "lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
