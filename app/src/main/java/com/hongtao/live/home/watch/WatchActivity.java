package com.hongtao.live.home.watch;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hongtao.live.R;
import com.hongtao.live.base.BaseActivity;
import com.hongtao.live.module.Room;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


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

    private StandardGSYVideoPlayer videoPlayer;

    private ImageView mIvAvatar;
    private TextView mTvNick, mTvIntroduction, mTvAttention;

    @Override
    public int getLayoutId() {
        return R.layout.activity_watch;
    }

    @Override
    public void initView() {
        Room room = getIntent().getParcelableExtra(KEY_ROOM);
        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.video_player);
        mIvAvatar = findViewById(R.id.watch_iv_avatar);
        mTvNick = findViewById(R.id.watch_tv_nick);
        mTvIntroduction = findViewById(R.id.watch_tv_room_introduction);
        mTvAttention = findViewById(R.id.watch_tv_attention);
        mTvAttention.setOnClickListener(this);

        mTvNick.setText(room.getNick());
        mTvIntroduction.setText(room.getRoomIntroduction());
        Glide.with(mIvAvatar.getContext())
                .load(room.getAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(mIvAvatar);

        String source1 = "rtmp://192.168.0.107:1935/Live/935245421";
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
    }

    @Override
    public void onBackPressed() {
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

    }
}
