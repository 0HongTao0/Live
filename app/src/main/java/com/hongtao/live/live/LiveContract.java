package com.hongtao.live.live;

import android.view.SurfaceHolder;

import com.hongtao.live.base.BaseContract;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public interface LiveContract {
    interface View extends BaseContract.BaseView {
    }

    interface Presenter extends BaseContract.BasePresenter {
        void initLiveManager();

        void startCameraPreview(SurfaceHolder surfaceHolder);

        void startLive();

        void stopLive();

        void startFocus();

        void switchCamera();

        void switchToDesktop();

        void switchToCamera();
    }
}
