package com.wangxin1248.sensor.bean;

/**
 * 构造MyFile对象
 */
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
