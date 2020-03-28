package com.hongtao.live.home.watch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hongtao.live.R;
import com.hongtao.live.base.BaseActivity;
import com.hongtao.live.chat.MessageAdapter;
import com.hongtao.live.chat.MessageApi;
import com.hongtao.live.chat.MessageService;
import com.hongtao.live.home.attention.AttentionApi;
import com.hongtao.live.home.watch.gift.SendGiftDialog;
import com.hongtao.live.module.Message;
import com.hongtao.live.module.Room;
import com.hongtao.live.net.ServiceGenerator;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created 2020/3/17.
 *
 * @author HongTao
 */
public class WatchActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "WatchActivity";
    private static final String KEY_ROOM = "key_room";

    public static void start(Context context, Room room) {
        Intent intent = new Intent(context, WatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_ROOM, room);
        context.startActivity(intent);
    }

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

    private StandardGSYVideoPlayer videoPlayer;
    private EditText mEtMessage;
    private TextView mTvAttention, mTvGetOff;
    private RecyclerView mRvMessage;
    private Room mRoom;

    @Override
    public int getLayoutId() {
        return R.layout.activity_watch;
    }

    @Override
    public void initView() {
        mRoom = getIntent().getParcelableExtra(KEY_ROOM);
        initMessageReceiver();
        initViewData(mRoom);
        startMessageService(mRoom);
    }

    private void initMessageReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MessageService.ACTION_MESSAGE);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void initViewData(Room room) {
        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.video_player);
        ImageView ivAvatar = findViewById(R.id.watch_iv_avatar);
        TextView tvNick = findViewById(R.id.watch_tv_nick);
        TextView tvIntroduction = findViewById(R.id.watch_tv_room_introduction);
        mTvAttention = findViewById(R.id.watch_tv_attention);
        mTvAttention.setOnClickListener(this);
        mTvGetOff = findViewById(R.id.watch_tv_get_off);
        mTvGetOff.setOnClickListener(this);
        TextView tvSend = findViewById(R.id.chat_tv_send);
        tvSend.setOnClickListener(this);
        mEtMessage = findViewById(R.id.chat_et_message);
        mRvMessage = findViewById(R.id.chat_rv_message);
        findViewById(R.id.watch_iv_send_gift).setOnClickListener(this);

        tvNick.setText(room.getNick());
        tvIntroduction.setText(room.getRoomIntroduction());
        Glide.with(ivAvatar.getContext())
                .load(room.getAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(ivAvatar);
        if (room.isAttention()) {
            showGetOffTv();
        } else {
            showAttentionTv();
        }

        String source1 = mRoom.getUrl();
        videoPlayer.setUp(source1, true, room.getNick());
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);

        //增加封面
        ImageView imageView = new ImageView(this);
        Glide.with(imageView).load(mRoom.getAvatar()).into(imageView);
        videoPlayer.setThumbImageView(imageView);
        videoPlayer.setIsTouchWiget(false);

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoPlayer.startPlayLogic();
    }

    private void startMessageService(Room room) {
        Intent intent = new Intent(this, MessageService.class);
        intent.putExtra(MessageService.KEY_ROOM_ID, room.getRoomId());
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        GSYVideoManager.releaseAllVideos();
        if (null != mBroadcastReceiver) {
            unregisterReceiver(mBroadcastReceiver);
        }
        stopService(new Intent(this, MessageService.class));
    }

    @Override
    public void onBackPressed() {
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    private void showAttentionTv() {
        mTvAttention.setVisibility(View.VISIBLE);
        mTvGetOff.setVisibility(View.GONE);
    }

    private void showGetOffTv() {
        mTvAttention.setVisibility(View.GONE);
        mTvGetOff.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.watch_tv_attention:
                AttentionApi attentionApi1 = ServiceGenerator.createService(AttentionApi.class);
                attentionApi1.attentionRoom(mRoom.getRoomId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe: ");
                            }

                            @Override
                            public void onNext(Object o) {
                                showGetOffTv();
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
                break;
            case R.id.watch_tv_get_off:
                AttentionApi attentionApi2 = ServiceGenerator.createService(AttentionApi.class);
                attentionApi2.getOffRoom(mRoom.getRoomId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe: ");
                            }

                            @Override
                            public void onNext(Object o) {
                                showAttentionTv();
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
                break;
            case R.id.chat_tv_send:
                MessageApi messageApi = ServiceGenerator.createService(MessageApi.class);
                messageApi.sendMessage(mRoom.getRoomId(), mEtMessage.getText().toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe: ");
                            }

                            @Override
                            public void onNext(Object object) {
                                mEtMessage.setText("");
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
                break;
            case R.id.watch_iv_send_gift:
                new SendGiftDialog(this, R.style.createRoomDialog, mRoom.getUserId(), mRoom.getRoomId()).show();
                break;
        }
    }
}
