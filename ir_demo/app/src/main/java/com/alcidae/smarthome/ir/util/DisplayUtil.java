package com.alcidae.smarthome.ir.util;

import android.content.Context;

public class DisplayUtil {
	/**
	 * 将px转为dip或dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}
	
	/**
	 * 将dip或dp转为px
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
	/**
	 * 将px转为sp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int)(pxValue / fontScale + 0.5f);
	}
	
	/**
	 * 将sp转为px
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int)(spValue * fontScale + 0.5f);
	}		
}
