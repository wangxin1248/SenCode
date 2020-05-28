package com.wangxin1248.sensor.views.adapter;

import android.hardware.Sensor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.wangxin1248.sensor.R;

import java.util.List;

/**
 * 给SensorActivity界面填充数据
 */
public class SensorActivityAdapter extends RecyclerView.Adapter<SensorActivityAdapter.SensorActivityViewHolder> {
    // 传感器对象列表
    private List<Sensor> sensors;
    // 事件回调监听
    private SensorActivityAdapter.OnItemClickListener onItemClickListener;


    /**
     * ViewHolder内部类，真正用来显示数据
     */
    public static class SensorActivityViewHolder extends RecyclerView.ViewHolder{
        public TextView adapter_text_view_name;
        public TextView adapter_text_view_type;
        public TextView adapter_text_view_power;
        SensorActivityViewHolder(View v){
            super(v);
            adapter_text_view_name = v.findViewById(R.id.adapter_text_view_name);
            adapter_text_view_type = v.findViewById(R.id.adapter_text_view_type);
            adapter_text_view_power = v.findViewById(R.id.adapter_text_view_power);
        }
    }


    /**
     * 构造函数
     * @param sensors
     */
    public SensorActivityAdapter(List<Sensor> sensors){
        this.sensors = sensors;
    }


    /**
     * 创建视图
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SensorActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_text_view,parent,false);
        SensorActivityViewHolder vh = new SensorActivityViewHolder(v);
        return vh;
    }

    /**
     * 填充列表内容
     * @param holder
     * @param position
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final @NonNull SensorActivityViewHolder holder, int position) {
        holder.adapter_text_view_name.setText(sensors.get(position).getName());
        holder.adapter_text_view_type.setText(sensors.get(position).getStringType());
        holder.adapter_text_view_power.setText(sensors.get(position).getPower()+"");

        // 如果设置了回调，则设置点击事件
        if (onItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    /**
     * 获取所需要填充元素的个数
     */
    @Override
    public int getItemCount() {
        return sensors.size();
    }

    /**
     * 列表项点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    /**
     * 设置回调监听
     * @param listener
     */
    public void setOnItemClickListener(SensorActivityAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
