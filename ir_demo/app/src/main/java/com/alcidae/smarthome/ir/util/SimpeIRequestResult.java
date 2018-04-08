package com.alcidae.smarthome.ir.util;

import android.content.Context;

import com.alcidae.smarthome.ir.IRUtils;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.utils.LogUtil;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/8 14:32 1.0
 * @time 2018/4/8 14:32
 * @project ir_demo com.alcidae.smarthome.ir.util
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/8 14:32
 */

public abstract class SimpeIRequestResult<T> implements IRequestResult<T> {

    private Context context;

    public SimpeIRequestResult(Context context) {
        this.context = context;
    }

    @Override
    public abstract void onSuccess(String s, T t);

    @Override
    public void onFail(Integer integer, String s) {
        LogUtil.e("failed : " + integer + " , description:" + s);
        if (context != null) {
            IRUtils.handleError(context, integer);
        }
    }
}
