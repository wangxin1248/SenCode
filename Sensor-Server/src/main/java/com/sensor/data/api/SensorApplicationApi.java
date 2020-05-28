package com.sensor.data.api;

import com.sensor.data.model.SensorBase;
import com.sensor.data.model.SensorData;
import com.sensor.data.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 09:27.
 * @Description: 项目接口设置
 */
@Api(value = "Sensor数据管理接口，提供传感器数据模型的获取操作",tags = {"sensor数据模型接口"})
public interface SensorApplicationApi {
    // 设备注册方法
    @ApiOperation("设备注册接口")
    public ResponseResult register(SensorBase sensor);

    // 判断设备是否注册
    @ApiOperation("判断设备是否注册接口")
    public ResponseResult isRegister(String phoneId);

    // 上传传感器的数据
    @ApiOperation("上传传感器的数据")
    public ResponseResult postData(SensorData sensorData);

    // 上传传感器感知的文件
    @ApiOperation("上传传感器的文件数据")
    public ResponseResult upload(String id, String length, MultipartFile[] file);
}
