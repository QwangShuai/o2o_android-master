package com.gjzg.bean;

import java.io.Serializable;


public class WithDrawBean implements Serializable {

    private String u_id;
    private String uwl_amount;
    private String p_id;
    private String uwl_card;
    private String uwl_truename;
    private String password;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getUwl_amount() {
        return uwl_amount;
    }

    public void setUwl_amount(String uwl_amount) {
        this.uwl_amount = uwl_amount;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getUwl_card() {
        return uwl_card;
    }

    public void setUwl_card(String uwl_card) {
        this.uwl_card = uwl_card;
    }

    public String getUwl_truename() {
        return uwl_truename;
    }

    public void setUwl_truename(String uwl_truename) {
        this.uwl_truename = uwl_truename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "WithDrawBean{" +
                "u_id='" + u_id + '\'' +
                ", uwl_amount='" + uwl_amount + '\'' +
                ", p_id='" + p_id + '\'' +
                ", uwl_card='" + uwl_card + '\'' +
                ", uwl_truename='" + uwl_truename + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
