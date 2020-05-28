package com.sensor.data.model.ext;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 16:05.
 * @Description: 传感器数据实体
 */
@Data
@ToString
public class SensorDD {
    private String time;
    private float value1;
    private float value2;
    private float value3;
}
