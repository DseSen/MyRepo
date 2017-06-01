package mymusicplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import adapter.MyRankSongsListViewAdapter;
import fragment.PlayMusicItemFragment;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jsonjavabean.AlbumSongs;
import jsonjavabean.LocalMusic;

import jsonjavabean.MusicListItem;
import jsonjavabean.RankSongs;
import jsonjavabean.SingerSongs;
import jsonjavabean.Songs;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.MusicUtils;

/**
 * Created by 啊丁 on 2017/3/29.
 */

public class RankSongsActivity extends AppCompatActivity {
    ListView listView;
    RankSongs rankSongs;
    AlbumSongs albumSongs;
    SingerSongs singerSongs;
    List<AlbumSongs.SonglistBean> songlistBeen;
    List<RankSongs.SongListBean> list;
    List<SingerSongs.SonglistBean> songlistBean;
    List<LocalMusic> localMusicList;
    List<MusicListItem> itemList;
    Toolbar toolbar;
    int type;
    String id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rank_songs);
        initState();
        init();


    }

    private void initState() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        type = intent.getIntExtra("type", 0);
        listView = (ListView) findViewById(R.id.rank_songs_listview);
        toolbar = (Toolbar) findViewById(R.id.songlist_toolbar);
        toolbar.getBackground().mutate().setAlpha(0);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    int h = getScrollY();//滚动距离
                    if (h > 550) {
                        toolbar.getBackground().mutate().setAlpha(255);

                    } else {
                        toolbar.getBackground().mutate().setAlpha(0);
                    }
                }
            }

            private int getScrollY() {
                int height = 0;
                for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                    ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
                    try {
                        height += itemRecod.height;
                    }catch (Exception e){

                    }
                }
                ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
                if (null == itemRecod) {
                    itemRecod = new ItemRecod();
                }
                return height - itemRecod.top;
            }

            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });

    }


    @Override
    protected void onResume() {
        if (PlayMusicService.list != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.rank_songs_framelayout, new PlayMusicItemFragment(), "inRankSongs").commit();
            listView.setPadding(0,0,0, ConstUtils.dip2px(this,50));
        }

        super.onResume();
    }


    public void init() {
        switch (type) {
            case ConstUtils.TYPE_RANK:
                setRankStyle();
                getRankSongs();
                break;
            case ConstUtils.TYPE_NEWALUBM:

                getNewAlbumSongs();
                break;
            case ConstUtils.TYPE_HOTSINGER:
                getSingerSongs();
                break;
            case ConstUtils.TYPE_LOCAL:
                getLocalSongs();
                break;
            case ConstUtils.TYPE_SERVERLIST:
                getMusicListItem();
                break;
        }
        toolbar.setNavigationIcon(R.drawable.lz);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick: ", "fanhui");
                finish();
            }
        });

    }

    private void setRankStyle() {
        View view = getLayoutInflater().inflate(R.layout.ranksongs_listview_head, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.ranksongs_listview_head_image);
        Log.e("init: ", id);
        switch (id) {
            case 1 + "":

                imageView.setBackgroundResource(R.drawable.newrank);
                getWindow().setStatusBarColor(getResources().getColor(R.color.xinge));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.xinge));
                toolbar.setTitle("新歌榜");
                break;
            case 2 + "":
                imageView.setBackgroundResource(R.drawable.regerank);
                getWindow().setStatusBarColor(getResources().getColor(R.color.hotrankcolor));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.hotrankcolor));
                toolbar.setTitle("热歌榜");
                break;
            case 21 + "":
                imageView.setBackgroundResource(R.drawable.oumei);
                getWindow().setStatusBarColor(getResources().getColor(R.color.oumei));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.oumei));
                toolbar.setTitle("欧美金曲榜");
                break;
            case 500 + "":
