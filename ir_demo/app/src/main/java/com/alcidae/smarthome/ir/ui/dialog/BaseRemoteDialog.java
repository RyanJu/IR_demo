package com.alcidae.smarthome.ir.ui.dialog;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.alcidae.smarthome.ir.data.db.IRBean;
import com.alcidae.smarthome.ir.util.DisplayUtil;
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

    protected void setWindowSize() {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (attributes != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            attributes.width = metrics.widthPixels - DisplayUtil.dip2px(getContext(), 15) * 2;
            window.setAttributes(attributes);
        }
    }
}
