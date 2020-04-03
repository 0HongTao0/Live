package com.upyun.screencapturecore.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.upyun.screencapturecore.R;

import java.io.IOException;

/**
 * Created by tanghuaiyu on 2016/11/2.
 */

public class CameraSource implements SurfaceHolder.Callback {
    public static final String TAG = "CameraSource";

    private Context mContext = null;
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    WindowManager windowManager;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private int mPreWidth = 640;
    private int mPreHeight = 480;
    private OrientationEventListener mOrientationEventListener;
    private Rotation mRotation = Rotation.ROTATION_90;
    private boolean attatched = false;

    enum Rotation {
        ROTATION_0,
        ROTATION_90,
        ROTATION_180,
        ROTATION_270
    };

    public CameraSource(Context context) {
        this.mContext = context;
        createFloatView();

        mOrientationEventListener = new OrientationEventListener(mContext) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return;
                }

                if (orientation > 330 || orientation < 30) {
                    if (mRotation != Rotation.ROTATION_90 && mCamera != null) {
                        mCamera.stopPreview();
                        mCamera.setDisplayOrientation(90);
                        mCamera.startPreview();
                        mRotation = Rotation.ROTATION_90;
                    }
                }
                else if (orientation > 60 && orientation < 120) {
                    if (mRotation != Rotation.ROTATION_180 && mCamera != null) {
                        mCamera.stopPreview();
                        mCamera.setDisplayOrientation(180);
                        mCamera.startPreview();
                        mRotation = Rotation.ROTATION_180;
                    }
                }
                else if (orientation > 150 && orientation < 210) {
                    if (mRotation != Rotation.ROTATION_270 && mCamera != null) {
                        mCamera.stopPreview();
                        mCamera.setDisplayOrientation(270);
                        mCamera.startPreview();
                        mRotation = Rotation.ROTATION_270;
                    }
                }
                else if (orientation > 240 && orientation < 300) {
                    if (mRotation != Rotation.ROTATION_0 && mCamera != null) {
                        mCamera.stopPreview();
                        mCamera.setDisplayOrientation(0);
                        mCamera.startPreview();
                        mRotation = Rotation.ROTATION_0;
                    }
                }
            }
        };

        mOrientationEventListener.enable();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void createFloatView() {
        initWindow();
        initUI();
    }

    public void enable() {
        openCamera();
        showFloatView(true);
    }

    public void disable() {
        stopCamera();
        showFloatView(false);
    }

    private void initWindow() {
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        wmParams.format = PixelFormat.TRANSPARENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public void setOffset(int x, int y) {
        wmParams.x = x > 0 ? x : 0;
        wmParams.y = y > 0 ? y : 0;
        if (attatched) {
            windowManager.updateViewLayout(mFloatLayout, wmParams);
        }
    }

    private void initUI() {
        // inflate layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.camera, null);
        surfaceView = (SurfaceView) mFloatLayout.findViewById(R.id.sv_camera);
        surfaceHolder = surfaceView.getHolder();
    }

    private void openCamera() {
        // open camera
        if (mCamera == null) {
            mCamera = getDefaultCamera();
        } else {
            return;
        }

        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();

            parameters.setPreviewSize(mPreWidth, mPreHeight);
            parameters.setPreviewFormat(ImageFormat.NV21);
            mCamera.setParameters(parameters);

            try {
                mCamera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.setDisplayOrientation(90);
        }

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
    }

    private Camera getDefaultCamera() {
        synchronized (Camera.class) {

            // Find the total number of cameras available
            int mNumberOfCameras = Camera.getNumberOfCameras();

            // Find the ID of the front-facing ("default") camera
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            for (int i = 0; i < mNumberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        return Camera.open(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            return null;
        }
    }

    private void stopCamera() {
        synchronized (Camera.class) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
    }

    private void showFloatView(boolean show) {
        if (show) {
            if (!attatched) {
                windowManager.addView(mFloatLayout, wmParams);
                attatched = true;
            }
        } else {
            if (attatched) {
                windowManager.removeView(mFloatLayout);
                attatched = false;
            }
        }
    }
}
