package com.alcidae.smarthome.ir.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.data.EventSendIR;
import com.alcidae.smarthome.ir.data.db.IRBean;
import com.alcidae.smarthome.ir.util.SimpeIRequestResult;
import com.alcidae.smarthome.ir.widget.BaseFloatDialog;
import com.hzy.tvmao.KKACManagerV2;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.ac.ACConstants;
import com.hzy.tvmao.ir.ac.ACStateV2;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;

import net.tsz.afinal.FinalDb;

import org.greenrobot.eventbus.EventBus;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/27 19:31 1.0
 * @time 2018/3/27 19:31
 * @project ir_demo com.alcidae.smarthome.ir.ui.dialog
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/27 19:31
 */

public class RemoteACDialog extends BaseRemoteDialog implements View.OnClickListener, DialogInterface.OnDismissListener {

    private View mCloseIv;
    private TextView mTitleTv;
    private View mTempDownIv;
    private View mTempUpIv;
    private TextView mTempTv;
    private View mPowerIv;
    private ImageView mModeStateIv;
    private View mModeIv;
    private View mSpeedIv;
    private View mDirectionIv;
    private View mSwingIv;
    private TextView mTimingTv;
    private SeekBar mTimingSeekbar;

    //schedule time
    private float mTiming;

    //target temp
    private int mTemp;

    private KKACManagerV2 mAcManger;

    //load from server
    private IrData mIrData;

