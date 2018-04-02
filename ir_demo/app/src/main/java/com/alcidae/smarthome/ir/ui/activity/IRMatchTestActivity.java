package com.alcidae.smarthome.ir.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.data.EventMatchSuccess;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.Device;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.BrandList;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;
import com.kookong.app.data.RemoteList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    protected IrData mTestIRData;

    private ExecutorService mThreadPool;

    public static void launch(Activity activity, int requestCode, int deviceType, BrandList.Brand brand) {
        Class targetClz = IRMatchTestActivity.class;
        if (deviceType == Device.AC) {
            //if is AC, need test by 3 step
            targetClz = IRMatchTestAcActivity.class;
        }

        Intent intent = new Intent(activity, targetClz);
        intent.putExtra("deviceType", deviceType);
        intent.putExtra("stb", brand);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_ir_match_test);
        initViews();
        initData();
        fillInViews();
        loadRemoteIds();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMatchSuccess eventMatchSuccess) {
        finish();
    }

    private void loadRemoteIds() {
        KookongSDK.getAllRemoteIds(mDeviceType, mBrand.brandId, 0, 0, new IRequestResult<RemoteList>() {
            @Override
            public void onSuccess(String s, RemoteList remoteList) {
                LogUtil.i("getAllRemoteIds succ "
                        + Arrays.toString(remoteList.rids.toArray()));
                mRemoteIds = remoteList.rids;
                mCurrentIdPosition = 0;
                loadCurrentRemoteData(mRemoteIds.get(mCurrentIdPosition), mDeviceType);
            }

            @Override
            public void onFail(Integer integer, String s) {
                LogUtil.e("getAllRemoteIds " + integer + " ," + s);
            }
        });
    }

    //获取当前remoteId对应的测试码
    protected void loadCurrentRemoteData(int remoteId, int mDeviceType) {
        KookongSDK.testIRDataById(String.valueOf(remoteId), mDeviceType, new IRequestResult<IrDataList>() {
            @Override
            public void onSuccess(String s, IrDataList irDataList) {
                LogUtil.i("testIRDataById onSuccess");
                setTitle();
                mTestIRData = irDataList.getIrDataList().get(0);


                printTestIR(mTestIRData);

            }

            private void printTestIR(IrData irData) {
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
                        + " ,fre-->" + irData.fre + " ,keys-->" + keys + " ,\nexts-->" + exts);
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

    protected void setTitle() {
        if (mCurrentIdPosition != -1 && mRemoteIds != null) {
            mTitleTv.setText(mBrand.ename + " " + IRUtils.deviceTypeToString(this, mDeviceType)
                    + " " + (mCurrentIdPosition + 1) + "/" + mRemoteIds.size());
        }
    }

    private void initData() {
        Intent intent = getIntent();
        mDeviceType = intent.getIntExtra("deviceType", -1);
        mBrand = (BrandList.Brand) intent.getSerializableExtra("stb");
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
                vibrate(50);
                onClickMatchButton();
                break;
        }
    }


    protected void onClickMatchButton() {

        if (mTestIRData != null) {
            //send ir data
        }
    }

    protected void onClickButtonYes() {
        //save this remote id and ir data
    }

    protected void onClickButtonNo() {
        if (mRemoteIds != null && mCurrentIdPosition < mRemoteIds.size()) {
            mCurrentIdPosition++;
            LogUtil.i("change next remote data " + mCurrentIdPosition);
            loadCurrentRemoteData(mRemoteIds.get(mCurrentIdPosition), mDeviceType);
        }
    }


    /**
     * 震动
     *
     * @param time
     */
    private void vibrate(long time) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    protected synchronized void runOnThread(Runnable runnable) {
        if (mThreadPool == null) {
            mThreadPool = Executors.newCachedThreadPool();
        }
        mThreadPool.execute(runnable);
    }
}
