package com.hongtao.live.live;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.hongtao.live.R;
import com.hongtao.live.base.BaseActivity;
import com.hongtao.live.home.watch.WatchActivity;
import com.hongtao.live.module.Room;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public class LiveActivity extends BaseActivity implements LiveContract.View, View.OnClickListener {
    private static final String TAG = "LiveFragment";

    private static final String KEY_ROOM = "key_room";

    public static void start(Context context, Room room) {
        Intent intent = new Intent(context, LiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_ROOM, room);
        context.startActivity(intent);
    }

    private LivePresenter mPresenter;
    private SurfaceView mLiveView;

    private View mBtnStart;
    private View mBtnStop;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mPresenter.stopLive();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
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
    public int getLayoutId() {
        return R.layout.fragment_live;
    }

    @Override
    public void initView() {
        mLiveView = findViewById(R.id.live_texture_view);
        findViewById(R.id.live_iv_switch_camera).setOnClickListener(this);
        mBtnStart = findViewById(R.id.live_iv_start);
        mBtnStart.setOnClickListener(this);
        mBtnStop = findViewById(R.id.live_iv_stop);
        mBtnStop.setOnClickListener(this);
        mPresenter = new LivePresenter(this, getIntent().getParcelableExtra(KEY_ROOM));
        mPresenter.startCameraPreview(this, mLiveView);
    }
}
