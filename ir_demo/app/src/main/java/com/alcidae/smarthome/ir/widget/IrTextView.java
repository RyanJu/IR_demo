package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.alcidae.smarthome.ir.util.TypefaceUtil;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/26 10:54 1.0
 * @time 2018/3/26 10:54
 * @project ir_demo com.alcidae.smarthome.ir.widget
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/26 10:54
 */

public class IrTextView extends android.support.v7.widget.AppCompatTextView {
    public IrTextView(Context context) {
        super(context);
        initTypeFace();
    }

    public IrTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypeFace();
    }

    public IrTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeFace();
    }


    private void initTypeFace() {
        Typeface tf = TypefaceUtil.getTypeface(getContext(), "pingfang_medium.ttf");
        if (tf != null) {
            setTypeface(tf);
        }
    }

}
