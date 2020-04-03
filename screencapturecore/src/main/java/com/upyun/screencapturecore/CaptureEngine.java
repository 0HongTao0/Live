package com.upyun.screencapturecore;

import android.content.Context;
import android.media.projection.MediaProjection;
import android.util.Log;

import com.upyun.screencapturecore.camera.CameraSource;
import com.upyun.screencapturecore.config.CaptureConfig;
import com.upyun.screencapturecore.encode.CaptureEncoder;

/**
 * Created by tanghuaiyu on 2016/11/1.
 */

public class CaptureEngine {
    private CaptureConfig config;
    public static final String TAG = CaptureEngine.class.getSimpleName();
    private CaptureEncoder mEncoder;
    private MediaProjection mMediaProjection = null;
    private boolean mIsRecording = false;
    private CameraSource mCameraSource;
    private Context mContext;

    public CaptureEngine(CaptureConfig config) {
        this.config = config;
        mEncoder = new CaptureEncoder();
        this.mContext = config.getContext();
    }

    public void start(MediaProjection mediaProjection) {

        if (config == null) {
            Log.e(TAG, "the config is null");
            return;
        }

        this.mMediaProjection = mediaProjection;

        //初始化操作
        mEncoder.startRecording(config.getEncodeConfig());
        mEncoder.prepareVirtualDisplay(mMediaProjection);
        mIsRecording = true;
    }

    public void stop() {
        if (mIsRecording) {
            if (mEncoder != null)
                mEncoder.stopRecording();
            mIsRecording = false;
        }

        if (mCameraSource != null) {
            mCameraSource.disable();
        }
    }

    public void setCameraEnable(boolean enable) {
        if (enable) {
            if (mCameraSource == null) {
                mCameraSource = new CameraSource(mContext);
            }
            mCameraSource.enable();
        } else {
            if (mCameraSource != null) {
                mCameraSource.disable();
            }
        }
    }
}
