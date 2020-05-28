package com.wangxin1248.sensor.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wangxin1248.sensor.R;
import com.wangxin1248.sensor.bean.MyFile;

import java.util.List;

/**
 * 给FileActivity界面填充数据
 */
public class FileActivityAdapter extends RecyclerView.Adapter<FileActivityAdapter.FileActivityViewHolder> {
    // 存储所有的文件
    private List<MyFile> myFiles;

    /**
     * 构造函数
     * @param myFiles
     */
    public FileActivityAdapter(List<MyFile> myFiles){
        this.myFiles = myFiles;
    }

    /**
     * ViewHolder内部类，真正用来显示数据
     */
    public static class FileActivityViewHolder extends RecyclerView.ViewHolder{
        public TextView adapter_file_view_name;
        public TextView adapter_file_view_size;
        public TextView adapter_file_view_status;
        public FileActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            adapter_file_view_name = itemView.findViewById(R.id.adapter_file_view_name);
            adapter_file_view_size = itemView.findViewById(R.id.adapter_file_view_size);
            adapter_file_view_status = itemView.findViewById(R.id.adapter_file_view_status);
        }
    }

    /**
     * 创建视图
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public FileActivityAdapter.FileActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_file_view,parent,false);
        FileActivityViewHolder vh = new FileActivityViewHolder(v);
        return vh;
    }

    /**
     * 填充列表内容
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull FileActivityAdapter.FileActivityViewHolder holder, int position) {
        // 设置具体显示内容
        holder.adapter_file_view_name.setText(myFiles.get(position).getName());
        holder.adapter_file_view_size.setText(myFiles.get(position).getSize());
        holder.adapter_file_view_status.setText(myFiles.get(position).getStatus());
    }

    /**
     * 获取所需填充的数据个数
     * @return
     */
    @Override
    public int getItemCount() {
        return myFiles.size();
    }
}
