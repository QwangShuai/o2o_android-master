package com.gjzg.bean;

import java.io.Serializable;
import java.util.List;

public class TInfoWorkerBean implements Serializable {

    private String tew_id;
    private String t_id;
    private String tew_skills;
    private String tew_worker_num;
    private String tew_price;
    private String tew_start_time;
    private String tew_end_time;
    private String r_province;
    private String r_city;
    private String r_area;
    private String tew_address;
    private String tew_lock;
    private String tew_status;
    private String tew_type;
    private int remaining;
    private List<TInfoOrderBean> tInfoOrderBeanList;

    private String skill;

    public String getTew_id() {
        return tew_id;
    }

    public void setTew_id(String tew_id) {
        this.tew_id = tew_id;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getTew_skills() {
        return tew_skills;
    }

    public void setTew_skills(String tew_skills) {
        this.tew_skills = tew_skills;
    }

    public String getTew_worker_num() {
        return tew_worker_num;
    }

    public void setTew_worker_num(String tew_worker_num) {
        this.tew_worker_num = tew_worker_num;
    }

    public String getTew_price() {
        return tew_price;
    }

    public void setTew_price(String tew_price) {
        this.tew_price = tew_price;
    }

    public String getTew_start_time() {
        return tew_start_time;
    }

    public void setTew_start_time(String tew_start_time) {
        this.tew_start_time = tew_start_time;
    }

    public String getTew_end_time() {
        return tew_end_time;
    }

    public void setTew_end_time(String tew_end_time) {
        this.tew_end_time = tew_end_time;
    }

    public String getR_province() {
        return r_province;
    }

    public void setR_province(String r_province) {
        this.r_province = r_province;
    }

    public String getR_city() {
        return r_city;
    }

    public void setR_city(String r_city) {
        this.r_city = r_city;
    }

    public String getR_area() {
        return r_area;
    }

    public void setR_area(String r_area) {
        this.r_area = r_area;
    }

    public String getTew_address() {
        return tew_address;
    }

    public void setTew_address(String tew_address) {
        this.tew_address = tew_address;
    }

    public String getTew_lock() {
        return tew_lock;
    }

    public void setTew_lock(String tew_lock) {
        this.tew_lock = tew_lock;
    }

    public String getTew_status() {
        return tew_status;
    }

    public void setTew_status(String tew_status) {
        this.tew_status = tew_status;
    }

    public String getTew_type() {
        return tew_type;
    }

    public void setTew_type(String tew_type) {
        this.tew_type = tew_type;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public List<TInfoOrderBean> gettInfoOrderBeanList() {
        return tInfoOrderBeanList;
    }

    public void settInfoOrderBeanList(List<TInfoOrderBean> tInfoOrderBeanList) {
        this.tInfoOrderBeanList = tInfoOrderBeanList;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "TInfoWorkerBean{" +
                "tew_id='" + tew_id + '\'' +
                ", t_id='" + t_id + '\'' +
                ", tew_skills='" + tew_skills + '\'' +
                ", tew_worker_num='" + tew_worker_num + '\'' +
                ", tew_price='" + tew_price + '\'' +
                ", tew_start_time='" + tew_start_time + '\'' +
                ", tew_end_time='" + tew_end_time + '\'' +
                ", r_province='" + r_province + '\'' +
                ", r_city='" + r_city + '\'' +
                ", r_area='" + r_area + '\'' +
                ", tew_address='" + tew_address + '\'' +
                ", tew_lock='" + tew_lock + '\'' +
                ", tew_status='" + tew_status + '\'' +
                ", tew_type='" + tew_type + '\'' +
                ", remaining=" + remaining +
                ", tInfoOrderBeanList=" + tInfoOrderBeanList +
                ", skill='" + skill + '\'' +
                '}';
    }
}
