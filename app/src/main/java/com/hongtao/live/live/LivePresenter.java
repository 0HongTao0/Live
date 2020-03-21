package com.hongtao.live.live;

import android.app.Activity;
import android.util.Log;
import android.view.SurfaceView;

import com.hongtao.live.LivePusherNew;
import com.hongtao.live.listener.LiveStateChangeListener;
import com.hongtao.live.module.Room;

/**
 * Created 2020/3/5.
 *
 * @author HongTao
 */
public class LivePresenter implements LiveContract.Presenter {
    private static final String TAG = "LivePresenter";
    private LiveContract.View mView;

    private LivePusherNew mLivePusher;
    private Room mRoom;

    public LivePresenter(LiveContract.View view, Room room) {
        this.mView = view;
        this.mRoom = room;
    }

    @Override
    public void initLiveManager() {

    }

    @Override
    public void startCameraPreview(Activity activity, SurfaceView surfaceView) {
        Log.d(TAG, "startCameraPreview: ");
        mLivePusher = new LivePusherNew(activity);
        mLivePusher.setPreviewDisplay(surfaceView.getHolder());
    }

    @Override
    public void startLive() {
        mLivePusher.startPush(mRoom.getUrl(), new LiveStateChangeListener() {
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
