package com.alcidae.smarthome.ir.ui.activity;

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
import com.hzy.tvmao.KKACManagerV2;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.Device;
import com.hzy.tvmao.ir.ac.ACConstants;
import com.hzy.tvmao.ir.ac.ACStateV2;
import com.hzy.tvmao.utils.DataStoreUtil;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.IrDataList;

import org.greenrobot.eventbus.EventBus;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/26 16:33 1.0
 * @time 2018/3/26 16:33
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/26 16:33
 */

public class IRMatchTestAcActivity extends IRMatchTestActivity {
    private static final int STEP_POWER = 0;
    private static final int STEP_MODE = 1;
    private static final int STEP_WIND_DIRECTION = 2;


    private int mStep = STEP_POWER;

    private KKACManagerV2 mAcManager;
    private boolean mErrorDialogShowedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAcManager = new KKACManagerV2();
    }


    @Override
    protected void fillInViews() {
        super.fillInViews();
        showStep();
    }

    private void showStep() {
        if (mStep == STEP_POWER) {
            mTestIv.setImageResource(R.drawable.selector_power_switch);
            mTestTv.setText(R.string.power);
        } else if (mStep == STEP_MODE) {
            mTestIv.setImageResource(R.drawable.selector_mode);
            mTestTv.setText(R.string.ir_mode);
        } else if (mStep == STEP_WIND_DIRECTION) {
            mTestIv.setImageResource(R.drawable.selector_wind_direction);
            mTestTv.setText(R.string.wind_direction);
        }
    }

    //根据remote id获取remote数据
    @Override
    protected void loadCurrentRemoteData(final int remoteId, final int deviceType) {

        KookongSDK.testIRDataById(String.valueOf(remoteId), Device.AC, new IRequestResult<IrDataList>() {
            @Override
            public void onSuccess(String s, IrDataList irDataList) {
                if (irDataList != null && irDataList.getIrDataList() != null) {
                    mTestIRData = irDataList.getIrDataList().get(0);

                    //根据空外数据初始化空调解码器
                    mAcManager.initIRData(mTestIRData.rid, mTestIRData.exts, null);
                    mAcManager.setACStateV2FromString("");

                    setTitle();
                    showStep();
                }
            }

            @Override
            public void onFail(Integer integer, String s) {
                LogUtil.e("getIRDataById failed " + integer + " ," + s);
            }
        });
    }

    @Override
    protected void onClickMatchButton() {
        if (mAcManager != null && mTestIRData != null) {
            runOnThread(new Runnable() {
                @Override
                public void run() {
                    int[] irArray = getAcIrByStep();
                    EventSendIR event = new EventSendIR();
                    event.setDeviceType(Device.AC);
                    event.setRemoteId(mRemoteIds.get(mCurrentIdPosition));
                    event.setIrDataArray(irArray);
                    event.setFrequency(mTestIRData.fre);
                    EventBus.getDefault().post(event);
                }
            });
        }
    }


    private int[] getAcIrByStep() {
        if (mAcManager == null || mAcManager.stateIsEmpty()) {
            Toast.makeText(IRMatchTestAcActivity.this, "error state", Toast.LENGTH_SHORT).show();
            return null;
        }
        switch (mStep) {
            case STEP_POWER:
                mAcManager.changePowerState();
                break;
            case STEP_MODE:
                if (mAcManager.getPowerState() == ACConstants.AC_POWER_OFF) {
                    mAcManager.changePowerState();
                }
                mAcManager.changeACModel();
                break;
            case STEP_WIND_DIRECTION:
                if (mAcManager.getCurUDDirectType() == ACStateV2.UDWindDirectType.UDDIRECT_ONLY_SWING) {//没有风向，风向不能使用
                    Toast.makeText(IRMatchTestAcActivity.this, "没有风向可调节", Toast.LENGTH_SHORT).show();
                    return null;
                }

                if (mAcManager.getCurUDDirectType() == ACStateV2.UDWindDirectType.UDDIRECT_ONLY_FIX) {//只有固定风向可用，扫风不能使用
                    Toast.makeText(IRMatchTestAcActivity.this, "扫风键不可用", Toast.LENGTH_SHORT).show();
                    return null;
                }
                mAcManager.changeUDWindDirect(ACStateV2.UDWindDirectKey.UDDIRECT_KEY_SWING);//切换风向，对于同时具备扫风和固定的空调：如果当前是扫风，再摁扫风时，切换到了固定风向；如果当前是固定风向，再摁扫风时，切换到扫风
                break;
        }
        return mAcManager.getACIRPatternIntArray();
    }

    @Override
    protected void onClickButtonYes() {
        if (mStep == STEP_WIND_DIRECTION) {
            //match success ,save it for user by custom name
            showNameWindow();
            return;
        }
        if (mStep < STEP_WIND_DIRECTION) {
            mStep++;
        }
        showStep();
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
                            new EventMatchSuccess(IRUtils.saveMatchedRemoteBean(mTestIRData.fre, mBrand, mDeviceType, mRemoteIds.get(mCurrentIdPosition),
                                    mAcManager.getACStateV2InString(), myDialog.getInput(),mTestIRData.exts,mTestIRData.keys)));
                }
            }
        });
    }


    @Override
    protected void onClickButtonNo() {
        showErrorDialog();
    }

    private void showErrorDialog() {
        if (!mErrorDialogShowedOnce) {
            mErrorDialogShowedOnce = true;
            UnsuccessDialog dialog = new UnsuccessDialog(this);
            dialog.show();
            dialog.setClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    toNextRemote();

                }
            });
        } else {
            toNextRemote();
        }
    }

    private void toNextRemote() {
        if (mRemoteIds != null && mCurrentIdPosition < mRemoteIds.size() && mCurrentIdPosition < mRemoteIds.size() - 1) {
            mCurrentIdPosition++;
            LogUtil.i("change next remote data " + mCurrentIdPosition);
            loadCurrentRemoteData(mRemoteIds.get(mCurrentIdPosition), mDeviceType);
            mStep = STEP_POWER;
            showStep();
        }
    }


    /**
     * Activity生命周期处理
     */
    @Override
    protected void onPause() {
        mAcManager.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAcManager.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        //使用 getACStateV2InString  保存当前空调的状态，测试时注释掉了
//        String acState = mAcManager.getACStateV2InString();
//        DataStoreUtil.i().putString("AC_STATE", acState);
        super.onStop();
    }

}
