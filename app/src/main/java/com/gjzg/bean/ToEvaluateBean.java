package com.gjzg.bean;

import java.io.Serializable;


public class ToEvaluateBean implements Serializable{

    private String u_id;
    private String t_id;
    private String tc_u_id;
    private String tce_desc;
    private String tc_start;
    private String tc_type;
    private String skill;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getTc_u_id() {
        return tc_u_id;
    }

    public void setTc_u_id(String tc_u_id) {
        this.tc_u_id = tc_u_id;
    }

    public String getTce_desc() {
        return tce_desc;
    }

    public void setTce_desc(String tce_desc) {
        this.tce_desc = tce_desc;
    }

    public String getTc_start() {
        return tc_start;
    }

    public void setTc_start(String tc_start) {
        this.tc_start = tc_start;
    }

    public String getTc_type() {
        return tc_type;
    }

    public void setTc_type(String tc_type) {
        this.tc_type = tc_type;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "ToEvaluateBean{" +
                "u_id='" + u_id + '\'' +
                ", t_id='" + t_id + '\'' +
                ", tc_u_id='" + tc_u_id + '\'' +
                ", tce_desc='" + tce_desc + '\'' +
                ", tc_start='" + tc_start + '\'' +
                ", tc_type='" + tc_type + '\'' +
                ", skill='" + skill + '\'' +
                '}';
    }
}
