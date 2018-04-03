package com.gjzg.bean;

import java.io.Serializable;


public class EmployerToTalkBean implements Serializable {

    private int type;
    private String skillId;
    private String skillName;
    private String startTime;
    private String endTime;
    private String workerIcon;
    private String workerName;
    private String workerSex;
    private String workerStatus;
    private String mobile;
    private String workerId;
    private String orderId;
    private String tewId;
    private String taskId;
    private String amount;
    private String price;
    private String o_confirm;
    private String o_status;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWorkerIcon() {
        return workerIcon;
    }

    public void setWorkerIcon(String workerIcon) {
        this.workerIcon = workerIcon;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerSex() {
        return workerSex;
    }

    public void setWorkerSex(String workerSex) {
        this.workerSex = workerSex;
    }

    public String getWorkerStatus() {
        return workerStatus;
    }

    public void setWorkerStatus(String workerStatus) {
        this.workerStatus = workerStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "EmployerToTalkBean{" +
                "type=" + type +
                ", skillId='" + skillId + '\'' +
                ", skillName='" + skillName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", workerIcon='" + workerIcon + '\'' +
                ", workerName='" + workerName + '\'' +
                ", workerSex='" + workerSex + '\'' +
                ", workerStatus='" + workerStatus + '\'' +
                ", mobile='" + mobile + '\'' +
                ", workerId='" + workerId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", tewId='" + tewId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", amount='" + amount + '\'' +
                ", price='" + price + '\'' +
                ", o_confirm='" + o_confirm + '\'' +
                ", o_status='" + o_status + '\'' +
                '}';
    }
}
