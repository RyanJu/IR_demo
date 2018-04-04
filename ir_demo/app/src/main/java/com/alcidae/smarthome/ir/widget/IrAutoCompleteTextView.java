package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/4 13:52 1.0
 * @time 2018/4/4 13:52
 * @project ir_demo com.alcidae.smarthome.ir.widget
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/4 13:52
 */

public class IrAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {
    public IrAutoCompleteTextView(Context context) {
        super(context);
        initTypeFace();
    }

    public IrAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeFace();
    }

    public IrAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace();
    }

    private void initTypeFace() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/pingfang_medium.ttf");
        setTypeface(tf);
    }
}
