package com.sensor.data.model;

import com.sensor.data.model.ext.DataNode;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 12:13.
 * @Description: 传感器数据对象
 */
@Data
@ToString
@Document(collection = "sensor_data")
public class SensorData {
    private String phoneId;
    private String time;
    private String sensorName;
    private DataNode dataNode;
}
