package com.sensor.data.model.ext;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 16:02.
 * @Description: 传感器所感知到的数据对象
 */
@Data
@ToString
public class DataNode {
    List<SensorDD> children;
}
