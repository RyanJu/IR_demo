package com.alcidae.smarthome.ir.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.util.ToastUtil;
import com.alcidae.smarthome.ir.widget.BaseFloatDialog;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/27 18:34 1.0
 * @time 2018/3/27 18:34
 * @project ir_demo com.alcidae.smarthome.ir.ui.dialog
 * @description input name of matched remote controller {@link com.alcidae.smarthome.ir.ui.activity.match.IRMatchBaseActivity}
 * @updateVersion 1.0
 * @updateTime 2018/3/27 18:34
 */

public class InputNameDialog extends BaseFloatDialog {
    private EditText mInputEt;
    private View mCancelBtn;
    private View mOkBtn;

    public InputNameDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_input_name);
        initView();
    }

    public String getInput() {
        return mInputEt.getText().toString();
    }


    private void initView() {
        mInputEt = findViewById(R.id.id_dialog_input_name_et);
        mCancelBtn = findViewById(R.id.id_dialog_input_name_cancel_btn);
        mOkBtn = findViewById(R.id.id_dialog_input_name_ok_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(InputNameDialog.this, DialogInterface.BUTTON_NEGATIVE);
                }
            }
        });

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mInputEt.getText().toString())) {
                    ToastUtil.toast(getContext(), R.string.ir_empty_name);
                    return;
                }

                if (clickListener != null) {
                    clickListener.onClick(InputNameDialog.this, DialogInterface.BUTTON_POSITIVE);
                }
            }
        });
    }

    Dialog.OnClickListener clickListener;

    public OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
