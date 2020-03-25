package com.hongtao.live.home.watch;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hongtao.live.module.Message;
import com.hongtao.live.net.ServiceGenerator;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created 2020/3/25.
 *
 * @author HongTao
 */
public class MessageService extends Service {
    private static final String TAG = "MessageService";
    public static final String KEY_ROOM_ID = "key_room_id";

    public static final String ACTION_MESSAGE = "com.hongtao.live.chat.message";
    public static final String KEY_MESSAGE = "key_message";

    private static final int SLEEP_TIME = 2000;
    private int mRoomId = -1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRoomId = intent.getIntExtra(KEY_ROOM_ID, -1);
        Log.d(TAG, "onStartCommand: mRoomId = " + mRoomId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mRoomId == -1) throw new IllegalStateException("without room id");
                MessageApi messageApi = ServiceGenerator.createService(MessageApi.class);
                while (true) {
                    messageApi.getMessage(mRoomId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Observer<List<Message>>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    Log.d(TAG, "onSubscribe: ");
                                }

                                @Override
                                public void onNext(List<Message> messages) {
                                    Intent intent = new Intent(ACTION_MESSAGE);
                                    intent.putExtra(KEY_MESSAGE, (Serializable) messages);
                                    sendBroadcast(intent);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onComplete() {
                                    Log.d(TAG, "onComplete: ");
                                }
                            });
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
