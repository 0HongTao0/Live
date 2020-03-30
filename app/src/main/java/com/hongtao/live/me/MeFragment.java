package com.hongtao.live.me;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
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

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public class MeFragment extends Fragment implements View.OnClickListener, MeContract.View, MoneyDialog.Callback, AlterTextDialog.Callback, AlterGenderDialog.Callback {
    private static final String TAG = "MeFragment";

    private MePresenter mMePresenter;

    private TextView mTvNick, mTvUserId, mTvGender, mTvBirthday, mTvJob, mTvAddress, mTvIntroduce, mTvLiveIntroduce, mTvMoney, mTvRecharge, mTvWithdraw, mTvRecord;
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
        mTvNick = rootView.findViewById(R.id.me_tv_nick);
        mTvNick.setOnClickListener(this);
        mTvUserId = rootView.findViewById(R.id.me_tv_user_id);
        mTvGender = rootView.findViewById(R.id.me_tv_gender);
        mTvBirthday = rootView.findViewById(R.id.me_tv_birthday);
        mTvJob = rootView.findViewById(R.id.me_tv_job);
        mTvJob.setOnClickListener(this);
        mTvAddress = rootView.findViewById(R.id.me_tv_address);
        mTvIntroduce = rootView.findViewById(R.id.me_tv_introduce);
        mTvIntroduce.setOnClickListener(this);
        mTvLiveIntroduce = rootView.findViewById(R.id.me_tv_live_introduce);
        mTvLiveIntroduce.setOnClickListener(this);
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
        rootView.findViewById(R.id.me_rl_gender).setOnClickListener(this);
        rootView.findViewById(R.id.me_rl_birthday).setOnClickListener(this);
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
        mTvNick.setText(user.getNick());
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
    public void showAlterTextDialog(String oldData, int type) {
        new AlterTextDialog(getContext(), R.style.LiveDialog, type, oldData, this).show();
    }

    @Override
    public void showAlterGenderDialog(int gender) {
        Log.d(TAG, "showAlterGenderDialog: ");
        new AlterGenderDialog(getContext(), R.style.LiveDialog, gender, this).show();
    }

    @Override
    public void showTimePickerView(String date) {
        Log.d(TAG, "showTimePickerView: " + date);
        String[] dateStrings = date.split("-");
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(Integer.valueOf(dateStrings[0]), Integer.valueOf(dateStrings[1]) - 1, Integer.valueOf(dateStrings[2]));
        Calendar startDate = Calendar.getInstance();
        startDate.set(1990, 0, 1);
        Calendar endDate = Calendar.getInstance();

        //正确设置方式 原因：注意事项有说明
//        startDate.set(2013, 0, 1);
//        endDate.set(2020, 11, 31);

        //时间选择器
        new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Log.d(TAG, "onTimeSelect: " + date.toString());
                mMePresenter.alterBirthday(date.getTime());
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("修改生日")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.GRAY)//确定按钮文字颜色
                .setCancelColor(Color.GRAY)//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build().show();
    }

    @Override
    public void showNullUser() {
        mTvNick.setText("");
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
            case R.id.me_tv_nick:
                showAlterTextDialog(mTvNick.getText().toString(), AlterTextDialog.TYPE_NICK);
                break;
            case R.id.me_tv_job:
                showAlterTextDialog(mTvJob.getText().toString(), AlterTextDialog.TYPE_JOB);
                break;
            case R.id.me_tv_introduce:
                showAlterTextDialog(mTvIntroduce.getText().toString(), AlterTextDialog.TYPE_INTRODUCE);
                break;
            case R.id.me_tv_live_introduce:
                showAlterTextDialog(mTvLiveIntroduce.getText().toString(), AlterTextDialog.TYPE_LIVE_INTRODUCE);
                break;
            case R.id.me_rl_gender:
                showAlterGenderDialog(mTvGender.getText().toString().equals("男") ? 1 : 0);
                break;
            case R.id.me_rl_birthday:
                showTimePickerView(mTvBirthday.getText().toString());
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

    @Override
    public void confirm(String data, int type) {
        switch (type) {
            case AlterTextDialog.TYPE_NICK:
                mMePresenter.alterNick(data);
                break;
            case AlterTextDialog.TYPE_JOB:
                mMePresenter.alterJob(data);
                break;
            case AlterTextDialog.TYPE_INTRODUCE:
                mMePresenter.alterIntroduction(data);
                break;
            case AlterTextDialog.TYPE_LIVE_INTRODUCE:
                mMePresenter.alterLiveIntroduction(data);
                break;
        }
    }

    @Override
    public void confirm(int gender) {
        mMePresenter.alterGender(gender);
    }
}
