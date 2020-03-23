package com.hongtao.live.stream;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.hongtao.live.LivePusherNew;


public class VideoStream implements Camera.PreviewCallback, CameraHelper.OnChangedSizeListener {


    private LivePusherNew mLivePusher;
    private CameraHelper cameraHelper;
    private int mBitrate;
    private int mFps;
    private int mRateControl;
    private int mProfile;
    private boolean isLiving;

    public VideoStream(LivePusherNew livePusher, Activity activity, int width, int height, int bitrate, int fps, int cameraId, int rateControl, int profile) {
        mLivePusher = livePusher;
        mBitrate = bitrate;
        mFps = fps;
        mRateControl = rateControl;
        mProfile = profile;
        cameraHelper = new CameraHelper(activity, cameraId, width, height);
        cameraHelper.setPreviewCallback(this);
        cameraHelper.setOnChangedSizeListener(this);
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        cameraHelper.setPreviewDisplay(surfaceHolder);
    }


    /**
     * nv21摄像头数据
     *
     * @param data data
     * @param camera camera
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isLiving) {
            mLivePusher.pushVideo(data);
        }
    }

    public void switchCamera() {
        cameraHelper.switchCamera();
    }

    @Override
    public void onChanged(int w, int h) {
        mLivePusher.setVideoCodecInfo(w, h, mFps, mBitrate, mRateControl, mProfile);
    }

    public void startLive() {
        isLiving = true;
    }

    public void stopLive() {
        isLiving = false;
    }

    public void release() {
        cameraHelper.release();
    }
}
