package com.wangxin1248.sensor.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
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
 * 用来保存gps的数据
 */
public class MyLocationListener implements android.location.LocationListener {

    // 时间工具类
    private TimeUtil timeUtil;
    // 文件
    private File file;
    // 保存数据
    private StringBuilder datas;
    // 文件存储数据
    private List<String> fileDates;
    // tag
    private String TAG = "保存GPS文件";
    // 停止文件写入广播
    private StopFileWriteReceiver stopFileWriteReceiver;


    /**
     * 构造函数
     */
    public MyLocationListener(){
        // 注册广播接收器
//        stopFileWriteReceiver = new StopFileWriteReceiver();
//        IntentFilter stopFileRegIntentFilter = new IntentFilter();
//        stopFileRegIntentFilter.addAction("com.example.communication.STOP_FILE_RECEIVER");
//        ApplicationUtil.getContext().registerReceiver(stopFileWriteReceiver, stopFileRegIntentFilter);
        // 初始化对象数据
        timeUtil = new TimeUtil();
        datas = new StringBuilder();
        fileDates = new ArrayList<>();
        // 设置传感器保存文件
        file = new File(ApplicationUtil.getContext().getFilesDir().getAbsolutePath() + File.separator +"gps" + ".csv");
        StringBuilder head = new StringBuilder();
        // 设置文件头信息
        head.append("date"+",");
        head.append("Longitude"+",");
        head.append("Latitude"+",");
        head.append("Altitude"+",");
        head.append("Bearing"+",");
        head.append("Accuracy");
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
     * 位置信息发生变化
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        // 保存数据
        datas.append(timeUtil.getTime()+",");
        datas.append(location.getLongitude()+",");
        datas.append(location.getLatitude()+",");
        datas.append(location.getAltitude()+",");
        datas.append(location.getBearing()+",");
        datas.append(location.getAccuracy());
        fileDates.add(datas.toString());
        // 清空数据
        datas.delete(0,datas.length());
        // 超过10条便进行保存
        if(fileDates.size()>10){
            // 超过1条数据时便将数据写入文件
            write2File();
        }
    }

    /**
     * 将数据保存到文件中
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

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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
