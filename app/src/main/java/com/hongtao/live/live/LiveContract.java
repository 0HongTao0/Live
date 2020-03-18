package com.hongtao.live.live;

import android.app.Activity;
import android.view.SurfaceView;

import com.hongtao.live.base.BaseContract;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public interface LiveContract {
    interface View extends BaseContract.BaseView {
        void showStartBtn();

        void showStopBtn();

    }

    interface Presenter extends BaseContract.BasePresenter {
        void initLiveManager();

        void startCameraPreview(Activity activity, SurfaceView surfaceView);

        void startLive();

        void stopLive();

        void startFocus();

        void switchCamera();

        void switchToDesktop();

        void switchToCamera();
    }
}
