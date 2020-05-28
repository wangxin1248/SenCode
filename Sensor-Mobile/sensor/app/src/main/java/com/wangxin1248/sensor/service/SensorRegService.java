package com.wangxin1248.sensor.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.wangxin1248.sensor.api.SensorAPI;
import com.wangxin1248.sensor.bean.MySensor;
import com.wangxin1248.sensor.bean.SensorBase;
import com.wangxin1248.sensor.bean.SensorNode;
import com.wangxin1248.sensor.networks.HttpCom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手机注册后台服务类
 */
public class SensorRegService extends IntentService {
    // 日志标识符
    private static final String TAG = "手机注册";
    // 传感器管理器
    private SensorManager sensorManager;
    // 当前设备唯一标识符
    private String id;
    // 传感器type数组
    private ArrayList<Integer> sensorTypes;
    // 请求网络工作类
    private HttpCom httpCom;
    // 广播数据
    private Intent isRegIntent = new Intent("com.example.communication.IS_REG_RECEIVER");
    private Intent doRegIntent = new Intent("com.example.communication.DO_REG_RECEIVER");
    // 网络传递对象
    private SensorBase sensorBase;
    private SensorNode sensorNode;
    private List<MySensor> list;

    /**
     * 构造后台服务对象
     */
    public SensorRegService(){
        super("SensorRegService");
        sensorBase = new SensorBase();
        sensorNode = new SensorNode();
        list = new ArrayList<>();
    }

    /**
     * 接收intent并执行相应操作
     * @param intent
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        id = intent.getStringExtra("id");
        sensorBase.setPhoneId(id);
        sensorTypes = intent.getIntegerArrayListExtra("sensorTypes");
        if(sensorTypes != null){
            // 所要发送传感器的数量
            addSensor();
            doReg();
        }else{
            isReg();
        }
    }

    /**
     * 将传感器添加到json数据中
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void addSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        for(int i=0;i<sensorTypes.size();i++){
            MySensor mySensor = new MySensor();
            // 将所有传感器的数据保存到data中去
            Sensor sensor = sensorManager.getDefaultSensor(sensorTypes.get(i));
            mySensor.setName(sensor.getName());
            mySensor.setStringType(sensor.getStringType());
            mySensor.setType(sensor.getType());
            mySensor.setPower(sensor.getPower());
            list.add(mySensor);
        }
    }

    /**
     * 执行注册操作
     */
    private void doReg() {
        sensorNode.setChildren(list);
        sensorBase.setSensorNode(sensorNode);
        // 将所要传递的对象转为json
        String json = JSON.toJSONString(sensorBase);
        // 启动网络服务来获取当前手机是否向服务器注册
        httpCom = new HttpCom(SensorAPI.URL_SEN_DO_REG.getUrl());
        // 获取返回数据
        httpCom.postJsonData(json, new HttpCom.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result.get("success") != null){
                        // 广播返回数据
                        doRegIntent.putExtra("doReg", result.get("code")+"");
                    }else{
                        doRegIntent.putExtra("doReg", "0");
                    }
                    sendBroadcast(doRegIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String result) {

            }
        });
    }

    /**
     * 判断手机是否注册
     */
    private void isReg() {
        // 启动网络服务来获取当前手机是否向服务器注册
        httpCom = new HttpCom(SensorAPI.URL_SEN_IS_REG.getUrl());
        // 获取返回数据
        httpCom.postJsonData(id, new HttpCom.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null){
                        // 广播返回数据
                        isRegIntent.putExtra("reg", result.get("code")+"");
                    }else{
                        isRegIntent.putExtra("reg", "0");
                    }
                    sendBroadcast(isRegIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String result) {

            }
        });

    }

}