//                        linearLayout.setBackgroundResource(R.drawable.);
//                        getWindow().setStatusBarColor(getResources().getColor(R.color.hotrankcolor));
//                        listView.addHeaderView(view);
//                        toolbar.setBackgroundColor(getResources().getColor(R.color.hotrankcolor));
                toolbar.setTitle("全球原创音乐榜");
                break;
            case 100 + "":
                imageView.setBackgroundResource(R.drawable.king);
                getWindow().setStatusBarColor(getResources().getColor(R.color.king));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.king));
                toolbar.setTitle("King榜");
                break;
            case 200 + "":
                imageView.setBackgroundResource(R.drawable.yuanchuang);
                getWindow().setStatusBarColor(getResources().getColor(R.color.yuanchuang));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.yuanchuang));
                toolbar.setTitle("原创音乐榜");
                break;
            case 20 + "":
                imageView.setBackgroundResource(R.drawable.huayu);
                getWindow().setStatusBarColor(getResources().getColor(R.color.huayu));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.huayu));
                toolbar.setTitle("华语金曲榜");
                break;
            case 22 + "":
                imageView.setBackgroundResource(R.drawable.laoge);
                getWindow().setStatusBarColor(getResources().getColor(R.color.laoge));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.laoge));
                toolbar.setTitle("经典老歌榜");
                break;
            case 25 + "":
                imageView.setBackgroundResource(R.drawable.wangluo);
                getWindow().setStatusBarColor(getResources().getColor(R.color.wangluo));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.wangluo));
                toolbar.setTitle("网络歌曲榜");
                break;
            case 24 + "":
                imageView.setBackgroundResource(R.drawable.yingshi);
                getWindow().setStatusBarColor(getResources().getColor(R.color.yingshi));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.yingshi));
                toolbar.setTitle("影视金曲榜");
                break;
            case 23 + "":
                imageView.setBackgroundResource(R.drawable.qingge);
                getWindow().setStatusBarColor(getResources().getColor(R.color.qingge));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.qingge));
                toolbar.setTitle("情歌对唱榜");
                break;
            case 8 + "":
                imageView.setBackgroundResource(R.drawable.bill);
                getWindow().setStatusBarColor(getResources().getColor(R.color.billcolor));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.billcolor));
                toolbar.setTitle("Billboard");
                break;
            case 11 + "":
                imageView.setBackgroundResource(R.drawable.yaogun);
                getWindow().setStatusBarColor(getResources().getColor(R.color.yaogun));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.yaogun));
                toolbar.setTitle("摇滚榜");
                break;
            case 7 + "":
                imageView.setBackgroundResource(R.drawable.chizha);
                getWindow().setStatusBarColor(getResources().getColor(R.color.chizha));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.chizha));
                toolbar.setTitle("叱咤歌曲榜");
                break;
            case 105 + "":
                imageView.setBackgroundResource(R.drawable.haotongxing);
                getWindow().setStatusBarColor(getResources().getColor(R.color.haotongxing));
                listView.addHeaderView(view,null,false);
                toolbar.setBackgroundColor(getResources().getColor(R.color.haotongxing));
                toolbar.setTitle("好童星榜");
                break;


        }
    }

    private void getMusicListItem() {
        RequestParams requestParams = new RequestParams("http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/getAllItemByListId.do");
        requestParams.addBodyParameter("listId", id);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                itemList = new Gson().fromJson(result, new TypeToken<List<MusicListItem>>() {
                }.getType());
                MyRankSongsListViewAdapter adapter = new MyRankSongsListViewAdapter(itemList, RankSongsActivity.this, ConstUtils.TYPE_SERVERLIST);
                listView.setAdapter(adapter);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String idOrLink = itemList.get(position).getUrl();
                if (idOrLink.length() > 50) {
                    String url = idOrLink;
                    Intent intent = new Intent();
                    intent.setAction("playReceive");
                    intent.putExtra("play", ConstUtils.STATE_PLAY);
                    intent.putExtra("position", position);
                    intent.putExtra("url", url);
                    PlayMusicService.randomList = itemList;
                    if (PlayMusicService.playMod==ConstUtils.PLAYMOD_DISORDER){
                        List temp=new ArrayList();
                        int i=0;
                        while(temp.size() !=itemList.size() ){

                            temp.add(itemList.get(i));
                            i++;
                        }
                        PlayMusicService.list =MusicUtils.getRandomList(temp);
                        intent.putExtra("position", MusicUtils.getRandomPosition(position-1));
                    }else{
                        PlayMusicService.list =itemList;
                        intent.putExtra("position", position-1);
                    }
                    PlayMusicService.type = ConstUtils.TYPE_SERVERLIST;
                    getSupportFragmentManager().beginTransaction().replace(R.id.rank_songs_framelayout, new PlayMusicItemFragment(), "inRankSongs").commit();
                    sendBroadcast(intent);
                } else {
                    RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + idOrLink);
                    requestParams.addQueryStringParameter("name", "value");
                    x.http().get(requestParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                            }.getType());
                            String url = songs.getBitrate().getFile_link();
                            Intent intent = new Intent();
                            intent.setAction("playReceive");
                            intent.putExtra("play", ConstUtils.STATE_PLAY);
                            intent.putExtra("position", position);
                            intent.putExtra("url", url);
                            PlayMusicService.randomList = itemList;
                            if (PlayMusicService.playMod==ConstUtils.PLAYMOD_DISORDER){
                                List temp=new ArrayList();
                                int i=0;
                                while(temp.size() !=itemList.size() ){

                                    temp.add(itemList.get(i));
                                    i++;
                                }
                                PlayMusicService.list =MusicUtils.getRandomList(temp);
                                intent.putExtra("position", MusicUtils.getRandomPosition(position-1));
                            }else{
                                PlayMusicService.list =itemList;
                                intent.putExtra("position", position-1);
                            }
                            PlayMusicService.type = ConstUtils.TYPE_SERVERLIST;
                            getSupportFragmentManager().beginTransaction().replace(R.id.rank_songs_framelayout, new PlayMusicItemFragment(), "inRankSongs").commit();
                            sendBroadcast(intent);
