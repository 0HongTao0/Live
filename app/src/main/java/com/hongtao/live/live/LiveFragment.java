package com.hongtao.live.live;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.hongtao.live.R;
import com.hongtao.live.view.AutoFitTextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public class LiveFragment extends Fragment implements LiveContract.View, TextureView.SurfaceTextureListener {
    private static final String TAG = "LiveFragment";
    private LivePresenter mPresenter;
    private AutoFitTextureView mLiveView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);
        mLiveView = rootView.findViewById(R.id.live_texture_view);
        mLiveView.setSurfaceTextureListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter = new LivePresenter(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mPresenter.startCameraPreview(mLiveView, getActivity().getWindowManager());
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
