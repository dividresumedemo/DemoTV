package com.example.resumedemo.data;

public class ResultHttpData {
    private int tag;
    private int rtnCode;
    private String rtnDataString;
    private Object rtnData;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnDataString() {
        return rtnDataString;
    }

    public void setRtnDataString(String rtnDataString) {
        this.rtnDataString = rtnDataString;
    }

    public Object getRtnData() {
        return rtnData;
    }

    public void setRtnData(Object rtnData) {
        this.rtnData = rtnData;
    }

}
