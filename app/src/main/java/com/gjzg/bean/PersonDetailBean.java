package com.gjzg.bean;


import java.util.List;

public class PersonDetailBean {

    private String icon;
    private String name;
    private String male;
    private String address;
    private String household;
    private String brief;
    private int count;
    private List<EvaluateBean> evaluateBeanList;

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

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHousehold() {
        return household;
    }

    public void setHousehold(String household) {
        this.household = household;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<EvaluateBean> getEvaluateBeanList() {
        return evaluateBeanList;
    }

    public void setEvaluateBeanList(List<EvaluateBean> evaluateBeanList) {
        this.evaluateBeanList = evaluateBeanList;
    }
}
