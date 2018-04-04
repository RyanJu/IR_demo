package com.alcidae.smarthome.ir.ui.activity.match;

import android.view.View;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.data.IRConst;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/3 15:34 1.0
 * @time 2018/4/3 15:34
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity.match
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/3 15:34
 */

public class IRMatchBoxActivtiy extends IRMatchCommonActivity {

    private static final int STEP_POWER = 0;
    private static final int STEP_SOUND = 1;


    @Override
    protected void showStep() {
        if (mStep == STEP_POWER) {
            //power
            mCenterButtonIv.setVisibility(View.VISIBLE);
            mVolumeLayout.setVisibility(View.GONE);
            mCenterButtonIv.setImageResource(R.drawable.selector_power_switch);
            mCenterButtonHintTv.setText(R.string.ir_power);
        } else if (mStep == STEP_SOUND) {
            mCenterButtonIv.setVisibility(View.GONE);
            mVolumeLayout.setVisibility(View.VISIBLE);
            mCenterButtonHintTv.setText(R.string.ir_volume);
        }
    }

    @Override
    protected int stepMax() {
        return STEP_SOUND;
    }

    @Override
    protected int[] getIrByStep() {
        if (mStep == STEP_POWER) {
            return IRUtils.searchKeyCodeIR(mIrData, IRConst.KEY.power);
        }
        return new int[0];
    }
}
