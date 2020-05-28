package com.wangxin1248.sensoraudio.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.wangxin1248.sensoraudio.api.SensorAPI;
import com.wangxin1248.sensoraudio.bean.FileEntity;
import com.wangxin1248.sensoraudio.network.HttpCom;
import com.wangxin1248.sensoraudio.utils.ApplicationUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/5/24 16:08.
 * @Description: 传感器数据上传服务类
 */
public class FileUploadService extends IntentService{
    // tag
    private String TAG = "文件上传";
    // 请求网络工作类
    private HttpCom httpCom;
    // 所要上传的文件信息
    private FileEntity fileEntity;
    private List<FileEntity> fileEntityList;
    // 声明所需要上传的文件名
    private ArrayList<String> fileNames;
    // 上传的参数
    private Map<String, String> params;
    // 当前设备唯一标识符
    private String id;
    // 广播数据
    private Intent fileUploadIntent = new Intent("com.example.communication.FILE_UPLOAD_RECEIVER");

    /**
     * 构造函数
     */
    public FileUploadService(){
        super("FileUploadService");
        params = new HashMap<String, String>();
        httpCom = new HttpCom(SensorAPI.URL_FILE_UPLOAD.getUrl());
        fileEntityList = new ArrayList<>();
    }

    /**
     * 后台服务执行
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 获取intent的参数
        fileNames = intent.getStringArrayListExtra("files");
        id = intent.getStringExtra("id");
        for(String name:fileNames){
            // 构建文件实体信息
            fileEntity = new FileEntity();
            fileEntity.mName = "filename";
            fileEntity.mFileName = name;
            fileEntity.mFile = new File(ApplicationUtil.getContext().getFilesDir().getAbsolutePath() + File.separator +name);
            fileEntityList.add(fileEntity);
        }
        // 构建参数信息
        params.put("id",id);
        params.put("length",fileNames.size()+"");
        uploadFile();
    }

    private void uploadFile() {
        httpCom.uploadFile(fileEntityList, params, new HttpCom.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

            }

            @Override
            public void onSuccess(String result) {
                if(result.length()>0){
                    fileUploadIntent.putExtra("upload","ok");
                    sendBroadcast(fileUploadIntent);
                }
            }
        });
    }

    /**
     * 关闭服务
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
