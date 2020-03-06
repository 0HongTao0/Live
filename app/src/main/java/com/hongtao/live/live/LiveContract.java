package com.hongtao.live.live;

import android.view.WindowManager;

import com.hongtao.live.base.BaseContract;
import com.hongtao.live.view.AutoFitTextureView;

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

        void startCameraPreview(AutoFitTextureView autoFitTextureView, WindowManager windowManager);

        void startLive();

        void stopLive();

        void startFocus();

        void switchCamera();

        void switchToDesktop();

        void switchToCamera();
    }
}
