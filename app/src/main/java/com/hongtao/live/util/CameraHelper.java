package com.hongtao.live.util;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;

import com.hongtao.live.LiveApplication;
import com.hongtao.live.media.listener.CameraNVDataListener;
import com.hongtao.live.view.AutoFitTextureView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * Created 2020/3/5.
 *
 * @author HongTao
 */
public class CameraHelper implements ImageReader.OnImageAvailableListener {
    private static final String TAG = "CameraHelper";

    private CameraManager mCameraManager;
    private WindowManager mWindowManager;
    private String currentCameraId;
    private String frontCameraId;
    private String backCameraId;

    private AutoFitTextureView mAutoFitTextureView;

    private CameraDevice mCameraDevice;

    private CaptureRequest.Builder mPreviewRequestBuilder;

    private CaptureRequest mPreviewRequest;

    private ImageReader mImageReader;

    private CameraCaptureSession mCaptureSession;

    private HandlerThread mCameraThread;
    private Handler mCameraHandler;

    private Size mPreviewSize;

    private int cameraWidth = 1080;
    private int cameraHeight = 1920;

    private CameraNVDataListener mCameraNVDataListener;

    public CameraHelper(AutoFitTextureView autoFitTextureView, WindowManager windowManager, CameraNVDataListener cameraNVDataListener) {
        this.mCameraManager = (CameraManager) LiveApplication.getContext().getSystemService(Context.CAMERA_SERVICE);
        this.mAutoFitTextureView = autoFitTextureView;
        this.mWindowManager = windowManager;
        this.mCameraNVDataListener = cameraNVDataListener;
        init();
    }


    private void init() {
        try {
            for (String cameraId : mCameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    frontCameraId = cameraId;
                } else if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    backCameraId = cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    private void openCamera(String cameraId) {
        try {
            if (ActivityCompat.checkSelfPermission(LiveApplication.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    Log.d(TAG, "onOpened: ");
                    mCameraDevice = camera;
                    startPreview();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    Log.d(TAG, "onDisconnected: ");
                    camera.close();
                    mCameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    Log.d(TAG, "onError: ");
                    camera.close();
                    mCameraDevice = null;
                }
            }, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void openFrontCamera() {
        CameraCharacteristics characteristics
                = null;
        try {
            characteristics = mCameraManager.getCameraCharacteristics(frontCameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        StreamConfigurationMap map = characteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map == null) {
            return;
        }
        mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), cameraWidth, cameraHeight);
        configureTransform(mAutoFitTextureView.getWidth(), mAutoFitTextureView.getHeight());
        openCamera(frontCameraId);
        currentCameraId = frontCameraId;
    }

    public void openBackCamera() {
        CameraCharacteristics characteristics
                = null;
        try {
            characteristics = mCameraManager.getCameraCharacteristics(backCameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        StreamConfigurationMap map = characteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map == null) {
            return;
        }
        mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), cameraWidth, cameraHeight);
        configureTransform(mAutoFitTextureView.getWidth(), mAutoFitTextureView.getHeight());
        openCamera(backCameraId);
        currentCameraId = backCameraId;
    }

    public void startPreview() {
        try {
            mImageReader = ImageReader.newInstance(mAutoFitTextureView.getWidth(), mAutoFitTextureView.getHeight(), ImageFormat.YUV_420_888, 10);//YUV_420_888
            mImageReader.setOnImageAvailableListener(this, mCameraHandler);
            assert mAutoFitTextureView != null;
            mAutoFitTextureView.getSurfaceTexture().setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(mAutoFitTextureView.getSurfaceTexture());
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);
            mPreviewRequestBuilder.addTarget(mImageReader.getSurface());
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            if (null == mCameraDevice) {
                                return;
                            }
                            mCaptureSession = cameraCaptureSession;
                            try {
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        new CameraCaptureSession.CaptureCallback() {
                                        }, mCameraHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            Log.d(TAG, "onConfigureFailed: ");
                        }
                    }, mCameraHandler
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mAutoFitTextureView || null == mPreviewSize || null == mWindowManager) {
            return;
        }
        int rotation = mWindowManager.getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mAutoFitTextureView.setTransform(matrix);
    }


    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        Log.d(TAG, "getOptimalSize: sizeMap = " + sizeMap[0].toString());
        return sizeMap[0];
    }

    public Size getPreviewSize() {
        return mPreviewSize;
    }

    public int getCameraWidth() {
        return mPreviewSize.getWidth();
    }

    public int getCameraHeight() {
        return mPreviewSize.getHeight();
    }

    public int getMorientation() {
        int morientation = 0;
        try {
            morientation = mCameraManager.getCameraCharacteristics(currentCameraId).get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return morientation;
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        Log.d(TAG, "onImageAvailable: ");
        Image image = reader.acquireLatestImage();
        if (image == null) return;
        byte[] cameraNV21 = ImageUtils.getBytesFromImageAsType(image, ImageUtils.NV21);

        if (mCameraNVDataListener != null) {
            mCameraNVDataListener.onCallback(cameraNV21);
        }

        image.close();
    }
}
