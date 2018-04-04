package com.alcidae.smarthome.ir.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.data.AreaBean;
import com.alcidae.smarthome.ir.util.DisplayUtil;
import com.alcidae.smarthome.ir.util.LocationUtil;
import com.alcidae.smarthome.ir.widget.BaseFloatDialog;

import java.io.IOException;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/4 17:55 1.0
 * @time 2018/4/4 17:55
 * @project ir_demo com.alcidae.smarthome.ir.ui.dialog
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/4 17:55
 */

public class ChooseAreaDialog extends BaseFloatDialog implements View.OnClickListener {
    private NumberPickerView mProvinceNpv;
    private NumberPickerView mCityNpv;

    private int mProvinceIndex;
    private int mCityIndex;
    private AreaBean mAreaBean;

    public ChooseAreaDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_choose_area);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        initView();
        initData();
    }

    private void initData() {
        try {
            mAreaBean = LocationUtil.readAreas(getContext());
            if (mAreaBean != null) {
                updateProvince();
                updateCity();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCity() {
        AreaBean.Province province = mAreaBean.getProvinces().get(mProvinceIndex);
        String[] citiesArray = new String[province.getCitys().size()];
        final List<AreaBean.City> cities = province.getCitys();
        final int size = cities.size();
        for (int i = 0; i < size; i++) {
            citiesArray[i] = cities.get(i).getCitysName();
        }
        mCityNpv.refreshByNewDisplayedValues(citiesArray);
        mCityNpv.setMinValue(0);
        mCityNpv.setMaxValue(size - 1);
        mCityNpv.setValue(mCityIndex);
    }

    private void updateProvince() {
        String[] provincesArray = new String[mAreaBean.getProvinces().size()];
        final List<AreaBean.Province> provinces = mAreaBean.getProvinces();
        final int size = provinces.size();
        for (int i = 0; i < size; i++) {
            provincesArray[i] = provinces.get(i).getProvinceName();
        }
        mProvinceNpv.refreshByNewDisplayedValues(provincesArray);
        mProvinceNpv.setMinValue(0);
        mProvinceNpv.setMaxValue(size - 1);
        mProvinceNpv.setValue(mProvinceIndex);
    }

    private void initView() {
        findViewById(R.id.id_dialog_choose_area_close_iv).setOnClickListener(this);
        findViewById(R.id.id_dialog_choose_area_ensure_iv).setOnClickListener(this);
        mProvinceNpv = findViewById(R.id.id_dialog_choose_area_province_npv);
        mCityNpv = findViewById(R.id.id_dialog_choose_area_city_npv);
        mProvinceNpv.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
                
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_choose_area_close_iv:
                dismiss();
                break;
            case R.id.id_dialog_choose_area_ensure_iv:

                break;
        }
    }
}
