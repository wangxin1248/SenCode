package com.wangxin1248.sensor.listener;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wangxin1248.sensor.api.SensorAPI;
import com.wangxin1248.sensor.bean.DataNode;
import com.wangxin1248.sensor.bean.SensorDD;
import com.wangxin1248.sensor.bean.SensorData;
import com.wangxin1248.sensor.networks.HttpCom;
import com.wangxin1248.sensor.utils.ApplicationUtil;
import com.wangxin1248.sensor.utils.DeviceUtil;
import com.wangxin1248.sensor.utils.TimeUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来保存所有传感器的数据并发送到服务器中
 */
public class SensorNetListener implements SensorEventListener {
    // 传感器对象
    private Sensor sensor;
    // tag
    private String TAG = "保存传感器数据";
    // 时间工具类
    private TimeUtil timeUtil;
    // 记录数据数量
    private int count;
    // 请求网络工作类
    private HttpCom httpCom;
    // 网络上传对象
    private SensorData sensorData;
    private DataNode dataNode;
    private List<SensorDD> list;
    private String id;
    // 当前监听器所需要完成的任务
    private int type;//0表示网络通信能耗监控；1表示数据处理能耗监控

    /**
     * 构造函数
     * @param sensor
     */
    public SensorNetListener(Sensor sensor, Activity activity, int s){
        type = s;
        // 获取当前传感器对象
        this.sensor = sensor;
        // 初始化timeUtil
        timeUtil = new TimeUtil();
        sensorData = new SensorData();
        dataNode = new DataNode();
        list = new ArrayList<>();
        // 获取设备唯一标识符
        id = DeviceUtil.getUniqueId(activity.getApplicationContext());
    }

    /**
     * 传感器数据产生了变化
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // 获取传感器改变数据
        float[] values = sensorEvent.values;
        SensorDD sensorDD = new SensorDD();
        sensorDD.setTime(timeUtil.getTime());
        sensorDD.setValue1(values[0]);
        // 有的传感器数据长度超过或等于3
        if(values.length>=3){
            sensorDD.setValue2(values[1]);
            sensorDD.setValue3(values[2]);
        }else{
            // 有的传感器长度为1
            sensorDD.setValue2(0);
            sensorDD.setValue3(0);
        }
        list.add(sensorDD);
        count++;
        if(count >= 1000){
            postData();
        }
    }

    /**
     * 将采集到的数据发送到服务器上
     */
    private void postData() {
        // 设置服务端所需的数据
        sensorData.setPhoneId(id);
        sensorData.setTime(timeUtil.getTime());
        dataNode.setChildren(list);
        sensorData.setDataNode(dataNode);
        sensorData.setSensorName(sensor.getName());
        // 将数据转为 json
        String json = JSON.toJSONString(sensorData);
        if(type == 0){
            // 网络通信能耗监控
            // 重新设置下一次需要发送的数据
            count = 0;
            sensorData = new SensorData();
            dataNode = new DataNode();
            list = new ArrayList<>();
            // 启动网络服务来进行数据上传
            httpCom = new HttpCom(SensorAPI.URL_SEN_DATA_POST.getUrl());
            httpCom.postJsonData(json, new HttpCom.VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    Toast.makeText(ApplicationUtil.getContext(),result.toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String result) {

                }
            });
        }else if(type == 1){
            // 数据处理能耗监控
            // 重新设置下一次需要发送的数据
            count = 0;
            sensorData = new SensorData();
            dataNode = new DataNode();
            list = new ArrayList<>();
        }
    }

    /**
     * 传感器精度产生了变化
     * @param sensor
     * @param i
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

