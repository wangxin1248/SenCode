package com.wangxin1248.sensoraudio.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/5/24 12:22.
 * @Description: 时间工具类
 */
public class TimeUtil {
    /**
     * 获取当前时间戳
     * @return
     */
    public String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
}
