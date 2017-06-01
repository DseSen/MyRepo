package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import fragment.PlayMusicItemFragment;
import jsonjavabean.AlbumSongs;
import jsonjavabean.DownLoadBean;
import jsonjavabean.LocalMusic;
import jsonjavabean.MusicListItem;
import jsonjavabean.NewMusic;
import jsonjavabean.RankSongs;
import jsonjavabean.SearchSongs;
import jsonjavabean.SingerSongs;
import jsonjavabean.Songs;
import mymusicplayer.PlayActivity;
import service.PlayMusicService;
import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/3/31.
 */

public class MyPlayItemViewPagerAdapter extends PagerAdapter {
    private List songListBeens;
    private Context context;
    private int type;


    public MyPlayItemViewPagerAdapter(List songListBeens, Context context,int type) {
        this.songListBeens = songListBeens;
        this.context = context;
        this.type=type;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            container.removeView((View) object);

        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
         View v = LayoutInflater.from(context).inflate(R.layout.playmusicitem_viewpager_slide, container,false);
        v.setBackgroundResource(R.drawable.playitemclick);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.actionStart(context);
                ((Activity)context).overridePendingTransition(R.anim.playenter,R.anim.noaction);
            }
        });
        final ImageView imageView= (ImageView) v.findViewById(R.id.playmusicitem_viewpager_slide_image);
        final TextView name= (TextView) v.findViewById(R.id.playmusicitem_viewpager_slide_text1);
        final TextView author= (TextView) v.findViewById(R.id.playmusicitem_viewpager_slide_text2);
       switch (type){
            case ConstUtils.TYPE_RANK:{
                RankSongs.SongListBean songListBean= (RankSongs.SongListBean) songListBeens.get(position%songListBeens.size());
                x.image().bind(imageView,songListBean.getPic_small());
                name.setText(songListBean.getTitle());
                author.setText(songListBean.getAuthor());}
            break;
            case ConstUtils.TYPE_NEWALUBM:{
                AlbumSongs.SonglistBean songlistBean= (AlbumSongs.SonglistBean) songListBeens.get(position%songListBeens.size());
                x.image().bind(imageView,songlistBean.getPic_small());
                name.setText(songlistBean.getTitle());
                author.setText(songlistBean.getAuthor());}
                break;
            case ConstUtils.TYPE_NEWSONG:{
                NewMusic.SongListBean songlistBean= (NewMusic.SongListBean) songListBeens.get(position%songListBeens.size());
                x.image().bind(imageView,songlistBean.getPic_small());
                name.setText(songlistBean.getTitle());
                author.setText(songlistBean.getAuthor());
                break;
            }
            case ConstUtils.TYPE_SEARCHSONGS:{

                Songs songs= (Songs) songListBeens.get(position%songListBeens.size());
                x.image().bind(imageView,songs.getSonginfo().getPic_small());
                name.setText(songs.getSonginfo().getTitle());
                author.setText(songs.getSonginfo().getAuthor());
//
                break;
            }
           case ConstUtils.TYPE_DOWNLOADSONGS:{

               DownLoadBean songs= (DownLoadBean) songListBeens.get(position%songListBeens.size());
               imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.nopic));
               name.setText(songs.getName());
               author.setText(songs.getAuthor());
//
               break;
           }
            case ConstUtils.TYPE_HOTSINGER:
            {
                SingerSongs.SonglistBean songlistBean= (SingerSongs.SonglistBean) songListBeens.get(position%songListBeens.size());
                x.image().bind(imageView,songlistBean.getPic_small());
                name.setText(songlistBean.getTitle());
                author.setText(songlistBean.getAuthor());

            }
            break;
           case ConstUtils.TYPE_LOCAL:
           {
               LocalMusic songlistBean= (LocalMusic) songListBeens.get(position%songListBeens.size());
               imageView.setBackgroundResource(R.mipmap.ic_launcher);
               name.setText(songlistBean.getTitle());
               author.setText(songlistBean.getAuthor());

           }
           break;
           case ConstUtils.TYPE_SERVERLIST:
           {
               MusicListItem songlistBean= (MusicListItem) songListBeens.get(position%songListBeens.size());
               if (songlistBean.getPic()==null){
                   imageView.setBackgroundResource(R.mipmap.ic_launcher);
               }else{
                   x.image().bind(imageView,songlistBean.getPic());
               }
               name.setText(songlistBean.getTitle());
               author.setText(songlistBean.getAuthor());

           }
           break;
        }
        container.addView(v);
        return v;
    }
}
