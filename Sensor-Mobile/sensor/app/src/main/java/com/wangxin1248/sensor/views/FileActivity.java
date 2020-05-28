package com.wangxin1248.sensor.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wangxin1248.sensor.R;
import com.wangxin1248.sensor.bean.MyFile;
import com.wangxin1248.sensor.service.FileUploadService;
import com.wangxin1248.sensor.utils.DeviceUtil;
import com.wangxin1248.sensor.views.adapter.FileActivityAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity {
    // 存储所有的文件
    private List<MyFile> myFiles;
    // 声明列表视图对象
    private RecyclerView recyclerView;
    // 声明列表填充器对象
    private FileActivityAdapter adapter;
    // 自定义文件对象
    private MyFile myFile;
    // 声明所需要上传的文件名
    private ArrayList<String> fileNames;
    // 声明试图管理对象
    private RecyclerView.LayoutManager layoutManager;
    // 声明上传文件按钮
    private Button activity_file_bt_upload;
    // 声明删除文件按钮
    private Button activity_file_bt_delete;
    // 声明后台文件上传服务
    private Intent fileIntent;
    // 当前文件夹下的所有文件
    private File[] subFile;
    // 当前文件夹
    private File file;
    // 设备标识符
    private String id;
    // 文件上传广播
    private UploadFileMsgReceiver uploadFileMsgReceiver;
    private String TAG = "FileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        initData();
        initView();
        updateView();
    }

    /**
     * 更新界面数据
     */
    private void updateView() {
        adapter = new FileActivityAdapter(myFiles);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        // 注销广播接收器
        unregisterReceiver(uploadFileMsgReceiver);
        super.onDestroy();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 获取设备唯一标识符
        id = DeviceUtil.getUniqueId(this);
        // 注册广播接收器
        uploadFileMsgReceiver = new UploadFileMsgReceiver();
        IntentFilter uploadFileRegIntentFilter = new IntentFilter();
        uploadFileRegIntentFilter.addAction("com.example.communication.FILE_UPLOAD_RECEIVER");
        registerReceiver(uploadFileMsgReceiver, uploadFileRegIntentFilter);
        // 后台文件上传
        fileIntent = new Intent(this, FileUploadService.class);
        // 保存所有的文件信息
        myFiles = new ArrayList<>();
        fileNames = new ArrayList<>();
        // 获取当前目录下的所有文件
        file = new File(this.getFilesDir().getAbsolutePath() + File.separator);
        subFile = file.listFiles();
        if(subFile != null){
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
                    String path = subFile[iFileLength].getPath();
                    long length = subFile[iFileLength].length()/(1024);
                    // 判断是否为csv结尾
                    if (filename.trim().toLowerCase().endsWith(".csv")) {
                        // 将当前文件的信息进行保存
                        String size = length+" Kb";
                        myFile = new MyFile(filename,size,path,"未上传",length);
                        myFiles.add(myFile);
                        fileNames.add(filename);
                    }
                }
            }
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        // 绑定视图对象
        recyclerView = findViewById(R.id.file_recycler_view);
        // 设置视图无变化，提升性能
        recyclerView.setHasFixedSize(true);
        // 使用线性LayoutManager
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        // 将支持的传感器列表交由 adapter 去显示
        adapter = new FileActivityAdapter(myFiles);
        recyclerView.setAdapter(adapter);
        // 文件上传处理
        activity_file_bt_upload = findViewById(R.id.activity_file_bt_upload);
        activity_file_bt_delete = findViewById(R.id.activity_file_bt_delete);
        activity_file_bt_upload.setText("一键上传所有文件");
        activity_file_bt_delete.setText("一键删除所有文件");
        if(subFile.length == 0){
            activity_file_bt_upload.setEnabled(false);
            activity_file_bt_delete.setEnabled(false);
        }else{
            activity_file_bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 删除文件夹下的所有文件
                    if(subFile != null){
                        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                            // 判断是否为文件夹
                            if (!subFile[iFileLength].isDirectory()) {
                                String filename = subFile[iFileLength].getName();
                                // 判断是否为csv结尾
                                if (filename.trim().toLowerCase().endsWith(".csv")) {
                                    subFile[iFileLength].delete();
                                }
                            }
                        }
                    }
                    activity_file_bt_delete.setText("已成功删除所有文件");
                    // 更新视图
                    updateView();
                }
            });
            activity_file_bt_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity_file_bt_upload.setText("正在上传...");
                    activity_file_bt_upload.setEnabled(false);
                    // 启动后台文件上传
                    fileIntent.putStringArrayListExtra("files",fileNames);
                    fileIntent.putExtra("id",id);
                    startService(fileIntent);
                }
            });
        }
    }

    public class UploadFileMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收广播到的数据
            String status = intent.getStringExtra("upload");
            if("ok".equals(status)){
                for(MyFile myFile:myFiles){
                    myFile.setStatus("已上传");
                }
                activity_file_bt_upload.setText("已成功上传所有文件");
                // 删除文件夹下的所有文件
                if(subFile != null){
                    for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                        // 判断是否为文件夹
                        if (!subFile[iFileLength].isDirectory()) {
                            String filename = subFile[iFileLength].getName();
                            // 判断是否为csv结尾
                            if (filename.trim().toLowerCase().endsWith(".csv")) {
                                subFile[iFileLength].delete();
                            }
                        }
                    }
                }
            }
            // 更新视图
            updateView();
        }
    }
}
