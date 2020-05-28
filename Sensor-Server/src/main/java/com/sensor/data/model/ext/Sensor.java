package com.sensor.data.model.ext;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 12:10.
 * @Description: 传感器对象
 */
@Data
@ToString
public class Sensor {
    private String name;// 传感器名称
    private String stringType;//传感器字符串类型
    private int type;//传感器int类型
    private float power;//传感器能耗
}
