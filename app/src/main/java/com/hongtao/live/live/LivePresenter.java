package com.hongtao.live.live;

import android.view.WindowManager;

import com.hongtao.live.media.MediaPublisher;
import com.hongtao.live.media.VideoGatherManager;
import com.hongtao.live.media.listener.CameraNVDataListener;
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

    private VideoGatherManager mVideoGatherManager;

    private AutoFitTextureView mAutoFitTextureView;

    private MediaPublisher mMediaPublisher;

    public LivePresenter(LiveContract.View view) {
        this.mView = view;
    }

    @Override
    public void initLiveManager() {

    }

    @Override
    public void startCameraPreview(AutoFitTextureView autoFitTextureView, WindowManager windowManager) {
        this.mAutoFitTextureView = autoFitTextureView;
        mMediaPublisher = new MediaPublisher();
        mMediaPublisher.setRtmpUrl("rtmp://192.168.1.103:1935/abcs/r");
        mCameraHelper = new CameraHelper(autoFitTextureView, windowManager, new CameraNVDataListener() {
            @Override
            public void onCallback(byte[] data) {
                if (mVideoGatherManager != null) mVideoGatherManager.putData(data);
            }
        });
        mCameraHelper.openBackCamera();
        mVideoGatherManager = new VideoGatherManager(mAutoFitTextureView, mCameraHelper);
        mMediaPublisher.initVideoGather(mVideoGatherManager);
        mMediaPublisher.initAudioGather();
        mMediaPublisher.startGather();
        mMediaPublisher.startMediaEncoder();
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
