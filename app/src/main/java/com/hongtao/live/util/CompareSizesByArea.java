package com.hongtao.live.util;

import android.util.Size;

import java.util.Comparator;

/**
 * Created 2020/3/6.
 *
 * @author HongTao
 */
public class CompareSizesByArea implements Comparator<Size> {

    @Override
    public int compare(Size lhs, Size rhs) {
        // We cast here to ensure the multiplications won't overflow
        return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                (long) rhs.getWidth() * rhs.getHeight());
    }

}
