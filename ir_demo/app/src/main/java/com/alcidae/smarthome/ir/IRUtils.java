package com.alcidae.smarthome.ir;

import android.content.Context;
import android.content.res.Resources;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.data.db.IRBean;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.ir.Device;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.BrandList;
import com.kookong.app.data.IrData;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static void init(Context context) {
        sContext = context.getApplicationContext();
        boolean result = KookongSDK.init(context.getApplicationContext(), key, irDeviceId);
        LogUtil.d("Verify result is " + result);
        KookongSDK.setDebugMode(true);
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
        IRBean irBean = new IRBean();
        irBean.setFrequency(frequency);
        irBean.setBrandId(brand.brandId);
        irBean.setBrandName(brand.ename);
        irBean.setDeviceType(deviceType);
        irBean.setRemoteId(remoteId);
        irBean.setAccState(accStateString);
        irBean.setCustomName(customName);
        irBean.setExts(exts);
        irBean.setKeys(keys);
        FinalDb finalDb = FinalDb.create(sContext);
        finalDb.save(irBean);
        return irBean;
    }

}
