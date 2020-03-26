package com.hongtao.live.live;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.hongtao.live.R;
import com.hongtao.live.base.BaseActivity;
import com.hongtao.live.home.watch.MessageAdapter;
import com.hongtao.live.home.watch.MessageService;
import com.hongtao.live.module.Message;
import com.hongtao.live.module.Room;
import com.hongtao.live.param.AudioParam;
import com.hongtao.live.param.VideoParam;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public class LiveActivity extends BaseActivity implements LiveContract.View, View.OnClickListener {
    private static final String TAG = "LiveFragment";

    private static final String KEY_ROOM = "key_room";

    private static final String KEY_VIDEO_PARAM = "key_video_param";
    private static final String KEY_AUDIO_PARAM = "key_audio_param";
    private RecyclerView mRvMessage;
    private EditText mEtMessage;
    private Room mRoom;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Message> messages = (List<Message>) intent.getSerializableExtra(MessageService.KEY_MESSAGE);
            MessageAdapter messageAdapter = new MessageAdapter(messages);
            LinearLayoutManager manager = new LinearLayoutManager(context) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            mRvMessage.setLayoutManager(manager);
            mRvMessage.setAdapter(messageAdapter);
            mRvMessage.scrollToPosition(messages.size() - 1);
            Log.d(TAG, "onReceive: " + messages.toString());
        }
    };

    public static void start(Context context, Room room) {
        Intent intent = new Intent(context, LiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_ROOM, room);
        context.startActivity(intent);
    }

    public static void start(Context context, Room room, VideoParam videoParam, AudioParam audioParam) {
        Intent intent = new Intent(context, LiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_ROOM, room);
        intent.putExtra(KEY_VIDEO_PARAM, videoParam);
        intent.putExtra(KEY_AUDIO_PARAM, audioParam);
        context.startActivity(intent);
    }

    private LivePresenter mPresenter;
    private SurfaceView mLiveView;

    private View mBtnStart;
    private View mBtnStop;


    @Override
    public void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mPresenter.startLive();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mPresenter.stopLive();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_iv_switch_camera:
                mPresenter.switchCamera();
                break;
            case R.id.live_iv_start:
                mPresenter.startLive();
                break;
            case R.id.live_iv_stop:
                mPresenter.stopLive();
                break;
            case R.id.chat_tv_send:
                mPresenter.sendMessage(mRoom, mEtMessage.getText().toString());
                break;
        }
    }

    @Override
    public void showStartBtn() {
        mBtnStart.setVisibility(View.VISIBLE);
        mBtnStop.setVisibility(View.GONE);
    }

    @Override
    public void showStopBtn() {
        mBtnStop.setVisibility(View.VISIBLE);
        mBtnStart.setVisibility(View.GONE);
    }

    @Override
    public void clearMessageEt() {
        mEtMessage.setText("");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_live;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        mRoom = intent.getParcelableExtra(KEY_ROOM);
        mLiveView = findViewById(R.id.live_texture_view);
        findViewById(R.id.live_iv_switch_camera).setOnClickListener(this);
        mBtnStart = findViewById(R.id.live_iv_start);
        mBtnStart.setOnClickListener(this);
        mBtnStop = findViewById(R.id.live_iv_stop);
        mBtnStop.setOnClickListener(this);
        mEtMessage = findViewById(R.id.chat_et_message);
        findViewById(R.id.chat_tv_send).setOnClickListener(this);
        mRvMessage = findViewById(R.id.chat_rv_message);
        mPresenter = new LivePresenter(this, mRoom);
        mPresenter.startCameraPreview(this
                , mLiveView
                , (VideoParam) intent.getExtras().get(KEY_VIDEO_PARAM)
                , (AudioParam) intent.getExtras().get(KEY_AUDIO_PARAM));
        initMessageReceiver();
        startMessageService(mRoom);
    }

    private void initMessageReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MessageService.ACTION_MESSAGE);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void startMessageService(Room room) {
        Intent intent = new Intent(this, MessageService.class);
        intent.putExtra(MessageService.KEY_ROOM_ID, room.getRoomId());
        startService(intent);
    }
}
