package com.gjzg.bean;

import java.io.Serializable;
import java.util.List;

public class TaskBean implements Serializable {

    private String taskId;
    private String collectId;
    private String authorId;
    private String tewId;
    private String icon;
    private String title;
    private String info;
    private String status;
    private String posX;
    private String posY;
    private int favorite;
    private List<TaskWorkerBean> taskWorkerBeanList;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCollectId() {
        return collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public List<TaskWorkerBean> getTaskWorkerBeanList() {
        return taskWorkerBeanList;
    }

    public void setTaskWorkerBeanList(List<TaskWorkerBean> taskWorkerBeanList) {
        this.taskWorkerBeanList = taskWorkerBeanList;
    }

    @Override
    public String toString() {
        return "TaskBean{" +
                "taskId='" + taskId + '\'' +
                ", collectId='" + collectId + '\'' +
                ", authorId='" + authorId + '\'' +
                ", tewId='" + tewId + '\'' +
                ", icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", status='" + status + '\'' +
                ", posX='" + posX + '\'' +
                ", posY='" + posY + '\'' +
                ", favorite=" + favorite +
                ", taskWorkerBeanList=" + taskWorkerBeanList +
                '}';
    }
}
