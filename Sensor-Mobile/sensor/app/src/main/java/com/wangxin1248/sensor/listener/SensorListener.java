package com.wangxin1248.sensor.listener;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.Toast;

import com.wangxin1248.sensor.utils.ApplicationUtil;
import com.wangxin1248.sensor.utils.TimeUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来保存所有传感器的数据
 */
public class SensorListener implements SensorEventListener {
    // 传感器对象
    private Sensor sensor;
    // tag
    private String TAG = "保存文件";
    // 时间工具类
    private TimeUtil timeUtil;
    // 保存数据
    private StringBuilder datas;
    // 文件存储数据
    private List<String> fileDates;
    // 文件
    private File file;
    // 停止文件写入广播
    private StopFileWriteReceiver stopFileWriteReceiver;

    /**
     * 构造函数
     * @param sensor
     */
    public SensorListener(Sensor sensor, Activity activity){
        // 注册广播接收器
//        stopFileWriteReceiver = new StopFileWriteReceiver();
//        IntentFilter stopFileRegIntentFilter = new IntentFilter();
//        stopFileRegIntentFilter.addAction("com.example.communication.STOP_FILE_RECEIVER");
//        ApplicationUtil.getContext().registerReceiver(stopFileWriteReceiver, stopFileRegIntentFilter);
        // 获取当前传感器对象
        this.sensor = sensor;
        // 当前传感器的名称
        String filename = sensor.getName();
        // 初始化timeUtil
        timeUtil = new TimeUtil();
        // 初始化传感器数据保存对象
        datas = new StringBuilder();
        // 初始化文件数据保存对象
        fileDates = new ArrayList<>();
        // 设置传感器保存文件
        file = new File(ApplicationUtil.getContext().getFilesDir().getAbsolutePath() + File.separator +filename + ".csv");
        StringBuilder head = new StringBuilder();
        // 设置文件头信息
        head.append("date"+",");
        head.append("data1"+",");
        head.append("data2"+",");
        head.append("data3"+",");
        head.append("data4"+",");
        head.append("data5");
        // 文件不存在时保存文件头信息
        if(!file.exists()) {
            try {
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.write(head.toString());
                bw.newLine();
                bw.close();
            } catch (IOException e) {
                Toast.makeText(ApplicationUtil.getContext(), "文件头写入失败" + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    /**
     * 传感器数据产生了变化
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // 获取传感器改变数据
        float[] data = sensorEvent.values;
        // 读取传感器数据
        datas.append(timeUtil.getTime()+",");
        for(int i = 0;i<data.length;i++){
            datas.append(data[i]+",");
        }
        fileDates.add(datas.toString());
        // 清空数据
        datas.delete(0,datas.length());
        if(fileDates.size()>1000){
            // 超过1000条数据时便将数据写入文件
            write2File();
        }
    }

    /**
     * 将采集到的数据写入文件
     */
    private void write2File() {
        // 将数据写入到文件中
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            for(int i=0;i<fileDates.size();i++){
                bw.write(fileDates.get(i));
                bw.newLine();
            }
            // 清空数据
            fileDates.clear();
            // 关闭流
            bw.close();
        } catch (IOException e) {
            Toast.makeText(ApplicationUtil.getContext(),"文件写入失败"+e.toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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

    public class StopFileWriteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收广播到的数据
            String doReg = intent.getStringExtra("listener");
            if("stop".equals(doReg)){
                // 结束监听，写入文件
                write2File();
            }
        }
    }
}

