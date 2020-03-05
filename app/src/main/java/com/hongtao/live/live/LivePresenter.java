package com.hongtao.live.live;

import android.view.SurfaceHolder;

import com.hongtao.live.util.CameraHelper;

/**
 * Created 2020/3/5.
 *
 * @author HongTao
 */
public class LivePresenter implements LiveContract.Presenter {
    private LiveContract.View mView;

    private CameraHelper mCameraHelper;

    public LivePresenter(LiveContract.View view) {
        this.mView = view;
    }

    @Override
    public void initLiveManager() {

    }

    @Override
    public void startCameraPreview(SurfaceHolder surfaceHolder) {
        mCameraHelper = new CameraHelper(surfaceHolder);
        mCameraHelper.openBackCamera();
    }

    @Override
    public void startLive() {

    }

    @Override
    public void stopLive() {

    }

    @Override
    public void startFocus() {

    }

    @Override
    public void switchCamera() {

    }

    @Override
    public void switchToDesktop() {

    }

    @Override
    public void switchToCamera() {

    }
}
