package com.gjzg.bean;


import java.util.List;

public class CollectWorker {

    /**
     * code : 1
     * data : {"data":[{"u_id":"5","u_img":"http://static-app.gangjianwang.com/static/head/5.jpg","u_name":"松仁玉米","u_task_status":"0","u_sex":"1","uei_info":"不说！","ucp_posit_x":"126.64433600","ucp_posit_y":"45.77873700","f_id":"56"},{"u_id":"18","u_img":"http://static-app.gangjianwang.com/static/head/18.jpg","u_name":"张旭","u_task_status":"0","u_sex":"0","uei_info":"工作认真负责","ucp_posit_x":"126.64445500","ucp_posit_y":"45.77874400","f_id":"55"}]}
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
             * u_id : 5
             * u_img : http://static-app.gangjianwang.com/static/head/5.jpg
             * u_name : 松仁玉米
             * u_task_status : 0
             * u_sex : 1
             * uei_info : 不说！
             * ucp_posit_x : 126.64433600
             * ucp_posit_y : 45.77873700
             * f_id : 56
             */

            private String u_id;
            private String u_img;
            private String u_name;
            private String u_task_status;
            private String u_sex;
            private String uei_info;
            private String ucp_posit_x;
            private String ucp_posit_y;
            private String f_id;

            public String getU_id() {
                return u_id;
            }

            public void setU_id(String u_id) {
                this.u_id = u_id;
            }

            public String getU_img() {
                return u_img;
            }

            public void setU_img(String u_img) {
                this.u_img = u_img;
            }

            public String getU_name() {
                return u_name;
            }

            public void setU_name(String u_name) {
                this.u_name = u_name;
            }

            public String getU_task_status() {
                return u_task_status;
            }

            public void setU_task_status(String u_task_status) {
                this.u_task_status = u_task_status;
            }

            public String getU_sex() {
                return u_sex;
            }

            public void setU_sex(String u_sex) {
                this.u_sex = u_sex;
            }

            public String getUei_info() {
                return uei_info;
            }

            public void setUei_info(String uei_info) {
                this.uei_info = uei_info;
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

            public String getF_id() {
                return f_id;
            }

            public void setF_id(String f_id) {
                this.f_id = f_id;
            }
        }
    }
}
