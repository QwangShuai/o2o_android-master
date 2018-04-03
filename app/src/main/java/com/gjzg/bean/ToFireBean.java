package com.gjzg.bean;

import java.io.Serializable;

public class ToFireBean implements Serializable {

    private String tewId;
    private String taskId;
    private String fireId;
    private String skillId;
    private String skillName;
    private String start;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
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

    public String getFireId() {
        return fireId;
    }

    public void setFireId(String fireId) {
        this.fireId = fireId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    @Override
    public String toString() {
        return "ToFireBean{" +
                "tewId='" + tewId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", fireId='" + fireId + '\'' +
                ", skillId='" + skillId + '\'' +
                ", skillName='" + skillName + '\'' +
                ", start='" + start + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
