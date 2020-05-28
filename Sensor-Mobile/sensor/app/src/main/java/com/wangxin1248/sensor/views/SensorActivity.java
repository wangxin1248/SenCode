package com.wangxin1248.sensor.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wangxin1248.sensor.R;
import com.wangxin1248.sensor.listener.MyLocationListener;
import com.wangxin1248.sensor.listener.SensorListener;
import com.wangxin1248.sensor.views.adapter.SensorActivityAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机传感器信息显示界面
 */
public class SensorActivity extends AppCompatActivity {
    // 声明传感器管理对象
    private SensorManager sensorManager;
    // 声明列表视图对象
    private RecyclerView recyclerView;
    // 声明列表试图管理对象
    private SensorActivityAdapter adapter;
    // 声明视图管理对象
    private RecyclerView.LayoutManager layoutManager;
    // 声明位置管理器对象
    private LocationManager locationManager;
    // 传感器对象
    private List<Sensor> sensors;
    // 传感器详细界面intent
    private Intent intent;
    // 广播执行传感器数据写入
    // 广播数据
    private Intent stopFileWriteIntent = new Intent("com.example.communication.STOP_FILE_RECEIVER");
    // 界面组件
    private Button activity_sensor_bt_file;
//    private TextView activity_sensor_text_view_longitude;
//    private TextView activity_sensor_text_view_latitude;
//    private TextView activity_sensor_text_view_height;
//    private TextView activity_sensor_text_view_speed;
//    private TextView activity_sensor_text_view_bear;
//    private TextView activity_sensor_text_view_accuracy;
    private String TAG = "保存文件";
    // 保存所有的sensorListener
    private List<SensorListener> listeners;
    // 位置listener
//    private MyLocationListener locationListener;
    // 15Hz
    private int SENSOR_RATE_UI = 66667;
    // 50Hz
    private int SENSOR_RATE_NORMAL = 20000;
    // 80Hz
    private int SENSOR_RATE_MIDDLE = 12500;
    // 100Hz
    private int SENSOR_RATE_FAST = 10000;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        initData();
        initView();
        initService();
//        showGpsData();
    }

    /**
     * 展示GPS数据
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void showGpsData() {
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        // 注册gps状态变化
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                // 当GPS定位信息发生改变时，更新定位
//                updateShow(location);
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//                // 当GPS LocationProvider可用时，更新定位
//                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    Activity#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                    return;
//                }
//                updateShow(locationManager.getLastKnownLocation(s));
//            }
//
//            @Override
//            public void onProviderDisabled(String s) {
//                updateShow(null);
//            }
//        });
//    }

    /**
     * 将 GPS 数据显示到视图上
     * @param location
     */
//    private void updateShow(Location location) {
//        if(location != null){
//            activity_sensor_text_view_longitude.setText(location.getLongitude() + "");
//            activity_sensor_text_view_latitude.setText(location.getLatitude() + "");
//            activity_sensor_text_view_height.setText(location.getAltitude() + "");
//            activity_sensor_text_view_speed.setText(location.getSpeed() + "");
//            activity_sensor_text_view_bear.setText(location.getBearing() + "");
//            activity_sensor_text_view_accuracy.setText(location.getAccuracy() + "");
//        }
//    }

    /**
     * 初始化后台服务
     */
    private void initService() {
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 创建传感器管理对象
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 创建视图管理器对象
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 获取系统支持的所有传感器对象列表
        sensors = sensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
        // 初始化sensorListeners
        listeners = new ArrayList<>();
        // 初始化跳转到传感器数据界面
        intent = new Intent(this, SensorDataActivity.class);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        // 查找视图
        activity_sensor_bt_file = findViewById(R.id.activity_sensor_bt_file);
//        activity_sensor_text_view_longitude = findViewById(R.id.activity_sensor_text_view_longitude);
//        activity_sensor_text_view_latitude = findViewById(R.id.activity_sensor_text_view_latitude);
//        activity_sensor_text_view_height = findViewById(R.id.activity_sensor_text_view_height);
//        activity_sensor_text_view_speed = findViewById(R.id.activity_sensor_text_view_speed);
//        activity_sensor_text_view_bear = findViewById(R.id.activity_sensor_text_view_bear);
//        activity_sensor_text_view_accuracy = findViewById(R.id.activity_sensor_text_view_accuracy);
        // 设置视图信息
        activity_sensor_bt_file.setText("开始采集");
//        activity_sensor_bt_file.setText("Start collection");
        activity_sensor_bt_file.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if ("结束采集".equals(activity_sensor_bt_file.getText())) {
                    // 停止采集活动，注销所有正在监听的listener
                    for (SensorListener listener : listeners) {
                        sensorManager.unregisterListener(listener);
                    }
                    // 停止位置信息采集
//                    locationManager.removeUpdates(locationListener);
                    // 发送广播，保存最后采集的数据
//                    stopFileWriteIntent.putExtra("listener","stop");
//                    sendBroadcast(stopFileWriteIntent);
                    // 设置按钮状态
                    activity_sensor_bt_file.setText("开始采集");
//                    activity_sensor_bt_file.setText("Start collection");
                } else {
                    // 开始进行数据采集
                    activity_sensor_bt_file.setText("结束采集");
//                    activity_sensor_bt_file.setText("End collection");
                    saveFile();
                }
            }
        });
        // 绑定视图对象
        recyclerView = findViewById(R.id.main_recycler_view);
        // 设置视图无变化，提升性能
        recyclerView.setHasFixedSize(true);
        // 使用线性LayoutManager
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // 将支持的传感器列表交由 adapter 去显示
        adapter = new SensorActivityAdapter(sensors);
        recyclerView.setAdapter(adapter);
        // 设置点击事件
        adapter.setOnItemClickListener(new SensorActivityAdapter.OnItemClickListener() {
            // 点击事件
            @Override
            public void onItemClick(View view, int position) {
                // 传递传感器对象
                intent.putExtra("type", sensors.get(position).getType());
                startActivity(intent);
            }

            // 长按操作
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 将采集传感器的数据写入文件
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveFile() {
        for (Sensor sensor : sensors) {
            SensorListener listener = new SensorListener(sensor, SensorActivity.this);
            // 注册监听当前传感器的数据变化
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            // 保存所有正在监听的listener
            listeners.add(listener);
        }
//        // 请求当前 GPS 信息
//        locationListener = new MyLocationListener();
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        // 注册gps状态变化
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
    }

    /**
     * 界面注销操作
     */
    @Override
    protected void onDestroy() {
        // 停止位置信息采集
//        locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }
}
