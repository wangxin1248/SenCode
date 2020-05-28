package com.wangxin1248.sensor.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MySensor {
    private String name;// 传感器名称
    private String stringType;//传感器字符串类型
    private int type;//传感器int类型
    private float power;//传感器能耗
}
