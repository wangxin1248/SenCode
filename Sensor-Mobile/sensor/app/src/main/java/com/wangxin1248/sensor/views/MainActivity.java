package com.wangxin1248.sensor.views;

//import android.location.GpsStatus;
//import android.location.LocationManager;
//import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wangxin1248.sensor.R;
import com.wangxin1248.sensor.service.SensorRegService;
import com.wangxin1248.sensor.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统主界面
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "检测传感器数据";
    // 声明传感器管理对象
    private SensorManager sensorManager;
    // 位置管理对象
//    private LocationManager locationManager;
    // 卫星数量
    private int count;
    // 卫星状态
//    private GpsStatus.Listener mGpsStatusCallback;
    // 声明传感器对象集合
    private List<Sensor> sensors;
    // 声明传感器id集合
    private ArrayList<Integer> sensorTypes;
    // 后台服务intent
    private Intent intent;
    // 跳转其他界面intent
    // 后台上传intent
    private Intent sensorIntent;
    private Intent fileIntent;
    // 传感器信息监控intent
    private Intent backSensorIntent;
    // 设备标识符
    private String id;
    // 界面组件
    private TextView activity_main_text_id;
    private TextView activity_main_text_sensor_num;
    private TextView activity_main_text_reg;
//    private TextView activity_main_text_gps;
    private TextView activity_main_text_info;
