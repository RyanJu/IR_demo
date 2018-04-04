package com.alcidae.smarthome.ir.data.db;

import android.content.Context;

import com.kookong.app.data.BrandList;
import com.kookong.app.data.IrData;
import com.kookong.app.data.SpList;
import com.kookong.app.data.StbList;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/28 15:20 1.0
 * @time 2018/3/28 15:20
 * @project ir_demo com.alcidae.smarthome.ir.data.db
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/28 15:20
 */

public class DbUtil {

    public static IRBean saveMatchedRemoteBean(Context context, int frequency, BrandList.Brand brand, SpList.Sp sp, StbList.Stb stb, int deviceType, int remoteId, String accStateString, String customName, HashMap<Integer, String> exts, ArrayList<IrData.IrKey> keys) {
        IRBean irBean = new IRBean();
        irBean.setFrequency(frequency);
        if (brand != null) {
            irBean.setBrandId(brand.brandId);
            irBean.setBrandName(brand.ename);
        }
        if (sp != null) {
            irBean.setSpId(sp.spId);
            irBean.setSpType(sp.type);
            irBean.setSpName(sp.spName);
        }
        if (stb != null) {
            irBean.setStb_bid(stb.bid);
            irBean.setStb_bname(stb.bname);
        }
        irBean.setDeviceType(deviceType);
        irBean.setRemoteId(remoteId);
        irBean.setAccState(accStateString);
        irBean.setCustomName(customName);
        irBean.setExts(exts);
        irBean.setKeys(keys);
        FinalDb finalDb = FinalDb.create(context);
        finalDb.save(irBean);
        return irBean;
    }

    public static List<IRBean> getIrBeans(Context context) {
        return FinalDb.create(context).findAll(IRBean.class);
    }
}
