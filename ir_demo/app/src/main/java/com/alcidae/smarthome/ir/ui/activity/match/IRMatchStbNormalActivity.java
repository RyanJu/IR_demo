package com.alcidae.smarthome.ir.ui.activity.match;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.util.SimpeIRequestResult;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.kookong.app.data.RemoteList;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/3 14:00 1.0
 * @time 2018/4/3 14:00
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity.match
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/3 14:00
 */

public class IRMatchStbNormalActivity extends IRMatchStbIPTVActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void loadIds() {
        if (mSp != null) {
            KookongSDK.getAllRemoteIds(mDeviceType, 0, mSp.spId, mAreaid, new SimpeIRequestResult<RemoteList>(this) {
                @Override
                public void onSuccess(String s, RemoteList remoteList) {
                    if (remoteList != null && remoteList.rids != null && !remoteList.rids.isEmpty()) {
                        mRemoteIds = remoteList.rids;
                        mCurrentIdPosition = 0;
                        loadTestData();
                    }
                }
            });
        }
    }

    @Override
    protected void updateTitle() {
        mTitleTv.setText(mSp.spName + " " + IRUtils.deviceTypeToString(this, mDeviceType) + " " + String.valueOf(mCurrentIdPosition + 1) + "/" + mRemoteIds.size());
    }

}