//                            PlayActivity.actionStart(RankSongsActivity.this);
                            overridePendingTransition(R.anim.playenter,R.anim.noaction);
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
            }
        });
    }


    private void getLocalSongs() {
        localMusicList= getIntent().getParcelableArrayListExtra("localMusic");
//        localMusicList = MusicUtils.getMusicData(RankSongsActivity.this);
        toolbar.setTitle(id);
        toolbar.getBackground().mutate().setAlpha(255);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW,toolbar.getId());
        listView.setAdapter(new MyRankSongsListViewAdapter(localMusicList, RankSongsActivity.this, ConstUtils.TYPE_LOCAL));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                PlayMusicService.randomList = localMusicList;
                if (PlayMusicService.playMod==ConstUtils.PLAYMOD_DISORDER){
                    List temp=new ArrayList();
                    int i=0;
                    while(temp.size() !=localMusicList.size() ){

                        temp.add(localMusicList.get(i));
                        i++;
                    }
                    PlayMusicService.list =MusicUtils.getRandomList(temp);
                    intent.putExtra("position", MusicUtils.getRandomPosition(position-1));
                }else{
                    PlayMusicService.list =localMusicList;
                    intent.putExtra("position", position-1);
                }
                String url = localMusicList.get(position).getFile_link();

                intent.setAction("playReceive");
                intent.putExtra("play", ConstUtils.STATE_PLAY);
                intent.putExtra("url", url);
                PlayMusicService.type = ConstUtils.TYPE_LOCAL;
                getSupportFragmentManager().beginTransaction().replace(R.id.rank_songs_framelayout, new PlayMusicItemFragment(), "inRankSongs").commit();
                sendBroadcast(intent);
