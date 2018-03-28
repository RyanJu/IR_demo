package com.alcidae.smarthome.ir.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.widget.BaseFloatDialog;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/27 19:14 1.0
 * @time 2018/3/27 19:14
 * @project ir_demo com.alcidae.smarthome.ir.ui.dialog
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/27 19:14
 */

public class UnsuccessDialog extends BaseFloatDialog {
    public UnsuccessDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_unsuccess);
        initView();
    }

    private void initView() {
        findViewById(R.id.id_dialog_unsuccess_ok_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null) {
                            clickListener.onClick(UnsuccessDialog.this, DialogInterface.BUTTON_NEGATIVE);
                        }
                    }
                });
    }

    DialogInterface.OnClickListener clickListener;

    public OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
