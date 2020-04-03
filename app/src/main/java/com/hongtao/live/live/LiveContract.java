package com.hongtao.live.live;

import android.app.Activity;
import android.media.projection.MediaProjection;
import android.view.SurfaceView;

import com.hongtao.live.base.BaseContract;
import com.hongtao.live.module.Room;
import com.hongtao.live.param.AudioParam;
import com.hongtao.live.param.VideoParam;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public interface LiveContract {
    interface View extends BaseContract.BaseView {
        void showStartBtn();

        void showStopBtn();

        void hideStartAndStopBtn();

        void clearMessageEt();

        void showSwitchToCamera();

        void showSwitchToDesktop();
    }

    interface Presenter extends BaseContract.BasePresenter {
        void initLiveManager();

        void startCameraPreviewDefault(Activity activity, SurfaceView surfaceView);

        void startCameraPreview(Activity activity, SurfaceView surfaceView, VideoParam videoParam, AudioParam audioParam);

        void startLive();

        void stopLive();

        void startFocus();

        void switchCamera();

        void switchToDesktop(MediaProjection mediaProjection, Activity activity);

        void switchToCamera(Activity activity, SurfaceView surfaceView, VideoParam videoParam, AudioParam audioParam);

        void sendMessage(Room room, String message);
    }
}
