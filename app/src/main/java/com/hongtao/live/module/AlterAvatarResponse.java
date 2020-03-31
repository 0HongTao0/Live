package com.hongtao.live.module;

/**
 * Created 2020/3/31.
 *
 * @author HongTao
 */
public class AlterAvatarResponse {
    public static final String MSG_AVATAR_TOO_LARGE = "头像图片太大";
    public static final String MSG_AVATAR_FORMATE_ERROR = "头像图片格式错误";
    public static final String MSG_AVATAR_ALTER_SUCCESS = "头像修改成功";
    public static final String MSG_AVATAR_ALTER_FAIl = "头像修改失败";

    public static final int CODE_AVATAR_TOO_LARGE = -1;
    public static final int CODE_AVATAR_FORMAT_ERROR = -2;
    public static final int CODE_AVATAR_ALTER_SUCCESS = 1;
    public static final int CODE_AVATAR_ALTER_FAIL = -3;

    private String url;
    private int code;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public AlterAvatarResponse(String url, int code) {
        this.url = url;
        this.code = code;
    }
}
