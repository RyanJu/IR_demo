package com.alcidae.smarthome.ir.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.ArrayMap;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/18 11:09 1.0
 * @time 2018/4/18 11:09
 * @project ir_demo com.alcidae.smarthome.ir.util
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/18 11:09
 */

public class TypefaceUtil {

    private static final Map<String, SoftReference<Typeface>> sTpCache = new HashMap<>();

    /**
     * @param ttfName must be under folder assets/font/
     * @return
     */
    public static synchronized Typeface getTypeface(Context context, String ttfName) {
        SoftReference<Typeface> typefaceRef = sTpCache.get(ttfName);
        if (typefaceRef == null || typefaceRef.get() == null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), ("font/" + ttfName).trim());
            sTpCache.put(ttfName, new SoftReference<Typeface>(typeface));
            return typeface;
        }
        return typefaceRef.get();
    }

    private TypefaceUtil() {
    }
}
