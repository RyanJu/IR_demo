package com.alcidae.smarthome.ir.ui.activity.match;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.data.EventMatchSuccess;
import com.alcidae.smarthome.ir.data.EventSendIR;
import com.alcidae.smarthome.ir.data.IRConst;
import com.alcidae.smarthome.ir.ui.dialog.InputNameDialog;
import com.alcidae.smarthome.ir.ui.dialog.UnsuccessDialog;
import com.alcidae.smarthome.ir.util.ToastUtil;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;
import com.kookong.app.data.StbList;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/3 9:29 1.0
 * @time 2018/4/3 9:29
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity.match
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/3 9:29
 */

public class IRMatchStbIPTVActivity extends IRMatchBaseActivity {

    private static final int STEP_ONE_POWER = 0;
    private static final int STEP_TWO_SOUND = 1;

    protected List<Integer> mRemoteIds;
    protected int mCurrentIdPosition;
    protected int mStep;
    protected IrData mIrData;
    protected boolean mErrorDialogShowedOnce;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadIds();
    }

    protected void loadIds() {
        if (mStb != null) {
            List<StbList.Remote> remotes = mStb.remotes;
            if (remotes != null) {
                mRemoteIds = new ArrayList<>(remotes.size());
                for (StbList.Remote remote : remotes) {
                    mRemoteIds.add(remote.rid);
                }
                mCurrentIdPosition = 0;
                loadTestData();
            }
        }
    }

    protected void loadTestData() {
        if (mRemoteIds != null && !mRemoteIds.isEmpty() && mCurrentIdPosition < mRemoteIds.size()) {
            int rid = mRemoteIds.get(mCurrentIdPosition);
            KookongSDK.testIRDataById(String.valueOf(rid), mDeviceType, new IRequestResult<IrDataList>() {
                @Override
                public void onSuccess(String s, IrDataList irDataList) {
                    LogUtil.i("testIRDataById succ " + irDataList.getIrDataList().size());
                    if (irDataList != null && irDataList.getIrDataList() != null && !irDataList.getIrDataList().isEmpty()) {
                        mIrData = irDataList.getIrDataList().get(0);
                        mStep = STEP_ONE_POWER;
                        updateTitle();
                        showStep();
                    }
                }

                @Override
                public void onFail(Integer integer, String s) {
                    ToastUtil.toast(IRMatchStbIPTVActivity.this, R.string.ir_error_network);
                }
            });
        }
    }

    private void showStep() {
        if (mStep == STEP_ONE_POWER) {
            mCenterButtonIv.setVisibility(View.VISIBLE);
            mVolumeLayout.setVisibility(View.GONE);
            mCenterButtonIv.setImageResource(R.drawable.selector_power_switch);
            mCenterButtonHintTv.setText(R.string.ir_power);
        } else if (mStep == STEP_TWO_SOUND) {
            mCenterButtonIv.setVisibility(View.GONE);
            mVolumeLayout.setVisibility(View.VISIBLE);
            mCenterButtonHintTv.setText(R.string.ir_volume);
        }
    }

    protected void updateTitle() {
        mTitleTv.setText(mStb.bname + " " + IRUtils.deviceTypeToString(this, mDeviceType) + " " + String.valueOf(mCurrentIdPosition + 1) + "/" + mRemoteIds.size());
    }

    @Override
    protected void onClickVolumeUp() {
        if (mIrData != null) {
            EventSendIR event = new EventSendIR();
            event.setDeviceType(mDeviceType);
            event.setRemoteId(mRemoteIds.get(mCurrentIdPosition));
            event.setIrDataArray(IRUtils.searchKeyCodeIR(mIrData, IRConst.KEY.volume_up));
            event.setFrequency(mIrData.fre);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    protected void onClickVolumeDown() {
        if (mIrData != null) {
            EventSendIR event = new EventSendIR();
            event.setDeviceType(mDeviceType);
            event.setRemoteId(mRemoteIds.get(mCurrentIdPosition));
            event.setIrDataArray(IRUtils.searchKeyCodeIR(mIrData, IRConst.KEY.volume_down));
            event.setFrequency(mIrData.fre);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    protected void onClickMatchButton() {
        if (mIrData != null) {
            EventSendIR event = new EventSendIR();
            event.setDeviceType(mDeviceType);
            event.setRemoteId(mRemoteIds.get(mCurrentIdPosition));
            event.setIrDataArray(getAcIrByStep());
            event.setFrequency(mIrData.fre);
            EventBus.getDefault().post(event);
        }
    }

    private int[] getAcIrByStep() {
        switch (mStep) {
            case STEP_ONE_POWER:
                return IRUtils.searchKeyCodeIR(mIrData, IRConst.KEY.power);
        }
        return null;
    }

    @Override
    protected void onClickButtonYes() {
        if (mStep < STEP_TWO_SOUND) {
            mStep++;
            showStep();
        } else {
            //success
            showNameWindow();
        }
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
                            new EventMatchSuccess(IRUtils.saveMatchedStbRemoteBean(mIrData.fre, mSp, mStb, mDeviceType, mRemoteIds.get(mCurrentIdPosition), myDialog.getInput(), mIrData.exts, mIrData.keys)));
                    IRMatchStbIPTVActivity.this.finish();
                }
            }
        });
    }

    @Override
    protected void onClickButtonNo() {
        if (mCurrentIdPosition < mRemoteIds.size()) {
            showErrorDialogOrGoNext();
        }
    }

    private void showErrorDialogOrGoNext() {
        if (!mErrorDialogShowedOnce) {
            mErrorDialogShowedOnce = true;
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
            findNextRemoteId();
        }
    }

    private void findNextRemoteId() {
        if (mRemoteIds != null && mCurrentIdPosition < mRemoteIds.size() - 1) {
            mCurrentIdPosition++;
            LogUtil.i("change next remote data " + mCurrentIdPosition);
            loadTestData();
        }
    }
}
