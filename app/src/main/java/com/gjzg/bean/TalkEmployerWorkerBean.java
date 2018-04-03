package com.gjzg.bean;

import java.io.Serializable;


public class TalkEmployerWorkerBean implements Serializable {

    private String id;
    private String skill;
    private String amount;
    private String price;
    private String startTime;
    private String endTime;
    private boolean select;
    private String tewId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
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

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getTewId() {
        return tewId;
    }

    public void setTewId(String tewId) {
        this.tewId = tewId;
    }

    @Override
    public String toString() {
        return "TalkEmployerWorkerBean{" +
                "id='" + id + '\'' +
                ", skill='" + skill + '\'' +
                ", amount='" + amount + '\'' +
                ", price='" + price + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", select=" + select +
                ", tewId='" + tewId + '\'' +
                '}';
    }
}
