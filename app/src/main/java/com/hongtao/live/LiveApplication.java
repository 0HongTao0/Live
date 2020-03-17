package com.hongtao.live;

import android.app.Application;
import android.content.Context;

/**
 * Created 2020/3/5.
 *
 * @author HongTao
 */
public class LiveApplication extends Application {
    private static final String TAG = LiveApplication.class.getSimpleName();

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        UserManager.getInstance().init();
    }

    public static Context getContext() {
        return sContext;
    }
}
