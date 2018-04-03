package com.gjzg.bean;

import java.io.Serializable;


public class ToTalkEmployerBean implements Serializable{

    private String t_id;

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    @Override
    public String toString() {
        return "ToTalkEmployerBean{" +
                "t_id='" + t_id + '\'' +
                '}';
    }
}
