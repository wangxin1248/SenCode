package com.wangxin1248.sensoraudio.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/5/24 11:04.
 * @Description: 歌曲信息工具类
 */
@Data
@NoArgsConstructor
@ToString
public class Mp3Info {
    /**
     * 路径
     */
    private String url;
    /**
     * 歌曲名
     */
    private String title;
    /**
     * 艺术家
     */
    private String artist;
    /**
     * 歌曲时长
     */
    private long duration;
    /**
     * id
     */
    private long id;
    /**
     * 专辑图片
     */
    private long album;
}
