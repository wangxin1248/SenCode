package com.sensor.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/7 22:56.
 * @Description: Sensor项目启动类
 */
@EntityScan("com.sensor.data.model")//扫描实体类
@ComponentScan(basePackages = {"com.sensor.data.api"})//扫描项目下的所有包
@ComponentScan(basePackages = {"com.sensor.data.controller"})//扫描项目下的所有包
@ComponentScan(basePackages = {"com.sensor.data.dao"})//扫描项目下的所有包
@ComponentScan(basePackages = {"com.sensor.data.service"})//扫描项目下的所有包
@ComponentScan(basePackages = {"com.sensor.data.config"})//扫描项目下的所有包
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SensorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SensorApplication.class,args);
    }
}
