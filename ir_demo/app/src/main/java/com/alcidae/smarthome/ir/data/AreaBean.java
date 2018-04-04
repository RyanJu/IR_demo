package com.alcidae.smarthome.ir.data;

import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/4 15:39 1.0
 * @time 2018/4/4 15:39
 * @project ir_demo com.alcidae.smarthome.ir.data
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/4 15:39
 */

public class AreaBean {
    List<Province> provinces;

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public static class Province {
        String provinceName;
        List<City> citys;

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public List<City> getCitys() {
            return citys;
        }

        public void setCitys(List<City> citys) {
            this.citys = citys;
        }

        @Override
        public String toString() {
            return "Province{" +
                    "provinceName='" + provinceName + '\'' +
                    ", citys=" + citys +
                    '}';
        }
    }

    public static class City {
        String citysName;

        public String getCitysName() {
            return citysName;
        }

        public void setCitysName(String citysName) {
            this.citysName = citysName;
        }

        @Override
        public String toString() {
            return "City{" +
                    "citysName='" + citysName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AreaBean{" +
                "provinces=" + provinces +
                '}';
    }
}
