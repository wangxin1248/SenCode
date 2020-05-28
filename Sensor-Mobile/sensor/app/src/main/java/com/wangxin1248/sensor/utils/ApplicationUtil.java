package com.wangxin1248.sensor.utils;

import android.app.Application;
import android.content.Context;

/**
 * 自定义Application工具类，用于全局获取context
 */
public class ApplicationUtil extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    /**
     * 获取全局上下文
     * */
    public static Context getContext() {
        return context;
    }
}
