package com.alcidae.smarthome.ir.data.db;

import com.kookong.app.data.IrData;
import com.kookong.app.data.SpList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/27 15:59 1.0
 * @time 2018/3/27 15:59
 * @project ir_demo com.alcidae.smarthome.ir.data
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/27 15:59
 */

public class IRBean {



    //database id,auto increase
    int id;
    int frequency;
    int brandId;
    String brandName;
    int deviceType;
    int remoteId;
    String customName;

    HashMap<Integer, String> exts;
    ArrayList<IrData.IrKey> keys;
    String accState;

    //stb operator id
    int spId;

    //stb type : 0 normal stb,1 iptv
    int spType;

    String spName;

    //IPTV运营商下有多个合作公司生产的iptv盒子，bid表示该公司品牌盒子的id
    int stb_bid;
    String stb_bname;

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public int getSpId() {
        return spId;
    }

    public void setSpId(int spId) {
        this.spId = spId;
    }

    public int getSpType() {
        return spType;
    }

    public void setSpType(int spType) {
        this.spType = spType;
    }

    public int getStb_bid() {
        return stb_bid;
    }

    public void setStb_bid(int stb_bid) {
        this.stb_bid = stb_bid;
    }

    public String getStb_bname() {
        return stb_bname;
    }

    public void setStb_bname(String stb_bname) {
        this.stb_bname = stb_bname;
    }

    public ArrayList<IrData.IrKey> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<IrData.IrKey> keys) {
        this.keys = keys;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public HashMap<Integer, String> getExts() {
        return exts;
    }

    public void setExts(HashMap<Integer, String> exts) {
        this.exts = exts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    public String getAccState() {
        return accState;
    }

    public void setAccState(String accState) {
        this.accState = accState;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public String toString() {
        return "IRBean{" +
                "id=" + id +
                ", frequency=" + frequency +
                ", brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", deviceType=" + deviceType +
                ", remoteId=" + remoteId +
                ", customName='" + customName + '\'' +
                ", spId=" + spId +
                ", spType=" + spType +
                ", spName='" + spName + '\'' +
                ", stb_bid=" + stb_bid +
                ", stb_bname='" + stb_bname + '\'' +
                '}';
    }
}
