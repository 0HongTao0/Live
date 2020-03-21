package com.hongtao.live.me;

import android.content.Context;

import com.hongtao.live.base.BaseContract;
import com.hongtao.live.module.Room;
import com.hongtao.live.module.User;

/**
 * Created 2020/3/19.
 *
 * @author HongTao
 */
public interface MeContract {
    interface View extends BaseContract.BaseView {
        void showUser(User user);

        void showNullUser();

        void showLogout();

        void showLogin();

        void showCreateRoomDialog(Room room);

        void startLiveActivity(Room room);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void getUser();

        void checkRoom();

        void createRoom(String roomName, String roomIntroduction);

        void updateRoom(Room room);

        void startLiving(Context context, Room room);
    }
}
