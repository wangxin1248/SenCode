package com.wangxin1248.sensoraudio.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/5/24 12:18.
 * @Description: 文件对象
 */
@Data
@NoArgsConstructor
@ToString
public class MyFile {
    private String name;
    private String size;
    private String path;
    private String status;
    private long length;
    public MyFile(String name, String size, String path, String status, long length) {
        this.name = name;
        this.size = size;
        this.path = path;
        this.status = status;
        this.length = length;
    }
}
