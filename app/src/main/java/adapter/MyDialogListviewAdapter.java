package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;
import java.util.Map;

import jsonjavabean.MusicList;
import utils.MusicUtils;

/**
 * Created by 啊丁 on 2017/4/11.
 */

public class MyDialogListviewAdapter extends BaseAdapter {
    Context context;
    List<Map<String,Object>> list;

    public MyDialogListviewAdapter(Context context, List<Map<String,Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size()+1;
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
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.dialog_collect_listview_item,parent,false);
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.dialog_collect_listview_item_imageview);
            viewHolder.textView1= (TextView) convertView.findViewById(R.id.dialog_collect_listview_item_name);
            viewHolder.textView2= (TextView) convertView.findViewById(R.id.dialog_collect_listview_item_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if (position==0){
            viewHolder.textView1.setText("新建播放列表");
            viewHolder.textView2.setText("");
            viewHolder.imageView.setBackgroundResource(R.drawable.addlist);
        }else{
            Map<String,Object> map=list.get(position-1);
            viewHolder.textView1.setText(((MusicList)map.get("musicList")).getListName());
            viewHolder.textView2.setText(map.get("count")+"首歌");
            viewHolder.imageView.setBackgroundResource(R.drawable.nopic);
        }


        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView1;
        TextView textView2;
    }
}
