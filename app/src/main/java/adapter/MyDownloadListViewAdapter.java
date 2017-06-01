package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.myapplication.R;
import java.util.List;
import jsonjavabean.DownLoadBean;

/**
 * Created by 啊丁 on 2017/5/20.
 */

public class MyDownloadListViewAdapter extends BaseAdapter {
    Context context;
    List<DownLoadBean> list;

    public MyDownloadListViewAdapter(Context context, List<DownLoadBean> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_download_listview_item, null);
            myHolder.textView = (TextView) convertView.findViewById(R.id.activity_download_listview_item_text);
            myHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.activity_download_listview_item_progress);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.textView.setText(list.get(position).getName());
        myHolder.progressBar.setMax((int) Long.parseLong(list.get(position).getMax()));
        myHolder.progressBar.setProgress((int) Long.parseLong(list.get(position).getProgress()));

        return convertView;
    }
    class MyHolder {
        TextView textView;
        ProgressBar progressBar;
    }
}
