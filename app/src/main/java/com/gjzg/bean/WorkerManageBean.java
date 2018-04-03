package com.gjzg.bean;


import java.io.Serializable;

public class WorkerManageBean implements Serializable {

    private String icon;
    private String title;
    private String info;
    private String o_status;
    private String o_confirm;
    private String taskId;
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "WorkerManageBean{" +
                "icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", o_status='" + o_status + '\'' +
                ", o_confirm='" + o_confirm + '\'' +
                ", taskId='" + taskId + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
