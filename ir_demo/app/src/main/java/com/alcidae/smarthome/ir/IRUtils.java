package com.alcidae.smarthome.ir;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.data.db.DbUtil;
import com.alcidae.smarthome.ir.data.db.IRBean;
import com.alcidae.smarthome.ir.ui.dialog.RemoteACDialog;
import com.alcidae.smarthome.ir.ui.dialog.RemoteBoxDialog;
import com.alcidae.smarthome.ir.ui.dialog.RemoteSTBDialog;
import com.alcidae.smarthome.ir.ui.dialog.RemoteTVDialog;
import com.alcidae.smarthome.ir.util.LocationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.Device;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.BrandList;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;
import com.kookong.app.data.SpList;
import com.kookong.app.data.StbList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/26 14:04 1.0
 * @time 2018/3/26 14:04
 * @project ir_demo com.alcidae.smarthome.ir
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/26 14:04
 */

public class IRUtils {

    private static final String key = "36C9647F80C96B0214EF3F912B6250A2";
    private static final String irDeviceId = "1";
    private static final String IR_SP_NAME = "IR_SP";
    private static Context sContext;

    private static String sProvince = "";
    private static String sCity = "";
    private static String sArea = "";

    public static void init(Context context) {
        sContext = context.getApplicationContext();
        boolean result = KookongSDK.init(context.getApplicationContext(), key, irDeviceId);
        LogUtil.d("Verify result is " + result);
        KookongSDK.setDebugMode(true);
    }

    /**
     * 需由对接实现，填入当前位置，供机顶盒匹配使用
     *
     * @param province
     * @param city
     * @param area
     */
    public static void setArea(String province, String city, String area) {
        sProvince = province;
        sCity = city;
        sArea = area;
    }

    public static String getProvince() {
        return sProvince;
    }

    public static String getCity() {
        return sCity;
    }

    public static String getArea() {
        return sArea;
    }

    public static String deviceTypeToString(Context context, int deviceType) {
        Resources resources = context.getResources();
        int res = 0;
        switch (deviceType) {
            case Device.AC:
                res = R.string.ir_ac;
                break;
            case Device.TV:
                res = R.string.ir_tv;
                break;
            case Device.STB:
                res = R.string.ir_stb;
                break;
            case Device.BOX:
                res = R.string.ir_net_box;
                break;
            case Device.DVD:
                res = R.string.ir_dvd;
                break;
            case Device.PA:
                res = R.string.ir_amplifer;
                break;
            case Device.PRO:
                res = R.string.ir_projector;
                break;
            case Device.FAN:
                res = R.string.ir_fan;
                break;
            case Device.LIGHT:
                res = R.string.ir_bulb;
                break;
            case Device.AIR_CLEANER:
                res = R.string.ir_air_clean;
                break;
            default:
                return "";
        }

        return resources.getString(res);
    }

    public static IRBean saveMatchedACRemoteBean(int frequency, BrandList.Brand brand, int deviceType, int remoteId, String accStateString, String customName, HashMap<Integer, String> exts, ArrayList<IrData.IrKey> keys) {
        return DbUtil.saveMatchedRemoteBean(sContext, frequency, brand, null, null, deviceType, remoteId, accStateString, customName, exts, keys);
    }

    public static IRBean saveMatchedStbRemoteBean(int frequency, SpList.Sp sp, StbList.Stb stb, int deviceType, int remoteId, String customName, HashMap<Integer, String> exts, ArrayList<IrData.IrKey> keys) {
        return DbUtil.saveMatchedRemoteBean(sContext, frequency, null, sp, stb, deviceType, remoteId, "", customName, exts, keys);
    }

    public static List<IRBean> getIrBeans() {
        return DbUtil.getIrBeans(sContext);
    }

    public static Dialog newRemoteDialog(@NonNull Context context, @NonNull IRBean bean) throws NullPointerException {
        if (bean == null) {
            return null;
        }

        Dialog dialog = null;
        switch (bean.getDeviceType()) {
            case Device.AC:
                dialog = new RemoteACDialog(context, bean);
                break;
            case Device.STB:
                dialog = new RemoteSTBDialog(context, bean);
                break;
            case Device.BOX:
                dialog = new RemoteBoxDialog(context, bean);
                break;
            case Device.TV:
                dialog = new RemoteTVDialog(context, bean);
                break;
            default:
                dialog = new Dialog(context);
        }
        return dialog;
    }

    public static void getIRData(int deviceType, int remoteId, final IRequestResult<IrDataList> result) {
        if (result == null) {
            return;
        }
        final String key = String.valueOf(deviceType) + "_" + String.valueOf(remoteId);
        final SharedPreferences sp = sContext.getSharedPreferences(IR_SP_NAME, Context.MODE_PRIVATE);
        if (sContext != null) {
            String saved = sp.getString(key, "");
            if (!TextUtils.isEmpty(saved)) {
                IrData irData = parseJson(saved, IrData.class);
                if (irData != null) {
                    IrDataList list = new IrDataList();
                    List<IrData> l = new ArrayList<>(1);
                    l.add(irData);
                    list.setIrDataList(l);
                    result.onSuccess("", list);
                    return;
                }
            }
        }

        KookongSDK.getIRDataById(String.valueOf(remoteId), deviceType, new IRequestResult<IrDataList>() {
            @Override
            public void onSuccess(String s, IrDataList irDataList) {
                if (irDataList != null && irDataList.getIrDataList() != null && !irDataList.getIrDataList().isEmpty()) {
                    IrData irData = irDataList.getIrDataList().get(0);
                    sp.edit().putString(key, toJson(irData)).apply();
                    result.onSuccess(s, irDataList);
                }
            }

            @Override
            public void onFail(Integer integer, String s) {
                result.onFail(integer, s);
            }
        });

    }

    public static String toJson(Object target) {
        try {
            String json = new ObjectMapper().writeValueAsString(target);
            LogUtil.i("tojson :" + json);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T parseJson(String json, Class<T> tClass) {
        try {
            return new ObjectMapper().readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int[] searchKeyCodeIR(IrData irData, int keyCode) {
        if (irData == null || irData.keys == null || irData.keys.isEmpty())
            return null;
        final ArrayList<IrData.IrKey> keys = irData.keys;
        IrData.IrKey key = null;
        for (int i = 0; i < keys.size(); i++) {
            key = keys.get(i);
            if (key.fid == keyCode) {
                return parsePulse(key.pulse);
            }
        }
        return null;
    }

    private static int[] parsePulse(String pulse) {
        if (TextUtils.isEmpty(pulse)) {
            return null;
        }
        try {
            String[] split = pulse.split(",");
            int[] result = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Integer.parseInt(split[i]);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
