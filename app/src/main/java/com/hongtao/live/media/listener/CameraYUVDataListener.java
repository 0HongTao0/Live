package com.hongtao.live.media.listener;

public interface CameraYUVDataListener {

    void onYUVDataReceiver(byte[] data, int width, int height);
}
