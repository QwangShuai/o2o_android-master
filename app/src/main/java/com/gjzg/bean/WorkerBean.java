package com.gjzg.bean;


import java.io.Serializable;

public class WorkerBean implements Serializable {

    private String u_id;
    private String u_mobile;
    private String u_idcard;
    private String u_sex;
    private String u_name;
    private String u_skills;
    private String uei_info;
    private String u_task_status;
    private String u_true_name;
    private String ucp_posit_x;
    private String ucp_posit_y;
    private String uei_address;
    private String u_in_time;
    private String u_last_edit_time;
    private String u_online;
    private String u_start;
    private String u_credit;
    private String u_top;
    private String u_recommend;
    private String u_jobs_num;
    private String u_worked_num;
    private String u_high_opinions;
    private String u_low_opinions;
    private String u_middile_opinions;
    private String u_dissensions;
    private String u_img;
    private int is_fav;
    private int relation;
    private int relation_type;
    private String o_confirm;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_mobile() {
        return u_mobile;
    }

    public void setU_mobile(String u_mobile) {
        this.u_mobile = u_mobile;
    }

    public String getU_idcard() {
        return u_idcard;
    }

    public void setU_idcard(String u_idcard) {
        this.u_idcard = u_idcard;
    }

    public String getU_sex() {
        return u_sex;
    }

    public void setU_sex(String u_sex) {
        this.u_sex = u_sex;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_skills() {
        return u_skills;
    }

    public void setU_skills(String u_skills) {
        this.u_skills = u_skills;
    }

    public String getUei_info() {
        return uei_info;
    }

    public void setUei_info(String uei_info) {
        this.uei_info = uei_info;
    }

    public String getU_task_status() {
        return u_task_status;
    }

    public void setU_task_status(String u_task_status) {
        this.u_task_status = u_task_status;
    }

    public String getU_true_name() {
        return u_true_name;
    }

    public void setU_true_name(String u_true_name) {
        this.u_true_name = u_true_name;
    }

    public String getUcp_posit_x() {
        return ucp_posit_x;
    }

    public void setUcp_posit_x(String ucp_posit_x) {
        this.ucp_posit_x = ucp_posit_x;
    }

    public String getUcp_posit_y() {
        return ucp_posit_y;
    }

    public void setUcp_posit_y(String ucp_posit_y) {
        this.ucp_posit_y = ucp_posit_y;
    }

    public String getUei_address() {
        return uei_address;
    }

    public void setUei_address(String uei_address) {
        this.uei_address = uei_address;
    }

    public String getU_in_time() {
        return u_in_time;
    }

    public void setU_in_time(String u_in_time) {
        this.u_in_time = u_in_time;
    }

    public String getU_last_edit_time() {
        return u_last_edit_time;
    }

    public void setU_last_edit_time(String u_last_edit_time) {
        this.u_last_edit_time = u_last_edit_time;
    }

    public String getU_online() {
        return u_online;
    }

    public void setU_online(String u_online) {
        this.u_online = u_online;
    }

    public String getU_start() {
        return u_start;
    }

    public void setU_start(String u_start) {
        this.u_start = u_start;
    }

    public String getU_credit() {
        return u_credit;
    }

    public void setU_credit(String u_credit) {
        this.u_credit = u_credit;
    }

    public String getU_top() {
        return u_top;
    }

    public void setU_top(String u_top) {
        this.u_top = u_top;
    }

    public String getU_recommend() {
        return u_recommend;
    }

    public void setU_recommend(String u_recommend) {
        this.u_recommend = u_recommend;
    }

    public String getU_jobs_num() {
        return u_jobs_num;
    }

    public void setU_jobs_num(String u_jobs_num) {
        this.u_jobs_num = u_jobs_num;
    }

    public String getU_worked_num() {
        return u_worked_num;
    }

    public void setU_worked_num(String u_worked_num) {
        this.u_worked_num = u_worked_num;
    }

    public String getU_high_opinions() {
        return u_high_opinions;
    }

    public void setU_high_opinions(String u_high_opinions) {
        this.u_high_opinions = u_high_opinions;
    }

    public String getU_low_opinions() {
        return u_low_opinions;
    }

    public void setU_low_opinions(String u_low_opinions) {
        this.u_low_opinions = u_low_opinions;
    }

    public String getU_middile_opinions() {
        return u_middile_opinions;
    }

    public void setU_middile_opinions(String u_middile_opinions) {
        this.u_middile_opinions = u_middile_opinions;
    }

    public String getU_dissensions() {
        return u_dissensions;
    }

    public void setU_dissensions(String u_dissensions) {
        this.u_dissensions = u_dissensions;
    }

    public String getU_img() {
        return u_img;
    }

    public void setU_img(String u_img) {
        this.u_img = u_img;
    }

    public int getIs_fav() {
        return is_fav;
    }

    public void setIs_fav(int is_fav) {
        this.is_fav = is_fav;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getRelation_type() {
        return relation_type;
    }

    public void setRelation_type(int relation_type) {
        this.relation_type = relation_type;
    }

    public String getO_confirm() {
        return o_confirm;
    }

    public void setO_confirm(String o_confirm) {
        this.o_confirm = o_confirm;
    }

    @Override
    public String toString() {
        return "WorkerBean{" +
                "u_id='" + u_id + '\'' +
                ", u_mobile='" + u_mobile + '\'' +
                ", u_idcard='" + u_idcard + '\'' +
                ", u_sex='" + u_sex + '\'' +
                ", u_name='" + u_name + '\'' +
                ", u_skills='" + u_skills + '\'' +
                ", uei_info='" + uei_info + '\'' +
                ", u_task_status='" + u_task_status + '\'' +
                ", u_true_name='" + u_true_name + '\'' +
                ", ucp_posit_x='" + ucp_posit_x + '\'' +
                ", ucp_posit_y='" + ucp_posit_y + '\'' +
                ", uei_address='" + uei_address + '\'' +
                ", u_in_time='" + u_in_time + '\'' +
                ", u_last_edit_time='" + u_last_edit_time + '\'' +
                ", u_online='" + u_online + '\'' +
                ", u_start='" + u_start + '\'' +
                ", u_credit='" + u_credit + '\'' +
                ", u_top='" + u_top + '\'' +
                ", u_recommend='" + u_recommend + '\'' +
                ", u_jobs_num='" + u_jobs_num + '\'' +
                ", u_worked_num='" + u_worked_num + '\'' +
                ", u_high_opinions='" + u_high_opinions + '\'' +
                ", u_low_opinions='" + u_low_opinions + '\'' +
                ", u_middile_opinions='" + u_middile_opinions + '\'' +
                ", u_dissensions='" + u_dissensions + '\'' +
                ", u_img='" + u_img + '\'' +
                ", is_fav=" + is_fav +
                ", relation=" + relation +
                ", relation_type=" + relation_type +
                ", o_confirm='" + o_confirm + '\'' +
                '}';
    }
}
