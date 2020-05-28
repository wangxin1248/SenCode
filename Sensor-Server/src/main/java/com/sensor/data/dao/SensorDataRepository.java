package com.sensor.data.dao;

import com.sensor.data.model.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 15:50.
 * @Description: sensordata集合对应DAO
 */
public interface SensorDataRepository extends MongoRepository<SensorData,String> {
}
