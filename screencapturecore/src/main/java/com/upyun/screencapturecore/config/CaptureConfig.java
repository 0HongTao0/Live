package com.upyun.screencapturecore.config;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.upyun.screencapturecore.encode.CaptureEncoder;

/**
 * Created by tanghuaiyu on 2016/11/1.
 */

public class CaptureConfig {
    public static final String TAG = "CaptureConfig";

    private PushMode mPushMode = PushMode.PUSH_MODE_PORTRAIT;
    private String mOutputPath;
    private int mPushWidth;
    private int mPushHeight;
    private int mBitRate;
    private int mDensity;
    private Context mContext;
    private CaptureEncoder.EncoderConfig mEncodeConfig;
    private DisplayMetrics metrics;

    public enum PushMode {
        PUSH_MODE_HORIZONTAL,
        PUSH_MODE_PORTRAIT
    };

    public enum VideoResolution {
        VIDEO_RESOLUTION_TYPE_360_640,
        VIDEO_RESOLUTION_TYPE_540_960,
        VIDEO_RESOLUTION_TYPE_720_1280
    };

    public CaptureConfig(Context context, String pushUrl, int bitrate, VideoResolution resolution, PushMode mode) {
        this.mContext = context;
        this.mOutputPath = pushUrl;
        this.mBitRate = bitrate;
        this.mPushMode = mode;

        metrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.densityDpi;

        switch (resolution) {
            case VIDEO_RESOLUTION_TYPE_360_640: {
                mPushWidth = (mode == PushMode.PUSH_MODE_PORTRAIT) ? 360 : 640;
                mPushHeight = (mode == PushMode.PUSH_MODE_PORTRAIT) ? 640 : 360;
                break;
            }
            case VIDEO_RESOLUTION_TYPE_540_960: {
                mPushWidth = (mode == PushMode.PUSH_MODE_PORTRAIT) ? 540 : 960;
                mPushHeight = (mode == PushMode.PUSH_MODE_PORTRAIT) ? 960 : 540;
                break;
            }
            case VIDEO_RESOLUTION_TYPE_720_1280: {
                mPushWidth = (mode == PushMode.PUSH_MODE_PORTRAIT) ? 720 : 1280;
                mPushHeight = (mode == PushMode.PUSH_MODE_PORTRAIT) ? 1280 : 720;
                break;
            }
        }

        Log.d(TAG, "push-url:" + mOutputPath + " bitrate:" + mBitRate + " mode:" + mPushMode + " width:" + mPushWidth + " height:" + mPushHeight);

        mEncodeConfig = new CaptureEncoder.EncoderConfig(mOutputPath, mPushWidth, mPushHeight, mBitRate, mDensity);
    }

    public CaptureEncoder.EncoderConfig getEncodeConfig() {
        return mEncodeConfig;
    }

    public Context getContext() {
        return mContext;
    }
}
