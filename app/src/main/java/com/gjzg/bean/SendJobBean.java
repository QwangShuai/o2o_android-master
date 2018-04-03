package com.gjzg.bean;

import java.io.Serializable;
import java.util.List;


public class SendJobBean implements Serializable{

    private String name;
    private String des;
    private String kind;
    private String area;
    private String adddress;
    private List<JobKindBean> jobKindBeanList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAdddress() {
        return adddress;
    }

    public void setAdddress(String adddress) {
        this.adddress = adddress;
    }

    public List<JobKindBean> getJobKindBeanList() {
        return jobKindBeanList;
    }

    public void setJobKindBeanList(List<JobKindBean> jobKindBeanList) {
        this.jobKindBeanList = jobKindBeanList;
    }

    @Override
    public String toString() {
        return "SendJobBean{" +
                "name='" + name + '\'' +
                ", des='" + des + '\'' +
                ", kind='" + kind + '\'' +
                ", area='" + area + '\'' +
                ", adddress='" + adddress + '\'' +
                ", jobKindBeanList=" + jobKindBeanList +
                '}';
    }
}
