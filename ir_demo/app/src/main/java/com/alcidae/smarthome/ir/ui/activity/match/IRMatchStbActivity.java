package com.alcidae.smarthome.ir.ui.activity.match;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.util.ToastUtil;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.BrandList;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;
import com.kookong.app.data.RemoteList;
import com.kookong.app.data.SpList;
import com.kookong.app.data.StbList;

import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/29 17:19 1.0
 * @time 2018/3/29 17:19
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity.match
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/29 17:19
 */

public class IRMatchStbActivity extends IRMatchBaseActivity {

    private static final int STEP_ONE_POWER = 0;
    private static final int STEP_TWO_TEMP_UP = 1;
    private static final int STEP_THREE_TEMP_DOWN = 2;

    private int mStep;

    private List<Integer> mRemoteIds;
    private int mCurrentIdIndex;
    private IrData mIrData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadIds();
    }


    private void loadIds() {
        if (mSp == null) {
            LogUtil.e("no sp found! error");
            return;
        }


        //type =0 normal stb, type = 1 IPTV
        if (!isStbTypeIPTV()) {
            KookongSDK.getAllRemoteIds(mDeviceType, 0, mSp.spId, mAreaid, new IRequestResult<RemoteList>() {
                @Override
                public void onSuccess(String s, RemoteList remoteList) {
                    LogUtil.i("getAllRemoteIds onSuccess " + s);
                    if (remoteList != null && remoteList.rids != null) {
                        mRemoteIds = remoteList.rids;
                        mCurrentIdIndex = 0;
                        loadIrDataByRemoteId();
                    }
                }

                @Override
                public void onFail(Integer integer, String s) {

                }
            });
        }else{
            KookongSDK.getIPTV(mSp.spId, new IRequestResult<StbList>() {
                @Override
                public void onSuccess(String s, StbList stbList) {
                    LogUtil.i("getIPTV onSuccess "+s +stbList.stbList.size()+"");
                }

                @Override
                public void onFail(Integer integer, String s) {

                }
            });
        }

    }

    private void loadIrDataByRemoteId() {
        if (mCurrentIdIndex >= 0 && mRemoteIds != null && mCurrentIdIndex < mRemoteIds.size()) {
            KookongSDK.getIRDataById(String.valueOf(mRemoteIds.get(mCurrentIdIndex)), new IRequestResult<IrDataList>() {
                @Override
                public void onSuccess(String s, IrDataList irDataList) {
                    if (irDataList != null && irDataList.getIrDataList() != null && !irDataList.getIrDataList().isEmpty()) {
                        mIrData = irDataList.getIrDataList().get(0);
                        onGetIrData();
                    }
                }

                @Override
                public void onFail(Integer integer, String s) {
                    ToastUtil.toast(IRMatchStbActivity.this, R.string.ir_error_network);
                }
            });
        }
    }

    private void onGetIrData() {

    }

    private boolean isStbTypeIPTV() {
        return mSp != null && mSp.type == 1;
    }

}
