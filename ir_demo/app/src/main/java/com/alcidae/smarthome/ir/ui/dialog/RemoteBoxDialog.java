package com.alcidae.smarthome.ir.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.data.EventSendIR;
import com.alcidae.smarthome.ir.data.IRConst;
import com.alcidae.smarthome.ir.data.db.IRBean;
import com.alcidae.smarthome.ir.util.SimpeIRequestResult;
import com.alcidae.smarthome.ir.widget.IrPadView;
import com.hzy.tvmao.interf.IRequestResult;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;

import org.greenrobot.eventbus.EventBus;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/4 11:06 1.0
 * @time 2018/4/4 11:06
 * @project ir_demo com.alcidae.smarthome.ir.ui.dialog
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/4 11:06
 */

public class RemoteBoxDialog extends BaseRemoteDialog implements View.OnClickListener {
    private IrData mIrData;

    public RemoteBoxDialog(@NonNull Context context, IRBean irBean) {
        super(context, irBean);
        setContentView(R.layout.dialog_box);
        initView();
        initData();
    }

    private void initData() {
        if (mIrBean == null) return;
        IRUtils.getIRData(mIrBean.getDeviceType(), mIrBean.getRemoteId(), new SimpeIRequestResult<IrDataList>(getContext()) {
            @Override
            public void onSuccess(String s, IrDataList irDataList) {
                mIrData = irDataList.getIrDataList().get(0);
            }
        });
    }

    private void initView() {
        if (mIrBean == null)
            return;
        TextView titleTv = findViewById(R.id.id_dialog_title);
        titleTv.setText(mIrBean.getCustomName());

        findViewById(R.id.id_dialog_title_close).setOnClickListener(this);
        findViewById(R.id.id_dialog_box_power_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_box_back_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_box_home_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_box_volume_up_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_box_volume_down_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_box_menu_iv).setOnClickListener(this);
        IrPadView padView = findViewById(R.id.id_dialog_box_padview);
        padView.setOnPadClickListener(new IrPadView.OnPadClickListener() {
            @Override
            public void clickUp() {
                sendIR(IRConst.KEY.navigate_up);
            }

            @Override
            public void clickDown() {
                sendIR(IRConst.KEY.navigate_down);
            }

            @Override
            public void clickLeft() {
                sendIR(IRConst.KEY.navigate_left);
            }

            @Override
            public void clickRight() {
                sendIR(IRConst.KEY.navigate_right);
            }

            @Override
            public void clickCenter() {
                sendIR(IRConst.KEY.ok);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_title_close:
                dismiss();
                break;
            case R.id.id_dialog_box_power_iv:
                sendIR(IRConst.KEY.power);
                break;
            case R.id.id_dialog_box_back_iv:
                sendIR(IRConst.KEY.back);
                break;
            case R.id.id_dialog_box_home_iv:
                sendIR(IRConst.KEY.homepage);
                break;
            case R.id.id_dialog_box_volume_up_iv:
                sendIR(IRConst.KEY.volume_up);
                break;
            case R.id.id_dialog_box_volume_down_iv:
                sendIR(IRConst.KEY.volume_down);
                break;
            case R.id.id_dialog_box_menu_iv:
                sendIR(IRConst.KEY.menu);
                break;
        }
    }

    private void sendIR(final int keyCode) {
        IRUtils.runOnThread(new Runnable() {
            @Override
            public void run() {
                if (mIrData != null) {
                    EventSendIR event = new EventSendIR();
                    event.setDeviceType(mIrBean.getDeviceType());
                    event.setRemoteId(mIrBean.getRemoteId());
                    event.setIrDataArray(IRUtils.searchKeyCodeIR(mIrData, keyCode));
                    event.setFrequency(mIrData.fre);
                    EventBus.getDefault().post(event);
                }
            }
        });

    }
}
