package com.gjzg.bean;

import java.io.Serializable;
import java.util.List;


public class TalkEmployerBean implements Serializable {

    private String icon;
    private String name;
    private String sex;
    private String mobile;
    private String address;
    private String desc;
    private String posX;
    private String posY;
    private String authorId;
    private String t_status;
    private int relation;
    private int relationType;
    private List<TalkEmployerWorkerBean> talkEmployerWorkerBeanList;

    public String getT_status() {
        return t_status;
    }

    public void setT_status(String t_status) {
        this.t_status = t_status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<TalkEmployerWorkerBean> getTalkEmployerWorkerBeanList() {
        return talkEmployerWorkerBeanList;
    }

    public void setTalkEmployerWorkerBeanList(List<TalkEmployerWorkerBean> talkEmployerWorkerBeanList) {
        this.talkEmployerWorkerBeanList = talkEmployerWorkerBeanList;
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    @Override
    public String toString() {
        return "TalkEmployerBean{" +
                "icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", desc='" + desc + '\'' +
                ", posX='" + posX + '\'' +
                ", posY='" + posY + '\'' +
                ", authorId='" + authorId + '\'' +
                ", t_status='" + t_status + '\'' +
                ", relation=" + relation +
                ", relationType=" + relationType +
                ", talkEmployerWorkerBeanList=" + talkEmployerWorkerBeanList +
                '}';
    }
}
