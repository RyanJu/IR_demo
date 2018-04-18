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
import com.alcidae.smarthome.ir.data.IRConst;
import com.alcidae.smarthome.ir.ui.dialog.MatchLoadingDialog;
import com.hzy.tvmao.ir.Device;
import com.kookong.app.data.BrandList;
import com.kookong.app.data.SpList;
import com.kookong.app.data.StbList;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/29 11:05 1.0
 * @time 2018/3/29 11:05
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description basic activity of match remote controller,has some common method and fields
 * @updateVersion 1.0
 * @updateTime 2018/3/29 11:05
 */

public abstract class IRMatchBaseActivity extends Activity implements View.OnClickListener {

    protected TextView mTitleTv;
    protected int mDeviceType;
    protected BrandList.Brand mBrand;
    protected ImageView mCenterButtonIv;
    protected TextView mCenterButtonHintTv;
    protected View mVolumeLayout;
    protected View mMatchBtn;
    protected int mAreaid;
    protected SpList.Sp mSp;
    protected StbList.Stb mStb;
    protected TextView mSkipHintTv;

    protected MatchLoadingDialog mLoadingDialog;

    public static void launchCommon(Activity activity, int requestCode, int deviceType, BrandList.Brand brand) {
        launch(activity, requestCode, deviceType, brand, 0, null);
    }

    public static void launchByStb(Activity activity, int requestCode, int deviceType, int areaId, @Nullable SpList.Sp sp) {
        launch(activity, requestCode, deviceType, null, areaId, sp);
    }

    public static void launchByIPTV(Activity activity, int requestCode, int deviceType, int areaId, SpList.Sp sp, StbList.Stb stb) {
        Intent intent = new Intent();
        Class targetClz = IRMatchStbIPTVActivity.class;
        intent.setClass(activity, targetClz);
        intent.putExtra("deviceType", deviceType);
        intent.putExtra("stb", stb);
        intent.putExtra("areaId", areaId);
        intent.putExtra("sp", sp);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * @param activity
     * @param requestCode
     * @param deviceType
     * @param brand       机顶盒填null
     * @param areaId      机顶盒必填，其他无效
     * @param sp          机顶盒必填，其他填null
     */
    private static void launch(Activity activity, int requestCode, int deviceType, @Nullable BrandList.Brand brand, int areaId, @Nullable SpList.Sp sp) {


        Intent intent = new Intent();
        Class targetClz = null;
        switch (deviceType) {
            //if is AC, need test by 3 step
            case Device.AC:
                targetClz = IRMatchAcActivity.class;
                break;
            case Device.STB:
                if (sp.type == IRConst.IPTV) {
                    //iptv is not calling this function
                    return;
                }
                targetClz = IRMatchStbNormalActivity.class;
                break;
            case Device.TV:
                targetClz = IRMatchTVActivity.class;
                break;
            case Device.BOX:
                targetClz = IRMatchBoxActivtiy.class;
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
        mStb = (StbList.Stb) intent.getSerializableExtra("stb");

        this.mSkipHintTv.setText(getString(R.string.ir_match_power_on_hint, IRUtils.deviceTypeToString(this, mDeviceType)));
    }

    private void initViews() {
        findViewById(R.id.id_ir_back_iv).setOnClickListener(this);
        this.mTitleTv = findViewById(R.id.id_activity_ir_match_test_title_tv);
        this.mCenterButtonIv = findViewById(R.id.id_activity_ir_match_test_btn_iv);
        this.mCenterButtonHintTv = findViewById(R.id.id_activity_ir_match_test_btn_text_tv);
        this.mVolumeLayout = findViewById(R.id.id_activity_ir_match_test_volume_layout);
        this.mCenterButtonIv.setOnClickListener(this);
        this.mSkipHintTv = findViewById(R.id.id_activity_ir_match_test_skip_hint_tv);
        findViewById(R.id.id_activity_ir_match_test_no_btn)
                .setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_yes_btn)
                .setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_volume_up_view).setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_volume_down_view).setOnClickListener(this);
        findViewById(R.id.id_activity_ir_match_test_skip_tv).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ir_back_iv:
                finish();
                break;
            case R.id.id_activity_ir_match_test_skip_tv:
                findViewById(R.id.id_activity_ir_match_test_skip_rl).setVisibility(View.GONE);
                onClickButtonYes();
                break;
            case R.id.id_activity_ir_match_test_no_btn:
                onClickButtonNo();
                break;
            case R.id.id_activity_ir_match_test_yes_btn:
                findViewById(R.id.id_activity_ir_match_test_skip_rl).setVisibility(View.GONE);
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
            mCenterButtonIv.setVisibility(View.GONE);
        } else {
            mVolumeLayout.setVisibility(View.GONE);
            mCenterButtonIv.setVisibility(View.VISIBLE);
        }
    }

    protected abstract void onClickVolumeUp();

    protected abstract void onClickVolumeDown();

    protected void fillTitle(String suffix) {
        try {
            String _s = suffix == null ? "" : suffix;
            mTitleTv.setText(IRUtils.getBrandNameByLocale(mBrand) + " " + IRUtils.deviceTypeToString(this, mDeviceType)
                    + " " + _s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void onClickMatchButton();

    protected abstract void onClickButtonYes();

    protected abstract void onClickButtonNo();

    protected void showSwitchRemoteDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new MatchLoadingDialog(this);
        }
        if (!isFinishing())
            mLoadingDialog.show();
    }

    protected void dismissSwitchRemoteDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

}
