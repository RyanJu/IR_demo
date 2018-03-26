package com.alcidae.smarthome.ir.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alcidae.smarthome.R;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/26 16:33 1.0
 * @time 2018/3/26 16:33
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/26 16:33
 */

public class IRMatchTestAcActivity extends IRMatchTestActivity {
    private static final int STEP_POWER = 0;
    private static final int STEP_MODE = 1;
    private static final int STEP_WIND_DIRECTION = 2;


    private int mStep = STEP_POWER;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void fillInViews() {
        super.fillInViews();
        showStep();
    }

    private void showStep() {
        if (mStep == STEP_POWER){
            mTestIv.setImageResource(R.drawable.selector_power_switch);
            mTestTv.setText(R.string.power);
        }else if (mStep == STEP_MODE){
            mTestIv.setImageResource(R.drawable.selector_mode);
            mTestTv.setText(R.string.mode);
        }else if (mStep == STEP_WIND_DIRECTION){
            mTestIv.setImageResource(R.drawable.selector_wind_direction);
            mTestTv.setText(R.string.wind_direction);
        }
    }
}