//    private TextView activity_main_text_gps_num;
    private Button activity_main_but_reg;
    private Button activity_main_but_col;
    private Button activity_main_but_gps;
    private Button activity_main_but_backcol;
    // 广播接收器
    private isRegMsgReceiver isMsgReceiver;
    private doRegMsgReceiver doMsgReceiver;


    /**
     * 创建界面
     * @param savedInstanceState
     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化数据
        initData();
        // 初始化视图
        initView();
        // 初始化服务
        initService();
    }

    /**
     * 摧毁界面
     */
    @Override
    protected void onDestroy() {
        // 关闭服务
        stopService(intent);
        // 注销广播
        unregisterReceiver(isMsgReceiver);
        unregisterReceiver(doMsgReceiver);
        // 停止gps监听
//        locationManager.removeGpsStatusListener(mGpsStatusCallback);
        super.onDestroy();
    }

    /**
     * 获取传感器对象
     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initData() {
        // 注册广播接收器
        isMsgReceiver = new isRegMsgReceiver();
        IntentFilter isRegIntentFilter = new IntentFilter();
        isRegIntentFilter.addAction("com.example.communication.IS_REG_RECEIVER");
        registerReceiver(isMsgReceiver, isRegIntentFilter);
        // 注册广播接收器
        doMsgReceiver = new doRegMsgReceiver();
        IntentFilter doRegIntentFilter = new IntentFilter();
        doRegIntentFilter.addAction("com.example.communication.DO_REG_RECEIVER");
        registerReceiver(doMsgReceiver, doRegIntentFilter);
        // 创建位置管理对象
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 创建传感器管理对象
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 获取系统支持的所有传感器对象列表
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        // 获取所有传感器的type并保存
        sensorTypes = new ArrayList<>();
        for (Sensor s : sensors) {
            sensorTypes.add(s.getType());
        }
        // 设置启动后台注册服务的intent
        intent = new Intent(this, SensorRegService.class);
        // 设置启动传感器详细列表的intent
        sensorIntent = new Intent(this, SensorActivity.class);
        // 设置后台监控传感器数据的intent
        backSensorIntent = new Intent(this, BatteryActivity.class);
        // 设置启动保存文件的intent
        fileIntent = new Intent(this, FileActivity.class);
        // 获取设备唯一标识符
        id = DeviceUtil.getUniqueId(this);
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        // 卫星状态监听
//        mGpsStatusCallback = new GpsStatus.Listener() {
//            @Override
//            public void onGpsStatusChanged(int event) {
//                if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
//                    //卫星状态改变
//                    fetchCurGpsStatus();
//                }
//            }
//        };
//        // 位置服务监听位置状态变化
//        locationManager.addGpsStatusListener(mGpsStatusCallback);
    }

    /**
     * 绘制界面
     */
    private void initView(){
        // 注册组件
        activity_main_text_id = findViewById(R.id.activity_main_text_id);
        activity_main_text_sensor_num = findViewById(R.id.activity_main_text_sensor_num);
        activity_main_text_reg = findViewById(R.id.activity_main_text_reg);
//        activity_main_text_gps = findViewById(R.id.activity_main_text_gps);
//        activity_main_text_gps_num = findViewById(R.id.activity_main_text_gps_num);
        activity_main_text_info = findViewById(R.id.activity_main_text_info);
        activity_main_but_reg = findViewById(R.id.activity_main_but_reg);
        activity_main_but_col = findViewById(R.id.activity_main_but_col);
        activity_main_but_backcol = findViewById(R.id.activity_main_but_backcol);
        activity_main_but_gps = findViewById(R.id.activity_main_but_gps);
        // 显示组件
        activity_main_text_id.setText(id);
        activity_main_text_sensor_num.setText(sensors.size()+"");
        activity_main_text_reg.setText("未注册");
//        activity_main_text_reg.setText("unregistered");
        activity_main_but_reg.setText("注册手机");
//        activity_main_but_reg.setText("Register phone");
        activity_main_but_col.setText("采集数据");
//        activity_main_but_col.setText("Data collection");
        activity_main_but_backcol.setText("能耗信息");
//        activity_main_but_backcol.setText("Energy consumption information");
        activity_main_but_gps.setText("上传数据");
//        activity_main_but_gps.setText("upload data");
//        activity_main_text_gps_num.setText(count+"");
        String info = "1.使用前请先注册\n" + "2.请连接校园网使用\n";
//        String info = "1.Please register before using\n" + "2.Please use the campus network\n";
        activity_main_text_info.setText(info);
//        if(isGpsAble(locationManager)){
//            activity_main_text_gps.setText("已开启");
//        }else{
//            activity_main_text_gps.setText("关闭，请手动打开");
//        }
        // 按钮功能实现
        if(activity_main_but_reg.isEnabled()){
            activity_main_but_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 启动后台注册手机服务
                    intent.putExtra("id",id);
                    intent.putIntegerArrayListExtra("sensorTypes",sensorTypes);
                    startService(intent);
                }
            });
        }
        activity_main_but_col.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打开传感器详情界面
                startActivity(sensorIntent);
            }
        });
        activity_main_but_backcol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打开能耗后台监控界面
                startActivity(backSensorIntent);
            }
        });
        activity_main_but_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打开文件信息
                 startActivity(fileIntent);
            }
        });
    }


    /**
     * 启动后台服务
     */
    private void initService() {
        intent.putExtra("id",id);
        startService(intent);
    }

    /**
     * 是否注册广播接收器
     */
    public class isRegMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 判断注册-接收广播到的数据
            String reg = intent.getStringExtra("reg");
            if("10000".equals(reg)){
                // 显示注册状态
                activity_main_text_reg.setText("已注册");
//                activity_main_text_reg.setText("registered");
                // 注册按钮无法点击
                activity_main_but_reg.setEnabled(false);
            }
        }
    }

    /**
     * 注册是否成功广播接收器
     */
    public class doRegMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 注册手机-接收广播到的数据
            String doReg = intent.getStringExtra("doReg");
            if("10000".equals(doReg)){
                // 注册成功
                activity_main_text_reg.setText("已注册");
//                activity_main_text_reg.setText("registered");
                activity_main_but_reg.setEnabled(false);
            }else{
                // 注册失败
                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 捕获当前卫星状态变化
     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void fetchCurGpsStatus() {
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        GpsStatus mStatus = locationManager.getGpsStatus(null);
//        // 获取卫星颗数的默认最大值
//        int maxSatellites = mStatus.getMaxSatellites();
//        // 创建一个迭代器保存所有卫星
//        Iterator<GpsSatellite> iters = mStatus.getSatellites().iterator();
//        // 卫星数
//        if (iters != null) {
//            while (iters.hasNext() && count <= maxSatellites) {
//                GpsSatellite s = iters.next();
//                if (s.usedInFix()) {
//                    count++;
//                }
//            }
//        }
//        // 卫星状态变化之后便更新
//        activity_main_text_gps_num.setText(count+"");
//        if (count < 3) {
//            //定位失败
//        } else {
//            //定位成功
//        }
//    }

    /**
     * 获取当前gps状态
     * @param lm
     * @return
     */
//    public boolean isGpsAble(LocationManager lm){
//        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)?true:false;
//    }
}