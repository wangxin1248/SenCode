package com.wangxin1248.sensor.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Data
@ToString
@Setter
public class SensorDD {
    private String time;
    private float value1;
    private float value2;
    private float value3;

    public String getTime() {
        return time;
    }

    public float getValue1() {
        return value1;
    }

    public float getValue2() {
        return value2;
    }

    public float getValue3() {
        return value3;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setValue1(float value1) {
        this.value1 = value1;
    }

    public void setValue2(float value2) {
        this.value2 = value2;
    }

    public void setValue3(float value3) {
        this.value3 = value3;
    }
}