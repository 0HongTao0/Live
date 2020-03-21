package com.hongtao.live.me;

import android.content.Context;
import android.util.Log;

import com.hongtao.live.UserManager;
import com.hongtao.live.home.RoomApi;
import com.hongtao.live.live.LiveActivity;
import com.hongtao.live.module.Room;
import com.hongtao.live.module.User;
import com.hongtao.live.net.ServiceGenerator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created 2020/3/20.
 *
 * @author HongTao
 */
public class MePresenter implements MeContract.Presenter {
    private static final String TAG = "MePresenter";

    private MeContract.View mView;

    public MePresenter(MeContract.View view) {
        mView = view;
    }

    @Override
    public void getUser() {
        if (UserManager.getInstance().isLogin()) {
            mView.showLogout();
            MeApi meApi = ServiceGenerator.createService(MeApi.class);
            meApi.getUser()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onNext(User user) {
                            Log.d(TAG, "onNext: " + user.toString());
                            mView.showUser(user);
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
        } else {
            mView.showLogin();
            mView.showNullUser();
        }
    }

    @Override
    public void checkRoom() {
        RoomApi roomApi = ServiceGenerator.createService(RoomApi.class);
        roomApi.checkRoom()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Room>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Room room) {
                        mView.showCreateRoomDialog(room);
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
    }

    @Override
    public void createRoom(String roomName, String roomIntroduction) {
        RoomApi roomApi = ServiceGenerator.createService(RoomApi.class);
        roomApi.createRoom(roomName, roomIntroduction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Room>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Room room) {
                        mView.startLiveActivity(room);
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
    }

    @Override
    public void updateRoom(Room room) {
        RoomApi roomApi = ServiceGenerator.createService(RoomApi.class);
        roomApi.updateRoom(room.getRoomId(), room.getRoomName(), room.getRoomIntroduction())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Room>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Room room) {
                        mView.startLiveActivity(room);
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
    }

    @Override
    public void startLiving(Context context, Room room) {
        LiveActivity.start(context, room);
    }
}
