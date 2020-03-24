package com.hongtao.live.home.watch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * Created 2020/3/24.
 *
 * @author HongTao
 */
public class VideoPlayerNoTouch extends StandardGSYVideoPlayer {
    public VideoPlayerNoTouch(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        mDismissControlTime = 0;
    }

    public VideoPlayerNoTouch(Context context) {
        super(context);
        mDismissControlTime = 0;
    }

    public VideoPlayerNoTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDismissControlTime = 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
