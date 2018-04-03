package com.gjzg.bean;

import java.util.List;

public class CollectTaskBean {

    /**
     * code : 1
     * data : {"data":[{"t_id":"41","t_title":"不想干了","t_amount":"10","t_duration":"1","t_author":"5","t_status":"0","f_id":"63","t_desc":"1","u_img":"http://static-app.gangjianwang.com/static/head/5.jpg"}]}
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
             * t_id : 41
             * t_title : 不想干了
             * t_amount : 10
             * t_duration : 1
             * t_author : 5
             * t_status : 0
             * f_id : 63
             * t_desc : 1
             * u_img : http://static-app.gangjianwang.com/static/head/5.jpg
             */
            private String t_id;
            private String t_title;
            private String t_amount;
            private String t_duration;
            private String t_author;
            private String t_status;
            private String f_id;
            private String t_desc;
            private String u_img;

            public String getT_title() {
                return t_title;
            }

            public void setT_title(String t_title) {
                this.t_title = t_title;
            }

            public String getT_amount() {
                return t_amount;
            }

            public void setT_amount(String t_amount) {
                this.t_amount = t_amount;
            }

            public String getT_duration() {
                return t_duration;
            }

            public void setT_duration(String t_duration) {
                this.t_duration = t_duration;
            }

            public String getT_author() {
                return t_author;
            }

            public void setT_author(String t_author) {
                this.t_author = t_author;
            }

            public String getT_status() {
                return t_status;
            }

            public void setT_status(String t_status) {
                this.t_status = t_status;
            }

            public String getF_id() {
                return f_id;
            }

            public void setF_id(String f_id) {
                this.f_id = f_id;
            }

            public String getT_desc() {
                return t_desc;
            }

            public void setT_desc(String t_desc) {
                this.t_desc = t_desc;
            }

            public String getU_img() {
                return u_img;
            }

            public void setU_img(String u_img) {
                this.u_img = u_img;
            }

            public String getT_id() {
                return t_id;
            }

            public void setT_id(String t_id) {
                this.t_id = t_id;
            }
        }
    }
}
