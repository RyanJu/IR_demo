package com.alcidae.smarthome.ir.data;

import java.util.Arrays;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/27 9:39 1.0
 * @time 2018/3/27 9:39
 * @project ir_demo com.alcidae.smarthome.ir.data
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/27 9:39
 */

public class EventSendIR {
    /**
     * ir frequency
     */
    int frequency;

    /**
     * id of this remote controller data from kookong sdk
     */
    int remoteId;

    /**
     *
     */
    int deviceType;

    /**
     * the ir data
     */
    int[] irDataArray;

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int[] getIrDataArray() {
        return irDataArray;
    }

    public void setIrDataArray(int[] irDataArray) {
        this.irDataArray = irDataArray;
    }

    @Override
    public String toString() {
        return "EventSendIR{" +
                "frequency=" + frequency +
                ", remoteId=" + remoteId +
                ", deviceType=" + deviceType +
                ", irDataArray=" + Arrays.toString(irDataArray) +
                '}';
    }
}
