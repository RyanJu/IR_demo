package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/28 9:24 1.0
 * @time 2018/3/28 9:24
 * @project ir_demo com.alcidae.smarthome.ir.widget
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/28 9:24
 */

public class MyTextViewDigital extends android.support.v7.widget.AppCompatTextView {
    public MyTextViewDigital(Context context) {
        super(context);
        initTypeFace();
    }

    public MyTextViewDigital(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypeFace();
    }

    public MyTextViewDigital(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace();
    }

    private void initTypeFace() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/digital_regualar.ttf");
        setTypeface(tf);
    }

}
