package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mymusicplayer.LoginActivity;
import service.PlayMusicService;

/**
 * Created by 啊丁 on 2017/3/30.
 */

public class MySideslipListViewAdapter extends BaseAdapter {
    Context context;
    List<Map<String, Object>> list;

    public MySideslipListViewAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
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
    public int getItemViewType(int position) {
        Map<String, Object> map = list.get(position);

        return (Integer) map.get("type");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, Object> map = list.get(position);

        ImageView imageView = null;
        TextView textView = null;
        if (getItemViewType(position) == 0) {
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.sideslip_listview_item_top, parent, false);
                imageView = (ImageView) convertView.findViewById(R.id.sideslip_listview_item_top_image);
                textView = (TextView) convertView.findViewById(R.id.sideslip_listview_item_top_text);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (PlayMusicService.isLogin==false) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }else{

                        }
                    }
                });
            }
        } else if (getItemViewType(position) == 1) {
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.sideslip_listview_item_set, parent, false);
                imageView = (ImageView) convertView.findViewById(R.id.sideslip_listview_item_set_image);
                textView = (TextView) convertView.findViewById(R.id.sideslip_listview_item_set_text);
            }
        } else if (getItemViewType(position) == 2) {
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.cut_listview_item, parent, false);
                return convertView;
            }
        }
        if (map.get("image") == null) {
            imageView.setBackgroundResource(R.mipmap.ic_launcher);
        } else {
            imageView.setBackgroundResource((int) map.get("image"));
        }
        if (map.get("text") == null){
            textView.setText("请登录");
        }else{
            textView.setText(map.get("text").toString());
        }
        return convertView;
    }
}
