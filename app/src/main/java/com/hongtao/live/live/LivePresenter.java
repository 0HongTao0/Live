package com.hongtao.live.live;

import android.graphics.SurfaceTexture;

import com.hongtao.live.util.CameraHelper;
import com.hongtao.live.view.AutoFitTextureView;

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
    public void startCameraPreview(AutoFitTextureView autoFitTextureView, int width, int height) {
        mCameraHelper = new CameraHelper(autoFitTextureView);
        mCameraHelper.openFrontCamera(width, height);
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
