package com.wangxin1248.sensor.bean;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DataNode {
    List<SensorDD> children;

    public List<SensorDD> getChildren() {
        return children;
    }

    public void setChildren(List<SensorDD> children) {
        this.children = children;
    }
}
