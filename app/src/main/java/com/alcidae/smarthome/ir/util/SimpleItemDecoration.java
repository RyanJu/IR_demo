package com.alcidae.smarthome.ir.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2017/9/30 15:20 1.0
 * @time 2017/9/30 15:20
 * @project secQreNew3.0 com.rrioo.smartlife.helper
 * @description RecycleView itemDecoration简单子类
 * @updateVersion 1.0
 * @updateTime 2017/9/30 15:20
 */

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {
    private int mLeft;
    private int mRight;
    private int mTop;
    private int mBottom;


    /**
     *
     * @param space
     */
    public SimpleItemDecoration(int space) {
        this(space, space, space, space);
    }

    /**
     *
     * @param horizontalSpace
     * @param verticaSpace
     */
    public SimpleItemDecoration(int horizontalSpace, int verticaSpace) {
        this(horizontalSpace, horizontalSpace, verticaSpace, verticaSpace);
    }

    /**
     *
     * @param mLeft
     * @param mRight
     * @param mTop
     * @param mBottom
     */
    public SimpleItemDecoration(int mLeft, int mRight, int mTop, int mBottom) {
        this.mLeft = mLeft;
        this.mRight = mRight;
        this.mTop = mTop;
        this.mBottom = mBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        outRect.set(mLeft, mTop, mRight, mBottom);
    }
}
