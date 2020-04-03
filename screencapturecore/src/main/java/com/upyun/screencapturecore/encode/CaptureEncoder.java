package com.upyun.screencapturecore.encode;

import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by tanghuaiyu on 16/10/8.
 */
public class CaptureEncoder implements Runnable {
    private static final String TAG = "CaptureEncoder";

    private VideoEncoderCore mVideoEncoder;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private EncoderConfig mConfig;
    private volatile EncoderHandler mHandler;

    private Object mReadyFence = new Object();      // guards ready/running
    private boolean mReady;
    private boolean mRunning;

    private static final int MSG_START_RECORDING = 0;
    private static final int MSG_STOP_RECORDING = 1;
    private static final int MSG_QUIT = 2;
    private static final int MSG_PREPARE_DISPLAY = 3;

    public CaptureEncoder() {
        Log.d(TAG, "CaptureEncoder");
    }

    @Override
    public void run() {
        Looper.prepare();
        synchronized (mReadyFence) {
            mHandler = new EncoderHandler(this);
            mReady = true;
            mReadyFence.notify();
        }
        Looper.loop();

        Log.d(TAG, "Encoder thread exiting");
        synchronized (mReadyFence) {
            mReady = mRunning = false;
            mHandler = null;
        }
    }

    private static class EncoderHandler extends Handler {
        private WeakReference<CaptureEncoder> mWeakEncoder;

        public EncoderHandler(CaptureEncoder encoder) {
            mWeakEncoder = new WeakReference<CaptureEncoder>(encoder);
        }

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            Object obj = msg.obj;

            CaptureEncoder encoder = mWeakEncoder.get();
            if (encoder == null) {
                Log.w(TAG, "EncoderHandler.handleMessage: encoder is null");
                return;
            }

            switch (what) {
                case MSG_START_RECORDING:
                    encoder.handleStartRecording((EncoderConfig) obj);
                    break;
                case MSG_STOP_RECORDING:
                    encoder.handleStopRecording();
                    break;
                case MSG_QUIT:
                    Looper.myLooper().quit();
                    break;
                case MSG_PREPARE_DISPLAY:
                    encoder.handlePrepareDisplay((MediaProjection) obj);
                    break;
                default:
                    throw new RuntimeException("Unhandled msg what=" + what);
            }
        }
    }


    /**
     * Tells the video recorder to start recording.  (Call from non-encoder thread.)
     * <p>
     * Creates a new thread, which will create an encoder using the provided configuration.
     * <p>
     * Returns after the recorder thread has started and is ready to accept Messages.  The
     * encoder may not yet be fully configured.
     */
    public void startRecording(EncoderConfig config) {
        Log.d(TAG, "Encoder: startRecording()");
        synchronized (mReadyFence) {
            if (mRunning) {
                Log.w(TAG, "Encoder thread already running");
                return;
            }
            mRunning = true;
            new Thread(this, "CaptureEncoder").start();
            while (!mReady) {
                try {
                    mReadyFence.wait();
                } catch (InterruptedException ie) {
                    // ignore
                }
            }
        }

        mHandler.sendMessage(mHandler.obtainMessage(MSG_START_RECORDING, config));
    }

    public void prepareVirtualDisplay(MediaProjection mp) {
        Log.d(TAG, "Encoder: prepareDisplay()");
        mHandler.sendMessage(mHandler.obtainMessage(MSG_PREPARE_DISPLAY, mp));
    }

    public void stopRecording() {
        Log.d(TAG, "Encoder: stopRecording()");
        mHandler.sendMessage(mHandler.obtainMessage(MSG_STOP_RECORDING));
        mHandler.sendMessage(mHandler.obtainMessage(MSG_QUIT));
    }

    private void handleStartRecording(EncoderConfig config) {
        Log.d(TAG, "handleStartRecording " + config);
        this.mConfig = config;
        prepareEncoder(mConfig.mWidth, mConfig.mHeight, mConfig.mBitRate, mConfig.mOutputPath);
    }

    private void prepareEncoder(int width, int height, int bitRate, String outputPath) {
        try {
            mVideoEncoder = new VideoEncoderCore(width, height, bitRate, outputPath);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private void handlePrepareDisplay(MediaProjection mp) {
        Log.d(TAG, "handlePrepareDisplay " + mp);

        this.mMediaProjection = mp;
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                mConfig.mWidth,
                mConfig.mHeight,
                mConfig.mDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                getInputSurface(),
                mDisplayCallback,
                null);
    }

    private VirtualDisplay.Callback mDisplayCallback = new VirtualDisplay.Callback(){
        @Override
        public void onPaused() {
            super.onPaused();
            Log.d(TAG, "VirtualDisplay.Callback onPaused");
        }

        @Override
        public void onResumed() {
            super.onResumed();
            Log.d(TAG, "VirtualDisplay.Callback onResumed");
        }

        @Override
        public void onStopped() {
            super.onStopped();
            Log.d(TAG, "VirtualDisplay.Callback onStopped");
        }
    };

    private void handleStopRecording() {
        Log.d(TAG, "handleStopRecording()");
        releaseEncoder();
    }

    private void releaseEncoder() {
        Log.d(TAG, "release encoder");
        mVideoEncoder.release();

        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }

        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    /**
     * Encoder configuration.
     * <p>
     * Object is immutable, which means we can safely pass it between threads without
     * explicit synchronization (and don't need to worry about it getting tweaked out from
     * under us).
     * <p>
     * TODO: make frame rate and iframe interval configurable?  Maybe use builder pattern
     * with reasonable defaults for those and bit rate.
     */
    public static class EncoderConfig {
        final String mOutputPath;
        final int mWidth;
        final int mHeight;
        final int mBitRate;
        final int mDensity;

        public EncoderConfig(String outputPath, int width, int height, int bitRate, int density) {
            mOutputPath = outputPath;
            mWidth = width;
            mHeight = height;
            mBitRate = bitRate;
            mDensity = density;
        }

        @Override
        public String toString() {
            return "EncoderConfig: " + mWidth + "x" + mHeight + " @" + mBitRate +
                    " to '" + mOutputPath + " density:" + mDensity;
        }
    }

    private Surface getInputSurface() {
        if (mVideoEncoder != null) {
            return mVideoEncoder.getInputSurface();
        }

        return null;
    }
}
