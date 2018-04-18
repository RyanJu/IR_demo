package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.alcidae.smarthome.ir.util.TypefaceUtil;

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

public class IrButton extends android.support.v7.widget.AppCompatButton {
    public IrButton(Context context) {
        super(context);
        initTypeFace();
    }

    public IrButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeFace();
    }

    public IrButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace();
    }

    private void initTypeFace() {
        Typeface tf = TypefaceUtil.getTypeface(getContext(), "pingfang_medium.ttf");
        if (tf != null)
            setTypeface(tf);
    }

}
