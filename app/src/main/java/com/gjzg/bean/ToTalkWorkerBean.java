package com.gjzg.bean;

import java.io.Serializable;


public class ToTalkWorkerBean implements Serializable {

    private String s_id;
    private String s_name;
    private String u_id;

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    @Override
    public String toString() {
        return "ToTalkWorkerBean{" +
                "s_id='" + s_id + '\'' +
                ", s_name='" + s_name + '\'' +
                ", u_id='" + u_id + '\'' +
                '}';
    }
}
