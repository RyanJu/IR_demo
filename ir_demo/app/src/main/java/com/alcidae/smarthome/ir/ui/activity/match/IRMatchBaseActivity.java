package com.alcidae.smarthome.ir.ui.activity.match;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.IRUtils;
import com.hzy.tvmao.ir.Device;
import com.kookong.app.data.BrandList;
import com.kookong.app.data.SpList;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/29 11:05 1.0
 * @time 2018/3/29 11:05
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/29 11:05
 */

public abstract class IRMatchBaseActivity extends Activity implements View.OnClickListener {

    protected TextView mTitleTv;
    protected int mDeviceType;
    protected BrandList.Brand mBrand;
    protected ImageView mTestIv;
    protected TextView mTestTv;
    protected View mVolumeLayout;
    protected View mMatchBtn;
    protected int mAreaid;
    protected SpList.Sp mSp;

    public static void launchCommon(Activity activity, int requestCode, int deviceType, BrandList.Brand brand) {
        launch(activity, requestCode, deviceType, brand, 0, null);
    }

    public static void launchByStb(Activity activity, int requestCode, int deviceType, int areaId, @Nullable SpList.Sp sp) {
        launch(activity, requestCode, deviceType, null, areaId, sp);
    }

//    public static void launchByIPTV(Activity activity,int requestCode,int deice)

    /**
     * @param activity
     * @param requestCode
     * @param deviceType
     * @param brand       机顶盒填null
     * @param areaId      机顶盒必填，其他无效
     * @param sp          机顶盒必填，其他填null
     */
    public static void launch(Activity activity, int requestCode, int deviceType, @Nullable BrandList.Brand brand, int areaId, @Nullable SpList.Sp sp) {

        Intent intent = new Intent();
        Class targetClz = null;
        switch (deviceType) {
            //if is AC, need test by 3 step
            case Device.AC:
                targetClz = IRMatchAcActivity.class;
                break;
            case Device.STB:
                targetClz = IRMatchStbActivity.class;
                break;
        }


        if (targetClz != null) {
            intent.setClass(activity, targetClz);
            intent.putExtra("deviceType", deviceType);
            intent.putExtra("brand", brand);
            intent.putExtra("areaId", areaId);
            intent.putExtra("sp", sp);
            activity.startActivityForResult(intent, requestCode);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_match_test);
        initViews();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mDeviceType = intent.getIntExtra("deviceType", -1);
        mBrand = (BrandList.Brand) intent.getSerializableExtra("brand");
        mAreaid = intent.getIntExtra("areaId", 0);
        mSp = (SpList.Sp) intent.getSerializableExtra("sp");
    }

    private void initViews() {
        findViewById(R.id.id_ir_back_iv).setOnClickListener(this);
        this.mTitleTv = findViewById(R.id.id_activity_ir_match_test_title_tv);
        this.mTestIv = findViewById(R.id.id_activity_ir_match_test_btn_iv);
        this.mTestTv = findViewById(R.id.id_activity_ir_match_test_btn_text_tv);
        this.mVolumeLayout = findViewById(R.id.id_activity_ir_match_test_volume_layout);
        this.mTestIv.setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_no_btn)
                .setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_yes_btn)
                .setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_volume_up_view).setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_volume_down_view).setOnClickListener(this);
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
            case R.id.id_activity_ir_match_test_volume_up_view:
                onClickVolumeUp();
                break;
            case R.id.id_activity_ir_match_test_volume_down_view:
                onClickVolumeDown();
                break;
        }
    }

    protected void showMatchVolume(boolean show) {
        if (show) {
            mVolumeLayout.setVisibility(View.VISIBLE);
            mTestIv.setVisibility(View.GONE);
        } else {
            mVolumeLayout.setVisibility(View.GONE);
            mTestIv.setVisibility(View.VISIBLE);
        }
    }

    protected void onClickVolumeUp() {

    }

    protected void onClickVolumeDown() {

    }

    protected void setTitle(String suffix) {
        try {
            String _s = suffix == null ? "" : suffix;
            mTitleTv.setText(mBrand.ename + " " + IRUtils.deviceTypeToString(this, mDeviceType)
                    + " " + _s);
        } catch (Exception e) {

        }
    }

    protected void onClickMatchButton() {

    }

    protected void onClickButtonYes() {

    }

    protected void onClickButtonNo() {

    }
}
