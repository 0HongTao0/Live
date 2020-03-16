package com.hongtao.live.live;

import android.app.Activity;
import android.util.Log;
import android.view.TextureView;

import com.hongtao.live.LivePusherNew;
import com.hongtao.live.listener.LiveStateChangeListener;

/**
 * Created 2020/3/5.
 *
 * @author HongTao
 */
public class LivePresenter implements LiveContract.Presenter {
    private static final String TAG = "LivePresenter";
    private final static String LIVE_URL = "rtmp://192.168.0.104/abcs/r";
    private LiveContract.View mView;

    private LivePusherNew mLivePusher;

    public LivePresenter(LiveContract.View view) {
        this.mView = view;
    }

    @Override
    public void initLiveManager() {

    }

    @Override
    public void startCameraPreview(Activity activity, TextureView autoFitTextureView) {
        Log.d(TAG, "startCameraPreview: ");
        mLivePusher = new LivePusherNew(activity, autoFitTextureView);
    }

    @Override
    public void startLive() {
        mLivePusher.startPush(LIVE_URL, new LiveStateChangeListener() {
            @Override
            public void onError(String msg) {
                Log.d(TAG, "onError: " + msg);
                mView.showStartBtn();
            }
        });
        mView.showStopBtn();
    }

    @Override
    public void stopLive() {
        mLivePusher.stopPush();
        mView.showStartBtn();
    }

    @Override
    public void startFocus() {

    }

    @Override
    public void switchCamera() {
        mLivePusher.switchCamera();
    }

    @Override
    public void switchToDesktop() {

    }

    @Override
    public void switchToCamera() {

    }

    public void release() {
        if (mLivePusher != null) {
            mLivePusher.release();
        }
    }
}
