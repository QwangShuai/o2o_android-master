package com.gjzg.bean;


import java.util.List;

public class Worker {

    /**
     * code : 1
     * data : {"data":[{"u_id":"18","u_mobile":"15945690251","u_idcard":"223335996998999999","u_sex":"0","u_name":"张旭","u_skills":",1,2,3,4,","uei_info":"工作认真负责","u_task_status":"0","u_true_name":"张旭","ucp_posit_x":"126.64445500","ucp_posit_y":"45.77874400","uei_address":"开原街3号","u_in_time":"1511576244","u_last_edit_time":"1511576244","u_online":"0","u_start":"0","u_credit":"0","u_top":"0","u_recommend":"0","u_jobs_num":"0","u_worked_num":"0","u_high_opinions":"0","u_low_opinions":"0","u_middle_opinions":"0","u_dissensions":"0","u_img":"http://static-app.gangjianwang.com/static/head/18.jpg","is_fav":0,"relation":0,"relation_type":0},{"u_id":"10","u_mobile":"18645034186","u_idcard":"232321199408080808","u_sex":"1","u_name":"张小二","u_skills":",1,2,3,4,5,6,","uei_info":"工作认真负责，吃苦耐劳！","u_task_status":"0","u_true_name":"张小二","ucp_posit_x":"126.64438700","ucp_posit_y":"45.77875900","uei_address":"。","u_in_time":"1510725721","u_last_edit_time":"1510822160","u_online":"1","u_start":"0","u_credit":"0","u_top":"0","u_recommend":"0","u_jobs_num":"0","u_worked_num":"0","u_high_opinions":"0","u_low_opinions":"1","u_middle_opinions":"0","u_dissensions":"0","u_img":"http://static-app.gangjianwang.com/static/head/10.jpg","is_fav":0,"relation":0,"relation_type":0},{"u_id":"5","u_mobile":"15104629758","u_idcard":"232321199407061818","u_sex":"1","u_name":"松仁玉米","u_skills":",1,2,3,4,6,5,","uei_info":"不说！","u_task_status":"0","u_true_name":"松仁玉米","ucp_posit_x":"126.64433600","ucp_posit_y":"45.77873700","uei_address":"你猜？","u_in_time":"1510449576","u_last_edit_time":"1510736485","u_online":"1","u_start":"0","u_credit":"0","u_top":"0","u_recommend":"0","u_jobs_num":"0","u_worked_num":"0","u_high_opinions":"1","u_low_opinions":"1","u_middle_opinions":"0","u_dissensions":"0","u_img":"http://static-app.gangjianwang.com/static/head/5.jpg","is_fav":0,"relation":0,"relation_type":0},{"u_id":"4","u_mobile":"18646011203","u_idcard":"230125199419941994","u_sex":"1","u_name":"Android Smm","u_skills":",1,2,3,4,5,6,","uei_info":"赵宇傻","u_task_status":"0","u_true_name":"Android Smm","ucp_posit_x":"126.64431100","ucp_posit_y":"45.77866900","uei_address":"开原街3号","u_in_time":"1510207096","u_last_edit_time":"1511147155","u_online":"1","u_start":"0","u_credit":"0","u_top":"0","u_recommend":"0","u_jobs_num":"0","u_worked_num":"0","u_high_opinions":"3","u_low_opinions":"4","u_middle_opinions":"5","u_dissensions":"0","u_img":"http://static-app.gangjianwang.com/static/head/4.jpg","is_fav":0,"relation":0,"relation_type":0},{"u_id":"3","u_mobile":"13163675676","u_idcard":"230624199206210719","u_sex":"1","u_name":"Yu","u_skills":",1,2,3,4,5,6,","uei_info":"我啥都会","u_task_status":"0","u_true_name":"Yu","ucp_posit_x":"126.64435300","ucp_posit_y":"45.77871500","uei_address":"哈尔滨","u_in_time":"1510193763","u_last_edit_time":"1510716565","u_online":"1","u_start":"0","u_credit":"0","u_top":"0","u_recommend":"0","u_jobs_num":"0","u_worked_num":"0","u_high_opinions":"0","u_low_opinions":"0","u_middle_opinions":"0","u_dissensions":"0","u_img":"http://static-app.gangjianwang.com/static/head/3.jpg","is_fav":0,"relation":0,"relation_type":0},{"u_id":"1","u_mobile":"15840344241","u_idcard":"210911199503230536","u_sex":"1","u_name":"你的","u_skills":"1,2,3,4,5,6","uei_info":"hah","u_task_status":"1","u_true_name":"你的","ucp_posit_x":"126.64414000","ucp_posit_y":"45.77865000","uei_address":"harbin","u_in_time":"1510192052","u_last_edit_time":"1511161256","u_online":"1","u_start":"0","u_credit":"0","u_top":"0","u_recommend":"0","u_jobs_num":"0","u_worked_num":"0","u_high_opinions":"1","u_low_opinions":"0","u_middle_opinions":"1","u_dissensions":"0","u_img":"http://static-app.gangjianwang.com/static/head/1.jpg","is_fav":0,"relation":0,"relation_type":0}]}
     */

    private int code;
    private DataBeanX data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * u_id : 18
             * u_mobile : 15945690251
             * u_idcard : 223335996998999999
             * u_sex : 0
             * u_name : 张旭
             * u_skills : ,1,2,3,4,
             * uei_info : 工作认真负责
             * u_task_status : 0
             * u_true_name : 张旭
             * ucp_posit_x : 126.64445500
             * ucp_posit_y : 45.77874400
             * uei_address : 开原街3号
             * u_in_time : 1511576244
             * u_last_edit_time : 1511576244
             * u_online : 0
             * u_start : 0
             * u_credit : 0
             * u_top : 0
             * u_recommend : 0
             * u_jobs_num : 0
             * u_worked_num : 0
             * u_high_opinions : 0
             * u_low_opinions : 0
             * u_middle_opinions : 0
             * u_dissensions : 0
             * u_img : http://static-app.gangjianwang.com/static/head/18.jpg
             * is_fav : 0
             * relation : 0
             * relation_type : 0
             */

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
            private String u_middle_opinions;
            private String u_dissensions;
            private String u_img;
            private int is_fav;
            private int relation;
            private int relation_type;

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

            public String getU_middle_opinions() {
                return u_middle_opinions;
            }

            public void setU_middle_opinions(String u_middle_opinions) {
                this.u_middle_opinions = u_middle_opinions;
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
        }
    }
}
