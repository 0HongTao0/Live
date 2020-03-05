package com.hongtao.live.base;

import android.Manifest;
import android.os.Bundle;
import android.view.WindowManager;

import com.hongtao.live.util.PermissionsUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * Created 2020/3/5.
 * 主要用于申请权限
 *
 * @author HongTao
 */
public abstract class BaseActivity extends FragmentActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 10;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
        PermissionsUtils.checkAndRequestMorePermissions(this, PERMISSIONS, REQUEST_CODE_PERMISSIONS,
                new PermissionsUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        setContentView(getLayoutId());
                        initView();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionsUtils.isPermissionRequestSuccess(grantResults)) {
            setContentView(getLayoutId());
            initView();
        }
    }

    public abstract int getLayoutId();

    public abstract void initView();
}
