package com.hongtao.live.stream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.TextureView;

import com.hongtao.live.LivePusherNew;
import com.hongtao.live.camera2.Camera2Helper;
import com.hongtao.live.camera2.Camera2Listener;
import com.hongtao.live.param.VideoParam;

/**
 * 视频推流:使用Camera2
 */
public class VideoStreamNew implements TextureView.SurfaceTextureListener, Camera2Listener {

    private static final String TAG = VideoStreamNew.class.getSimpleName();

    private LivePusherNew mLivePusher;
    private Camera2Helper camera2Helper;
    private boolean isLiving;
    private TextureView mTextureView;
    private Context mContext;
    private VideoParam mVideoParam;

    public VideoStreamNew(LivePusherNew livePusher, TextureView textureView, VideoParam videoParam, Context context) {
        this.mLivePusher = livePusher;
        this.mTextureView = textureView;
        this.mVideoParam = videoParam;
        this.mContext = context;
        mTextureView.setSurfaceTextureListener(this);
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
//        cameraHelper.setPreviewDisplay(surfaceHolder);
    }

    /**
     * 开始预览
     */
    private void startPreview() {
        int rotateDegree = 0;
        if (mContext instanceof Activity) {
            rotateDegree = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();
        }
        Log.e(TAG, "preview width=" + mTextureView.getWidth() + "--height=" + mTextureView.getHeight());
        camera2Helper = new Camera2Helper.Builder()
                .cameraListener(this)
                .specificCameraId(Camera2Helper.CAMERA_ID_BACK)
                .context(mContext.getApplicationContext())
                .previewOn(mTextureView)
//                .previewViewSize(new Point(mTextureView.getWidth(), mTextureView.getHeight()))
                .previewViewSize(new Point(mVideoParam.getWidth(), mVideoParam.getHeight()))
                .rotation(rotateDegree)
                .build();
        camera2Helper.start();
    }

    public void switchCamera() {
        if (camera2Helper != null) {
            camera2Helper.switchCamera();
        }
    }

    public void startLive() {
        isLiving = true;
    }

    public void stopLive() {
        isLiving = false;
    }

    public void release() {
        if (camera2Helper != null) {
            camera2Helper.stop();
            camera2Helper.release();
            camera2Helper = null;
        }
    }

    /**
     * 停止预览
     */
    private void stopPreview() {
        if (camera2Helper != null) {
            camera2Helper.stop();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureAvailable...");
        startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.e(TAG, "onSurfaceTextureDestroyed...");
        stopPreview();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * nv21摄像头数据
     *
     * @param y plane of y
     * @param u plane of u
     * @param v plane of v
     */
    @Override
    public void onPreviewFrame(byte[] y, byte[] u, byte[] v) {
        if (isLiving && mLivePusher != null) {
            mLivePusher.pushVideo(y, u, v);
        }
    }

    @Override
    public void onCameraOpened(Size previewSize, int displayOrientation) {
        Log.e(TAG, "onCameraOpened previewSize=" + previewSize.toString());
        if (mLivePusher != null && mVideoParam != null) {
            mLivePusher.setVideoCodecInfo(previewSize.getWidth(), previewSize.getHeight(),
                    mVideoParam.getFrameRate(), mVideoParam.getBitRate(), mVideoParam.getRateControl(), mVideoParam.getProfile());
        }
    }

    @Override
    public void onCameraClosed() {
        Log.e(TAG, "onCameraClosed");
    }

    @Override
    public void onCameraError(Exception e) {
        Log.e(TAG, "onCameraError=" + e.toString());
    }

}
