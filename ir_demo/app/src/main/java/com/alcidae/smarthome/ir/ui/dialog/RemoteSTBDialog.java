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
import com.alcidae.smarthome.ir.widget.IrPadView;
import com.hzy.tvmao.interf.IRequestResult;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;

import org.greenrobot.eventbus.EventBus;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/3 15:53 1.0
 * @time 2018/4/3 15:53
 * @project ir_demo com.alcidae.smarthome.ir.ui.dialog
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/3 15:53
 */

public class RemoteSTBDialog extends BaseRemoteDialog implements View.OnClickListener {

    private IrPadView mPadView;
    private IrData mIrData;
    private View mPadLayout;
    private View mNumLayout;

    public RemoteSTBDialog(@NonNull Context context, IRBean irBean) {
        super(context, irBean);
        setContentView(R.layout.dialog_stb);
        initView();
        initData();
    }

    private void initData() {
        if (mIrBean == null) {
            return;
        }
        IRUtils.getIRData(mIrBean.getDeviceType(), mIrBean.getRemoteId(), new IRequestResult<IrDataList>() {
            @Override
            public void onSuccess(String s, IrDataList irDataList) {
                mIrData = irDataList.getIrDataList().get(0);
            }

            @Override
            public void onFail(Integer integer, String s) {

            }
        });
    }

    private void initView() {
        if (mIrBean == null) {
            return;
        }
        TextView titleTv = findViewById(R.id.id_dialog_title);
        titleTv.setText(mIrBean.getCustomName());
        mPadLayout = findViewById(R.id.id_dialog_stb_pad_1_ll);
        mNumLayout = findViewById(R.id.id_dialog_stb_pad_num_ll);
        findViewById(R.id.id_dialog_title_close).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_power_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_menu_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_alternate_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_back_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_volume_up_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_volume_down_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_home_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_mute_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_channel_up_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_channel_down_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_0_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_1_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_2_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_3_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_4_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_5_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_6_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_7_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_8_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_9_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_input_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_stb_num_exit_iv).setOnClickListener(this);

        mPadView = findViewById(R.id.id_dialog_stb_padview);
        mPadView.setOnPadClickListener(new IrPadView.OnPadClickListener() {
            @Override
            public void clickUp() {
                sendIr(IRConst.KEY.navigate_up);
            }

            @Override
            public void clickDown() {
                sendIr(IRConst.KEY.navigate_down);
            }

            @Override
            public void clickLeft() {
                sendIr(IRConst.KEY.navigate_left);
            }

            @Override
            public void clickRight() {
                sendIr(IRConst.KEY.navigate_right);
            }

            @Override
            public void clickCenter() {
                sendIr(IRConst.KEY.ok);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_title_close:
                dismiss();
                break;
            case (R.id.id_dialog_stb_power_iv):
                power();
                break;
            case (R.id.id_dialog_stb_menu_iv):
                menu();
                break;
            case (R.id.id_dialog_stb_alternate_iv):
                alternate();
                break;
            case (R.id.id_dialog_stb_back_iv):
                sendIr(IRConst.KEY.back);
                break;
            case (R.id.id_dialog_stb_volume_up_iv):
                sendIr(IRConst.KEY.volume_up);
                break;
            case (R.id.id_dialog_stb_volume_down_iv):
                sendIr(IRConst.KEY.volume_down);
                break;
            case (R.id.id_dialog_stb_home_iv):
                sendIr(IRConst.KEY.homepage);
                break;
            case (R.id.id_dialog_stb_mute_iv):
                sendIr(IRConst.KEY.mute);
                break;
            case (R.id.id_dialog_stb_channel_up_iv):
                sendIr(IRConst.KEY.channel_up);
                break;
            case (R.id.id_dialog_stb_channel_down_iv):
                sendIr(IRConst.KEY.channel_down);
                break;
            case (R.id.id_dialog_stb_num_iv):
                changePadToNum();
                break;
            case (R.id.id_dialog_stb_num_0_iv):
                sendIr(IRConst.KEY.num_0);
                break;
            case (R.id.id_dialog_stb_num_1_iv):
                sendIr(IRConst.KEY.num_1);
                break;
            case (R.id.id_dialog_stb_num_2_iv):
                sendIr(IRConst.KEY.num_2);
                break;
            case (R.id.id_dialog_stb_num_3_iv):
                sendIr(IRConst.KEY.num_3);
                break;
            case (R.id.id_dialog_stb_num_4_iv):
                sendIr(IRConst.KEY.num_4);
                break;
            case (R.id.id_dialog_stb_num_5_iv):
                sendIr(IRConst.KEY.num_5);
                break;
            case (R.id.id_dialog_stb_num_6_iv):
                sendIr(IRConst.KEY.num_6);
                break;
            case (R.id.id_dialog_stb_num_7_iv):
                sendIr(IRConst.KEY.num_7);
                break;
            case (R.id.id_dialog_stb_num_8_iv):
                sendIr(IRConst.KEY.num_8);
                break;
            case (R.id.id_dialog_stb_num_9_iv):
                sendIr(IRConst.KEY.num_9);
                break;
            case (R.id.id_dialog_stb_num_input_iv):
                sendIr(IRConst.KEY.number);
                break;
            case (R.id.id_dialog_stb_num_exit_iv):
                changeNumToPad();
                break;
        }
    }

    private void changeNumToPad() {
        mPadLayout.setVisibility(View.VISIBLE);
        mNumLayout.setVisibility(View.GONE);
    }

    //change nav pad to numbers pad
    private void changePadToNum() {
        mPadLayout.setVisibility(View.GONE);
        mNumLayout.setVisibility(View.VISIBLE);
    }

    private void alternate() {
        sendIr(IRConst.KEY.last);
    }

    private void menu() {
        sendIr(IRConst.KEY.menu);
    }

    private void power() {
        sendIr(IRConst.KEY.power);
    }

    private void sendIr(int keyCode) {
        if (mIrData != null) {
            EventSendIR event = new EventSendIR();
            event.setDeviceType(mIrBean.getDeviceType());
            event.setRemoteId(mIrBean.getRemoteId());
            event.setIrDataArray(IRUtils.searchKeyCodeIR(mIrData, IRConst.KEY.power));
            event.setFrequency(mIrData.fre);
            EventBus.getDefault().post(event);
        }
    }
}
