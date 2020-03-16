package com.hongtao.live.module;

/**
 * Created 2020/3/16.
 *
 * @author HongTao
 */
public class LoginData {
    private String token;
    private int code;

    public LoginData(String token, int code) {
        this.token = token;
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
