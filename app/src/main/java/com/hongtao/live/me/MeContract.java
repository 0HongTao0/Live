package com.hongtao.live.me;

import android.content.Context;

import com.hongtao.live.base.BaseContract;
import com.hongtao.live.module.Room;
import com.hongtao.live.module.User;
import com.hongtao.live.param.AudioParam;
import com.hongtao.live.param.VideoParam;

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

        void showAlterTextDialog(String oldData, int type);

        void showAlterGenderDialog(int gender);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void getUser();

        void checkRoom();

        void createRoom(String roomName, String roomIntroduction);

        void updateRoom(Room room);

        void startLiving(Context context, Room room, VideoParam videoParam, AudioParam audioParam);

        void recharge(float money);

        void withdraw(float money);

        void alterNick(String nick);

        void alterJob(String job);

        void alterIntroduction(String introduction);

        void alterLiveIntroduction(String liveIntroduction);

        void alterGender(int gender);
    }
}
