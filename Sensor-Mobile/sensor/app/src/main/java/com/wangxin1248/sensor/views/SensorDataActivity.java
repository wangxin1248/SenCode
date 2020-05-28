package com.wangxin1248.sensor.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.wangxin1248.sensor.R;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorDataActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView activity_sensor_text_view_name;
    private TextView activity_sensor_text_view_type;
    private TextView activity_sensor_text_view_power;
    private TextView activity_sensor_text_view_string;
    private TextView activity_sensor_text_view_data;
    private TextView activity_sensor_text_view_accu;
    private final String TAG = "数据改变";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);
        initData();
        initView();
    }

    /**
     * 初始化界面所需的数据
     */
    public void initData(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Intent intent = getIntent();
        sensor = sensorManager.getDefaultSensor(intent.getIntExtra("type",0));
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void initView(){
        activity_sensor_text_view_name = findViewById(R.id.activity_sensor_text_view_name);
        activity_sensor_text_view_type = findViewById(R.id.activity_sensor_text_view_type);
        activity_sensor_text_view_power = findViewById(R.id.activity_sensor_text_view_power);
        activity_sensor_text_view_string = findViewById(R.id.activity_sensor_text_view_string);
        activity_sensor_text_view_data = findViewById(R.id.activity_sensor_text_view_data);
        activity_sensor_text_view_accu = findViewById(R.id.activity_sensor_text_view_accu);
        activity_sensor_text_view_name.setText(sensor.getName());
        activity_sensor_text_view_type.setText(sensor.getStringType());
        activity_sensor_text_view_power.setText(sensor.getPower()+"");
        activity_sensor_text_view_string.setText(sensor.toString());
    }

    /**
     * 传感器数据改变
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        String data = "";
        float[] datas = sensorEvent.values;
        for(int i=0;i<datas.length;i++){
            data += datas[i]+"\n";
        }
        activity_sensor_text_view_data.setText(data);
//        Log.d(TAG, "onAccuracyChanged: "+data);
    }

    /**
     * 传感器精度改变
     * @param sensor
     * @param i
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        activity_sensor_text_view_accu.setText(sensor.getName()+"i is "+i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
