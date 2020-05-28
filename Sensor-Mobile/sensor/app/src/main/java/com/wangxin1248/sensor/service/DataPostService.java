package com.wangxin1248.sensor.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 后台数据上传服务
 * 当前未使用
 */
public class DataPostService extends IntentService{
    private static final String TAG = "后台数据上传服务";
    // 传感器管理对象
    private SensorManager sensorManager;
    // 传感器对象
    private Sensor sensor;
    //

    /**
     * 构造函数
     */
    public DataPostService() {
        super("DataPostService");
    }

    /**
     * 后台服务执行
     * @param intent 传递过来的intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 获取系统传感器管理对象
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 获取传感器id
        int type = intent.getIntExtra("type",0);
    }
}
