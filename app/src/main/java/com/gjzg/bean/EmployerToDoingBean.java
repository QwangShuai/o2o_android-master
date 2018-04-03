package com.gjzg.bean;

import java.io.Serializable;


public class EmployerToDoingBean implements Serializable {

    private int type;
    private String skillId;
    private String skillName;
    private String startTime;
    private String endTime;
    private String icon;
    private String name;
    private String status;
    private String sex;
    private String mobile;
    private String workerId;
    private String orderId;
    private String tewId;
    private String taskId;
    private String o_status;
    private String o_confirm;

    public String getO_status() {
        return o_status;
    }

    public void setO_status(String o_status) {
        this.o_status = o_status;
    }

    public String getO_confirm() {
        return o_confirm;
    }

    public void setO_confirm(String o_confirm) {
        this.o_confirm = o_confirm;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    @Override
    public String toString() {
        return "EmployerToDoingBean{" +
                "type=" + type +
                ", skillId='" + skillId + '\'' +
                ", skillName='" + skillName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", sex='" + sex + '\'' +
                ", mobile='" + mobile + '\'' +
                ", workerId='" + workerId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", tewId='" + tewId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", o_status='" + o_status + '\'' +
                ", o_confirm='" + o_confirm + '\'' +
                '}';
    }
}
