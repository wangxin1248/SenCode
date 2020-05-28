package com.wangxin1248.sensor.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wangxin1248.sensor.R;
import com.wangxin1248.sensor.api.SensorAPI;
import com.wangxin1248.sensor.listener.SensorListener;
import com.wangxin1248.sensor.listener.SensorNetListener;
import com.wangxin1248.sensor.networks.HttpCom;
import com.wangxin1248.sensor.service.DataPostService;
import com.wangxin1248.sensor.utils.ApplicationUtil;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将手机传感器的数据进行json化显示
 */
public class BatteryActivity extends AppCompatActivity {
    private static final String TAG = "BatteryActivity";
    // 传感器管理器对象
    private SensorManager sensorManager;
    // spanner
    private Spinner battery_activity_button_spinner;
    // 启动按钮
    private Button battery_activity_button_apply;
    private Button battery_activity_button_apply_1;
    private Button battery_activity_button_apply_2;
    // 传感器数据
    private List<String> sensorNames;
    // 保存向 spinner 中保存的数据
    private ArrayAdapter<String> adapter;
    // 传感器type
    private int type;
    // 传感器对象列表
    private List<Sensor> sensors;
    // 启动的监听类
    private List<SensorNetListener> listeners;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        initData();
        initView();
        initService();
    }

    // 初始化数据
    private void initData(){
        listeners = new ArrayList<>();
        // sensormanager 控制
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorNames = new ArrayList<>();
        // 获取当前的传感器名称列表
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor : sensors){
            sensorNames.add(sensor.getName()+"&"+sensor.getType());
        }
        sensorNames.add("ALL&"+9999);
        // 创建向 spinner 中设置的数据
        adapter = new ArrayAdapter<String>(BatteryActivity.this,android.R.layout.simple_spinner_item,sensorNames);
    }

    // 初始化界面数据
    private void initView(){
        battery_activity_button_spinner = findViewById(R.id.battery_activity_button_spinner);
        battery_activity_button_apply = findViewById(R.id.battery_activity_button_apply);
        battery_activity_button_apply_1 = findViewById(R.id.battery_activity_button_apply_1);
        battery_activity_button_apply_2 = findViewById(R.id.battery_activity_button_apply_2);
        battery_activity_button_spinner.setAdapter(adapter);
        // 设置点击事件
        battery_activity_button_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = Integer.parseInt(adapterView.getItemAtPosition(i).toString().split("&")[1]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        battery_activity_button_apply.setText("网络通信能耗监控");
//        battery_activity_button_apply.setText("Network communication");
        battery_activity_button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("Network communication".equals(battery_activity_button_apply.getText())){
                    if(type == 9999){
                        // 监控所有传感器对象
                        for(Sensor sensor : sensors){
                            SensorNetListener listener = new SensorNetListener(sensor, BatteryActivity.this,0);
                            // 注册监听当前传感器的数据变化
                            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
                            listeners.add(listener);
                        }
                    }else{
                        Sensor sensor = sensorManager.getDefaultSensor(type);
                        SensorNetListener listener = new SensorNetListener(sensor, BatteryActivity.this,0);
                        // 注册监听当前传感器的数据变化
                        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
                        listeners.add(listener);
                    }
                    battery_activity_button_apply.setText("停止");
//                    battery_activity_button_apply.setText("Stop");
                }else{
                    // 取消监听
                    for(SensorNetListener listener : listeners){
                        sensorManager.unregisterListener(listener);
                    }
                    listeners.clear();
                    battery_activity_button_apply.setText("网络通信能耗监控");
                }

            }
        });
        battery_activity_button_apply_1.setText("数据处理能耗监控");
//        battery_activity_button_apply_1.setText("Data processing");
        battery_activity_button_apply_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("Data processing".equals(battery_activity_button_apply_1.getText())){
                    if(type == 9999){
                        // 监控所有传感器对象
                        for(Sensor sensor : sensors){
                            SensorNetListener listener = new SensorNetListener(sensor, BatteryActivity.this,1);
                            // 注册监听当前传感器的数据变化
                            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
                            listeners.add(listener);
                        }
                    }else{
                        Sensor sensor = sensorManager.getDefaultSensor(type);
                        SensorNetListener listener = new SensorNetListener(sensor, BatteryActivity.this,1);
                        // 注册监听当前传感器的数据变化
                        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
                        listeners.add(listener);
                    }
                    battery_activity_button_apply_1.setText("停止");
//                    battery_activity_button_apply_1.setText("Stop");
                }else{
                    // 取消监听
                    for(SensorNetListener listener : listeners){
                        sensorManager.unregisterListener(listener);
                    }
                    listeners.clear();
                    battery_activity_button_apply_1.setText("数据处理能耗监控");
                }
            }
        });
        battery_activity_button_apply_2.setText("状态控制能耗监控");
//        battery_activity_button_apply_2.setText("State control");
        battery_activity_button_apply_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("State control".equals(battery_activity_button_apply_2.getText())){
                    if(type == 9999){
                    }else{
                    }
                    battery_activity_button_apply_2.setText("停止");
//                    battery_activity_button_apply_2.setText("Stop");
                }else{
                    battery_activity_button_apply_2.setText("状态控制能耗监控");
                }
            }
        });
    }

    // 初始化后台服务
    private void initService(){

    }


    // 绑定注册的传感器
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
