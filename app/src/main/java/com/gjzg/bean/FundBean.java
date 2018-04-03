package com.gjzg.bean;

public class FundBean {

    /**
     * code : 1
     * data : {"data":{"u_id":"4","uef_overage":"87076.4","uef_ticket":"0","uef_envelope":"0"}}
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
         * data : {"u_id":"4","uef_overage":"87076.4","uef_ticket":"0","uef_envelope":"0"}
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
             * u_id : 4
             * uef_overage : 87076.4
             * uef_ticket : 0
             * uef_envelope : 0
             */

            private String u_id;
            private String uef_overage;
            private String uef_ticket;
            private String uef_envelope;

            public String getU_id() {
                return u_id;
            }

            public void setU_id(String u_id) {
                this.u_id = u_id;
            }

            public String getUef_overage() {
                return uef_overage;
            }

            public void setUef_overage(String uef_overage) {
                this.uef_overage = uef_overage;
            }

            public String getUef_ticket() {
                return uef_ticket;
            }

            public void setUef_ticket(String uef_ticket) {
                this.uef_ticket = uef_ticket;
            }

            public String getUef_envelope() {
                return uef_envelope;
            }

            public void setUef_envelope(String uef_envelope) {
                this.uef_envelope = uef_envelope;
            }
        }
    }
}
