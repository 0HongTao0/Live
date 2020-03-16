package com.hongtao.live.module;

/**
 * Created 2020/3/13.
 *
 * @author HongTao
 */
public class Response<T> {
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_OFFLINE = 0;
    public static final int CODE_FAIL = -1;
    private int status;
    private String msg;
    private T data;

    public Response(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
