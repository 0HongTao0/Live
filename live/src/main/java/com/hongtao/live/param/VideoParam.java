package com.hongtao.live.param;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 视频相关参数
 */

public class VideoParam implements Parcelable {

    public static final int RESOLUTION_HEIGHT[] = new int[]{480, 480, 800, 540, 720};
    public static final int RESOLUTION_WIDTH[] = new int[]{720, 640, 800, 960, 1280};
    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;
    /**
     * 摄像头 ID
     */
    private int cameraId;
    /**
     * 比特率
     */
    private int bitRate;
    /**
     * 帧率
     */
    private int frameRate;

    public static final int RATE_CONTROL[] = new int[]{2, 0, 1};
    public static final int RATE_CONTROL_X264_RC_CQP = 0;//恒定质量
    public static final int RATE_CONTROL_X264_RC_CRF = 1;//恒定码率
    public static final int RATE_CONTROL_X264_RC_ABR = 2;//平均码率
    /**
     * 码率控制
     */
    private int rateControl;

    public static final int PROFILE_BASELINE = 0;
    public static final int PROFILE_MAIN = 1;
    public static final int PROFILE_HIGH = 2;
    public static final int PROFILE_HIGH10 = 3;
    public static final int PROFILE_HIGH422 = 4;
    public static final int PROFILE_HIGH444 = 5;
    /**
     * 画质
     */
    private int profile;

    public VideoParam(int width, int height, int cameraId, int bitRate, int frameRate) {
        this.width = width;
        this.height = height;
        this.cameraId = cameraId;
        this.bitRate = bitRate;
        this.frameRate = frameRate;
    }

    public VideoParam(int width, int height, int cameraId, int bitRate, int frameRate, int rateControl, int profile) {
        this.width = width;
        this.height = height;
        this.cameraId = cameraId;
        this.bitRate = bitRate;
        this.frameRate = frameRate;
        this.rateControl = rateControl;
        this.profile = profile;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getRateControl() {
        return rateControl;
    }

    public void setRateControl(int rateControl) {
        this.rateControl = rateControl;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.cameraId);
        dest.writeInt(this.bitRate);
        dest.writeInt(this.frameRate);
        dest.writeInt(this.rateControl);
        dest.writeInt(this.profile);
    }

    protected VideoParam(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.cameraId = in.readInt();
        this.bitRate = in.readInt();
        this.frameRate = in.readInt();
        this.rateControl = in.readInt();
        this.profile = in.readInt();
    }

    public static final Parcelable.Creator<VideoParam> CREATOR = new Parcelable.Creator<VideoParam>() {
        @Override
        public VideoParam createFromParcel(Parcel source) {
            return new VideoParam(source);
        }

        @Override
        public VideoParam[] newArray(int size) {
            return new VideoParam[size];
        }
    };

    @Override
    public String toString() {
        return "VideoParam{" +
                "width=" + width +
                ", height=" + height +
                ", cameraId=" + cameraId +
                ", bitRate=" + bitRate +
                ", frameRate=" + frameRate +
                ", rateControl=" + rateControl +
                ", profile=" + profile +
                '}';
    }
}
