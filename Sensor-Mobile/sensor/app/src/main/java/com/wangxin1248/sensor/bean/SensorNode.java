package com.wangxin1248.sensor.bean;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SensorNode {
    List<MySensor> children;

    public List<MySensor> getChildren() {
        return children;
    }

    public void setChildren(List<MySensor> children) {
        this.children = children;
    }
}
