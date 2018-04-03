package com.gjzg.bean;

import java.io.Serializable;


public class ToChangePriceBean implements Serializable {

    private String tew_id;
    private String t_id;
    private String t_author;
    private String amount;
    private String worker_num;
    private String start_time;
    private String end_time;
    private String o_worker;
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

    public String getT_author() {
        return t_author;
    }

    public void setT_author(String t_author) {
        this.t_author = t_author;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWorker_num() {
        return worker_num;
    }

    public void setWorker_num(String worker_num) {
        this.worker_num = worker_num;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getO_worker() {
        return o_worker;
    }

    public void setO_worker(String o_worker) {
        this.o_worker = o_worker;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "ToChangePriceBean{" +
                "tew_id='" + tew_id + '\'' +
                ", t_id='" + t_id + '\'' +
                ", t_author='" + t_author + '\'' +
                ", amount='" + amount + '\'' +
                ", worker_num='" + worker_num + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", o_worker='" + o_worker + '\'' +
                ", skill='" + skill + '\'' +
                '}';
    }
}
