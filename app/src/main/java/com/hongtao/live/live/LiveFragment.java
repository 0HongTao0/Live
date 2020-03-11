package com.hongtao.live.live;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.hongtao.live.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public class LiveFragment extends Fragment implements LiveContract.View, View.OnClickListener {
    private static final String TAG = "LiveFragment";
    private LivePresenter mPresenter;
    private TextureView mLiveView;

    private View mBtnStart;
    private View mBtnStop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);
        mLiveView = rootView.findViewById(R.id.live_texture_view);
        rootView.findViewById(R.id.live_iv_switch_camera).setOnClickListener(this);
        mBtnStart = rootView.findViewById(R.id.live_iv_start);
        mBtnStart.setOnClickListener(this);
        mBtnStop = rootView.findViewById(R.id.live_iv_stop);
        mBtnStop.setOnClickListener(this);
        mPresenter = new LivePresenter(this);
        mPresenter.startCameraPreview(getActivity(), mLiveView);
        return rootView;
    }

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
}
