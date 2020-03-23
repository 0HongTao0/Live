package com.hongtao.live.param;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 音频相关参数
 */

public class AudioParam implements Parcelable {
    public static final int CHANNEL_CONFIG[] = new int[]{0x4 | 0x8, 0x4, 0x8, 0x10, 0x20};
    private int channelConfig;
    public static final int SAMPLE_RATE[] = new int[]{44100, 22050, 16000, 11025};
    private int sampleRate;
    private int audioFormat;
    private int numChannels;
    private int accType;
    /**
     * AAC编码类型
     */
    public static final int ACC_MAIN = 1;
    public static final int ACC_LOW = 2;
    public static final int ACC_SSR = 3;
    public static final int ACC_LTP = 4;

    public int getAccType() {
        return accType;
    }

    public void setAccType(int accType) {
        this.accType = accType;
    }

    public AudioParam(int channelConfig, int sampleRate, int audioFormat, int numChannels, int accType) {
        this.channelConfig = channelConfig;
        this.sampleRate = sampleRate;
        this.audioFormat = audioFormat;
        this.numChannels = numChannels;
        this.accType = accType;
    }

    public AudioParam(int sampleRate, int channelConfig, int audioFormat, int numChannels) {
        this.sampleRate = sampleRate;
        this.channelConfig = channelConfig;
        this.audioFormat = audioFormat;
        this.numChannels = numChannels;
    }

    public int getChannelConfig() {
        return channelConfig;
    }

    public void setChannelConfig(int channelConfig) {
        this.channelConfig = channelConfig;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
    }

    public int getNumChannels() {
        return numChannels;
    }

    public void setNumChannels(int numChannels) {
        this.numChannels = numChannels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.channelConfig);
        dest.writeInt(this.sampleRate);
        dest.writeInt(this.audioFormat);
        dest.writeInt(this.numChannels);
        dest.writeInt(this.accType);
    }

    protected AudioParam(Parcel in) {
        this.channelConfig = in.readInt();
        this.sampleRate = in.readInt();
        this.audioFormat = in.readInt();
        this.numChannels = in.readInt();
        this.accType = in.readInt();
    }

    public static final Parcelable.Creator<AudioParam> CREATOR = new Parcelable.Creator<AudioParam>() {
        @Override
        public AudioParam createFromParcel(Parcel source) {
            return new AudioParam(source);
        }

        @Override
        public AudioParam[] newArray(int size) {
            return new AudioParam[size];
        }
    };

    @Override
    public String toString() {
        return "AudioParam{" +
                "channelConfig=" + channelConfig +
                ", sampleRate=" + sampleRate +
                ", audioFormat=" + audioFormat +
                ", numChannels=" + numChannels +
                ", accType=" + accType +
                '}';
    }
}
