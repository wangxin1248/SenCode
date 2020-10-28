package com.wangxin1248.sensoraudio.view;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wangxin1248.sensoraudio.R;
import com.wangxin1248.sensoraudio.bean.Mp3Info;
import com.wangxin1248.sensoraudio.bean.MyFile;
import com.wangxin1248.sensoraudio.listener.SensorListener;
import com.wangxin1248.sensoraudio.service.FileUploadService;
import com.wangxin1248.sensoraudio.service.MusicService;
import com.wangxin1248.sensoraudio.utils.ApplicationUtil;
import com.wangxin1248.sensoraudio.utils.MusicUtil;
import com.wangxin1248.sensoraudio.view.adapter.MyAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/5/24 9:40.
 * @Description: 应用主界面
 */
public class MainActivity extends AppCompatActivity  {
    private Handler handler;//交互页面处理机制
    private static final String TAG="MainActivity";
    private LinearLayout cl_main;
    private TextView tv_leftTime,tv_rightTime,tv_song;
    private Button but_start;
    private Button but_sensor_data_set;
    private Button but_upload;
    private EditText et_sensor_data;
    private SeekBar seekBar;
    private ImageButton ib_precious,ib_state,ib_next;
    private MusicService.MyBinder myBinder;
    private ListView lv_music;
    private List<Mp3Info> list;//数据集
    private MusicUtil utils;
    private MyAdapter adapter;
    private SimpleDateFormat time=new SimpleDateFormat("m:ss");//时间显示格式
    private Intent MediaServiceIntent;
    private boolean isPlaying=false;//判断是否在播放
    private int seek_flag=0;//进度条标志量
    private int music_index=0;//音乐索引
    private String song;//歌曲名字
    public ButtonBroadcastReceiver buttonBroadcastReceiver;//广播接收器
    private NotificationManager manager;
    private Notification notify;
    private RemoteViews remoteViews;
    public final static String BUTTON_PREV_ID="BUTTON_PREV_ID";//对应Action
    public final static String BUTTON_PLAY_ID="BUTTON_PLAY_ID";
    public final static String BUTTON_NEXT_ID="BUTTON_NEXT_ID";
    public final static String BUTTON_CLOSE_ID="BUTTON_CLOSE_ID";
    // 声明传感器管理对象
    private SensorManager sensorManager;
    // 声明传感器监听器对象集合
    private List<SensorListener> sensorListeners;
    // 声明传感器对象集合
    private List<Sensor> sensors;
    // 声明传感器采集频率
    private static int SENSOR_RATE = 0;
    // 广播数据
    private Intent stopFileWriteIntent = new Intent("com.example.communication.STOP_FILE_RECEIVER");
    // 声明后台文件上传服务
    private Intent fileIntent;
    // 当前文件夹下的所有文件
    private File[] subFile;
    // 当前文件夹
    private File file;
    // 设备标识符
    private String id;
    // 自定义文件对象
    private MyFile myFile;
    // 存储所有的文件
    private List<MyFile> myFiles;
    // 声明所需要上传的文件名
    private ArrayList<String> fileNames;
    // 文件上传广播
    private UploadFileMsgReceiver uploadFileMsgReceiver;



    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){//全屏显示
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        // 初始化界面视图
        InitView();
        // 初始化界面数据
        InitData();
        // 绑定服务
        MediaServiceIntent =new Intent(this,MusicService.class);//MediaServiceIntent为一个Intent
        bindService(MediaServiceIntent,connection,BIND_AUTO_CREATE);
        check();
    }

    /**
     * 初始化视图数据
     */
    private void InitData() throws IOException {
        // 创建传感器管理对象
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors = new ArrayList<>();
        sensorListeners = new ArrayList<>();
        // 获取线性加速度传感器对象
        sensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        // 获取陀螺仪传感器对象
        sensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        // 获取当前目录下的所有文件
        file = new File(ApplicationUtil.getContext().getExternalFilesDir("").getAbsolutePath() + File.separator);
        subFile = file.listFiles();
        // 后台文件上传
        fileIntent = new Intent(this, FileUploadService.class);
        // 保存所有的文件信息
        myFiles = new ArrayList<>();
        fileNames = new ArrayList<>();
        if(subFile != null) {
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
                    String path = subFile[iFileLength].getPath();
                    long length = subFile[iFileLength].length() / (1024);
                    // 判断是否为csv结尾
                    if (filename.trim().toLowerCase().endsWith(".csv")) {
                        // 将当前文件的信息进行保存
                        String size = length+" Kb";
                        myFile = new MyFile(filename,size,path,"未上传",length);
                        myFiles.add(myFile);
                        fileNames.add(filename);
                        Log.d(TAG, "InitData: "+filename+"size:"+size);
                    }
                }
            }
        }
        // 注册文件上传广播接收器
        uploadFileMsgReceiver = new UploadFileMsgReceiver();
        IntentFilter uploadFileRegIntentFilter = new IntentFilter();
        uploadFileRegIntentFilter.addAction("com.example.communication.FILE_UPLOAD_RECEIVER");
        registerReceiver(uploadFileMsgReceiver, uploadFileRegIntentFilter);
    }

    /**
     * 初始化各个控件，初始状态，以及监听
     */
    private void InitView(){
        cl_main=findViewById(R.id.cl_main);
        tv_leftTime=findViewById(R.id.tv_leftTime);
        tv_rightTime=findViewById(R.id.tv_rightTime);
        tv_song=findViewById(R.id.tv_song);
        seekBar=findViewById(R.id.seekBar);
        ib_precious=findViewById(R.id.ib_precious);
        ib_state=findViewById(R.id.ib_state);
        ib_next=findViewById(R.id.ib_next);
        lv_music=findViewById(R.id.lv_music);
        but_start=findViewById(R.id.but_start);
        but_sensor_data_set=findViewById(R.id.but_sensor_data_set);
        but_upload=findViewById(R.id.but_upload);
        et_sensor_data=findViewById(R.id.et_sensor_data);
        et_sensor_data.setInputType(InputType.TYPE_CLASS_NUMBER);

        utils=new MusicUtil();

        manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews=new RemoteViews(getPackageName(),R.layout.notification_view);

        cl_main.setBackgroundResource(R.mipmap.background);
        ib_state.setImageResource(R.drawable.start);

        list=new ArrayList<>();
        list= MusicUtil.getMp3InfoList(this);
        adapter=new MyAdapter(this,list);
        lv_music.setAdapter(adapter);
        adapter.setCurrentItem(0);
        song=list.get(0).getTitle();
        tv_song.setText(song);

        // 歌曲列表点击某个Item
        lv_music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                check();
                // 记录当前歌曲位置
                music_index=position;
                // 是否播放状态
                isPlaying=false;
                adapter.setCurrentItem(music_index);
                adapter.notifyDataSetInvalidated();
                song=list.get(music_index).getTitle();
                tv_song.setText(song);
                ib_state.setImageResource(R.drawable.stop);
                showNotification();
                // 开始播放所选择的音乐
                myBinder.seekToPosition(seek_flag);
                but_start.callOnClick();
            }
        });
        // 为按钮设置点击事件
        ib_precious.setOnClickListener(l);
        ib_state.setOnClickListener(l);
        ib_next.setOnClickListener(l);
        but_start.setOnClickListener(l);
        but_sensor_data_set.setOnClickListener(l);
        but_upload.setOnClickListener(l);
        handler=new Handler();
        setNotification();
        initButtonReceiver();
        showNotification();
    }

    /**
     * 主页按钮点击事件监听
     */
    /**
     * 状态栏播放控制按钮点击事件监听
     */
    public View.OnClickListener l=new View.OnClickListener() {
        @SneakyThrows
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_state:// 播放和暂停
                    check();
                    if(!isPlaying){
                        isPlaying=true;
                        song=list.get(music_index).getTitle();
                        tv_song.setText(song);
                        ib_state.setImageResource(R.drawable.stop);
                        if(seek_flag==0) {//是0说明刚开始播放
                            myBinder.playMusic(music_index);
                        }else{//暂停过后的情况
                            myBinder.playMusic(music_index);
                            myBinder.seekToPosition(seek_flag);
                        }
                    }else{
                        isPlaying=false;
                        ib_state.setImageResource(R.drawable.start);
                        seek_flag=myBinder.getPlayPosition();//获取当前位置暂停
                        myBinder.pauseMusic();
                    }
                    showNotification();
                    break;
                case R.id.ib_precious:// 上一首
                    check();
                    isPlaying=true;
                    ib_state.setImageResource(R.drawable.stop);
                    seek_flag=0;
                    if(music_index<=0){
                        music_index=list.size()-1;
                    }else{
                        music_index-=1;
                    }
                    showNotification();
                    seekBar.setMax(myBinder.getProgress(music_index));
                    song=list.get(music_index).getTitle();
                    tv_song.setText(song);
                    myBinder.preciousMusic();
                    break;
                case R.id.ib_next:// 下一首
                    check();
                    isPlaying=true;
                    ib_state.setImageResource(R.drawable.stop);
                    seek_flag=0;
                    if(music_index>=list.size()-1){
                        music_index=0;
                    }else{
                        music_index+=1;
                    }
                    showNotification();
                    seekBar.setMax(myBinder.getProgress(music_index));
                    song=list.get(music_index).getTitle();
                    tv_song.setText(song);
                    myBinder.nextMusic();
                    break;
                case R.id.but_start:// 播放音乐
                    Log.d(TAG, "onClick: 播放音乐");
                    check();
                    if(!isPlaying){
                        isPlaying=true;
                        song=list.get(music_index).getTitle();
                        tv_song.setText(song);
                        ib_state.setImageResource(R.drawable.stop);
                        if(seek_flag==0) {//是0说明刚开始播放
                            myBinder.playMusic(music_index);
                        }else{//暂停过后的情况
                            myBinder.playMusic(music_index);
                            myBinder.seekToPosition(seek_flag);
                        }
                        // 启动感知传感器数据
                        for(final Sensor sensor:sensors){
                            // 多线程执行
                            runOnUiThread(new Runnable() {
                                @SneakyThrows
                                @Override
                                public void run() {
                                    // 创建传感器监听对象
                                    SensorListener sensorListener = new SensorListener(sensor,MainActivity.this);
                                    sensorListeners.add(sensorListener);
                                    sensorManager.registerListener(sensorListener,sensor, SENSOR_RATE);
                                    return;
                                }
                            });
                        }
                        // 按钮显示暂停
                        but_start.setText("暂停");
                    }else{
                        isPlaying=false;
                        ib_state.setImageResource(R.drawable.start);
                        seek_flag=myBinder.getPlayPosition();//获取当前位置暂停
                        myBinder.pauseMusic();
                        // 停止感知传感器数据
                        // 取消注册传感器
                        for(SensorListener sensorListener:sensorListeners){
                            sensorManager.unregisterListener(sensorListener);
                        }
                        // 广播保存文件
                        stopFileWriteIntent.putExtra("listener", "stop");
                        sendBroadcast(stopFileWriteIntent);
                        // 按钮显示播放
                        but_start.setText("播放");
                        // 提示保存文件的目录
                        Toast.makeText(MainActivity.this, "请打开"+file.getAbsolutePath()+"查看保存的文件",Toast.LENGTH_LONG).show();
                    }
                    showNotification();
                    break;
                case R.id.but_sensor_data_set:// 设置传感器采样率
                    Log.d(TAG, "onClick: 设置传感器采样率");
                    String sensor_rate = String.valueOf(et_sensor_data.getText());
                    Log.d(TAG, "onClick: 传感器采样率"+sensor_rate);
                    if(!"".equals(sensor_rate)) {
                        SENSOR_RATE = Integer.parseInt(sensor_rate);
                    }
                    break;
                case R.id.but_upload:// 上传感知数据
                    Log.d(TAG, "onClick:上传感知数据");
                    but_upload.setText("正在上传...");
                    but_upload.setEnabled(false);
                    // 启动后台文件上传
                    fileIntent.putStringArrayListExtra("files",fileNames);
                    fileIntent.putExtra("id",id);
                    startService(fileIntent);
                    showNotification();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Service连接初始化
     */
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder= (MusicService.MyBinder) service;
            seekBar.setMax(myBinder.getProgress());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //这里是判断进度条移动是不是用户所为
                    if(fromUser){
                        myBinder.seekToPosition(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            handler.post(runnable);
            Log.d(TAG, "Service与Activity已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 界面刷新
     */
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(myBinder.getPlayPosition());
            tv_leftTime.setText(time.format(myBinder.getPlayPosition())+"");
            tv_rightTime.setText(time.format(myBinder.getProgress()-myBinder.getPlayPosition())+"");
            // 时间不够了自动触发下一首
            if(myBinder.getProgress()-myBinder.getPlayPosition()<1000){
                // 停止播放状态
                but_start.callOnClick();
                return;
//                runOnUiThread(new Runnable() {//使用uiThread触发点击事件
//                    @Override
//                    public void run() {
//                        // 播放下一首
//                        ib_next.performClick();
//                    }
//                });
            }
            handler.postDelayed(runnable,1000);
        }
    };

    /**
     * 检查权限
     */
    private void check(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                Log.d(TAG,"---------------------写权限不够-----------------");
            }
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                Log.d(TAG,"---------------------读权限不够-----------------");
            }
        }
    }

    /**
     * 权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "---------------------写权限够了-----------------------------");
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "---------------------读权限够了-----------------------------");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置通知
     */
    private void setNotification(){
        String channelID="cary";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(channelID,"xxx",NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }
        Intent intent=new Intent(MainActivity.this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(MainActivity.this,0,intent,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notify=new Notification.Builder(MainActivity.this,channelID)
                    .setWhen(System.currentTimeMillis())
                    .setSound(null)
                    .build();
        }
        notify.icon=android.R.drawable.btn_star;
        notify.contentIntent=pi;
        notify.contentView=remoteViews;
        notify.flags=Notification.FLAG_ONGOING_EVENT;
        remoteViews.setOnClickPendingIntent(R.id.notice,pi);
        //上一首
        Intent prevIntent=new Intent(BUTTON_PREV_ID);
        PendingIntent prevPendingIntent=PendingIntent.getBroadcast(this,0,prevIntent,0);
        remoteViews.setOnClickPendingIntent(R.id.widget_prev,prevPendingIntent);
        //播放暂停
        Intent playIntent=new Intent(BUTTON_PLAY_ID);
        PendingIntent playPendingIntent=PendingIntent.getBroadcast(this,0,playIntent,0);
        remoteViews.setOnClickPendingIntent(R.id.widget_play,playPendingIntent);
        //下一首
        Intent nextIntent=new Intent(BUTTON_NEXT_ID);
        PendingIntent nextPendingIntent=PendingIntent.getBroadcast(this,0,nextIntent,0);
        remoteViews.setOnClickPendingIntent(R.id.widget_next,nextPendingIntent);
        //关闭
        Intent closeIntent=new Intent(BUTTON_CLOSE_ID);
        PendingIntent closePendingIntent=PendingIntent.getBroadcast(this,0,closeIntent,0);
        remoteViews.setOnClickPendingIntent(R.id.widget_close,closePendingIntent);
    }

    /**
     * 展示通知
     */
    private void showNotification(){
        if(isPlaying){
            remoteViews.setImageViewResource(R.id.widget_play,R.drawable.stop);
        }else{
            remoteViews.setImageViewResource(R.id.widget_play,R.drawable.start);
        }
        remoteViews.setImageViewBitmap(R.id.widget_album,utils.getArtwork(MainActivity.this,list.get(music_index).getId(),list.get(music_index).getAlbum(),true,false));
        remoteViews.setImageViewResource(R.id.widget_close,android.R.drawable.ic_menu_close_clear_cancel);
        remoteViews.setTextViewText(R.id.widget_title,list.get(music_index).getTitle());
        remoteViews.setTextViewText(R.id.widget_artist,list.get(music_index).getArtist());
        remoteViews.setTextColor(R.id.widget_title,Color.BLACK);
        remoteViews.setTextColor(R.id.widget_artist,Color.BLACK);
        notify.contentView=remoteViews;
        manager.notify(100,notify);
    }

    /**
     * 注册广播
     */
    private void initButtonReceiver(){
        buttonBroadcastReceiver=new ButtonBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BUTTON_PREV_ID);
        intentFilter.addAction(BUTTON_PLAY_ID);
        intentFilter.addAction(BUTTON_NEXT_ID);
        intentFilter.addAction(BUTTON_CLOSE_ID);
        registerReceiver(buttonBroadcastReceiver,intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        myBinder.closeMusic();
        if(remoteViews!=null){
            manager.cancel(100);
        }
        // 注销广播接收器
        unregisterReceiver(uploadFileMsgReceiver);
    }


    /**
     * 状态栏按钮广播接收器
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver{

        //基本都是通过触发uiThread，执行相应按钮被按下的操作
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.d(TAG,"--------------------收到action:"+action+"--------------------------");
            if(action.equals(BUTTON_PREV_ID)){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ib_precious.performClick();
                        return;
                    }
                });
            }
            if(action.equals(BUTTON_PLAY_ID)){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ib_state.performClick();
                        return;
                    }
                });
            }
            if(action.equals(BUTTON_NEXT_ID)){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ib_next.performClick();
                        return;
                    }
                });
            }
            if(action.equals(BUTTON_CLOSE_ID)){
                handler.removeCallbacks(runnable);
                myBinder.closeMusic();
                unbindService(connection);
                if(remoteViews!=null){
                    manager.cancel(100);
                }
                unregisterReceiver(buttonBroadcastReceiver);
                finish();
            }
        }
    }
    /**
     * 文件上传服务广播接收器
     */
    public class UploadFileMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收广播到的数据
            String status = intent.getStringExtra("upload");
            if("ok".equals(status)){
                for(MyFile myFile:myFiles){
                    myFile.setStatus("已上传");
                }
                but_upload.setText("已成功上传所有文件");
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
            showNotification();
        }
    }
}

