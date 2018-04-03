package com.gjzg.bean;

import java.io.Serializable;


public class WorkerScreenBean implements Serializable {

    private String u_true_name;
    private String u_status;

    public String getU_true_name() {
        return u_true_name;
    }

    public void setU_true_name(String u_true_name) {
        this.u_true_name = u_true_name;
    }

    public String getU_status() {
        return u_status;
    }

    public void setU_status(String u_status) {
        this.u_status = u_status;
    }

    @Override
    public String toString() {
        return "WorkerScreenBean{" +
                "u_true_name='" + u_true_name + '\'' +
                ", u_status='" + u_status + '\'' +
                '}';
    }
}
