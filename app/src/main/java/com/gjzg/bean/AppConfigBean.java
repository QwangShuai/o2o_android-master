package com.gjzg.bean;


public class AppConfigBean {

    /**
     * code : 1
     * data : {"data":{"android_version":"1.0","ios_version":"1.0","charge_rate":"0.1","official_website":"www.gangjianwang.com","service_telephone":"4000788889","token_valid":"2"}}
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
        /**
         * data : {"android_version":"1.0","ios_version":"1.0","charge_rate":"0.1","official_website":"www.gangjianwang.com","service_telephone":"4000788889","token_valid":"2"}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * android_version : 1.0
             * ios_version : 1.0
             * charge_rate : 0.1
             * official_website : www.gangjianwang.com
             * service_telephone : 4000788889
             * token_valid : 2
             */

            private String android_version;
            private String ios_version;
            private String charge_rate;
            private String official_website;
            private String service_telephone;
            private String token_valid;

            public String getAndroid_version() {
                return android_version;
            }

            public void setAndroid_version(String android_version) {
                this.android_version = android_version;
            }

            public String getIos_version() {
                return ios_version;
            }

            public void setIos_version(String ios_version) {
                this.ios_version = ios_version;
            }

            public String getCharge_rate() {
                return charge_rate;
            }

            public void setCharge_rate(String charge_rate) {
                this.charge_rate = charge_rate;
            }

            public String getOfficial_website() {
                return official_website;
            }

            public void setOfficial_website(String official_website) {
                this.official_website = official_website;
            }

            public String getService_telephone() {
                return service_telephone;
            }

            public void setService_telephone(String service_telephone) {
                this.service_telephone = service_telephone;
            }

            public String getToken_valid() {
                return token_valid;
            }

            public void setToken_valid(String token_valid) {
                this.token_valid = token_valid;
            }
        }
    }
}