//                PlayActivity.actionStart(RankSongsActivity.this);
                overridePendingTransition(R.anim.playenter,R.anim.noaction);

            }
        });
    }

    private void getNewAlbumSongs() {

        listView = (ListView) findViewById(R.id.rank_songs_listview);

        RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.album.getAlbumInfo&format=json&album_id=" + id);
        requestParams.addQueryStringParameter("name", "value");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                albumSongs = new Gson().fromJson(result, new TypeToken<AlbumSongs>() {
                }.getType());
                songlistBeen = albumSongs.getSonglist();
               View view= setHeader(albumSongs.getAlbumInfo().getPic_big());
//                Glide.with(RankSongsActivity.this).load(albumSongs.getAlbumInfo().getPic_s1000()).bitmapTransform(new BlurTransformation(RankSongsActivity.this, 25,1)).crossFade(1000).into(imageView);
                listView.addHeaderView(view,null,false);
                toolbar.setTitle(albumSongs.getAlbumInfo().getTitle());
                MyRankSongsListViewAdapter adapter = new MyRankSongsListViewAdapter(songlistBeen, RankSongsActivity.this, ConstUtils.TYPE_NEWALUBM);
                listView.setAdapter(adapter);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String songId = songlistBeen.get(position-1).getSong_id();
                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songId);
                requestParams.addQueryStringParameter("name", "value");
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                        }.getType());
                        String url = songs.getBitrate().getFile_link();
                        Intent intent = new Intent();
                        intent.setAction("playReceive");
                        intent.putExtra("play", ConstUtils.STATE_PLAY);
                        intent.putExtra("position", position-1);
                        intent.putExtra("url", url);
                        PlayMusicService.randomList = songlistBeen;
                        if (PlayMusicService.playMod==ConstUtils.PLAYMOD_DISORDER){
                            List temp=new ArrayList();
                            int i=0;
                            while(temp.size() !=songlistBeen.size() ){

                                temp.add(songlistBeen.get(i));
                                i++;
                            }
                            PlayMusicService.list =MusicUtils.getRandomList(temp);
                            intent.putExtra("position", MusicUtils.getRandomPosition(position-1));
                        }else{
                            PlayMusicService.list =songlistBeen;
                            intent.putExtra("position", position-1);
                        }
                        PlayMusicService.type = ConstUtils.TYPE_NEWALUBM;
                        getSupportFragmentManager().beginTransaction().replace(R.id.rank_songs_framelayout, new PlayMusicItemFragment(), "inRankSongs").commit();
                        sendBroadcast(intent);
                        Log.e("MYTAG", "test---------");
//                        PlayActivity.actionStart(RankSongsActivity.this);
                        overridePendingTransition(R.anim.playenter,R.anim.noaction);

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

    private void getRankSongs() {
        RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=ppzs&operator=0&method=baidu.ting.billboard.billList&format=json&type=" + id + "&offset=0&size=100&fields=song_id%2Ctitle%2Cauthor%2Calbum_title%2Cpic_big%2Cpic_small%2Chavehigh%2Call_rate%2Ccharge%2Chas_mv_mobile%2Clearn%2Csong_source%2Ckorean_bb_song");
        requestParams.addQueryStringParameter("name", "value");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                rankSongs = new Gson().fromJson(result, new TypeToken<RankSongs>() {
                }.getType());
                list = rankSongs.getSong_list();
                MyRankSongsListViewAdapter adapter = new MyRankSongsListViewAdapter(list, RankSongsActivity.this, ConstUtils.TYPE_RANK);
                listView.setAdapter(adapter);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String songId = rankSongs.getSong_list().get(position-1).getSong_id();
                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songId);
                requestParams.addQueryStringParameter("name", "value");
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                        }.getType());
                        String url = songs.getBitrate().getFile_link();
                        Intent intent = new Intent();
                        intent.setAction("playReceive");
                        intent.putExtra("play", ConstUtils.STATE_PLAY);
//                        intent.putExtra("position", position-1);
                        intent.putExtra("url", url);
                        PlayMusicService.randomList = list;
                        if (PlayMusicService.playMod==ConstUtils.PLAYMOD_DISORDER){
                            List temp=new ArrayList();
                            int i=0;
                            while(temp.size() !=list.size() ){

                                temp.add(list.get(i));
                                i++;
                            }
                            PlayMusicService.list =MusicUtils.getRandomList(temp);
                            intent.putExtra("position", MusicUtils.getRandomPosition(position-1));
                        }else{
                            PlayMusicService.list =list;
                            intent.putExtra("position", position-1);
                        }
                        PlayMusicService.type = ConstUtils.TYPE_RANK;
                        getSupportFragmentManager().beginTransaction().replace(R.id.rank_songs_framelayout, new PlayMusicItemFragment(), "inRankSongs").commit();
                        sendBroadcast(intent);
