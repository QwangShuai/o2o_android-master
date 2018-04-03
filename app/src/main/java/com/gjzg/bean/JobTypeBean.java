package com.gjzg.bean;


import java.io.Serializable;

public class JobTypeBean implements Serializable {

    private String id;
    private String name;

    public JobTypeBean() {
    }

    public JobTypeBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "JobTypeBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
