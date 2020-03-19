package com.hongtao.live.home;

import android.content.Context;

import com.hongtao.live.base.BaseContract;
import com.hongtao.live.module.Room;

import java.util.List;

/**
 * Created 2020/3/19.
 *
 * @author HongTao
 */
public interface HomeContract {
    interface View extends BaseContract.BaseView {
        void showRoomList(List<Room> rooms);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void getRoomList();

        void searchRoom(String key);

        void startWatchActivity(Room room, Context context);
    }
}
