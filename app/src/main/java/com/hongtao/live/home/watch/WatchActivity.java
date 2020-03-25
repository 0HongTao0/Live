package com.hongtao.live.home.watch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hongtao.live.R;
import com.hongtao.live.base.BaseActivity;
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
        TextView tvAttention = findViewById(R.id.watch_tv_attention);
        tvAttention.setOnClickListener(this);
        TextView tvSend = findViewById(R.id.watch_tv_send);
        tvSend.setOnClickListener(this);
        mEtMessage = findViewById(R.id.watch_et_message);
        mRvMessage = findViewById(R.id.watch_rv_message);

        tvNick.setText(room.getNick());
        tvIntroduction.setText(room.getRoomIntroduction());
        Glide.with(ivAvatar.getContext())
                .load(room.getAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(ivAvatar);

        String source1 = mRoom.getUrl();
        videoPlayer.setUp(source1, true, room.getNick());
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);
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
        intent.putExtra(MessageService.KEY_ROOM_ID, mRoom.getRoomId());
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (null != mBroadcastReceiver) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.watch_tv_attention:
                break;
            case R.id.watch_tv_send:
                MessageApi messageApi = ServiceGenerator.createService(MessageApi.class);
                messageApi.sendMessage(mRoom.getRoomId(), mEtMessage.getText().toString(), 1)
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
        }
    }
}
