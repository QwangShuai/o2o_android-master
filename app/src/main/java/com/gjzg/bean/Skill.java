package com.gjzg.bean;


import java.util.List;

public class Skill {

    /**
     * code : 200
     * data : [{"s_id":"1","s_name":"电工","s_info":"与电相关的工种","s_desc":"走线 配电 电器处理","s_status":"1","img":"http://static-app.gangjianwang.com/static/skills/1.jpg"},{"s_id":"2","s_name":"水暖工","s_info":"上下水，暖气 调整、安装","s_desc":"上下水，暖气 调整、安装","s_status":"1","img":"http://static-app.gangjianwang.com/static/skills/2.jpg"},{"s_id":"3","s_name":"泥瓦工","s_info":"切墙 抹灰","s_desc":"切墙 抹灰","s_status":"1","img":"http://static-app.gangjianwang.com/static/skills/3.jpg"},{"s_id":"4","s_name":"力工","s_info":"以体力劳动为主，有膀子力气的工人","s_desc":"抬沙子 端水泥 搬砖","s_status":"1","img":"http://static-app.gangjianwang.com/static/skills/4.jpg"},{"s_id":"5","s_name":"木工","s_info":"家具打造 地板安装 棚顶 橱柜 门窗","s_desc":"家具打造 地板安装 棚顶 橱柜 门窗","s_status":"1","img":"http://static-app.gangjianwang.com/static/skills/5.jpg"},{"s_id":"6","s_name":"油漆工","s_info":"油漆工","s_desc":"油漆工","s_status":"1","img":"http://static-app.gangjianwang.com/static/skills/6.jpg"}]
     */

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * s_id : 1
         * s_name : 电工
         * s_info : 与电相关的工种
         * s_desc : 走线 配电 电器处理
         * s_status : 1
         * img : http://static-app.gangjianwang.com/static/skills/1.jpg
         */

        private String s_id;
        private String s_name;
        private String s_info;
        private String s_desc;
        private String s_status;
        private String img;

        public String getS_id() {
            return s_id;
        }

        public void setS_id(String s_id) {
            this.s_id = s_id;
        }

        public String getS_name() {
            return s_name;
        }

        public void setS_name(String s_name) {
            this.s_name = s_name;
        }

        public String getS_info() {
            return s_info;
        }

        public void setS_info(String s_info) {
            this.s_info = s_info;
        }

        public String getS_desc() {
            return s_desc;
        }

        public void setS_desc(String s_desc) {
            this.s_desc = s_desc;
        }

        public String getS_status() {
            return s_status;
        }

        public void setS_status(String s_status) {
            this.s_status = s_status;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
