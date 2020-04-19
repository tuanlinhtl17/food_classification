package com.example.foodclassificationapp.entity;

public class MyWeight {
    private String time;
    private String value;
    private String date;

    public MyWeight(String time, String value, String date) {
        this.time = time;
        this.value = value;
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
