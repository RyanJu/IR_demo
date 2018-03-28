package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/27 18:33 1.0
 * @time 2018/3/27 18:33
 * @project ir_demo com.alcidae.smarthome.ir.widget
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/27 18:33
 */

public class MyButton extends android.support.v7.widget.AppCompatButton {
    public MyButton(Context context) {
        super(context);
        initTypeFace();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeFace();
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace();
    }

    private void initTypeFace() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/pingfang_medium.ttf");
        setTypeface(tf);
    }

}
