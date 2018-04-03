package com.gjzg.bean;


import java.io.Serializable;

public class InputPasswordBean implements Serializable {

    private int type;
    private int number;
    private String letter;

    public InputPasswordBean() {
    }

    public InputPasswordBean(int type, int number, String letter) {
        this.type = type;
        this.number = number;
        this.letter = letter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "InputPasswordBean{" +
                "type=" + type +
                ", number=" + number +
                ", letter='" + letter + '\'' +
                '}';
    }
}
