package com.alcidae.smarthome.ir.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.data.IRDataBean;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.Device;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.BrandList;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;
import com.kookong.app.data.RemoteList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/26 15:10 1.0
 * @time 2018/3/26 15:10
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/26 15:10
 */

public class IRMatchTestActivity extends Activity implements View.OnClickListener {

    protected TextView mTitleTv;
    protected int mDeviceType;
    protected BrandList.Brand mBrand;
    protected ImageView mTestIv;
    protected TextView mTestTv;

    //所有遥控器ID，一个ID对应一套红外码
    protected List<Integer> mRemoteIds;
    protected int mCurrentIdPosition = -1;

    protected List<IrData> mCurrentTestIRDatas;

    public static void launch(Activity activity, int requestCode, int deviceType, BrandList.Brand brand) {
        Class targetClz = IRMatchTestActivity.class;
        if (deviceType == Device.AC) {
            //if is AC, need test by 3 step
            targetClz = IRMatchTestAcActivity.class;
        }

        Intent intent = new Intent(activity, targetClz);
        intent.putExtra("deviceType", deviceType);
        intent.putExtra("brand", brand);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_match_test);
        initViews();
        initData();
        fillInViews();
        loadRemoteIds();
    }

    private void loadRemoteIds() {
        KookongSDK.getAllRemoteIds(mDeviceType, mBrand.brandId, 0, 0, new IRequestResult<RemoteList>() {
            @Override
            public void onSuccess(String s, RemoteList remoteList) {
                LogUtil.i("getAllRemoteIds succ "
                        + Arrays.toString(remoteList.rids.toArray()));
                mRemoteIds = remoteList.rids;
                mCurrentIdPosition = 0;
                loadCurrentRemoteData();
            }

            @Override
            public void onFail(Integer integer, String s) {
                LogUtil.e("getAllRemoteIds " + integer + " ," + s);
            }
        });
    }

    //获取当前remoteId对应的测试码
    private void loadCurrentRemoteData() {
        KookongSDK.testIRDataById(String.valueOf(mRemoteIds.get(mCurrentIdPosition)), mDeviceType, new IRequestResult<IrDataList>() {
            @Override
            public void onSuccess(String s, IrDataList irDataList) {
                LogUtil.i("testIRDataById onSuccess");
                setTitle();
                mCurrentTestIRDatas = irDataList.getIrDataList();
                printTestIR(mCurrentTestIRDatas);
            }

            private void printTestIR(List<IrData> irDatas) {
                for (IrData irData : irDatas) {
                    String keys = "{";
                    if (irData.keys != null) {
                        for (IrData.IrKey key : irData.keys) {
                            keys += " [( fid-->" + key.fid + " ,fname-->" + key.fname + " ,fkey-->" + key.fkey + " )] ";
                        }
                    }
                    keys += "}";

                    String exts = "{";
                    if (irData.exts != null && !irData.exts.isEmpty()) {
                        Set<Map.Entry<Integer, String>> entries = irData.exts.entrySet();
                        for (Map.Entry<Integer, String> entry : entries) {
                            exts += " [entry: key:" + entry.getKey() + ",value:" + entry.getValue() + "] \n";
                        }
                    }
                    exts += "}";

                    LogUtil.d("test ir:  rid-->" + irData.rid + " , type-->" + irData.type
                            + " ,fre-->" + irData.fre + " ,keys-->" + keys + " ,\nexts-->"+exts);
                }
            }

            @Override
            public void onFail(Integer integer, String s) {
                LogUtil.e("testIRDataById onFail " + integer + " ," + s);
            }
        });
    }


    @CallSuper
    protected void fillInViews() {
        setTitle();
    }

    private void setTitle() {
        if (mCurrentIdPosition != -1 && mRemoteIds != null) {
            mTitleTv.setText(mBrand.ename + " " + IRUtils.deviceTypeToString(this, mDeviceType)
                    + " " + (mCurrentIdPosition + 1) + "/" + mRemoteIds.size());
        }
    }

    private void initData() {
        Intent intent = getIntent();
        mDeviceType = intent.getIntExtra("deviceType", -1);
        mBrand = (BrandList.Brand) intent.getSerializableExtra("brand");
    }

    private void initViews() {
        findViewById(R.id.id_ir_back_iv).setOnClickListener(this);
        this.mTitleTv = findViewById(R.id.id_activity_ir_match_test_title_tv);
        this.mTestIv = findViewById(R.id.id_activity_ir_match_test_btn_iv);
        this.mTestTv = findViewById(R.id.id_activity_ir_match_test_btn_text_tv);
        this.mTestIv.setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_no_btn)
                .setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_yes_btn)
                .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ir_back_iv:
                finish();
                break;
            case R.id.id_activity_ir_match_test_no_btn:
                onClickButtonNo();
                break;
            case R.id.id_activity_ir_match_test_yes_btn:
                onClickButtonYes();
                break;
            case R.id.id_activity_ir_match_test_btn_iv:
                onClickMatchButton();
                break;
        }
    }

    protected void onClickMatchButton() {
        if (mCurrentIdPosition >= 0 && mRemoteIds != null) {
        }
    }

    protected void onClickButtonYes() {

    }

    protected void onClickButtonNo() {
        mCurrentIdPosition++;
        setTitle();
    }
}
