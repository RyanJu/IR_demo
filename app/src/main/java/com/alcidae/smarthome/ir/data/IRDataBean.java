package com.alcidae.smarthome.ir.data;

import java.io.Serializable;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/26 11:59 1.0
 * @time 2018/3/26 11:59
 * @project ir_demo com.alcidae.smarthome.ir.data
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/26 11:59
 */

public class IRDataBean implements Serializable {
    static final long serialVersionUID = 1;

    private int irDeviceType;

    public int getIrDeviceType() {
        return irDeviceType;
    }

    public void setIrDeviceType(int irDeviceType) {
        this.irDeviceType = irDeviceType;
    }
}
