package com.gjzg.bean;

public class EvaluateBean {

    private String icon;
    private String info;
    private String time;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "EvaluateBean{" +
                "icon='" + icon + '\'' +
                ", info='" + info + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
