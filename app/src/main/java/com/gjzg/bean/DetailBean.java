package com.gjzg.bean;

import java.util.List;

public class DetailBean {

    /**
     * code : 1
     * data : {"data":[{"id":"10","amount":"125,00","time":"1512004850","balances":"87076.4","type":"withdraw"},{"amount":"1000,00","id":"277","time":"1511941202","balances":"0","type":"pay"},{"amount":"100,00","id":"263","time":"1511937868","balances":"0","type":"pay"},{"amount":"100,00","id":"264","time":"1511937868","balances":"0","type":"pay"},{"amount":"100,00","id":"260","time":"1511937398","balances":"0","type":"pay"},{"amount":"100,00","id":"261","time":"1511937398","balances":"0","type":"pay"},{"amount":"100,00","id":"256","time":"1511924495","balances":"0","type":"pay"},{"amount":"2793,80","id":"248","time":"1511849515","balances":"0","type":"income"},{"id":"7","amount":"500,00","time":"1511849352","balances":"88406.4","type":"withdraw"},{"amount":"9500,00","id":"243","time":"1511849122","balances":"0","pfl_rate":"0","type":"pay"},{"amount":"0,95","id":"241","time":"1511848483","balances":"0","type":"income"},{"amount":"279,21","id":"188","time":"1511159851","balances":"0","pfl_rate":"0","type":"pay"},{"amount":"280,43","id":"185","time":"1511148733","balances":"0","pfl_rate":"0","type":"pay"},{"amount":"561,34","id":"169","time":"1511146589","balances":"0","pfl_rate":"0","type":"pay"},{"amount":"7,60","id":"135","time":"1510736007","balances":"0","pfl_rate":"0","type":"pay"},{"amount":"950,00","id":"127","time":"1510733001","balances":"0","pfl_rate":"0","type":"pay"},{"amount":"0,95","id":"120","time":"1510731713","balances":"0","type":"income"},{"amount":"9,50","id":"97","time":"1510715362","balances":"0","type":"income"},{"amount":"9,50","id":"91","time":"1510649629","balances":"0","type":"income"},{"amount":"9,50","id":"74","time":"1510642816","balances":"0","type":"income"},{"amount":"9,50","id":"69","time":"1510640978","balances":"0","type":"income"},{"amount":"19,00","id":"65","time":"1510640138","balances":"0","type":"income"},{"amount":"95,00","id":"61","time":"1510639431","balances":"0","type":"income"},{"amount":"95,00","id":"46","time":"1510552728","balances":"0","pfl_rate":"0","type":"pay"},{"id":"2","amount":"1,00","time":"1510538896","balances":"99900","type":"withdraw"},{"id":"1","amount":"12,00","time":"1510478309","balances":"99901","type":"withdraw"},{"amount":"1,00","id":"98","time":"1510474268","balances":"99913","type":"recharge"},{"amount":"3,80","id":"34","time":"1510454777","balances":"0","pfl_rate":"0","type":"pay"},{"amount":"241,48","id":"24","time":"1510222425","balances":"0","type":"income"},{"amount":"258,58","id":"23","time":"1510222425","balances":"0","type":"income"},{"amount":"94,05","id":"19","time":"1510220350","balances":"0","pfl_rate":"0","type":"pay"}]}
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
             * id : 10
             * amount : 125,00
             * time : 1512004850
             * balances : 87076.4
             * type : withdraw
             * pfl_rate : 0
             */

            private String id;
            private String amount;
            private String time;
            private String balances;
            private String type;
            private String pfl_rate;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getBalances() {
                return balances;
            }

            public void setBalances(String balances) {
                this.balances = balances;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPfl_rate() {
                return pfl_rate;
            }

            public void setPfl_rate(String pfl_rate) {
                this.pfl_rate = pfl_rate;
            }
        }
    }
}
