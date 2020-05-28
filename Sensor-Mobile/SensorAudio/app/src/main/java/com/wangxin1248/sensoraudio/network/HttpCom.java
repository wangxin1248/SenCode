package com.wangxin1248.sensoraudio.network;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.wangxin1248.sensoraudio.bean.FileEntity;
import com.wangxin1248.sensoraudio.utils.ApplicationUtil;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


/**
 * 自定义网络请求类
 */
public class HttpCom {
    private static final String TAG = "HttpCom";
    private RequestQueue queue;
    private String url;

    /**
     * 构造网络请求对象
     *
     * @param url
     */
    public HttpCom(String url) {
        // 创建请求路径
        this.url = url;
    }

    /**
     * 为请求添加参数
     * @param url
     * @param map
     * @return
     */
    private String appendParameter(String url, Map<String, String> map) {
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for(Map.Entry<String,String> entry : map.entrySet()){
            builder.appendQueryParameter(entry.getKey(),entry.getValue());
        }
        return builder.build().getQuery();
    }

    /**
     * 发送json请求
     * @param json
     */
    public void postJsonData(final String json,final VolleyCallback callback) {
        // 创建一个请求队列
        queue = Volley.newRequestQueue(ApplicationUtil.getContext());
        // 创建一个Json请求
        com.wangxin1248.sensoraudio.network.MyJsonObjectRequest jsonObjectRequest = new com.wangxin1248.sensoraudio.network.MyJsonObjectRequest(url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.toString());
                Toast.makeText(ApplicationUtil.getContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        // 将请求添加到队列中去
        queue.add(jsonObjectRequest);
        // 开始上传数据
        queue.start();
    }


    /**
     * 上传文件数据
     * @param fileEntityList 文件实体列表
     * @param params 参数
     * @param callback 回调函数
     */
    public void uploadFile(List<FileEntity> fileEntityList, final Map<String, String> params, final VolleyCallback callback){
        queue = Volley.newRequestQueue(ApplicationUtil.getContext());
        com.wangxin1248.sensoraudio.network.MyMultipartRequest myMultipartRequest = new com.wangxin1248.sensoraudio.network.MyMultipartRequest(url, params, fileEntityList, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.toString());
                Toast.makeText(ApplicationUtil.getContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        // 将请求添加到队列中去
        queue.add(myMultipartRequest);
        // 开始上传数据
        queue.start();
    }

    /**
     * 异步回调接口
     */
    public interface VolleyCallback {
        void onSuccess(JSONObject result);
        void onSuccess(String result);
    }
}
