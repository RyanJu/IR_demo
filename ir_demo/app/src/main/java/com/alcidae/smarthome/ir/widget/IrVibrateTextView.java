package com.alcidae.smarthome.ir.widget;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/12 10:10 1.0
 * @time 2018/4/12 10:10
 * @project ir_demo com.alcidae.smarthome.ir.widget
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/12 10:10
 */

public class IrVibrateTextView extends IrTextView {
    private Vibrator mVibrator;

    public IrVibrateTextView(Context context) {
        super(context);
        init();
    }

    public IrVibrateTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IrVibrateTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
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
