package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/27 18:32 1.0
 * @time 2018/3/27 18:32
 * @project ir_demo com.alcidae.smarthome.ir.widget
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/27 18:32
 */

public class IrEditText extends android.support.v7.widget.AppCompatEditText {
    public IrEditText(Context context) {
        super(context);
        initTypeFace();
    }

    public IrEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeFace();
    }

    public IrEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace();
    }

    private void initTypeFace() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/pingfang_medium.ttf");
        setTypeface(tf);
    }

}
