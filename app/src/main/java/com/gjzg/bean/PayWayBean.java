package com.gjzg.bean;

public class PayWayBean {

    private String p_id;
    private String p_type;
    private String p_name;
    private String p_info;
    private String p_status;
    private String p_author;
    private String p_last_editor;
    private String p_last_edit_time;
    private String p_default;
    private String img;
    private boolean check;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_info() {
        return p_info;
    }

    public void setP_info(String p_info) {
        this.p_info = p_info;
    }

    public String getP_status() {
        return p_status;
    }

    public void setP_status(String p_status) {
        this.p_status = p_status;
    }

    public String getP_author() {
        return p_author;
    }

    public void setP_author(String p_author) {
        this.p_author = p_author;
    }

    public String getP_last_editor() {
        return p_last_editor;
    }

    public void setP_last_editor(String p_last_editor) {
        this.p_last_editor = p_last_editor;
    }

    public String getP_last_edit_time() {
        return p_last_edit_time;
    }

    public void setP_last_edit_time(String p_last_edit_time) {
        this.p_last_edit_time = p_last_edit_time;
    }

    public String getP_default() {
        return p_default;
    }

    public void setP_default(String p_default) {
        this.p_default = p_default;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "PayWayBean{" +
                "p_id='" + p_id + '\'' +
                ", p_type='" + p_type + '\'' +
                ", p_name='" + p_name + '\'' +
                ", p_info='" + p_info + '\'' +
                ", p_status='" + p_status + '\'' +
                ", p_author='" + p_author + '\'' +
                ", p_last_editor='" + p_last_editor + '\'' +
                ", p_last_edit_time='" + p_last_edit_time + '\'' +
                ", p_default='" + p_default + '\'' +
                ", check=" + check +
                '}';
    }
}
