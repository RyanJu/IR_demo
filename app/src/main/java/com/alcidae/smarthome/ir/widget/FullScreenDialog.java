package com.alcidae.smarthome.ir.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.alcidae.smarthome.R;


/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2017/12/29 10:10 1.0
 * @time 2017/12/29 10:10
 * @project secQreNew3.0 com.rrioo.smartlife.widget
 * @description
 * @updateVersion 1.0
 * @updateTime 2017/12/29 10:10
 */

public class FullScreenDialog extends Dialog {
    public FullScreenDialog(@NonNull Context context) {
        super(context, R.style.FullScrennDialogStyle);
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
    }

    @Override
    public void show() {
        setFullScreenLayout();
        super.show();
    }

    protected void setFullScreenLayout() {
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
