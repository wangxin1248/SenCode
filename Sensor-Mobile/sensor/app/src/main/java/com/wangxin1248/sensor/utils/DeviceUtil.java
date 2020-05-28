package com.wangxin1248.sensor.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 提供设备的相关信息获取的工具类
 */
public class DeviceUtil {

    /**
     * 获取加密之后的设备的唯一标识符
     * @param context
     * @return
     */
    public static String getUniqueId(Context context){
        // 获取 AndroidId
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        // 根据 AndroidId 和 Serial Number 组成设备的唯一标识符
        String id = androidID + Build.SERIAL;

        // 对设备的唯一标识符进行 MD5 加密并返回
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }


    /**
     * 对字符串进行 MD5 加密
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String toMD5(String text) throws NoSuchAlgorithmException {
        // 获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        // 通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            // 循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            // 将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            // 转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            // 将循环结果添加到缓冲区
            sb.append(hexString);
        }

        // 返回整个结果
        return sb.toString();
    }
}
