package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/28 10:30 1.0
 * @time 2018/3/28 10:30
 * @project ir_demo com.alcidae.smarthome.ir.widget
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/28 10:30
 */

public class MyVibrateImageView extends ImageView {
    private Vibrator mVibrator = null;

    public MyVibrateImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setClickable(true);
        setFocusable(true);
        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    public MyVibrateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyVibrateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean performClick() {
        mVibrator.vibrate(50);
        return super.performClick();
    }
}
