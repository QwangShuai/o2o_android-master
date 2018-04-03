package com.gjzg.bean;

import java.io.Serializable;

public class RegionBean implements Serializable{

    private String r_id;
    private String r_pid;
    private String r_shortname;
    private String r_name;
    private String r_first;
    private String r_status;
    private String r_hot;

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getR_pid() {
        return r_pid;
    }

    public void setR_pid(String r_pid) {
        this.r_pid = r_pid;
    }

    public String getR_shortname() {
        return r_shortname;
    }

    public void setR_shortname(String r_shortname) {
        this.r_shortname = r_shortname;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getR_first() {
        return r_first;
    }

    public void setR_first(String r_first) {
        this.r_first = r_first;
    }

    public String getR_status() {
        return r_status;
    }

    public void setR_status(String r_status) {
        this.r_status = r_status;
    }

    public String getR_hot() {
        return r_hot;
    }

    public void setR_hot(String r_hot) {
        this.r_hot = r_hot;
    }

    @Override
    public String toString() {
        return "RegionBean{" +
                "r_id='" + r_id + '\'' +
                ", r_pid='" + r_pid + '\'' +
                ", r_shortname='" + r_shortname + '\'' +
                ", r_name='" + r_name + '\'' +
                ", r_first='" + r_first + '\'' +
                ", r_status='" + r_status + '\'' +
                ", r_hot='" + r_hot + '\'' +
                '}';
    }
}
