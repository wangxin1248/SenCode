package com.sensor.data.model.ext;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 11:36.
 * @Description: 传感器数据对象
 */
@Data
@ToString
public class SensorNode {
    List<Sensor> children;
}
