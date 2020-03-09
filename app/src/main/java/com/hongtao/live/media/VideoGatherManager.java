package com.hongtao.live.media;

import android.util.Log;

import com.hongtao.live.StreamProcessManager;
import com.hongtao.live.media.listener.CameraYUVDataListener;
import com.hongtao.live.util.CameraHelper;
import com.hongtao.live.view.AutoFitTextureView;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created 2020/3/8.
 *
 * @author HongTao
 */
public class VideoGatherManager {
    private static final String TAG = "VideoGatherManager";

    private CameraHelper mCameraHelper;

    private CameraYUVDataListener yuvDataListener;

    private int scaleWidth = 480;
    private int scaleHeight = 640;
    private int cropStartX = 0;
    private int cropStartY = 0;
    private int cropWidth = 480;
    private int cropHeight = 640;

    private LinkedBlockingQueue<byte[]> mQueue = new LinkedBlockingQueue<>();
    private Thread workThread;
    private boolean loop;


    public VideoGatherManager(AutoFitTextureView autoFitTextureView, CameraHelper cameraHelper) {
        this.mCameraHelper = cameraHelper;
        Log.d(TAG, "VideoGatherManager: cameraWidth = " + mCameraHelper.getCameraWidth() + "    cameraHeight = " + mCameraHelper.getCameraHeight());
        StreamProcessManager.init(mCameraHelper.getCameraWidth(), mCameraHelper.getCameraHeight(), scaleWidth, scaleHeight);
        StreamProcessManager.encoderVideoinit(scaleWidth, scaleHeight, scaleWidth, scaleHeight);
        initWorkThread();
        loop = true;
        workThread.start();
    }

    private void initWorkThread() {
        Log.d(TAG, "initWorkThread: ");
        workThread = new Thread() {
            @Override
            public void run() {
                while (loop && !Thread.interrupted()) {
                    try {
                        Log.d(TAG, "run: workThread is run mQueue.size = " + mQueue.size());
                        //获取阻塞队列中的数据，没有数据的时候阻塞
                        byte[] srcData = mQueue.take();
                        //生成I420(YUV标准格式数据及YUV420P)目标数据，生成后的数据长度width * height * 3 / 2
                        final byte[] dstData = new byte[scaleWidth * scaleHeight * 3 / 2];
                        final int morientation = mCameraHelper.getMorientation();
                        //压缩NV21(YUV420SP)数据，元素数据位1080 * 1920，很显然这样的数据推流会很占用带宽，我们压缩成480 * 640 的YUV数据
                        //为啥要转化为YUV420P数据？因为是在为转化为H264数据在做准备，NV21不是标准的，只能先通过转换，生成标准YUV420P数据，
                        //然后把标准数据encode为H264流
                        StreamProcessManager.compressYUV(srcData, mCameraHelper.getCameraWidth(), mCameraHelper.getCameraHeight(), dstData, scaleHeight, scaleWidth, 0, morientation, morientation == 270);

                        //进行YUV420P数据裁剪的操作，测试下这个借口，我们可以对数据进行裁剪，裁剪后的数据也是I420数据，我们采用的是libyuv库文件
                        //这个libyuv库效率非常高，这也是我们用它的原因
                        final byte[] cropData = new byte[cropWidth * cropHeight * 3 / 2];
                        StreamProcessManager.cropYUV(dstData, scaleWidth, scaleHeight, cropData, cropWidth, cropHeight, cropStartX, cropStartY);

                        //自此，我们得到了YUV420P标准数据，这个过程实际上就是NV21转化为YUV420P数据
                        //注意，有些机器是NV12格式，只是数据存储不一样，我们一样可以用libyuv库的接口转化
                        if (yuvDataListener != null) {
                            yuvDataListener.onYUVDataReceiver(cropData, mCameraHelper.getCameraWidth(), mCameraHelper.getCameraHeight());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
    }

    public void setYuvDataListener(CameraYUVDataListener listener) {
        this.yuvDataListener = listener;
    }

    public void putData(byte[] data) {
        Log.d(TAG, "putData: data length" + data.length);
        try {
            mQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
