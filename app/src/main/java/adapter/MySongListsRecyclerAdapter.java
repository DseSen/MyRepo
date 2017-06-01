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
import jsonjavabean.MvBeans;
import jsonjavabean.MvLists;
import jsonjavabean.NewAlbum;
import jsonjavabean.NewMusic;
import jsonjavabean.Songs;
import mymusicplayer.MvPlayActivity;
import mymusicplayer.PlayActivity;
import mymusicplayer.RankSongsActivity;
import service.PlayMusicService;
import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/4/18.
 */

public class MySongListsRecyclerAdapter extends RecyclerView.Adapter<MySongListsRecyclerAdapter.ViewHolder> {
    Context context;
    List list;
    int type;

    public MySongListsRecyclerAdapter(Context context, List list,int type) {
        this.context = context;
        this.list = list;
        this.type=type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.recomment_musiclist_listview_item_recyclerview_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (type){
            case ConstUtils.TYPE_NEWALUBM:{
                final NewAlbum.PlazeAlbumListBean.RMBean.AlbumListBean.ListBean listBean= (NewAlbum.PlazeAlbumListBean.RMBean.AlbumListBean.ListBean) list.get(position);
                x.image().bind(holder.imageView,listBean.getPic_big());
                holder.textView.setText(listBean.getTitle());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String albumId = listBean.getAlbum_id();
                        RankSongsActivity.actionStart(context, ConstUtils.TYPE_NEWALUBM,albumId);
                    }
                });
                break;
            }
            case ConstUtils.TYPE_HOTSINGER:{
                final HotSinger.ArtistBean listBean= (HotSinger.ArtistBean) list.get(position);
                x.image().bind(holder.imageView,listBean.getAvatar_big());
                holder.textView.setText(listBean.getName());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String authorId=listBean.getTing_uid();
                        Log.e("onClick:authorId ", authorId);
                        RankSongsActivity.actionStart(context,ConstUtils.TYPE_HOTSINGER,authorId);
                    }
                });
                break;
            }
            case ConstUtils.TYPE_NEWSONG:{
                Log.e( "onBindViewHolder: ", "TYPE_NEWSONG");
                final NewMusic.SongListBean listBean= (NewMusic.SongListBean) list.get(position);
                x.image().bind(holder.imageView,listBean.getAvatar_big());
                holder.textView.setText(listBean.getTitle());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String songId = listBean.getSong_id();
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
                                songses.add(listBean);
                                PlayMusicService.list = songses;
                                PlayMusicService.type = ConstUtils.TYPE_NEWSONG;
                                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.activity_recomment_framelayout, new PlayMusicItemFragment(), "inRecommentSongs").commit();
                                Log.e("onSuccess: ", "转换成功");
                                context.sendBroadcast(intent);
                                PlayActivity.actionStart(context);
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
                break;
            }
            case ConstUtils.TYPE_SONGLISTS:{
                final MvLists.ResultBean.MvListBean diyInfoBean= (MvLists.ResultBean.MvListBean) list.get(position);
                x.image().bind(holder.imageView,diyInfoBean.getThumbnail());
                holder.textView.setText(diyInfoBean.getTitle());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mvID = diyInfoBean.getMv_id();
                        Log.e("onClick: ", mvID);
                        RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.9.1&channel=ppzs&operator=0&provider=11%2C12&method=baidu.ting.mv.playMV&format=json&mv_id="+mvID+"&song_id=&definition=0");
                        x.http().get(requestParams, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e( "onSuccess: ","请求成功" );
                                MvBeans mvBeans=new Gson().fromJson(result, new TypeToken<MvBeans>() {
                                }.getType());
                                String url=mvBeans.getResult().getFiles().get_$31().getFile_link();
                                MvPlayActivity.actionStart(context,url);
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
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recomment_musiclist_listview_item_recyclerview_image);
            textView = (TextView) itemView.findViewById(R.id.recomment_musiclist_listview_item_recyclerview_text);
            view = itemView.findViewById(R.id.recomment_musiclist_listview_item_recyclerview_item);
        }
    }
}
