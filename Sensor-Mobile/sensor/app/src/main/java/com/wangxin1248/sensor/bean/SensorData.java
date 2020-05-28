package com.wangxin1248.sensor.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SensorData {
    private String phoneId;
    private String time;
    private String sensorName;
    private DataNode dataNode;
}
