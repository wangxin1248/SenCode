package com.sensor.data.service;

import com.sensor.data.dao.SensorBaseRepository;
import com.sensor.data.dao.SensorDataRepository;
import com.sensor.data.exception.ExceptionCast;
import com.sensor.data.model.SensorBase;
import com.sensor.data.model.SensorData;
import com.sensor.data.model.response.CommonCode;
import com.sensor.data.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/4/8 12:34.
 * @Description: sensor对应功能实现
 */
@Service
public class SensorService {
    @Autowired
    SensorBaseRepository sensorBaseRepository;
    @Autowired
    SensorDataRepository sensorDataRepository;

    /**
     * 手机设备注册
     * @param sensor sensorBase对象
     * @return ResponseResult
     */
    public ResponseResult register(SensorBase sensor) {
        if(sensor == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 首先查看该对象是否存在
        SensorBase one = sensorBaseRepository.findByPhoneId(sensor.getPhoneId());
        if(one != null){
            // 存在则更新对象
            one.setSensorNode(sensor.getSensorNode());
            sensorBaseRepository.save(one);
        }else{
            // 不存在则新增对象
            sensorBaseRepository.save(sensor);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 判断当前设备是否注册成功
     * @param phoneId 设备id
     * @return ResponseResult
     */
    public ResponseResult isRegister(String phoneId) {
        if(StringUtils.isEmpty(phoneId)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SensorBase sensorBase = sensorBaseRepository.findByPhoneId(phoneId);
        if(sensorBase == null){
            return new ResponseResult(CommonCode.FAIL);
        }else{
            return new ResponseResult(CommonCode.SUCCESS);
        }
    }

    /**
     * 上传传感器感知到的数据
     * @param sensorData 传感器感知数据
     * @return ResponseResult
     */
    public ResponseResult postData(SensorData sensorData) {
        if(sensorData == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        sensorDataRepository.save(sensorData);
        System.out.println("保存了一条记录，时间："+sensorData.getTime());
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 上传传感器感知的文件进行保存
     *
     * @param id 手机id
     * @param length 文件长度
     * @param files 文件列表
     * @return ResponseResult
     */
    public ResponseResult upload(String id, String length, MultipartFile[] files) {
        if(files != null){
            try {
                // 遍历所有的文件
                for(int i=0;i<Integer.parseInt(length);i++){
                    // 创建输出流
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File("data/"+ new Date()+"/"+files[i].getOriginalFilename())));
                    // 写入输出流
                    outputStream.write(files[i].getBytes());
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ResponseResult(CommonCode.SUCCESS);
        }else{
            return new ResponseResult(CommonCode.INVALID_PARAM);
        }
    }
}
