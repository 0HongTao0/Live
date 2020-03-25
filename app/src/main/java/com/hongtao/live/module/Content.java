package com.hongtao.live.module;

/**
 * Created 2020/3/16.
 *
 * @author HongTao
 */
public interface Content {
    class Message{
        public static final String MSG_NO_TOKEN = "未携带token";
        public static final String MSG_TOKEN_EXPIRATION = "token过期";

        public static final String MSG_LOGIN_SUCCESS = "登录成功";
        public static final String MSG_LOGIN_FAIL = "登录失败，请检查账号密码。";
        public static final String MSG_REGISTERED_SUCCESS = "注册成功,请登录。";
        public static final String MSG_REGISTERED_FAIL_SAME_ID = "ID已存在";

        public static final String MSG_ME_GET_SUCCESS = "获取用户信息成功";

        public static final String MSG_ROOM_GET_SUCCESS = "直播间存在,获取信息成功";
        public static final String MSG_ROOM_GET_FAIL = "直播间不存在,需要创建直播间";
        public static final String MSG_ROOM_CREATE_SUCCESS = "创建直播间成功";
        public static final String MSG_ROOM_UPDATE_SUCCESS = "更新直播间成功";

        public static final String MSG_MESSAGE_SEND_SUCCESS = "发送信息成功";
        public static final String MSG_MESSAGE_GET_SUCCESS = "获取信息成功";
    }

    class Code{
        public static final int CODE_LOGIN_SUCCESS = 1;
        public static final int CODE_LOGIN_FAIL = -1;
        public static final int CODE_REGISTERED_SUCCESS = 1;
        public static final int CODE_REGISTERED_FAIL_SAME_ID = -1;

        public static final int CODE_ROOM_EXIST = 1;
        public static final int CODE_ROOM_NOT_EXIST = -1;
        public static final int CODE_ROOM_CREATE = 2;
        public static final int CODE_ROOM_UPDATE = 3;

        public static final int CODE_MESSAGE_SEND_SUCCESS = 1;
    }
}
