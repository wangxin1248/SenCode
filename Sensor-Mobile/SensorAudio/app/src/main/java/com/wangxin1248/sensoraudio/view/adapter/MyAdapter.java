package com.wangxin1248.sensoraudio.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wangxin1248.sensoraudio.R;
import com.wangxin1248.sensoraudio.bean.Mp3Info;
import com.wangxin1248.sensoraudio.utils.MusicUtil;

import java.util.List;

/**
 * @Author: Xin Wang.
 * @Date:Created in 2020/5/24 11:41.
 * @Description: 为歌曲列表设置adapter
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Mp3Info> list;
    private ViewHolder holder=null;
    private int currentItem=0;

    //获得位置
    public  void setCurrentItem(int currentItem){
        this.currentItem=currentItem;
    }
    public MyAdapter(Context context,List<Mp3Info> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.list_item,null);
            holder.tv_title=convertView.findViewById(R.id.tv_title);
            holder.tv_artist=convertView.findViewById(R.id.tv_artist);
            holder.tv_duration=convertView.findViewById(R.id.tv_duration);
            holder.tv_position=convertView.findViewById(R.id.tv_position);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_artist.setText(list.get(position).getArtist());
        long duration = list.get(position).getDuration();
        String time= MusicUtil.formatTime(duration);
        holder.tv_duration.setText(time);
        holder.tv_position.setText(position+1+"");
        if(currentItem == position){
            holder.tv_title.setSelected(true);
            holder.tv_position.setSelected(true);
            holder.tv_duration.setSelected(true);
            holder.tv_artist.setSelected(true);
        }else{
            holder.tv_title.setSelected(false);
            holder.tv_position.setSelected(false);
            holder.tv_duration.setSelected(false);
            holder.tv_artist.setSelected(false);
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_title;//歌曲名
        TextView tv_artist;//歌手
        TextView tv_duration;//时长
        TextView tv_position;//序号
    }
}
