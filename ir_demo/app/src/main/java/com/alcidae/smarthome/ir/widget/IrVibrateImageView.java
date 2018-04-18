package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.Random;

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

public class IrVibrateImageView extends android.support.v7.widget.AppCompatImageView {
    private Vibrator mVibrator = null;



    public IrVibrateImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setClickable(true);
        setFocusable(true);
        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    public IrVibrateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IrVibrateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean performClick() {
        boolean ret = super.performClick();
        if (mVibrator.hasVibrator()) {
            mVibrator.vibrate(50);
        }
        return ret;
    }
}
