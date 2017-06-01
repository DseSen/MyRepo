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

import jsonjavabean.AlbumSongs;
import jsonjavabean.DownLoadBean;
import jsonjavabean.HotSinger;
import jsonjavabean.LocalMusic;
import jsonjavabean.MusicListItem;
import jsonjavabean.RankSongs;
import jsonjavabean.SearchSongs;
import jsonjavabean.SingerSongs;
import utils.ConstUtils;
import utils.MusicUtils;

/**
 * Created by 啊丁 on 2017/3/29.
 */

public class MyRankSongsListViewAdapter extends BaseAdapter {
    private List songListBeens;
    private Context context;
    private int type;

    public MyRankSongsListViewAdapter(List songListBeens, Context context, int type) {
        this.songListBeens = songListBeens;
        this.context = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        return songListBeens.size();
    }

    @Override
    public Object getItem(int position) {
        return songListBeens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.rank_songs_listview_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.rank_songs_imageview);
            holder.textView1 = (TextView) convertView.findViewById(R.id.rank_songs_textview_name);
            holder.textView2 = (TextView) convertView.findViewById(R.id.rank_songs_textview_author);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case ConstUtils.TYPE_RANK: {
                RankSongs.SongListBean songListBean = (RankSongs.SongListBean) songListBeens.get(position);
                x.image().bind(holder.imageView, songListBean.getPic_big());
                holder.textView1.setText(songListBean.getTitle());
                holder.textView2.setText(songListBean.getAuthor());
            }
            break;
            case ConstUtils.TYPE_NEWALUBM: {
                AlbumSongs.SonglistBean albumSongs = (AlbumSongs.SonglistBean) songListBeens.get(position);
                x.image().bind(holder.imageView, albumSongs.getPic_big());
                holder.textView1.setText(albumSongs.getTitle());
                holder.textView2.setText(albumSongs.getAuthor());

            }
            break;
            case ConstUtils.TYPE_HOTSINGER: {
                SingerSongs.SonglistBean songlistBean = (SingerSongs.SonglistBean) songListBeens.get(position);
                x.image().bind(holder.imageView, songlistBean.getPic_big());
                holder.textView1.setText(songlistBean.getTitle());
                holder.textView2.setText(songlistBean.getAuthor());

            }
            break;
            case ConstUtils.TYPE_SEARCHSONGS: {
                SearchSongs.SongListBean songlistBean = (SearchSongs.SongListBean) songListBeens.get(position);
                ViewGroup.LayoutParams layoutParams=holder.imageView.getLayoutParams();
                layoutParams.height=0;
                layoutParams.width=0;
                holder.imageView.setLayoutParams(layoutParams);
                holder.textView1.setText(songlistBean.getTitle().replace("<em>","").replace("</em>",""));
                holder.textView2.setText(songlistBean.getAuthor().replace("<em>","").replace("</em>",""));

            }
            break;
            case ConstUtils.TYPE_DOWNLOADSONGS: {
                DownLoadBean downLoadBean= (DownLoadBean) songListBeens.get(position);
                ViewGroup.LayoutParams layoutParams=holder.imageView.getLayoutParams();
                layoutParams.height=0;
                layoutParams.width=0;
                holder.imageView.setLayoutParams(layoutParams);
                holder.textView1.setText(downLoadBean.getName());
                holder.textView2.setText(downLoadBean.getAuthor());

            }
            break;
            case ConstUtils.TYPE_LOCAL: {
                LocalMusic songlistBean = (LocalMusic) songListBeens.get(position);
                MusicUtils.setHead(holder.imageView,songlistBean.getMusicId());
                holder.textView1.setText(songlistBean.getTitle());
                holder.textView2.setText(songlistBean.getAuthor());

            }
            break;
            case ConstUtils.TYPE_SERVERLIST: {
                MusicListItem songlistBean = (MusicListItem) songListBeens.get(position);
                x.image().bind(holder.imageView,songlistBean.getPic());
                holder.textView1.setText(songlistBean.getTitle());
                holder.textView2.setText(songlistBean.getAuthor());

            }
            break;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
    }
}
