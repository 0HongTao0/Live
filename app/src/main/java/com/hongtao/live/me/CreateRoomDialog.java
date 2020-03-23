package com.hongtao.live.me;

import android.app.Dialog;
import android.content.Context;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hongtao.live.R;
import com.hongtao.live.module.Room;
import com.hongtao.live.param.AudioParam;
import com.hongtao.live.param.VideoParam;

import androidx.annotation.NonNull;

/**
 * Created 2020/3/20.
 *
 * @author HongTao
 */
public class CreateRoomDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "CreateRoomDialog";

    private EditText mEtRoomName, mEtRoomIntroduction, mEtBitRate, mEtFrameRate;
    private SwitchMaterial mSwCameraId;
    private Spinner mSpResolution, mSpRateControl, mSpProfile, mSpSampleRate, mSpChannelConfig, mSpAccType;

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
        ImageView ivAvatar = findViewById(R.id.room_dialog_iv_avatar);
        TextView tvNick = findViewById(R.id.room_dialog_tv_nick);
        mEtRoomName = findViewById(R.id.room_dialog_et_room_name);
        mEtRoomIntroduction = findViewById(R.id.room_dialog_et_room_introduction);
        Button btnConfirm = findViewById(R.id.room_dialog_btn_confirm);
        Button btnCancel = findViewById(R.id.room_dialog_btn_cancel);
        mSwCameraId = findViewById(R.id.room_dialog_sw_camera_id);
        mSpResolution = findViewById(R.id.room_dialog_sp_resolution);
        mEtBitRate = findViewById(R.id.room_dialog_et_bit_rate);
        mEtFrameRate = findViewById(R.id.room_dialog_et_frame_rate);
        mSpRateControl = findViewById(R.id.room_dialog_sp_rate_control);
        mSpProfile = findViewById(R.id.room_dialog_sp_profile);
        mSpSampleRate = findViewById(R.id.room_dialog_sp_sample_rate);
        mSpChannelConfig = findViewById(R.id.room_dialog_sp_channel_config);
        mSpAccType = findViewById(R.id.room_dialog_sp_acc_type);

        Glide.with(ivAvatar.getContext())
                .load(mRoom.getAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(ivAvatar);
        tvNick.setText(mRoom.getNick());
        mEtRoomName.setText(mRoom.getRoomName());
        mEtRoomIntroduction.setText(mRoom.getRoomIntroduction());

        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.room_dialog_btn_confirm:
                mRoom.setRoomName(mEtRoomName.getText().toString());
                mRoom.setRoomIntroduction(mEtRoomIntroduction.getText().toString());

                //videoParam
                int cameraId = mSwCameraId.isChecked() ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
                int width = VideoParam.RESOLUTION_WIDTH[mSpResolution.getSelectedItemPosition()];
                int height = VideoParam.RESOLUTION_HEIGHT[mSpResolution.getSelectedItemPosition()];
                int bitRate = Integer.parseInt(mEtBitRate.getText().toString());
                int frameRate = Integer.parseInt(mEtFrameRate.getText().toString());
                int rateControl = VideoParam.RATE_CONTROL[mSpRateControl.getSelectedItemPosition()];
                int profile = mSpProfile.getSelectedItemPosition();
                VideoParam videoParam = new VideoParam(width, height, cameraId, bitRate, frameRate, rateControl, profile);

                //audioParam
                int sampleRate = AudioParam.SAMPLE_RATE[mSpSampleRate.getSelectedItemPosition()];
                int channelConfig = AudioParam.CHANNEL_CONFIG[mSpChannelConfig.getSelectedItemPosition()];
                int channelNum = mSpChannelConfig.getSelectedItemPosition() == 0 ? 2 : 1;
                int accType = mSpAccType.getSelectedItemPosition() + 1;
                AudioParam audioParam = new AudioParam(channelConfig, sampleRate, AudioFormat.ENCODING_PCM_16BIT, channelNum, accType);

                mCallback.onConfirm(mRoom, videoParam, audioParam);
                break;
            case R.id.room_dialog_btn_cancel:
                dismiss();
                mCallback.onCancel();
                break;
        }
        dismiss();
    }

    interface Callback {
        void onConfirm(Room room, VideoParam videoParam, AudioParam audioParam);

        void onCancel();
    }
}
