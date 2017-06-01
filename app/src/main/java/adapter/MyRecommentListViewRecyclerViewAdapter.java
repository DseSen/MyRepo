package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import fragment.PlayMusicItemFragment;
import jsonjavabean.HotSinger;
import jsonjavabean.NewAlbum;
import jsonjavabean.NewMusic;
import jsonjavabean.Songs;
import mymusicplayer.PlayActivity;
import mymusicplayer.RankSongsActivity;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.MusicUtils;

/**
 * Created by 啊丁 on 2017/4/1.
 */

public class MyRecommentListViewRecyclerViewAdapter extends RecyclerView.Adapter<MyRecommentListViewRecyclerViewAdapter.MyHolder> {
    private Context context;
    private List list;
    private int item_position;

    public MyRecommentListViewRecyclerViewAdapter(Context context, List list, int position) {
        this.context = context;
        this.list = list;
        this.item_position = position;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recomment_musiclist_listview_item_recyclerview_item, null);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        switch (item_position) {
            case 0: {
                final NewAlbum.PlazeAlbumListBean.RMBean.AlbumListBean.ListBean listBean = (NewAlbum.PlazeAlbumListBean.RMBean.AlbumListBean.ListBean) list.get(position);
                x.image().bind(holder.imageView, listBean.getPic_big());
                holder.textView.setText(listBean.getTitle());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String albumId = listBean.getAlbum_id();
                        RankSongsActivity.actionStart(context, ConstUtils.TYPE_NEWALUBM,albumId);
                    }
                });
            }
            break;
            case 1: {
                final NewMusic.SongListBean songListBean = (NewMusic.SongListBean) list.get(position);
                x.image().bind(holder.imageView, songListBean.getPic_big());
                holder.textView.setText(songListBean.getTitle());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String songId = songListBean.getSong_id();
                        RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songId);
                        requestParams.addQueryStringParameter("name", "value");
                        x.http().get(requestParams, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e("onSuccess: ", "网络成功");
                                Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                                }.getType());
                                String url = songs.getBitrate().getFile_link();
                                Intent intent = new Intent();
                                intent.setAction("playReceive");
                                intent.putExtra("play", ConstUtils.STATE_PLAY);
                                intent.putExtra("position", 0);
                                intent.putExtra("url", url);
                                List<NewMusic.SongListBean> songses = new ArrayList<NewMusic.SongListBean>();
                                songses.add(songListBean);
                                PlayMusicService.list = songses;
//                                if (PlayMusicService.playMod==ConstUtils.PLAYMOD_DISORDER){
//                                    List temp=new ArrayList();
//                                    int i=0;
//                                    while(temp.size() !=songses.size() ){
//
//                                        temp.add(songses.get(i));
//                                        i++;
//                                    }
//                                    PlayMusicService.randomList =MusicUtils.getRandomList(0,temp);
//
//                                }else{
                                    PlayMusicService.randomList =songses;
//                                }

                                PlayMusicService.type = ConstUtils.TYPE_NEWSONG;
                                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.main_framelaout_playitem, new PlayMusicItemFragment(), "inNewSongs").commit();
                                Log.e("onSuccess: ", "转换成功");
                                context.sendBroadcast(intent);
//                                PlayActivity.actionStart(context);
                                ((Activity)context).overridePendingTransition(R.anim.playenter,R.anim.noaction);


                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {

                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                });
            }
            break;
            case 2: {
                final HotSinger.ArtistBean artistBean = (HotSinger.ArtistBean) list.get(position);
                x.image().bind(holder.imageView, artistBean.getAvatar_big());
                holder.textView.setText(artistBean.getName());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String authorId=artistBean.getTing_uid();
                        Log.e("onClick:authorId ", authorId);
                        RankSongsActivity.actionStart(context,ConstUtils.TYPE_HOTSINGER,authorId);
                    }
                });
            }
            break;
            default:
                break;
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        View view;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recomment_musiclist_listview_item_recyclerview_image);
            textView = (TextView) itemView.findViewById(R.id.recomment_musiclist_listview_item_recyclerview_text);
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.height=ConstUtils.dip2px(context,150);
            view = itemView.findViewById(R.id.recomment_musiclist_listview_item_recyclerview_item);
        }
    }
}
