package com.gjzg.bean;


import com.gjzg.bean.PublishWorkerBean;

import java.io.Serializable;
import java.util.List;

public class PublishBean implements Serializable {

    private String authorId;
    private String title;
    private String info;
    private String type;
    private String area;
    private String typeId;
    private String provinceId;
    private String cityId;
    private String areaId;
    private String address;
    private String pass;
    private String positionX;
    private String positionY;
    private List<PublishWorkerBean> publishWorkerBeanList;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPositionX() {
        return positionX;
    }

    public void setPositionX(String positionX) {
        this.positionX = positionX;
    }

    public String getPositionY() {
        return positionY;
    }

    public void setPositionY(String positionY) {
        this.positionY = positionY;
    }

    public List<PublishWorkerBean> getPublishWorkerBeanList() {
        return publishWorkerBeanList;
    }

    public void setPublishWorkerBeanList(List<PublishWorkerBean> publishWorkerBeanList) {
        this.publishWorkerBeanList = publishWorkerBeanList;
    }
}
