package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import org.xutils.x;

import java.util.List;

import jsonjavabean.RankList;

/**
 * Created by 啊丁 on 2017/3/29.
 */

public class MyRankListViewAdapter extends BaseAdapter {
    private Context context;
    private List<RankList.ContentBeanX> contentBeanXList;

    public MyRankListViewAdapter(Context context, List<RankList.ContentBeanX> contentBeanXList) {
        this.context = context;
        this.contentBeanXList = contentBeanXList;
    }

    @Override
    public int getCount() {
        return contentBeanXList.size();
    }

    @Override
    public Object getItem(int position) {
        return contentBeanXList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=((Activity)context).getLayoutInflater().inflate(R.layout.rank_listview_item,parent,false);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.rank_listview_imageview);
            holder.textView1= (TextView) convertView.findViewById(R.id.rank_listview_textview1);
            holder.textView2= (TextView) convertView.findViewById(R.id.rank_listview_textview2);
            holder.textView3=(TextView) convertView.findViewById(R.id.rank_listview_textview3);
            holder.textView4=(TextView) convertView.findViewById(R.id.rank_listview_textview4);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        RankList.ContentBeanX contentBeanX=contentBeanXList.get(position);
        x.image().bind(holder.imageView,contentBeanX.getPic_s260());
        holder.textView1.setText("1."+contentBeanX.getContent().get(0).getTitle()+"-"+contentBeanX.getContent().get(0).getAuthor());
        holder.textView2.setText("2."+contentBeanX.getContent().get(1).getTitle()+"-"+contentBeanX.getContent().get(1).getAuthor());
        holder.textView3.setText("3."+contentBeanX.getContent().get(2).getTitle()+"-"+contentBeanX.getContent().get(2).getAuthor());
        holder.textView4.setText("4."+contentBeanX.getContent().get(3).getTitle()+"-"+contentBeanX.getContent().get(3).getAuthor());
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
    }
}
