package com.gjzg.bean;

import java.io.Serializable;

public class SkillsBean implements Serializable {

    private String s_id;
    private String s_name;
    private String s_info;
    private String s_desc;
    private String s_status;
    private String img;
    private boolean check;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

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

    public String getS_info() {
        return s_info;
    }

    public void setS_info(String s_info) {
        this.s_info = s_info;
    }

    public String getS_desc() {
        return s_desc;
    }

    public void setS_desc(String s_desc) {
        this.s_desc = s_desc;
    }

    public String getS_status() {
        return s_status;
    }

    public void setS_status(String s_status) {
        this.s_status = s_status;
    }

    @Override
    public String toString() {
        return "SkillsBean{" +
                "s_id='" + s_id + '\'' +
                ", s_name='" + s_name + '\'' +
                ", s_info='" + s_info + '\'' +
                ", s_desc='" + s_desc + '\'' +
                ", s_status='" + s_status + '\'' +
                ", img='" + img + '\'' +
                ", check=" + check +
                '}';
    }
}
