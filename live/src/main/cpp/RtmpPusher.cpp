#include <jni.h>
#include <string>
#include "include/rtmp/rtmp.h"
#include "safe_queue.h"
#include "PushInterface.h"
#include "VideoStream.h"
#include "AudioStream.h"

#define RTMP_PUSHER_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    extern "C" \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hongtao_live_LivePusherNew_ ## FUNC_NAME \
    (JNIEnv *env, jobject instance, ##__VA_ARGS__)\

SafeQueue<RTMPPacket *> packets;
VideoStream *videoStream = 0;
int isStart = 0;
pthread_t pid;

int readyPushing = 0;
uint32_t start_time;

AudioStream *audioStream = 0;

//子线程回调给Java需要用到JavaVM
JavaVM *javaVM;
//调用类
jobject jobject_error;

/***************与Java层对应**************/
//视频编码器打开失败
const int ERROR_VIDEO_ENCODER_OPEN = 0x01;
//视频帧编码失败
const int ERROR_VIDEO_ENCODE = 0x02;
//音频编码器打开失败
const int ERROR_AUDIO_ENCODER_OPEN = 0x03;
//音频帧编码失败
const int ERROR_AUDIO_ENCODE = 0x04;
//RTMP连接失败
const int ERROR_RTMP_CONNECT = 0x05;
//RTMP连接流失败
const int ERROR_RTMP_CONNECT_STREAM = 0x06;
//RTMP发送数据包失败
const int ERROR_RTMP_SEND_PACKET = 0x07;

/***************与Java层对应**************/

//当调用System.loadLibrary时，会回调这个方法
jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVM = vm;
    return JNI_VERSION_1_6;
}

//回调异常给java
void throwErrToJava(int error_code) {
    JNIEnv *env;
    javaVM->AttachCurrentThread(&env, NULL);
    jclass classErr = env->GetObjectClass(jobject_error);
    jmethodID methodErr = env->GetMethodID(classErr, "errorFromNative", "(I)V");
    env->CallVoidMethod(jobject_error, methodErr, error_code);
    javaVM->DetachCurrentThread();
}

void callback(RTMPPacket *packet) {
    if (packet) {
        packet->m_nTimeStamp = RTMP_GetTime() - start_time;
        packets.push(packet);
    }
}

void releasePackets(RTMPPacket *&packet) {
    if (packet) {
        RTMPPacket_Free(packet);
        delete packet;
        packet = 0;
    }
}

void *start(void *args) {
    char *url = static_cast<char *>(args);
    RTMP *rtmp = 0;
    do {
        rtmp = RTMP_Alloc();
        if (!rtmp) {
            LOGE("RTMP_Alloc fail");
            break;
        }
        RTMP_Init(rtmp);
        int ret = RTMP_SetupURL(rtmp, url);
        if (!ret) {
            LOGE("RTMP_SetupURL:%s", url);
            break;
        }
        //超时时间
        rtmp->Link.timeout = 5;
        RTMP_EnableWrite(rtmp);
        ret = RTMP_Connect(rtmp, 0);
        if (!ret) {
            LOGE("RTMP_Connect:%s", url);
            throwErrToJava(ERROR_RTMP_CONNECT);
            break;
        }
        ret = RTMP_ConnectStream(rtmp, 0);
        if (!ret) {
            LOGE("RTMP_ConnectStream:%s", url);
            throwErrToJava(ERROR_RTMP_CONNECT_STREAM);
            break;
        }
        //开始时间
        start_time = RTMP_GetTime();
        //开始推流
        readyPushing = 1;
        packets.setWork(1);
        callback(audioStream->getAudioTag());
        RTMPPacket *packet = 0;
        while (readyPushing) {
            packets.pop(packet);
            if (!readyPushing) {
                break;
            }
            if (!packet) {
                continue;
            }

            packet->m_nInfoField2 = rtmp->m_stream_id;
            ret = RTMP_SendPacket(rtmp, packet, 1);
            releasePackets(packet);
            if (!ret) {
                LOGE("RTMP_SendPacket fail...");
                throwErrToJava(ERROR_RTMP_SEND_PACKET);
                break;
            }
        }
        releasePackets(packet);
    } while (0);
    isStart = 0;
    readyPushing = 0;
    packets.setWork(0);
    packets.clear();
    if (rtmp) {
        RTMP_Close(rtmp);
        RTMP_Free(rtmp);
    }
    delete (url);
    return 0;
}

RTMP_PUSHER_FUNC(void, native_1init) {
    LOGI("native init...");
    videoStream = new VideoStream;
    videoStream->setVideoCallback(callback);
    audioStream = new AudioStream;
    audioStream->setAudioCallback(callback);
    packets.setReleaseCallback(releasePackets);
    jobject_error = env->NewGlobalRef(instance);
}

RTMP_PUSHER_FUNC(void, native_1setVideoCodecInfo,
        jint width, jint height, jint fps, jint bitrate, jint rateControl, jint profile) {
    if (videoStream) {
        videoStream->setVideoEncInfo(width, height, fps, bitrate, rateControl, profile);
    }
}

RTMP_PUSHER_FUNC(void, native_1start, jstring path_) {
    LOGI("native start...");
    if (isStart) {
        return;
    }
    isStart = 1;
    const char *path = env->GetStringUTFChars(path_, 0);
    char *url = new char[strlen(path) + 1];
    strcpy(url, path);
    pthread_create(&pid, 0, start, url);
    env->ReleaseStringUTFChars(path_, path);
}

RTMP_PUSHER_FUNC(void, native_1pushVideo, jbyteArray data_) {
    if (!videoStream || !readyPushing) {
        return;
    }
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    videoStream->encodeData(data);
    env->ReleaseByteArrayElements(data_, data, 0);
}

RTMP_PUSHER_FUNC(void, native_1pushVideoNew, jbyteArray y, jbyteArray u, jbyteArray v) {
    if (!videoStream || !readyPushing) {
        return;
    }
    jbyte *y_plane = env->GetByteArrayElements(y, NULL);
    jbyte *u_plane = env->GetByteArrayElements(u, NULL);
    jbyte *v_plane = env->GetByteArrayElements(v, NULL);
    videoStream->encodeDataNew(y_plane, u_plane, v_plane);
    env->ReleaseByteArrayElements(y, y_plane, 0);
    env->ReleaseByteArrayElements(u, u_plane, 0);
    env->ReleaseByteArrayElements(v, v_plane, 0);
}

RTMP_PUSHER_FUNC(void, native_1setAudioCodecInfo, jint sampleRateInHz, jint channels, jint accType) {
    if (audioStream) {
        audioStream->setAudioEncInfo(sampleRateInHz, channels, accType);
    }
}

RTMP_PUSHER_FUNC(jint, getInputSamples) {
    if (audioStream) {
        return audioStream->getInputSamples();
    }
    return -1;
}

RTMP_PUSHER_FUNC(void, native_1pushAudio, jbyteArray data_) {
    if (!audioStream || !readyPushing) {
        return;
    }
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    audioStream->encodeData(data);
    env->ReleaseByteArrayElements(data_, data, 0);
}

RTMP_PUSHER_FUNC(void, native_1stop) {
    LOGI("native stop...");
    readyPushing = 0;
    packets.setWork(0);
    pthread_join(pid, 0);
}

RTMP_PUSHER_FUNC(void, native_1release) {
    LOGI("native release...");
    env->DeleteGlobalRef(jobject_error);
    DELETE(videoStream);
    DELETE(audioStream);
}