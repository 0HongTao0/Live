package com.hongtao.live.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hongtao.live.MainActivity;
import com.hongtao.live.R;
import com.hongtao.live.base.BaseActivity;
import com.hongtao.live.module.Content;
import com.hongtao.live.module.LoginData;
import com.hongtao.live.net.ServiceGenerator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created 2020/3/16.
 *
 * @author HongTao
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextInputEditText mEtUserId;
    private TextInputEditText mEtPassword;
    private Button mBtnLogin;
    private Button mBtnRegistered;

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
        mEtUserId = findViewById(R.id.login_et_user_id);
        mEtPassword = findViewById(R.id.login_et_password);
        mBtnLogin = findViewById(R.id.login_btn_login);
        mBtnLogin.setOnClickListener(this);
        mBtnRegistered = findViewById(R.id.login_btn_registered);
        mBtnRegistered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LoginApi api = ServiceGenerator.createService(LoginApi.class);
        switch (v.getId()) {
            case R.id.login_btn_login:
                api.postLogin(mEtUserId.getText().toString(), mEtPassword.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<LoginData>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onNext(LoginData loginData) {
                            if (loginData.getCode() == Content.Code.CODE_LOGIN_SUCCESS) {
                                finish();
                                MainActivity.start(LoginActivity.this);
                            } else {
                                Toast.makeText(LoginActivity.this, Content.Message.MSG_LOGIN_FAIL, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: ");
                        }
                    });
                break;
            case R.id.login_btn_registered:
                api.postRegistered(mEtUserId.getText().toString(), mEtPassword.getText().toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<LoginData>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe: ");
                            }

                            @Override
                            public void onNext(LoginData loginData) {
                                if (loginData.getCode() == Content.Code.CODE_REGISTERED_FAIL_SAME_ID) {
                                    Toast.makeText(LoginActivity.this, Content.Message.MSG_REGISTERED_FAIL_SAME_ID, Toast.LENGTH_SHORT).show();
                                } else if (loginData.getCode() == Content.Code.CODE_REGISTERED_SUCCESS) {
                                    Toast.makeText(LoginActivity.this, Content.Message.MSG_REGISTERED_SUCCESS, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
                            }
                        });
                break;
        }
    }
}
