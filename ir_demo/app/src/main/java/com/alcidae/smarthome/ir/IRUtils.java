package com.alcidae.smarthome.ir;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.data.db.DbUtil;
import com.alcidae.smarthome.ir.data.db.IRBean;
import com.alcidae.smarthome.ir.ui.dialog.RemoteACDialog;
import com.alcidae.smarthome.ir.ui.dialog.RemoteBoxDialog;
import com.alcidae.smarthome.ir.ui.dialog.RemoteSTBDialog;
import com.alcidae.smarthome.ir.ui.dialog.RemoteTVDialog;
import com.alcidae.smarthome.ir.util.ToastUtil;
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
import java.lang.ref.WeakReference;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.NotYetBoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    /**
     * sdk key of kookong sdk,can't be modified!
     */
    private static final String key = "36C9647F80C96B0214EF3F912B6250A2";

    /**
     * unique id for each phone
     */
    private static String sAppId = null;

    private static final String IR_SP_NAME = "IR_SP";

    private static final String IR_DEVICE_KYE = "device";

    /**
     * ApplicationContext
     */
    private static Context sContext;

    private static String sProvince = "";
    private static String sCity = "";
    private static String sArea = "";


    private static ExecutorService sThreadPool = null;
    private static final Object sLock = new Object();

    private IRUtils() {
    }

    /**
     * initial method ,need to call first
     *
     * @param context
     */
    public static void init(Context context) {
        sContext = context.getApplicationContext();
        if (sAppId == null) {
            sAppId = getSp().getString(IR_DEVICE_KYE, null);
            if (sAppId == null) {
                getSp().edit().putString(IR_DEVICE_KYE, sAppId = newDeviceID(sContext)).apply();
            }
        }
        boolean result = KookongSDK.init(context.getApplicationContext(), key, sAppId);
        LogUtil.d("Verify result is " + result);
        KookongSDK.setDebugMode(true);
    }

    private static String newDeviceID(Context sContext) {
        StringBuilder idBuilder = new StringBuilder("IR");
        TelephonyManager manager = (TelephonyManager) sContext.getSystemService(Context.TELEPHONY_SERVICE);
        idBuilder.append(getMac(sContext));

        UUID uuid = UUID.randomUUID();
        if (uuid != null) {
            idBuilder.append(uuid.toString());
        }

        return idBuilder.toString();
    }

    public static String getMac(Context sContext) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface != null) {
                    String displayName = networkInterface.getDisplayName();
                    if ("wlan0".equals(displayName)) {
                        //this mac address
                        byte[] hardwareAddress = networkInterface.getHardwareAddress();
                        if (hardwareAddress != null && hardwareAddress.length > 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (byte b : hardwareAddress) {
                                stringBuilder.append(String.format("%02x", b & 0xFF)).append(":");
                            }
                            if (stringBuilder.length() > 0) {
                                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            }
                            return stringBuilder.toString();
                        }
                    }
                }
            }

            WifiManager wifiManager = (WifiManager) sContext.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null) {
                    return connectionInfo.getMacAddress();
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * saved current area,note that 3rd param is not necessary
     *
     * @param province
     * @param city
     * @param area
     */
    public static void setArea(@NonNull String province, @NonNull String city, @Nullable String area) {
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

    /**
     * cast from deviceType integer to string
     *
     * @param context
     * @param deviceType
     * @return
     */
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

    /**
     * save a air conditioner matched remote controller to local
     *
     * @param frequency
     * @param brand
     * @param deviceType
     * @param remoteId
     * @param accStateString get from KKACManager
     * @param customName
     * @param exts
     * @param keys
     * @return
     */
    public static IRBean saveMatchedACRemoteBean(int frequency, BrandList.Brand brand, int deviceType, int remoteId, String accStateString, String customName, HashMap<Integer, String> exts, ArrayList<IrData.IrKey> keys) {
        return DbUtil.saveMatchedRemoteBean(sContext, frequency, brand, null, null, deviceType, remoteId, accStateString, customName, exts, keys);
    }

    /**
     * save a device(not air conditioner) matched remote controller to local
     *
     * @param frequency
     * @param sp
     * @param stb
     * @param deviceType
     * @param remoteId
     * @param customName
     * @param exts
     * @param keys
     * @return
     */
    public static IRBean saveMatchedNonACRemoteBean(int frequency, SpList.Sp sp, StbList.Stb stb, int deviceType, int remoteId, String customName, HashMap<Integer, String> exts, ArrayList<IrData.IrKey> keys) {
        return DbUtil.saveMatchedRemoteBean(sContext, frequency, null, sp, stb, deviceType, remoteId, "", customName, exts, keys);
    }

    /**
     * get saved remote controllers from local
     *
     * @return
     */
    public static List<IRBean> getIrBeans() {
        return DbUtil.getIrBeans(sContext);
    }

    /**
     * create control dialog of target type of device
     *
     * @param context
     * @param bean    should get from db
     * @return
     * @throws NullPointerException
     */
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
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * get ir data from local disk if exists,otherwise download it from servers and save to local
     *
     * @param deviceType
     * @param remoteId
     * @param result
     */
    public static void getIRData(int deviceType, int remoteId, final IRequestResult<IrDataList> result) {
        if (result == null) {
            return;
        }
        final String key = String.valueOf(deviceType) + "_" + String.valueOf(remoteId);
        final SharedPreferences sp = getSp();
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

    private static SharedPreferences getSp() {
        return sContext.getSharedPreferences(IR_SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * get a json string from object
     *
     * @param target
     * @return
     */
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

    /**
     * parse a json string to object
     *
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T parseJson(String json, Class<T> tClass) {
        try {
            return new ObjectMapper().readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * search from key list,if keycode matched
     *
     * @param irData
     * @param keyCode
     * @return
     */
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

    /**
     * parse a string formatted ir to integer array
     *
     * @param pulse
     * @return
     */
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

    /**
     * if current locale language is CN, take the Chinese name of brand,otherwise English
     *
     * @param brand
     * @return
     */
    public static String getBrandNameByLocale(BrandList.Brand brand) {
        if (brand == null) return "";

        if (sContext == null) return "";

        Locale locale = Locale.getDefault();
        if (Locale.CHINA.getLanguage().equals(locale.getLanguage())) {
            return brand.cname;
        }
        return brand.ename;
    }

    /**
     * handle error code toast
     * should be called in UI thread
     *
     * @param context
     * @param code
     */
    public static void handleError(Context context, int code) {
        int errorRes = R.string.ir_error_network;
        switch (code) {
            case 1://
                break;
        }
        ToastUtil.toast(context, errorRes);
    }


    /**
     * helper method to run on threads
     *
     * @param runnable
     */
    public static void runOnThread(final Runnable runnable) {
        synchronized (sLock) {
            if (sThreadPool == null) {
                sThreadPool = Executors.newSingleThreadExecutor();
            }
        }
        sThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (runnable != null) {
                        runnable.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
