package com.sensor.data.controller;

import com.sensor.data.api.SensorApplicationApi;
import com.sensor.data.model.SensorBase;
import com.sensor.data.model.SensorData;
import com.sensor.data.model.response.ResponseResult;
import com.sensor.data.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 10:57.
 * @Description: 项目接口请求控制类
 */
@RestController// 该controller输出json数据
@RequestMapping("/sensor")// 该controller的访问路径
public class SensorController implements SensorApplicationApi {
    // 注入service
    @Autowired
    SensorService sensorService;

    // 注册设备
    @Override
    @PostMapping("/register")
    public ResponseResult register(@RequestBody SensorBase sensor) {
        return sensorService.register(sensor);
    }

    // 判断设备是否注册
    @Override
    @PostMapping("/isregister")
    public ResponseResult isRegister(@RequestBody String phoneId) {
        return sensorService.isRegister(phoneId);
    }

    // 上传传感器感知数据
    @Override
    @PostMapping("/postdata")
    public ResponseResult postData(@RequestBody SensorData sensorData) {
        return sensorService.postData(sensorData);
    }

    // 上传传感器感知的文件
    @Override
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("id") String id, @RequestParam("length") String length,@RequestParam("filename") MultipartFile[] files) {
        return sensorService.upload(id,length,files);
    }
}
