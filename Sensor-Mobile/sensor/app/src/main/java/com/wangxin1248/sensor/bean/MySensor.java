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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStringType() {
        return stringType;
    }

    public void setStringType(String stringType) {
        this.stringType = stringType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }
}
