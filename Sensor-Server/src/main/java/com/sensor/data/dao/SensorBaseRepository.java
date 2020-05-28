package com.sensor.data.dao;

import com.sensor.data.model.SensorBase;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 12:30.
 * @Description: SensorBase集合对应dao
 */
public interface SensorBaseRepository extends MongoRepository<SensorBase,String> {
    public SensorBase findByPhoneId(String phoneId);
}
