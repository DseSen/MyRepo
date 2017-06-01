package adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;
import java.util.Map;

import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/4/10.
 */

public class MyPlayFragmentListViewAdapter extends BaseAdapter {
    Context context;
    List list;
    int type;

    public MyPlayFragmentListViewAdapter(Context context, List list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
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

        switch (type) {
            case ConstUtils.TYPE_HEAD: {
                Map<String,Object> map= (Map<String, Object>) list.get(position);
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.fragment_play_listview_item, null);
                }
                ImageView imageView= (ImageView) convertView.findViewById(R.id.fragment_play_listview_item_image);
                TextView textView= (TextView) convertView.findViewById(R.id.fragment_play_listview_item_text);
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),(Integer) map.get("head")));
                textView.setText(map.get("func").toString());
                break;
            }
        }
        return convertView;
    }
}
