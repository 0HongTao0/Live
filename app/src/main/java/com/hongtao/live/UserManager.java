package com.hongtao.live;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created 2020/3/17.
 *
 * @author HongTao
 */
public class UserManager {
    private static final String TAG = "UserManager";

    private static final String SP_NAME_USER = "sp_user";
    public static final String KEY_TOKEN = "key_token";


    private String mToken;

    private volatile static UserManager instance;

    private SharedPreferences mSharedPreferences;

    private boolean isInit = false;


    private UserManager() {

    }

    public static UserManager getInstance() {
        if (null == instance) {
            synchronized (UserManager.class){
                if (null == instance) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        mSharedPreferences = LiveApplication.getContext().getSharedPreferences(SP_NAME_USER, Context.MODE_PRIVATE);
        mToken = mSharedPreferences.getString(KEY_TOKEN, "");
        isInit = true;
    }

    public void saveToken(String token) {
        checkInit();
        this.mToken = token;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        if ("".equals(mToken)){
            mToken = mSharedPreferences.getString(KEY_TOKEN, "");
        }
        return mToken;
    }

    public void offline() {
        mToken = "";
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_TOKEN, mToken);
        editor.apply();
    }

    private void checkInit() {
        if (!isInit) throw new IllegalStateException("user manager 没有初始化");
    }

    public boolean isLogin() {
        return !"".equals(mToken);
    }
}
