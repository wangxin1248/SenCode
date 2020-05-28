package com.sensor.data.model;

import com.sensor.data.model.ext.SensorNode;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 11:13.
 * @Description: 手机基本信息
 */
@Data
@ToString
@Document(collection = "sensor_base")
public class SensorBase {
    @Id
    private String phoneId;// 手机id
    private SensorNode sensorNode;// 该手机中配置的传感器对象
}
