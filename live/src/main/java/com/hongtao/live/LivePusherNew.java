package com.hongtao.live;

import android.app.Activity;
import android.media.AudioFormat;
import android.view.SurfaceHolder;

import com.hongtao.live.camera2.Camera2Helper;
import com.hongtao.live.listener.LiveStateChangeListener;
import com.hongtao.live.param.AudioParam;
import com.hongtao.live.param.VideoParam;
import com.hongtao.live.stream.AudioStream;
import com.hongtao.live.stream.VideoStream;

public class LivePusherNew {

    //视频编码器打开失败
    private final static int ERROR_VIDEO_ENCODER_OPEN = 0x01;
    //视频帧编码失败
    private final static int ERROR_VIDEO_ENCODE = 0x02;
    //音频编码器打开失败
    private final static int ERROR_AUDIO_ENCODER_OPEN = 0x03;
    //音频帧编码失败
    private final static int ERROR_AUDIO_ENCODE = 0x04;
    //RTMP连接失败
    private final static int ERROR_RTMP_CONNECT = 0x05;
    //RTMP连接流失败
    private final static int ERROR_RTMP_CONNECT_STREAM = 0x06;
    //RTMP发送数据包失败
    private final static int ERROR_RTMP_SEND_PACKET = 0x07;

    //视频参数
    private static final int VIDEO_WIDTH = 640;//分辨率设置
    private static final int VIDEO_HEIGHT = 480;
    private static final int VIDEO_BIT_RATE = 800000;//kb/s
    private static final int VIDEO_FRAME_RATE = 10;//fps
    private static final int VIDEO_RATE_CONTROL = VideoParam.RATE_CONTROL_X264_RC_ABR; //=RC_ABR 恒定码率
    private static final int VIDEO_PROFILE = VideoParam.PROFILE_BASELINE; //BASELINE 基本画质
    //音频参数
    private static final int SAMPLE_RATE = 44100;//采样率：Hz
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;//立体声道
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;//pcm16位
    private static final int NUM_CHANNELS = 2;//声道数

    static {
        System.loadLibrary("live");
    }

    private AudioStream audioStream;
    private VideoStream videoStream;
//    private VideoStreamNew videoStream;

    private LiveStateChangeListener liveStateChangeListener;

    public LivePusherNew(Activity activity) {
        VideoParam videoParam = new VideoParam(VIDEO_WIDTH, VIDEO_HEIGHT, Integer.valueOf(Camera2Helper.CAMERA_ID_BACK), VIDEO_BIT_RATE, VIDEO_FRAME_RATE);
        AudioParam audioParam = new AudioParam(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, NUM_CHANNELS);
        native_init();
        videoStream = new VideoStream(this, activity, videoParam.getWidth(), videoParam.getHeight(),
                videoParam.getBitRate(), videoParam.getFrameRate(), videoParam.getCameraId(), VIDEO_RATE_CONTROL, VIDEO_PROFILE);
        audioStream = new AudioStream(this, audioParam);
    }

    public LivePusherNew(Activity activity, VideoParam videoParam, AudioParam audioParam) {
        native_init();
        videoStream = new VideoStream(this, activity, videoParam.getWidth(), videoParam.getHeight(),
                videoParam.getBitRate(), videoParam.getFrameRate(), videoParam.getCameraId(), videoParam.getRateControl(), videoParam.getProfile());
        audioStream = new AudioStream(this, audioParam);
    }

//    public LivePusherNew(Activity activity, TextureView textureView) {
//        VideoParam videoParam = new VideoParam(VIDEO_WIDTH, VIDEO_HEIGHT, Integer.valueOf(Camera2Helper.CAMERA_ID_BACK), VIDEO_BIT_RATE, VIDEO_FRAME_RATE);
//        AudioParam audioParam = new AudioParam(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, NUM_CHANNELS);
//
//        native_init();
//        videoStream = new VideoStreamNew(this, textureView, videoParam, activity);
//        audioStream = new AudioStream(this, audioParam);
//    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        videoStream.setPreviewDisplay(surfaceHolder);
    }

    public void switchCamera() {
        videoStream.switchCamera();
    }

    public void startPreview(){
        videoStream.startPreview();
    }

    /**
     * 设置静音
     *
     * @param isMute 是否静音
     */
    public void setMute(boolean isMute) {
        audioStream.setMute(isMute);
    }

    public void startPush(String path, LiveStateChangeListener stateChangeListener) {
        this.liveStateChangeListener = stateChangeListener;
        native_start(path);
        videoStream.startLive();
        audioStream.startLive();
    }

    public void stopPush() {
        videoStream.stopLive();
        audioStream.stopLive();
        native_stop();
    }

    public void release() {
        videoStream.release();
        audioStream.release();
        native_release();
    }

    /**
     * 当native报错时，回调这个方法
     *
     * @param errCode errCode
     */
    public void errorFromNative(int errCode) {
        //停止推流
        stopPush();
        if (liveStateChangeListener != null) {
            String msg = "";
            switch (errCode) {
                case ERROR_VIDEO_ENCODER_OPEN:
                    msg = "视频编码器打开失败...";
                    break;
                case ERROR_VIDEO_ENCODE:
                    msg = "视频帧编码失败...";
                    break;
                case ERROR_AUDIO_ENCODER_OPEN:
                    msg = "音频编码器打开失败...";
                    break;
                case ERROR_AUDIO_ENCODE:
                    msg = "音频帧编码失败...";
                    break;
                case ERROR_RTMP_CONNECT:
                    msg = "RTMP连接失败...";
                    break;
                case ERROR_RTMP_CONNECT_STREAM:
                    msg = "RTMP连接流失败...";
                    break;
                case ERROR_RTMP_SEND_PACKET:
                    msg = "RTMP发送数据包失败...";
                    break;
                default:
                    break;
            }
            liveStateChangeListener.onError(msg);
        }
    }

    public void setVideoCodecInfo(int width, int height, int fps, int bitrate, int rateControl, int profile) {
        native_setVideoCodecInfo(width, height, fps, bitrate, rateControl, profile);
    }

    public void setAudioCodecInfo(int sampleRateInHz, int channels, int accType) {
        native_setAudioCodecInfo(sampleRateInHz, channels, accType);
    }

    public void start(String path) {
        native_start(path);
    }

    public int getInputSample() {
        return getInputSamples();
    }

    public void pushAudio(byte[] data) {
        native_pushAudio(data);
    }

    public void pushVideo(byte[] data) {
        native_pushVideo(data);
    }

    public void pushVideo(byte[] y, byte[] u, byte[] v) {
        native_pushVideoNew(y, u, v);
    }

    private native void native_init();

    private native void native_start(String path);

    private native void native_setVideoCodecInfo(int width, int height, int fps, int bitrate, int rateControl, int profile);

    private native void native_setAudioCodecInfo(int sampleRateInHz, int channels, int accType);

    private native int getInputSamples();

    private native void native_pushAudio(byte[] data);

    private native void native_pushVideo(byte[] data);

    private native void native_pushVideoNew(byte[] y, byte[] u, byte[] v);

    private native void native_stop();

    private native void native_release();

}
