package com.alcidae.smarthome.ir.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.widget.BaseFloatDialog;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/13 17:30 1.0
 * @time 2018/4/13 17:30
 * @project ir_demo com.alcidae.smarthome.ir.ui.dialog
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/13 17:30
 */

public class MatchLoadingDialog extends BaseFloatDialog {
    public MatchLoadingDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_match_loading);
    }

}
