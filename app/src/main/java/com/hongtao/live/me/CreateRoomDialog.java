package com.hongtao.live.me;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hongtao.live.R;
import com.hongtao.live.module.Room;

import androidx.annotation.NonNull;

/**
 * Created 2020/3/20.
 *
 * @author HongTao
 */
public class CreateRoomDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "CreateRoomDialog";

    private ImageView mIvAvatar;
    private TextView mTvNick;
    private EditText mEtRoomName, mEtRoomIntroduction;
    private Button mBtnConfirm, mBtnCancel;

    private Room mRoom;
    private Callback mCallback;

    public CreateRoomDialog(@NonNull Context context, int themeResId, Room room, Callback callback) {
        super(context, themeResId);
        mRoom = room;
        mCallback = callback;
    }

    public CreateRoomDialog(@NonNull Context context, Room room, Callback callback) {
        super(context);
        this.mRoom = room;
        this.mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_room);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mIvAvatar = findViewById(R.id.room_dialog_iv_avatar);
        mTvNick = findViewById(R.id.room_dialog_tv_nick);
        mEtRoomName = findViewById(R.id.room_dialog_et_room_name);
        mEtRoomIntroduction = findViewById(R.id.room_dialog_et_room_introduction);
        mBtnConfirm = findViewById(R.id.room_dialog_btn_confirm);
        mBtnCancel = findViewById(R.id.room_dialog_btn_cancel);

        Glide.with(mIvAvatar.getContext())
                .load(mRoom.getAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(mIvAvatar);
        mTvNick.setText(mRoom.getNick());
        mEtRoomName.setText(mRoom.getRoomName());
        mEtRoomIntroduction.setText(mRoom.getRoomIntroduction());

        mBtnCancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.room_dialog_btn_confirm:
                mRoom.setRoomName(mEtRoomName.getText().toString());
                mRoom.setRoomIntroduction(mEtRoomIntroduction.getText().toString());
                mCallback.onConfirm(mRoom);
                break;
            case R.id.room_dialog_btn_cancel:
                dismiss();
                mCallback.onCancel();
                break;
        }
        dismiss();
    }

    interface Callback {
        void onConfirm(Room room);

        void onCancel();
    }
}
