package com.hongtao.live.home;

import android.content.Context;
import android.util.Log;

import com.hongtao.live.home.watch.WatchActivity;
import com.hongtao.live.module.Room;
import com.hongtao.live.net.ServiceGenerator;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created 2020/3/19.
 *
 * @author HongTao
 */
public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = "HomePresenter";
    private HomeContract.View mView;

    public HomePresenter(HomeContract.View view) {
        mView = view;
    }

    @Override
    public void getRoomList() {
        RoomApi api = ServiceGenerator.createService(RoomApi.class);
        api.getRooms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Room>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<Room> rooms) {
                        mView.showRoomList(rooms);
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
    public void searchRoom(String key) {
        RoomApi api = ServiceGenerator.createService(RoomApi.class);
        api.searchRooms(key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Room>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<Room> rooms) {
                        mView.showRoomList(rooms);
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
    public void startWatchActivity(Room room, Context context) {
        WatchActivity.start(context, room);
    }
}