//                        PlayActivity.actionStart(RankSongsActivity.this);
                        overridePendingTransition(R.anim.playenter,R.anim.noaction);
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

    private void getSingerSongs() {
        toolbar.getBackground().mutate().setAlpha(255);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW,toolbar.getId());
        RequestParams req = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.9.1&channel=ppzs&operator=0&method=baidu.ting.artist.getSongList&format=json&order=2&tinguid=" + id + "&artistid=" + id + "&offset=0&limits=50");
        req.addQueryStringParameter("name", "value");
        x.http().get(req, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                singerSongs = new Gson().fromJson(result, new TypeToken<SingerSongs>() {
                }.getType());
                songlistBean = singerSongs.getSonglist();
                toolbar.setTitle(singerSongs.getSonglist().get(0).getAuthor());
                MyRankSongsListViewAdapter adapter = new MyRankSongsListViewAdapter(songlistBean, RankSongsActivity.this, ConstUtils.TYPE_HOTSINGER);
                listView.setAdapter(adapter);

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String songId = songlistBean.get(position).getSong_id();
                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songId);
                requestParams.addQueryStringParameter("name", "value");
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                        }.getType());
                        String url = songs.getBitrate().getFile_link();
                        Intent intent = new Intent();
                        intent.setAction("playReceive");
                        intent.putExtra("play", ConstUtils.STATE_PLAY);
                        intent.putExtra("position", position);
                        intent.putExtra("url", url);
                        PlayMusicService.randomList = songlistBean;
                        if (PlayMusicService.playMod==ConstUtils.PLAYMOD_DISORDER){
                            List temp=new ArrayList();
                            int i=0;
                            while(temp.size() !=songlistBean.size() ){

                                temp.add(songlistBean.get(i));
                                i++;
                            }
                            PlayMusicService.list =MusicUtils.getRandomList(temp);
                            intent.putExtra("position", MusicUtils.getRandomPosition(position-1));
                        }else{
                            PlayMusicService.list =songlistBean;
                            intent.putExtra("position", position-1);
                        }
                        PlayMusicService.type = ConstUtils.TYPE_HOTSINGER;
                        getSupportFragmentManager().beginTransaction().replace(R.id.rank_songs_framelayout, new PlayMusicItemFragment(), "inRankSongs").commit();
                        sendBroadcast(intent);
//                        PlayActivity.actionStart(RankSongsActivity.this);
                        overridePendingTransition(R.anim.playenter,R.anim.noaction);

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

    public static void actionStart(Context context, int type, String id) {
        Intent intent = new Intent(context, RankSongsActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
    public static void actionStart(Context context, int type, String id,ArrayList<LocalMusic> localMusics) {
        Intent intent = new Intent(context, RankSongsActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        intent.putParcelableArrayListExtra("localMusic",localMusics);
        context.startActivity(intent);
    }
    private View setHeader(final String pic_url){
        View view = getLayoutInflater().inflate(R.layout.album_listview_head,null);
        final FrameLayout frameLayout= (FrameLayout) view.findViewById(R.id.album_listview_head);
        final ImageView imageView = (ImageView) view.findViewById(R.id.album_listview_head_image);
        new Thread() {
            @Override
            public void run() {
                super.run();
                ImageLoader.getInstance().loadImageSync(pic_url);
                ImageLoader.getInstance().loadImage(pic_url, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, final View view, final Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                frameLayout.setBackground(ConstUtils.getForegroundDrawable(loadedImage, RankSongsActivity.this));
                                imageView.setImageBitmap(loadedImage);
                            }
                        });
                    }
                });

            }
        }.start();
        return view;
    }
}
