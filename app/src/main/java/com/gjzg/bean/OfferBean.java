package com.gjzg.bean;

import java.util.List;

public class OfferBean {

    /**
     * code : 1
     * data : {"data":[{"wm_title":"【任务：工人确认】","um_in_time":"1511941068","wm_type":"1","wm_id":"466","um_id":"434","wm_desc":"工人已确认任务，请进入雇主发布任务管理中查看","um_status":"1"},{"wm_title":"【任务：工人辞职】","um_in_time":"1511159852","wm_type":"1","wm_id":"305","um_id":"273","wm_desc":"解除关系，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：工人确认】","um_in_time":"1511159775","wm_type":"1","wm_id":"303","um_id":"271","wm_desc":"工人已确认任务，请进入雇主发布任务管理中查看","um_status":"1"},{"wm_title":"【任务：有工人接单】","um_in_time":"1511159574","wm_type":"1","wm_id":"301","um_id":"269","wm_desc":"有工人接单，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：工人辞职】","um_in_time":"1511148733","wm_type":"1","wm_id":"299","um_id":"267","wm_desc":"解除关系，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：工人确认】","um_in_time":"1511148681","wm_type":"1","wm_id":"297","um_id":"265","wm_desc":"工人已确认任务，请进入雇主发布任务管理中查看","um_status":"1"},{"wm_title":"【任务：有工人接单】","um_in_time":"1511148643","wm_type":"1","wm_id":"295","um_id":"263","wm_desc":"有工人接单，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：订单取消】","um_in_time":"1511147284","wm_type":"1","wm_id":"283","um_id":"251","wm_desc":"订单取消，请进入工人工作管理中查看","um_status":"1"},{"wm_title":"【任务：有工人接单】","um_in_time":"1511146911","wm_type":"1","wm_id":"273","um_id":"241","wm_desc":"有工人接单，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：有工人接单】","um_in_time":"1511146784","wm_type":"1","wm_id":"271","um_id":"239","wm_desc":"有工人接单，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：有工人接单】","um_in_time":"1511146696","wm_type":"1","wm_id":"270","um_id":"238","wm_desc":"有工人接单，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：有工人接单】","um_in_time":"1511146694","wm_type":"1","wm_id":"269","um_id":"237","wm_desc":"有工人接单，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：工人辞职】","um_in_time":"1511146589","wm_type":"1","wm_id":"266","um_id":"234","wm_desc":"解除关系，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：工人确认】","um_in_time":"1511146122","wm_type":"1","wm_id":"264","um_id":"232","wm_desc":"工人已确认任务，请进入雇主发布任务管理中查看","um_status":"1"},{"wm_title":"【任务：有工人接单】","um_in_time":"1511146041","wm_type":"1","wm_id":"259","um_id":"227","wm_desc":"有工人接单，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：邀约成功】","um_in_time":"1510822303","wm_type":"1","wm_id":"220","um_id":"188","wm_desc":"邀约成功，请进入工人工作管理中查看","um_status":"1"},{"wm_title":"【任务：工人确认】","um_in_time":"1510736003","wm_type":"1","wm_id":"189","um_id":"157","wm_desc":"工人已确认任务，请进入雇主发布任务管理中查看","um_status":"1"},{"wm_title":"【任务：有工人接单】","um_in_time":"1510735992","wm_type":"1","wm_id":"187","um_id":"155","wm_desc":"有工人接单，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：工人确认】","um_in_time":"1510732993","wm_type":"1","wm_id":"176","um_id":"144","wm_desc":"工人已确认任务，请进入雇主发布任务管理中查看","um_status":"1"},{"wm_title":"【任务：有工人接单】","um_in_time":"1510732943","wm_type":"1","wm_id":"174","um_id":"142","wm_desc":"有工人接单，请进入雇主发布管理中查看","um_status":"1"},{"wm_title":"【任务：订单结算】","um_in_time":"1510731713","wm_type":"1","wm_id":"163","um_id":"131","wm_desc":"订单结算，请进入工人工作管理中查看","um_status":"1"},{"wm_title":"【任务：雇主确认】","um_in_time":"1510731599","wm_type":"1","wm_id":"161","um_id":"129","wm_desc":"雇主已确认任务，请进入工人工作管理中查看","um_status":"1"},{"wm_title":"【任务：邀约成功】","um_in_time":"1510731535","wm_type":"1","wm_id":"160","um_id":"128","wm_desc":"邀约成功，请进入工人工作管理中查看","um_status":"1"},{"wm_title":"【任务：邀约成功】","um_in_time":"1510729929","wm_type":"1","wm_id":"157","um_id":"125","wm_desc":"邀约成功，请进入工人工作管理中查看","um_status":"1"},{"wm_title":"【任务：订单结算】","um_in_time":"1510715362","wm_type":"1","wm_id":"129","um_id":"97","wm_desc":"订单结算，请进入工人工作管理中查看","um_status":"1"}]}
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
             * wm_title : 【任务：工人确认】
             * um_in_time : 1511941068
             * wm_type : 1
             * wm_id : 466
             * um_id : 434
             * wm_desc : 工人已确认任务，请进入雇主发布任务管理中查看
             * um_status : 1
             */

            private String wm_title;
            private String um_in_time;
            private String wm_type;
            private String wm_id;
            private String um_id;
            private String wm_desc;
            private String um_status;

            public String getWm_title() {
                return wm_title;
            }

            public void setWm_title(String wm_title) {
                this.wm_title = wm_title;
            }

            public String getUm_in_time() {
                return um_in_time;
            }

            public void setUm_in_time(String um_in_time) {
                this.um_in_time = um_in_time;
            }

            public String getWm_type() {
                return wm_type;
            }

            public void setWm_type(String wm_type) {
                this.wm_type = wm_type;
            }

            public String getWm_id() {
                return wm_id;
            }

            public void setWm_id(String wm_id) {
                this.wm_id = wm_id;
            }

            public String getUm_id() {
                return um_id;
            }

            public void setUm_id(String um_id) {
                this.um_id = um_id;
            }

            public String getWm_desc() {
                return wm_desc;
            }

            public void setWm_desc(String wm_desc) {
                this.wm_desc = wm_desc;
            }

            public String getUm_status() {
                return um_status;
            }

            public void setUm_status(String um_status) {
                this.um_status = um_status;
            }
        }
    }
}
