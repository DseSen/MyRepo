package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import jsonjavabean.LocalMusic;
import utils.FileFilter;
import utils.MusicUtils;

/**
 * Created by 啊丁 on 2017/4/13.
 */

public class MyLocalMusicAdapter extends BaseAdapter {
    Context context;
    List<String> className;
    Map<String, List<LocalMusic>> fileList;
    int type;

    public MyLocalMusicAdapter(Context context, List<String> className, Map<String, List<LocalMusic>> fileList, int type) {
        this.context = context;
        this.className = className;
        this.fileList = fileList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return className.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.rank_songs_listview_item, parent, false);
            viewholder.imageView = (ImageView) convertView.findViewById(R.id.rank_songs_imageview);
            viewholder.title = (TextView) convertView.findViewById(R.id.rank_songs_textview_name);
            viewholder.count = (TextView) convertView.findViewById(R.id.rank_songs_textview_author);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        List<LocalMusic> list = fileList.get(className.get(position));
        String size = list.size() + "";

        int musicId = list.get(0).getMusicId();
        switch (type) {
            case 1: {

                viewholder.title.setText(className.get(position));
                MusicUtils.setHead(viewholder.imageView, musicId);
                viewholder.count.setText(size + "首歌");

                break;
            }
            case 2: {

                viewholder.title.setText(className.get(position));
                MusicUtils.setHead(viewholder.imageView, musicId);
                viewholder.count.setText(size + "首歌");

                break;
            }
            case 3: {
                String[] s = className.get(position).split("/");
                viewholder.title.setText(s[s.length - 1]);
                viewholder.imageView.setBackgroundResource(R.drawable.folder);
                viewholder.count.setText(list.size() + "首歌" + className.get(position));

            }
            break;
        }


        return convertView;
    }



    class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView count;

    }
}
