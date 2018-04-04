package com.alcidae.smarthome.ir.ui.activity.match;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

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
import com.kookong.app.data.RemoteList;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/3 14:25 1.0
 * @time 2018/4/3 14:25
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity.match
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/3 14:25
 */

public abstract class IRMatchCommonActivity extends IRMatchBaseActivity {

    private int mCurrentIdPosition;
    private List<Integer> mRemoteIds;
    protected IrData mIrData;
    protected int mStep;
    private boolean mErrorDialogShowedOnce;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadIds();
    }

    private void loadIds() {
        if (mBrand != null) {
            KookongSDK.getAllRemoteIds(mDeviceType, mBrand.brandId, 0, 0, new IRequestResult<RemoteList>() {
                @Override
                public void onSuccess(String s, RemoteList remoteList) {
                    if (remoteList != null && remoteList.rids != null && !remoteList.rids.isEmpty()) {
                        mCurrentIdPosition = 0;
                        mRemoteIds = remoteList.rids;
                        loadIRData();
                    }
                }

                @Override
                public void onFail(Integer integer, String s) {
                    ToastUtil.toast(IRMatchCommonActivity.this, R.string.ir_error_network);
                }
            });
        }
    }

    private void loadIRData() {
        if (mCurrentIdPosition < mRemoteIds.size()) {
            KookongSDK.testIRDataById(String.valueOf(mRemoteIds.get(mCurrentIdPosition)), mDeviceType, new IRequestResult<IrDataList>() {

                @Override
                public void onSuccess(String s, IrDataList irDataList) {
                    LogUtil.i("testIRDataById succ");
                    if (irDataList != null && irDataList.getIrDataList() != null && !irDataList.getIrDataList().isEmpty()) {
                        mIrData = irDataList.getIrDataList().get(0);
                        mStep = 0;
                        showStep();
                        updateTitle();

                    }
                }

                @Override
                public void onFail(Integer integer, String s) {

                }
            });
        }
    }

    private void updateTitle() {
        fillTitle(String.valueOf(mCurrentIdPosition + 1) + "/" + mRemoteIds.size());
    }

    protected abstract void showStep();

    protected abstract int stepMax();

    protected abstract int[] getIrByStep();

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
            event.setIrDataArray(getIrByStep());
            event.setFrequency(mIrData.fre);
            EventBus.getDefault().post(event);
        }
    }



    @Override
    protected void onClickButtonYes() {
        if (mStep < stepMax()) {
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
                    IRMatchCommonActivity.this.finish();
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
            loadIRData();
        }
    }
}
