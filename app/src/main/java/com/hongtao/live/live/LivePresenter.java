package com.hongtao.live.live;

import android.app.Activity;
import android.media.projection.MediaProjection;
import android.util.Log;
import android.view.SurfaceView;

import com.hongtao.live.LivePusherNew;
import com.hongtao.live.chat.MessageApi;
import com.hongtao.live.listener.LiveStateChangeListener;
import com.hongtao.live.module.Room;
import com.hongtao.live.net.ServiceGenerator;
import com.hongtao.live.param.AudioParam;
import com.hongtao.live.param.VideoParam;
import com.upyun.screencapturecore.CaptureEngine;
import com.upyun.screencapturecore.config.CaptureConfig;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created 2020/3/5.
 *
 * @author HongTao
 */
public class LivePresenter implements LiveContract.Presenter {
    private static final String TAG = "LivePresenter";
    private static final int TYPE_CAMERA = 1;
    private static final int TYPE_DESKTOP = 2;
    private int mType = 1;

    private LiveContract.View mView;

    private boolean isLiving = false;

    private LivePusherNew mLivePusher;
    private Room mRoom;

    private CaptureConfig.PushMode mPushMode;
    private CaptureConfig.VideoResolution mVideoResolution;
    private CaptureConfig mConfig;
    private CaptureEngine mEngine;
    private MediaProjection mMediaProjection;

    private VideoParam mVideoParam;

    public LivePresenter(LiveContract.View view, Room room) {
        this.mView = view;
        this.mRoom = room;
    }

    @Override
    public void initLiveManager() {

    }

    @Override
    public void startCameraPreviewDefault(Activity activity, SurfaceView surfaceView) {
        Log.d(TAG, "startCameraPreview: ");
        mLivePusher = new LivePusherNew(activity);
        mLivePusher.setPreviewDisplay(surfaceView.getHolder());
    }

    @Override
    public void startCameraPreview(Activity activity, SurfaceView surfaceView, VideoParam videoParam, AudioParam audioParam) {
        Log.d(TAG, "startCameraPreview: " + videoParam.toString());
        Log.d(TAG, "startCameraPreview: " + audioParam.toString());
        mLivePusher = new LivePusherNew(activity, videoParam, audioParam);
        mLivePusher.setPreviewDisplay(surfaceView.getHolder());
        mVideoParam = videoParam;
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
    public void switchToDesktop(MediaProjection mediaProjection, Activity activity) {
        Log.d(TAG, "switchToDesktop: ");
        mMediaProjection = mediaProjection;
        mLivePusher.stopPush();
        mLivePusher.release();
        mLivePusher = null;
        mPushMode = CaptureConfig.PushMode.PUSH_MODE_PORTRAIT;
        mVideoResolution = CaptureConfig.VideoResolution.VIDEO_RESOLUTION_TYPE_540_960;
        mConfig = new CaptureConfig(activity, mRoom.getUrl(), mVideoParam.getBitRate(), mVideoResolution, mPushMode);
        mEngine = new CaptureEngine(mConfig);
        mEngine.start(mediaProjection);
        mView.showSwitchToCamera();
        mView.hideStartAndStopBtn();
        mType = TYPE_DESKTOP;
    }

    @Override
    public void switchToCamera(Activity activity, SurfaceView surfaceView, VideoParam videoParam, AudioParam audioParam) {
        Log.d(TAG, "switchToCamera: ");
        mEngine.stop();
        mLivePusher = new LivePusherNew(activity, videoParam, audioParam);
        mLivePusher.setPreviewDisplay(surfaceView.getHolder());
        mLivePusher.startPush(mRoom.getUrl(), new LiveStateChangeListener() {
            @Override
            public void onError(String msg) {
                Log.d(TAG, "onError: " + msg);
                mView.showStartBtn();
            }
        });
        mLivePusher.startPreview();
        mView.showSwitchToDesktop();
        mView.showStopBtn();
        mType = TYPE_CAMERA;
    }

    @Override
    public void sendMessage(Room room, String message) {
        MessageApi messageApi = ServiceGenerator.createService(MessageApi.class);
        messageApi.sendMessage(mRoom.getRoomId(), message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Object object) {
                        mView.clearMessageEt();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    public void release() {
        if (mLivePusher != null) {
            mLivePusher.release();
        }
    }
}