    public RemoteACDialog(@NonNull Context context, IRBean irBean) {
        super(context, irBean);
        mAcManger = new KKACManagerV2();
        initAcManger(irBean);

        setContentView(R.layout.dialog_ac);
        initView();
        try {
            updatePanel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadIrData();
        setOnDismissListener(this);
    }

    private void loadIrData() {
        IRUtils.getIRData(mIrBean.getDeviceType(), mIrBean.getRemoteId(), new SimpeIRequestResult<IrDataList>(getContext()) {
            @Override
            public void onSuccess(String s, IrDataList irDataList) {
                if (irDataList != null && irDataList.getIrDataList() != null && !irDataList.getIrDataList().isEmpty()) {
                    mIrData = irDataList.getIrDataList().get(0);
                    mAcManger.initIRData(mIrData.rid, mIrData.exts, mIrData.keys);
                    mAcManger.setACStateV2FromString(mIrBean.getAccState());
                }
            }
        });
    }

    private void initAcManger(IRBean irBean) {
        if (irBean != null) {
            try {
                mAcManger.initIRData(irBean.getRemoteId(), irBean.getExts(), irBean.getKeys());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                mAcManger.setACStateV2FromString(irBean.getAccState());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        this.mCloseIv = findViewById(R.id.id_dialog_title_close);
        this.mTitleTv = findViewById(R.id.id_dialog_title);
        this.mTempDownIv = findViewById(R.id.id_dialog_ac_temp_down_iv);
        this.mTempUpIv = findViewById(R.id.id_dialog_ac_temp_up_iv);
        this.mTempTv = findViewById(R.id.id_dialog_ac_temp_tv);
        this.mPowerIv = findViewById(R.id.id_dialog_ac_power_iv);
        this.mModeStateIv = findViewById(R.id.id_dialog_ac_cool_heat_iv);
        this.mModeIv = findViewById(R.id.id_dialog_ac_mode_iv);
        this.mSpeedIv = findViewById(R.id.id_dialog_ac_speed_iv);
        this.mDirectionIv = findViewById(R.id.id_dialog_ac_wind_direction_iv);
        this.mSwingIv = findViewById(R.id.id_dialog_ac_swing_iv);
        this.mTimingTv = findViewById(R.id.id_dialog_ac_timing_tv);
        this.mTimingSeekbar = findViewById(R.id.id_dialog_ac_timing_seekbar);

        String ac = getContext().getResources().getString(R.string.ir_ac);
        String title = mIrBean != null ? mIrBean.getBrandName() + " " + ac : ac;
        mTitleTv.setText(mIrBean.getCustomName());

        mCloseIv.setOnClickListener(this);
        mTempDownIv.setOnClickListener(this);
        mTempUpIv.setOnClickListener(this);
        mPowerIv.setOnClickListener(this);
        mModeIv.setOnClickListener(this);
        mSpeedIv.setOnClickListener(this);
        mDirectionIv.setOnClickListener(this);
        mSwingIv.setOnClickListener(this);

        initTiming();


    }

    private void initTiming() {
        if (mAcManger.isTimeingCanUse()) {
            mTimingSeekbar.setEnabled(true);
            mAcManger.timeingCheck();
            mTimingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int max = seekBar.getMax();
                    mTiming = parseTimeFromProgress(progress, max);
                    mTimingTv.setText(String.valueOf(mTiming));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                /**
                 * every half hour
                 * @param progress
                 * @param max
                 * @return
                 */
                private float parseTimeFromProgress(int progress, int max) {
                    float percent = (float) progress / (float) max;
                    int t = (int) (48 * percent);
                    float result = ((float) t) / 2;
                    return result;
                }
            });
        } else {
            mTimingSeekbar.setEnabled(false);
        }

    }

    private void updatePanel() throws Exception {
        if (mAcManger != null) {
            int powerState = mAcManger.getPowerState();
            if (powerState == ACConstants.AC_POWER_OFF) {
                //power off

                mPowerIv.setSelected(true);
                mTempDownIv.setEnabled(false);
                mTempUpIv.setEnabled(false);
                mModeIv.setEnabled(false);
                mSpeedIv.setEnabled(false);
                mDirectionIv.setEnabled(false);
                mSwingIv.setEnabled(false);
                mTimingSeekbar.setEnabled(false);

            } else {
                //power on

                mPowerIv.setSelected(false);
                mTempDownIv.setEnabled(true);
                mTempUpIv.setEnabled(true);
                mModeIv.setEnabled(true);
                mSpeedIv.setEnabled(true);
                mDirectionIv.setEnabled(true);
                mSwingIv.setEnabled(true);
                mTimingSeekbar.setEnabled(true);

            }

            updatePanelMode();

            updatePanelTemp();
        }
    }

    private void updatePanelTemp() throws Exception {
        if (mAcManger.isTempCanControl()) {//查看当前模式下，温度是否可以调节
            mTempTv.setText(String.valueOf(mAcManger.getCurTemp()));
        } else {
            mTempTv.setText("NA");//温度不可调节时，mKKACManager.getCurTemp()获取的温度值是-1
        }
    }

    private void updatePanelMode() throws Exception {
        //空调模式显示
        int modelType = mAcManger.getCurModelType();
        String modelInfo = "";
        switch (modelType) {
            case ACConstants.AC_MODE_COOL:
                modelInfo = "制冷";
                mModeStateIv.setImageResource(R.drawable.ir_ac_mode_cold);
                break;
            case ACConstants.AC_MODE_HEAT:
                modelInfo = "制热";
                mModeStateIv.setImageResource(R.drawable.ir_ac_mode_heat);
                break;
            case ACConstants.AC_MODE_AUTO:
                modelInfo = "自动";
                mModeStateIv.setImageResource(R.drawable.ir_ac_mode_auto);
                break;
            case ACConstants.AC_MODE_FAN:
                modelInfo = "送风";
                mModeStateIv.setImageResource(R.drawable.ir_ac_mode_fan);
                break;
            case ACConstants.AC_MODE_DRY:
                modelInfo = "除湿";
                mModeStateIv.setImageResource(R.drawable.ir_ac_mode_dry);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_title_close:
                dismiss();
                break;
            case R.id.id_dialog_ac_power_iv:
                //power
                onClickPower();
                break;
            case R.id.id_dialog_ac_temp_down_iv:
                onClickTempDown();
                break;
            case R.id.id_dialog_ac_temp_up_iv:
                onClickTempUp();
                break;
            case R.id.id_dialog_ac_mode_iv:
                onClickMode();
                break;
            case R.id.id_dialog_ac_speed_iv:
                onClickSpeed();
                break;
            case R.id.id_dialog_ac_wind_direction_iv:
                onClickDirection();
                break;
            case R.id.id_dialog_ac_swing_iv:
                onClickSwing();
                break;
        }

        try {
            updatePanel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClickSwing() {
        try {
            mAcManger.changeUDWindDirect(ACStateV2.UDWindDirectKey.UDDIRECT_KEY_SWING);
            sendIR();
        } catch (Exception e) {

        }
    }

    private void onClickDirection() {
        try {
            mAcManger.changeUDWindDirect(ACStateV2.UDWindDirectKey.UDDIRECT_KEY_FIX);
            sendIR();
        } catch (Exception e) {

        }
    }

    private void onClickSpeed() {
        try {
            mAcManger.changeWindSpeed();
            sendIR();
        } catch (Exception e) {

        }
    }

    private void onClickMode() {
        try {
            mAcManger.changeACModel();
            sendIR();
        } catch (Exception e) {

        }
    }

    private void onClickTempUp() {
        try {
            int maxTemp = mAcManger.getMaxTemp();
            int curTemp = mAcManger.getCurTemp();
            curTemp++;
            curTemp = curTemp > maxTemp ? maxTemp : curTemp;
            mAcManger.setTargetTemp(curTemp);
            sendIR();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClickTempDown() {
        try {
            int minTemp = mAcManger.getMinTemp();
            int curTemp = mAcManger.getCurTemp();
            curTemp--;
            curTemp = curTemp < minTemp ? minTemp : curTemp;
            mAcManger.setTargetTemp(curTemp);
            sendIR();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClickPower() {
        try {
            if (mAcManger == null) {
                return;
            }
            mAcManger.changePowerState();
            sendIR();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendIR() {
        EventSendIR eventSendIR = new EventSendIR();
        eventSendIR.setFrequency(mIrBean.getFrequency());
        eventSendIR.setDeviceType(mIrBean.getDeviceType());
        eventSendIR.setRemoteId(mIrBean.getRemoteId());
        eventSendIR.setIrDataArray(mAcManger.getACIRPatternIntArray());
        EventBus.getDefault().post(eventSendIR);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        saveState();
    }

    private void saveState() {
        try {
            if (mAcManger != null) {
                mIrBean.setExts(mIrData.exts);
                mIrBean.setKeys(mIrBean.getKeys());
                mIrBean.setAccState(mAcManger.getACStateV2InString());
                FinalDb.create(getContext()).update(mIrBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
