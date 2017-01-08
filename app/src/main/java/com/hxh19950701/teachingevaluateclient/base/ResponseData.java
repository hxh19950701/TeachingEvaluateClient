package com.hxh19950701.teachingevaluateclient.base;

public class ResponseData<DataType> {

    private int code;
    private String msg;
    private DataType data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataType getData() {
        return data;
    }

    public void setData(DataType data) {
        this.data = data;
    }
}