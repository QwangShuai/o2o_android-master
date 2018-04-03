package com.gjzg.bean;

import java.io.Serializable;


public class MessageBean implements Serializable {

    private String wm_title;
    private String wm_desc;
    private String um_in_time;
    private String wm_type;
    private String wm_id;
    private String um_id;
    private String um_status;

    public MessageBean() {
    }

    public MessageBean(String wm_title, String wm_desc, String um_in_time, String wm_type, String wm_id, String um_id) {
        this.wm_title = wm_title;
        this.wm_desc = wm_desc;
        this.um_in_time = um_in_time;
        this.wm_type = wm_type;
        this.wm_id = wm_id;
        this.um_id = um_id;
    }

    public String getWm_title() {
        return wm_title;
    }

    public void setWm_title(String wm_title) {
        this.wm_title = wm_title;
    }

    public String getWm_desc() {
        return wm_desc;
    }

    public void setWm_desc(String wm_desc) {
        this.wm_desc = wm_desc;
    }

    public String getUm_in_time() {
        return um_in_time;
    }

    public void setUm_in_time(String um_in_time) {
        this.um_in_time = um_in_time;
    }

    public String getWm_type() {
        return wm_type;
    }

    public void setWm_type(String wm_type) {
        this.wm_type = wm_type;
    }

    public String getWm_id() {
        return wm_id;
    }

    public void setWm_id(String wm_id) {
        this.wm_id = wm_id;
    }

    public String getUm_id() {
        return um_id;
    }

    public void setUm_id(String um_id) {
        this.um_id = um_id;
    }

    public String getUm_status() {
        return um_status;
    }

    public void setUm_status(String um_status) {
        this.um_status = um_status;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "wm_title='" + wm_title + '\'' +
                ", wm_desc='" + wm_desc + '\'' +
                ", um_in_time='" + um_in_time + '\'' +
                ", wm_type='" + wm_type + '\'' +
                ", wm_id='" + wm_id + '\'' +
                ", um_id='" + um_id + '\'' +
                ", um_status='" + um_status + '\'' +
                '}';
    }
}
