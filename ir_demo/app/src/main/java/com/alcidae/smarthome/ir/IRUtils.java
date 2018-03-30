package com.alcidae.smarthome.ir;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.data.db.DbUtil;
import com.alcidae.smarthome.ir.data.db.IRBean;
import com.alcidae.smarthome.ir.ui.dialog.AcRemoteControllerDialog;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.ir.Device;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.BrandList;
import com.kookong.app.data.IrData;

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
    private static Context sContext;

    private static String sProvince = "广东省";
    private static String sCity = "深圳市";
    private static String sArea = "南山区";


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

    public static IRBean saveMatchedRemoteBean(int frequency, BrandList.Brand brand, int deviceType, int remoteId, String accStateString, String customName, HashMap<Integer, String> exts, ArrayList<IrData.IrKey> keys) {
        return DbUtil.saveMatchedRemoteBean(sContext, frequency, brand, deviceType, remoteId, accStateString, customName, exts, keys);
    }

    public static List<IRBean> getIrBeans() {
        return DbUtil.getIrBeans(sContext);
    }

    public static Dialog newRemoteDialog(@NonNull Context context, @NonNull IRBean bean) throws NullPointerException {
        if (bean == null) {
            return null;
        }

        if (bean.getDeviceType() == Device.AC) {
            return new AcRemoteControllerDialog(context, bean);
        } else {
            return new Dialog(context);
        }

    }

}
