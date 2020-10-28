package com.wangxin1248.sensor.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SensorBase {
    private String phoneId;// 手机id
    private SensorNode sensorNode;// 该手机中配置的传感器对象

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public SensorNode getSensorNode() {
        return sensorNode;
    }

    public void setSensorNode(SensorNode sensorNode) {
        this.sensorNode = sensorNode;
    }
}
