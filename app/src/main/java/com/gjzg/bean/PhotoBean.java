package com.gjzg.bean;

import java.io.Serializable;


public class PhotoBean implements Serializable{

    private String path;
    private boolean check;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "PhotoBean{" +
                "path='" + path + '\'' +
                ", check=" + check +
                '}';
    }
}
