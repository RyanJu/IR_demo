package com.alcidae.smarthome.ir.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/29 11:17 1.0
 * @time 2018/3/29 11:17
 * @project ir_demo com.alcidae.smarthome.ir.util
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/29 11:17
 */

public class ToastUtil {
    public static void toast(Context context,@StringRes int strRes){
        Toast.makeText(context,strRes,Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context,@StringRes int strRes){
        Toast.makeText(context,strRes,Toast.LENGTH_LONG).show();
    }

    public static void toastLong(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }

}
