package com.hongtao.live.login;

import android.content.Context;
import android.content.Intent;

import com.hongtao.live.R;
import com.hongtao.live.base.BaseActivity;

/**
 * Created 2020/3/16.
 *
 * @author HongTao
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }
}
