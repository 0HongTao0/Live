package com.hongtao.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hongtao.live.R;

/**
 * Created 2020/3/5.
 *
 * @author HongTao
 */
public class CameraSurfaceView extends FrameLayout implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private ImageView ivFoucView;

    public CameraSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.layout_camera, null);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.surface);
        ivFoucView = (ImageView) view.findViewById(R.id.iv_focus);
        removeAllViews();
        addView(view);

        mSurfaceView.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
