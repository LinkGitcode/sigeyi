package com.sigeyi.mvp.model.entity;

import java.io.Serializable;

public class BaseJson<T> implements Serializable {
    private T data;
    private String ret;
    private String msg;

    public T getData() {
        return data;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
