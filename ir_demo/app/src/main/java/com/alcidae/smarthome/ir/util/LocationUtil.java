package com.alcidae.smarthome.ir.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.alcidae.smarthome.ir.data.AreaBean;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzy.tvmao.utils.LogUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/4 15:15 1.0
 * @time 2018/4/4 15:15
 * @project ir_demo com.alcidae.smarthome.ir.util
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/4 15:15
 */

public class LocationUtil {

    private static AreaBean sAreaBean = null;

    public static Address getAddress(Context context, double latitude, double lontitude) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, lontitude, 10);
        if (addresses != null && !addresses.isEmpty()) {
            return addresses.get(0);
        }
        return null;
    }


    public static AreaBean readAreas(Context context) throws IOException {
        if (sAreaBean != null ) {
            return sAreaBean;
        }
        AssetManager assets = context.getAssets();
        InputStream inputStream = assets.open("cities.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line).append(" ");
        }
        ObjectMapper mapper = new ObjectMapper();
        return sAreaBean = mapper.readValue(jsonBuilder.toString(), AreaBean.class);
    }

    //use android location tool
    public static void getLocation(Context context, LocationListener locationListener) {
        if (locationListener == null) {
            return;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String gps = LocationManager.GPS_PROVIDER;
        String network = LocationManager.NETWORK_PROVIDER;
        Location result = locationManager.getLastKnownLocation(gps);
        if (result == null) {
            result = locationManager.getLastKnownLocation(network);
        }

        if (result == null) {
            locationManager.requestLocationUpdates(gps, 1000, 10, locationListener);
        } else {
            locationListener.onLocationChanged(result);
        }
    }

    //use baidu location sdk
    public static void getLocation(Context context, final OnLocationListener locationListener) {
        final LocationClient locationClient = new LocationClient(context.getApplicationContext());
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (locationListener != null && bdLocation != null) {
                    locationListener.onLocationGet(bdLocation.getLatitude(), bdLocation.getLongitude(), bdLocation.getCountryCode(),
                            bdLocation.getCountry(), bdLocation.getProvince(), bdLocation.getCity(), bdLocation.getDistrict());
                }
            }
        });

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setIgnoreKillProcess(false);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        locationClient.start();
    }

    public interface OnLocationListener {
        void onLocationGet(double latitude, double longitude, String countryCode, String country, String province, String city, String district);
    }
}
