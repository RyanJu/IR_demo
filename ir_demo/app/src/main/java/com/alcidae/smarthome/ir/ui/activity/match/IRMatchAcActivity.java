package com.alcidae.smarthome.ir.ui.activity.match;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.data.EventMatchSuccess;
import com.alcidae.smarthome.ir.data.EventSendIR;
import com.alcidae.smarthome.ir.ui.dialog.InputNameDialog;
import com.alcidae.smarthome.ir.ui.dialog.UnsuccessDialog;
import com.alcidae.smarthome.ir.util.SimpleIRequestResult;
import com.hzy.tvmao.KKACManagerV2;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.ir.Device;
import com.hzy.tvmao.ir.ac.ACConstants;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;
import com.kookong.app.data.RemoteList;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/29 11:09 1.0
 * @time 2018/3/29 11:09
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/29 11:09
 */

public class IRMatchAcActivity extends IRMatchBaseActivity {

    private static final int STEP_ONE_POWER = 0;
    private static final int STEP_TWO_MODE = 1;
    private static final int STEP_THREE_TEMP_UP = 2;
    private static final int STEP_FOUR_TEMP_DOWN = 3;


    private int mCurRemoteId;
    private List<Integer> mRemoteIds;
    private int mCurrentIdIndex;
    private IrData mCurrentIRData;
    private int mStep = STEP_ONE_POWER;
    private KKACManagerV2 mAcManager;
    private boolean mErrorDialogShowedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadID();
    }

    @Override
    protected void onClickVolumeUp() {
        //not use
    }

    @Override
    protected void onClickVolumeDown() {
        //not use
    }

    private void loadID() {
        KookongSDK.getAllRemoteIds(mDeviceType, mBrand.brandId, 0, 0, new SimpleIRequestResult<RemoteList>(this) {
            @Override
            public void onSuccess(String s, RemoteList remoteList) {
                if (remoteList != null && remoteList.rids != null) {
                    mRemoteIds = remoteList.rids;
                    mCurrentIdIndex = 0;
                    mCurRemoteId = mRemoteIds.get(mCurrentIdIndex);

                    loadIRData();


                }
            }
        });
    }

    private void loadIRData() {
        KookongSDK.testIRDataById(String.valueOf(mCurRemoteId), mDeviceType, new SimpleIRequestResult<IrDataList>(this) {
            @Override
            public void onSuccess(String s, IrDataList irDataList) {
                dismissSwitchRemoteDialog();
                if (irDataList != null && irDataList.getIrDataList() != null && !irDataList.getIrDataList().isEmpty()) {
                    List<IrData> list = irDataList.getIrDataList();
                    mCurrentIRData = list.get(0);

                    //根据空外数据初始化空调解码器
                    mAcManager = new KKACManagerV2();
                    mAcManager.initIRData(mCurrentIRData.rid, mCurrentIRData.exts, mCurrentIRData.keys);
                    mAcManager.setACStateV2FromString("");

                    mStep = STEP_ONE_POWER;

                    updateTitle();
                    updateStep();

                }
            }
        });
    }

    private void updateTitle() {
        fillTitle((mCurrentIdIndex + 1) + "/" + mRemoteIds.size());
    }

    private void updateStep() {
        if (mStep == STEP_ONE_POWER) {
            mCenterButtonIv.setImageResource(R.drawable.selector_power_switch);
            mCenterButtonHintTv.setText(R.string.ir_power);
        } else if (mStep == STEP_TWO_MODE) {
            mCenterButtonIv.setImageResource(R.drawable.selector_mode);
            mCenterButtonHintTv.setText(R.string.ir_mode);
        } else if (mStep == STEP_THREE_TEMP_UP) {
            mCenterButtonIv.setImageResource(R.drawable.selector_ac_temp_up);
            mCenterButtonHintTv.setText(R.string.ir_temp);
        } else if (mStep == STEP_FOUR_TEMP_DOWN) {
            mCenterButtonIv.setImageResource(R.drawable.selector_ac_temp_down);
            mCenterButtonHintTv.setText(R.string.ir_temp);
        }
    }

    @Override
    protected void onClickMatchButton() {
        if (mAcManager != null && mCurrentIRData != null) {
            EventSendIR event = new EventSendIR();
            event.setDeviceType(Device.AC);
            event.setRemoteId(mCurRemoteId);
            event.setIrDataArray(getAcIrByStep());
            event.setFrequency(mCurrentIRData.fre);
            EventBus.getDefault().post(event);
        }
    }


    private int[] getAcIrByStep() {
        if (mAcManager == null || mAcManager.stateIsEmpty()) {
            Toast.makeText(IRMatchAcActivity.this, "error state", Toast.LENGTH_SHORT).show();
            return null;
        }
        switch (mStep) {
            case STEP_ONE_POWER:
                mAcManager.changePowerState();
                break;
            case STEP_TWO_MODE:
                if (mAcManager.getPowerState() == ACConstants.AC_POWER_OFF) {
                    mAcManager.changePowerState();
                }
                mAcManager.changeACModel();
                break;
            case STEP_THREE_TEMP_UP:
                if (mAcManager.getPowerState() == ACConstants.AC_POWER_OFF) {
                    mAcManager.changePowerState();
                }
                mAcManager.increaseTmp();
                break;
            case STEP_FOUR_TEMP_DOWN:
                if (mAcManager.getPowerState() == ACConstants.AC_POWER_OFF) {
                    mAcManager.changePowerState();
                }
                mAcManager.decreaseTmp();
                break;
        }
        return mAcManager.getACIRPatternIntArray();
    }

    @Override
    protected void onClickButtonYes() {
        if (mStep == STEP_FOUR_TEMP_DOWN) {
            //match success ,save it for user by custom name
            showNameWindow();
            return;
        }
        if (mStep < STEP_FOUR_TEMP_DOWN) {
            mStep++;
        }
        updateStep();
    }

    private void showNameWindow() {
        InputNameDialog dialog = new InputNameDialog(this);
        dialog.show();
        dialog.setClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.dismiss();
                } else if (which == DialogInterface.BUTTON_POSITIVE) {
                    InputNameDialog myDialog = (InputNameDialog) dialog;
                    dialog.dismiss();
                    EventBus.getDefault().post(
                            new EventMatchSuccess(IRUtils.saveMatchedACRemoteBean(mCurrentIRData.fre, mBrand, mDeviceType, mRemoteIds.get(mCurrentIdIndex),
                                    mAcManager.getACStateV2InString(), myDialog.getInput(), mCurrentIRData.exts, mCurrentIRData.keys)));
                    IRMatchAcActivity.this.finish();
                }
            }
        });
    }


    @Override
    protected void onClickButtonNo() {
        showErrorDialog();
    }

    private void showErrorDialog() {
        if (isTheLastOne()) {
            UnsuccessDialog dialog = new UnsuccessDialog(this);
            dialog.show();
            dialog.setClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    findNextRemoteId();

                }
            });
        } else {
            showSwitchRemoteDialog();
            findNextRemoteId();
        }
    }

    private boolean isTheLastOne() {
        return mRemoteIds != null && mRemoteIds.size() - 1 == mCurrentIdIndex;
    }

    private void findNextRemoteId() {
        if (mRemoteIds != null && mCurrentIdIndex < mRemoteIds.size() - 1) {
            mCurrentIdIndex++;
            LogUtil.i("change next remote data " + mCurrentIdIndex);

            mCurRemoteId = mRemoteIds.get(mCurrentIdIndex);

            loadIRData();
        }
    }


    /**
     * Activity生命周期处理
     */
    @Override
    protected void onPause() {
        if (mAcManager != null) {
            mAcManager.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAcManager != null) {
            mAcManager.onResume();
        }
        super.onResume();
    }
}
