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

    public String getPhoneId() {
        return phoneId;
    }

    public String getTime() {
        return time;
    }

    public String getSensorName() {
        return sensorName;
    }

    public DataNode getDataNode() {
        return dataNode;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public void setDataNode(DataNode dataNode) {
        this.dataNode = dataNode;
    }
}
