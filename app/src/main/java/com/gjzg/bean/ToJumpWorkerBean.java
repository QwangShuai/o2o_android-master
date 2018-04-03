package com.gjzg.bean;

import java.io.Serializable;


public class ToJumpWorkerBean implements Serializable {

    private String orderId;
    private String tewId;
    private String taskId;
    private String workerId;
    private String skillId;
    private String s_name;
    private String o_confirm;
    private String o_status;
    private String tewPrice;
    private String tew_start_time;
    private String tew_end_time;
    private String tew_worker_num;


    public String getTew_worker_num() {
        return tew_worker_num;
    }

    public void setTew_worker_num(String tew_worker_num) {
        this.tew_worker_num = tew_worker_num;
    }

    public String getTewPrice() {
        return tewPrice;
    }

    public void setTewPrice(String tewPrice) {
        this.tewPrice = tewPrice;
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

    public String getO_confirm() {
        return o_confirm;
    }

    public void setO_confirm(String o_confirm) {
        this.o_confirm = o_confirm;
    }

    public String getO_status() {
        return o_status;
    }

    public void setO_status(String o_status) {
        this.o_status = o_status;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTewId() {
        return tewId;
    }

    public void setTewId(String tewId) {
        this.tewId = tewId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    @Override
    public String toString() {
        return "ToJumpWorkerBean{" +
                "orderId='" + orderId + '\'' +
                ", tewId='" + tewId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", workerId='" + workerId + '\'' +
                ", skillId='" + skillId + '\'' +
                ", s_name='" + s_name + '\'' +
                ", o_confirm='" + o_confirm + '\'' +
                ", o_status='" + o_status + '\'' +
                ", tewPrice='" + tewPrice + '\'' +
                ", tew_start_time='" + tew_start_time + '\'' +
                ", tew_end_time='" + tew_end_time + '\'' +
                ", tew_worker_num='" + tew_worker_num + '\'' +
                '}';
    }
}
