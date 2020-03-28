package com.hongtao.live.live;

import android.app.Activity;
import android.util.Log;
import android.view.SurfaceView;

import com.hongtao.live.LivePusherNew;
import com.hongtao.live.chat.MessageApi;
import com.hongtao.live.listener.LiveStateChangeListener;
import com.hongtao.live.module.Room;
import com.hongtao.live.net.ServiceGenerator;
import com.hongtao.live.param.AudioParam;
import com.hongtao.live.param.VideoParam;

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
