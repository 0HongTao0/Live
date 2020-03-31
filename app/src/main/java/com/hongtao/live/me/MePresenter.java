package com.hongtao.live.me;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hongtao.live.LiveApplication;
import com.hongtao.live.UserManager;
import com.hongtao.live.home.RoomApi;
import com.hongtao.live.live.LiveActivity;
import com.hongtao.live.module.City;
import com.hongtao.live.module.Content;
import com.hongtao.live.module.Country;
import com.hongtao.live.module.NormalResponse;
import com.hongtao.live.module.Province;
import com.hongtao.live.module.Room;
import com.hongtao.live.module.User;
import com.hongtao.live.money.MoneyApi;
import com.hongtao.live.net.ServiceGenerator;
import com.hongtao.live.param.AudioParam;
import com.hongtao.live.param.VideoParam;

import java.util.List;

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
    public void startLiving(Context context, Room room, VideoParam videoParam, AudioParam audioParam) {
        LiveActivity.start(context, room, videoParam, audioParam);
    }

    @Override
    public void recharge(float money) {
        MoneyApi moneyApi = ServiceGenerator.createService(MoneyApi.class);
        moneyApi.recharge(money)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_MONEY_RECHARGE_SUCCESS, Toast.LENGTH_SHORT).show();
                        getUser();
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
    public void withdraw(float money) {
        MoneyApi moneyApi = ServiceGenerator.createService(MoneyApi.class);
        moneyApi.withdraw(money)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        if (normalResponse.getCode() == NormalResponse.CODE_SUCCESS) {
                            Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_MONEY_WITHDRAW_SUCCESS, Toast.LENGTH_SHORT).show();
                            getUser();
                        } else if (normalResponse.getCode() == NormalResponse.CODE_FAIL) {
                            Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_MONEY_WITHDRAW_FAIL, Toast.LENGTH_SHORT).show();
                        }
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
    public void alterNick(String nick) {
        ServiceGenerator.createService(MeApi.class).alterNick(nick)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        if (normalResponse.getCode() == NormalResponse.CODE_SUCCESS) {
                            Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_ME_ALTER_SUCCESS, Toast.LENGTH_SHORT).show();
                            getUser();
                        }
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
    public void alterJob(String job) {
        ServiceGenerator.createService(MeApi.class).alterJob(job)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        if (normalResponse.getCode() == NormalResponse.CODE_SUCCESS) {
                            Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_ME_ALTER_SUCCESS, Toast.LENGTH_SHORT).show();
                            getUser();
                        }
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
    public void alterIntroduction(String introduction) {
        ServiceGenerator.createService(MeApi.class).alterIntroduce(introduction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        if (normalResponse.getCode() == NormalResponse.CODE_SUCCESS) {
                            Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_ME_ALTER_SUCCESS, Toast.LENGTH_SHORT).show();
                            getUser();
                        }
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
    public void alterLiveIntroduction(String liveIntroduction) {
        ServiceGenerator.createService(MeApi.class).alterLiveIntroduce(liveIntroduction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        if (normalResponse.getCode() == NormalResponse.CODE_SUCCESS) {
                            Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_ME_ALTER_SUCCESS, Toast.LENGTH_SHORT).show();
                            getUser();
                        }
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
    public void alterGender(int gender) {
        ServiceGenerator.createService(MeApi.class).alterGender(gender)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        if (normalResponse.getCode() == NormalResponse.CODE_SUCCESS) {
                            Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_ME_ALTER_SUCCESS, Toast.LENGTH_SHORT).show();
                            getUser();
                        }
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
    public void alterBirthday(long birthday) {
        ServiceGenerator.createService(MeApi.class).alterBirthday(birthday)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        if (normalResponse.getCode() == NormalResponse.CODE_SUCCESS) {
                            Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_ME_ALTER_SUCCESS, Toast.LENGTH_SHORT).show();
                            getUser();
                        }
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
    public void getProvinces() {
        AddressApi addressApi = ServiceGenerator.createService(AddressApi.class);
        addressApi.getProvince()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Province>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<Province> provinces) {
                        mView.showProvinces(provinces);
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
    public void getCities(int provinceId) {
        AddressApi addressApi = ServiceGenerator.createService(AddressApi.class);
        addressApi.getCity(provinceId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<City>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<City> cities) {
                        mView.showCity(cities);
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
    public void getCountry(int cityId) {
        AddressApi addressApi = ServiceGenerator.createService(AddressApi.class);
        addressApi.getCountry(cityId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Country>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<Country> countries) {
                        mView.showCountry(countries);
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
    public void alterAddress(int addressId) {
        ServiceGenerator.createService(MeApi.class).alterAddress(addressId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        if (normalResponse.getCode() == NormalResponse.CODE_SUCCESS) {
                            Toast.makeText(LiveApplication.getContext(), Content.Message.MSG_ME_ALTER_SUCCESS, Toast.LENGTH_SHORT).show();
                            getUser();
                        }
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
}
