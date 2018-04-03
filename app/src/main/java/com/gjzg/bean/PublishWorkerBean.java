package com.gjzg.bean;


import java.io.Serializable;

public class PublishWorkerBean implements Serializable {

    private String id;
    private String kind;
    private String amount;
    private String salary;
    private String startTime;
    private String endTime;

    public PublishWorkerBean() {
    }

    public PublishWorkerBean(String id, String kind, String amount, String salary, String startTime, String endTime) {
        this.id = id;
        this.kind = kind;
        this.amount = amount;
        this.salary = salary;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
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

}
