package com.hongtao.live.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hongtao.live.R;
import com.hongtao.live.UserManager;
import com.hongtao.live.login.LoginActivity;
import com.hongtao.live.module.Content;
import com.hongtao.live.module.Room;
import com.hongtao.live.module.User;
import com.hongtao.live.money.MoneyDialog;
import com.hongtao.live.money.MoneyRecordDialog;
import com.hongtao.live.param.AudioParam;
import com.hongtao.live.param.VideoParam;
import com.hongtao.live.util.DateUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public class MeFragment extends Fragment implements View.OnClickListener, MeContract.View, MoneyDialog.Callback {
    private static final String TAG = "MeFragment";

    private MePresenter mMePresenter;

    private TextView mTvUserName, mTvUserId, mTvGender, mTvBirthday, mTvJob, mTvAddress, mTvIntroduce, mTvLiveIntroduce, mTvMoney, mTvRecharge, mTvWithdraw, mTvRecord;
    private ImageView mIvAvatar, mIvLive;
    private Button mBtnLogin, mBtnLogout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);
        initView(rootView);
        mMePresenter = new MePresenter(this);
        mMePresenter.getUser();
        return rootView;
    }


    private void initView(View rootView) {
        mTvUserName = rootView.findViewById(R.id.me_tv_user_name);
        mTvUserId = rootView.findViewById(R.id.me_tv_user_id);
        mTvGender = rootView.findViewById(R.id.me_tv_gender);
        mTvBirthday = rootView.findViewById(R.id.me_tv_birthday);
        mTvJob = rootView.findViewById(R.id.me_tv_job);
        mTvAddress = rootView.findViewById(R.id.me_tv_address);
        mTvIntroduce = rootView.findViewById(R.id.me_tv_introduce);
        mTvLiveIntroduce = rootView.findViewById(R.id.me_tv_live_introduce);
        mIvLive = rootView.findViewById(R.id.me_iv_live);
        mIvLive.setOnClickListener(this);
        mIvAvatar = rootView.findViewById(R.id.me_iv_avatar);
        mBtnLogin = rootView.findViewById(R.id.me_btn_login);
        mBtnLogout = rootView.findViewById(R.id.me_btn_logout);
        mBtnLogin.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        mTvMoney = rootView.findViewById(R.id.me_tv_money);
        mTvRecharge = rootView.findViewById(R.id.me_tv_recharge);
        mTvRecharge.setOnClickListener(this);
        mTvWithdraw = rootView.findViewById(R.id.me_tv_withdraw);
        mTvWithdraw.setOnClickListener(this);
        mTvRecord = rootView.findViewById(R.id.me_tv_record);
        mTvRecord.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMePresenter.getUser();
    }

    @Override
    public void showLogin() {
        mBtnLogin.setVisibility(View.VISIBLE);
        mBtnLogout.setVisibility(View.GONE);
    }

    @Override
    public void showLogout() {
        mBtnLogin.setVisibility(View.GONE);
        mBtnLogout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUser(User user) {
        mTvUserName.setText(user.getNick());
        mTvUserId.setText(user.getUserId());
        mTvGender.setText(user.getGender() == 0 ? "女" : "男");
        mTvBirthday.setText(DateUtil.getDate(user.getBirthday()));
        mTvJob.setText(user.getJob());
        mTvAddress.setText(user.getAddress());
        mTvIntroduce.setText(user.getIntroduction());
        mTvLiveIntroduce.setText(user.getLiveIntroduction());
        mTvMoney.setText("余额：" + user.getMoney());
        Glide.with(mIvAvatar.getContext())
                .load(user.getAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(mIvAvatar);
    }

    private VideoParam mVideoParam;
    private AudioParam mAudioParam;

    @Override
    public void showCreateRoomDialog(Room room) {
        new CreateRoomDialog(getContext(), R.style.LiveDialog, room, new CreateRoomDialog.Callback() {
            @Override
            public void onConfirm(Room room, VideoParam videoParam, AudioParam audioParam) {
                mVideoParam = videoParam;
                mAudioParam = audioParam;
                if (room.getCode() == Content.Code.CODE_ROOM_NOT_EXIST) {
                    mMePresenter.createRoom(room.getRoomName(), room.getRoomIntroduction());
                } else if (room.getCode() == Content.Code.CODE_ROOM_EXIST) {
                    mMePresenter.updateRoom(room);
                }
            }

            @Override
            public void onCancel() {

            }
        }).show();
    }

    @Override
    public void startLiveActivity(Room room) {
        if (null == mVideoParam || null == mAudioParam) {
            throw new IllegalStateException("video param and audio param not init");
        }
        mMePresenter.startLiving(getContext(), room, mVideoParam, mAudioParam);
    }

    @Override
    public void showNullUser() {
        mTvUserName.setText("");
        mTvUserId.setText("");
        mTvGender.setText("");
        mTvBirthday.setText("");
        mTvJob.setText("");
        mTvAddress.setText("");
        mTvIntroduce.setText("");
        mTvLiveIntroduce.setText("");
        Glide.with(mIvAvatar.getContext())
                .load("")
                .into(mIvAvatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_btn_logout:
                UserManager.getInstance().offline();
                showNullUser();
                showLogin();
                break;
            case R.id.me_btn_login:
                LoginActivity.start(getContext());
                break;
            case R.id.me_iv_live:
                mMePresenter.checkRoom();
                break;
            case R.id.me_tv_recharge:
                new MoneyDialog(getContext(), R.style.LiveDialog, MoneyDialog.TYPE_RECHARGE, this).show();
                break;
            case R.id.me_tv_withdraw:
                new MoneyDialog(getContext(), R.style.LiveDialog, MoneyDialog.TYPE_WITHDRAW, this).show();
                break;
            case R.id.me_tv_record:
                new MoneyRecordDialog(getContext(), R.style.LiveDialog).show();
                break;
        }
    }

    @Override
    public void confirm(int type, float money) {
        if (type == MoneyDialog.TYPE_RECHARGE) {
            mMePresenter.recharge(money);
        } else if (type == MoneyDialog.TYPE_WITHDRAW) {
            mMePresenter.withdraw(money);
        }
    }
}
