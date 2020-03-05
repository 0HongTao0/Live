package com.hongtao.live.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
public class LiveFragment extends Fragment implements LiveContract.View, SurfaceHolder.Callback{
    private static final String TAG = "LiveFragment";
    private LivePresenter mPresenter;
    private SurfaceView mLiveView;
    private SurfaceHolder mSurfaceHolder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);
        mLiveView = rootView.findViewById(R.id.live_surface_view);
        mLiveView.getHolder().addCallback(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter = new LivePresenter(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        mPresenter.startCameraPreview(mSurfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
