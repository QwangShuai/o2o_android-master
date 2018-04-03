package com.gjzg.bean;

import java.io.Serializable;


public class ArticleBean implements Serializable{

    private String a_id;
    private String a_title;
    private String a_in_time;
    private String a_link;
    private String a_img;
    private String a_top;
    private String a_recommend;
    private String a_desc;

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getA_title() {
        return a_title;
    }

    public void setA_title(String a_title) {
        this.a_title = a_title;
    }

    public String getA_in_time() {
        return a_in_time;
    }

    public void setA_in_time(String a_in_time) {
        this.a_in_time = a_in_time;
    }

    public String getA_link() {
        return a_link;
    }

    public void setA_link(String a_link) {
        this.a_link = a_link;
    }

    public String getA_img() {
        return a_img;
    }

    public void setA_img(String a_img) {
        this.a_img = a_img;
    }

    public String getA_top() {
        return a_top;
    }

    public void setA_top(String a_top) {
        this.a_top = a_top;
    }

    public String getA_recommend() {
        return a_recommend;
    }

    public void setA_recommend(String a_recommend) {
        this.a_recommend = a_recommend;
    }

    public String getA_desc() {
        return a_desc;
    }

    public void setA_desc(String a_desc) {
        this.a_desc = a_desc;
    }

    @Override
    public String toString() {
        return "ArticleBean{" +
                "a_id='" + a_id + '\'' +
                ", a_title='" + a_title + '\'' +
                ", a_in_time='" + a_in_time + '\'' +
                ", a_link='" + a_link + '\'' +
                ", a_img='" + a_img + '\'' +
                ", a_top='" + a_top + '\'' +
                ", a_recommend='" + a_recommend + '\'' +
                ", a_desc='" + a_desc + '\'' +
                '}';
    }
}
