package com.wangxin1248.sensor.api;

/**
 * 保存用于与后端进行网络通信的Api
 */
public enum SensorAPI {
    URL_SEN_IS_REG("http://192.168.199.107:61001/sensor/isregister"),// 是否注册设备
    URL_SEN_DO_REG("http://192.168.199.107:61001/sensor/register"),// 注册设备
    URL_FILE_UPLOAD("http://192.168.199.107:61001/sensor/upload"),// 文件上传请求网址
    URL_SEN_DATA_POST("http://192.168.199.107:61001/sensor/postdata"),// 上传传感器数据
    URL_SEN_BATTERY_POST("http://192.168.199.107:61001/sensor/postBattery/");// 能耗数据上传

    String url;
    SensorAPI(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
