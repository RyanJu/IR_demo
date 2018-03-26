package com.alcidae.smarthome.ir.util;

import android.support.v7.widget.RecyclerView;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/26 11:46 1.0
 * @time 2018/3/26 11:46
 * @project ir_demo com.alcidae.smarthome.ir.util
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/26 11:46
 */

public interface SimpleOnItemClickListener<T> {
    void onClickItem(RecyclerView.Adapter adapter, int position, T data);
}
