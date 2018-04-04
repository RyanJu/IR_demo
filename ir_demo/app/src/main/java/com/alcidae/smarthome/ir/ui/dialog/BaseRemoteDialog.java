package com.alcidae.smarthome.ir.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alcidae.smarthome.ir.data.db.IRBean;
import com.alcidae.smarthome.ir.widget.BaseFloatDialog;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/3 15:57 1.0
 * @time 2018/4/3 15:57
 * @project ir_demo com.alcidae.smarthome.ir.ui.dialog
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/3 15:57
 */

public class BaseRemoteDialog extends BaseFloatDialog {
    protected IRBean mIrBean;
    public BaseRemoteDialog(@NonNull Context context,@NonNull IRBean irBean) {
        super(context);
        this.mIrBean = irBean;
    }
}
