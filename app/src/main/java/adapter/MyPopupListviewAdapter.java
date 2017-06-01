package adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

import jsonjavabean.AlbumSongs;
import jsonjavabean.DownLoadBean;
import jsonjavabean.HotSinger;
import jsonjavabean.LocalMusic;
import jsonjavabean.MusicListItem;
import jsonjavabean.NewMusic;
import jsonjavabean.RankSongs;
import jsonjavabean.SearchSongs;
import jsonjavabean.SingerSongs;
import jsonjavabean.Songs;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.MusicUtils;

/**
 * Created by 啊丁 on 2017/4/5.
 */

public class MyPopupListviewAdapter extends BaseAdapter {
    List list;
    Context context;
    int type;

    public MyPopupListviewAdapter(List list, Context context,int type) {

        this.list = list;
        this.context = context;
        this.type=type;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.popup_musiclist_listview_item,parent,false);
            holder.name= (TextView) convertView.findViewById(R.id.popup_musiclist_listview_name);
            holder.author= (TextView) convertView.findViewById(R.id.popup_musiclist_listview_author);
            holder.delete= (ImageView) convertView.findViewById(R.id.popup_musiclist_listview_delete);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        switch (type){
            case ConstUtils.TYPE_RANK:{
                RankSongs.SongListBean songListBean= (RankSongs.SongListBean) list.get(position);
                holder.name.setText(songListBean.getTitle());
                holder.author.setText(songListBean.getAuthor());}
                break;
            case ConstUtils.TYPE_NEWALUBM: {
                AlbumSongs.SonglistBean songlistBean = (AlbumSongs.SonglistBean) list.get(position);
                holder.name.setText(songlistBean.getTitle());
                holder.author.setText(songlistBean.getAuthor());
                break;
            }
            case ConstUtils.TYPE_NEWSONG: {
                NewMusic.SongListBean songlistBean = (NewMusic.SongListBean) list.get(position);
                holder.name.setText(songlistBean.getTitle());
                holder.author.setText(songlistBean.getAuthor());
                break;
            }
            case ConstUtils.TYPE_SEARCHSONGS: {
                Songs songs = (Songs) list.get(position);
                holder.name.setText(songs.getSonginfo().getTitle());
                holder.author.setText(songs.getSonginfo().getAuthor());
                break;
            }
            case ConstUtils.TYPE_HOTSINGER: {
                SingerSongs.SonglistBean songlistBean = (SingerSongs.SonglistBean) list.get(position);
                holder.name.setText(songlistBean.getTitle());
                holder.author.setText(songlistBean.getAuthor());
                break;
            }
            case ConstUtils.TYPE_LOCAL: {
                LocalMusic songlistBean = (LocalMusic) list.get(position);
                holder.name.setText(songlistBean.getTitle());
                holder.author.setText(songlistBean.getAuthor());
                break;
            }
            case ConstUtils.TYPE_DOWNLOADSONGS :{
                DownLoadBean songlistBean = (DownLoadBean) list.get(position);
                holder.name.setText(songlistBean.getName());
                holder.author.setText(songlistBean.getAuthor());
                break;
            }
            case ConstUtils.TYPE_SERVERLIST: {
                MusicListItem songlistBean = (MusicListItem) list.get(position);
                holder.name.setText(songlistBean.getTitle());
                holder.author.setText(songlistBean.getAuthor());
                break;
            }
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e( "onClick: ","删除了" );
                if (PlayMusicService.list.size()==0){
                    return;
                }
                if (PlayMusicService.list!=PlayMusicService.randomList){
                    PlayMusicService.list.remove(MusicUtils.getSectionPosition(position));
                }

                PlayMusicService.randomList.remove(position);

                MyPopupListviewAdapter.this.notifyDataSetChanged();
                if (MusicUtils.getSectionPosition(position)==PlayMusicService.position){
                    Intent intent1 = new Intent();
                    intent1.setAction("changeView");
                    intent1.putExtra("state", ConstUtils.STATE_NEXT);
                    context.sendBroadcast(intent1);
                    context.sendBroadcast(new Intent("notifyReceiverNext"));
                }else if (position<PlayMusicService.position){
                    PlayMusicService.position-=1;
                }

                context.sendBroadcast(new Intent("notifyViewPager"));
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView name;
        TextView author;
        ImageView delete;
    }
}
