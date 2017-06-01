package mymusicplayer;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.MyDialogListviewAdapter;
import adapter.MyPlayActivityViewPagerAdapter;
import adapter.MyPlayItemViewPagerAdapter;
import adapter.MyPopupListviewAdapter;
import jsonjavabean.AlbumSongs;
import jsonjavabean.DownLoadBean;
import jsonjavabean.LocalMusic;
import jsonjavabean.MusicList;
import jsonjavabean.MusicListItem;
import jsonjavabean.NewMusic;
import jsonjavabean.RankSongs;
import jsonjavabean.SingerSongs;
import jsonjavabean.Songs;
import me.wcy.lrcview.LrcView;
import service.DownloadService;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.MusicUtils;


/**
 * Created by 啊丁 on 2017/4/14.
 */

public class PlayActivity extends AppCompatActivity {
    Toolbar mToolbar;
    ViewPager viewPager;
    TextView currentTime, allTime;
    ImageView playOrPause, last, next,ic_list,ic_playMode;
    SeekBar seekBar;
    Button btn_loop;
    boolean isTouch;
    RelativeLayout rootRelativeLayout;
    PlayMainReceiver playMainReceiver;
    MyPlayActivityViewPagerAdapter myPlayActivityViewPagerAdapter;
    LrcView lrcView;
    Handler handler = new Handler();
    ListView listView,dialogListView;
    Dialog dialog;
    MyPopupListviewAdapter listviewAdapter;
    EditText et_addList;
    List<Map<String,Object>> dialogList=new ArrayList<Map<String, Object>>();
    MyDialogListviewAdapter myDialogListviewAdapter;
    int position;
    PopupWindow popupWindow;
    @ViewInject(R.id.myView)
    View myView;
    ObjectAnimator mRotateAnimation;
    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (PlayMusicService.mediaPlayer != null) {
                handler.postDelayed(r, 500);
                seekBar.setMax(PlayMusicService.mediaPlayer.getDuration());
                seekBar.setProgress(PlayMusicService.mediaPlayer.getCurrentPosition());
                currentTime.setText(MusicUtils.formatTime(PlayMusicService.mediaPlayer.getCurrentPosition()));
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        handler.post(r);
        initView();
        initState();
        initListener();
        initReceiver();

//        linearLayout.setBackground(ConstUtils.getForegroundDrawable(R.drawable.haotongxing,PlayActivity.this));
    }

    private void initView() {

        rootRelativeLayout = (RelativeLayout) findViewById(R.id.activity_play);
        viewPager = (ViewPager) findViewById(R.id.activity_play_viewpager);
        myPlayActivityViewPagerAdapter = new MyPlayActivityViewPagerAdapter(PlayMusicService.list, PlayActivity.this, PlayMusicService.type);
        viewPager.setAdapter(myPlayActivityViewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(PlayMusicService.position + PlayMusicService.list.size() * 10);
        RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.activity_play);
        mToolbar = (Toolbar) findViewById(R.id.activity_play_toolBar);
        mToolbar.setNavigationIcon(R.drawable.lz);
        mToolbar.setTitle("歌名");
        mToolbar.setSubtitle("作者");
        setSupportActionBar(mToolbar);
        currentTime = (TextView) findViewById(R.id.activity_play_tvCurrentTime);
        allTime = (TextView) findViewById(R.id.activity_play_tvTotalTime);
        playOrPause = (ImageView) findViewById(R.id.activity_play_playOrPause);
        last = (ImageView) findViewById(R.id.activity_play_last);
        next = (ImageView) findViewById(R.id.activity_play_next);
        ic_list= (ImageView) findViewById(R.id.activity_play_list);
        seekBar = (SeekBar) findViewById(R.id.activity_play_musicSeekBar);
        ic_playMode= (ImageView) findViewById(R.id.activity_play_mode);
        lrcView = (LrcView) findViewById(R.id.activity_play_lrc);
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    if (PlayMusicService.mediaPlayer.isPlaying()) {
                        lrcView.updateTime(seekBar.getProgress());
                    }
                }
            }
        }.start();
    }

    private void initState() {

        mRotateAnimation = ObjectAnimator.ofFloat(viewPager.findViewWithTag(viewPager.getCurrentItem()), "rotation", 0, 359);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(25 * 1000);
        mRotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        mRotateAnimation.start();

        if (PlayMusicService.mediaPlayer != null && PlayMusicService.play_flag == true) {
            playOrPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause));
            allTime.setText(MusicUtils.formatTime(PlayMusicService.mediaPlayer.getDuration()));
            seekBar.setMax(PlayMusicService.mediaPlayer.getDuration());
        } else if (PlayMusicService.mediaPlayer == null || PlayMusicService.play_flag == false) {
            playOrPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
        }

        switch (PlayMusicService.playMod){
            case ConstUtils.PLAYMOD_SEQUENCE:
                ic_playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_mode_shunxu));
                break;
            case ConstUtils.PLAYMOD_SINGLE:
                ic_playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_mode_single));
                break;
            case ConstUtils.PLAYMOD_DISORDER:
                ic_playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_mode_disorder));
                break;
        }

        switch (PlayMusicService.type) {
            case ConstUtils.TYPE_RANK: {
                RankSongs.SongListBean songListBean = (RankSongs.SongListBean) PlayMusicService.list.get(PlayMusicService.position);
                String pic_url = songListBean.getPic_big();
                setBackGround(pic_url);
                mToolbar.setSubtitle(songListBean.getAuthor());
                mToolbar.setTitle(songListBean.getTitle());
                showLrc(songListBean.getLrclink());
            }
            break;
            case ConstUtils.TYPE_NEWALUBM: {
                AlbumSongs.SonglistBean albumSongs = (AlbumSongs.SonglistBean) PlayMusicService.list.get(PlayMusicService.position);
                String pic_url = albumSongs.getPic_big();
                setBackGround(pic_url);
                mToolbar.setSubtitle(albumSongs.getAuthor());
                mToolbar.setTitle(albumSongs.getTitle());
                showLrc(albumSongs.getLrclink());
                break;
            }
            case ConstUtils.TYPE_NEWSONG: {
                NewMusic.SongListBean songs = (NewMusic.SongListBean) PlayMusicService.list.get(PlayMusicService.position);
                String pic_url = songs.getPic_big();
                setBackGround(pic_url);
                mToolbar.setSubtitle(songs.getAuthor());
                mToolbar.setTitle(songs.getTitle());
                showLrc(songs.getLrclink());
                break;
            }
            case ConstUtils.TYPE_SEARCHSONGS: {
                Songs songs = (Songs) PlayMusicService.list.get(PlayMusicService.position);
                String pic_url = songs.getSonginfo().getPic_big();
                setBackGround(pic_url);
                mToolbar.setSubtitle(songs.getSonginfo().getAuthor());
                mToolbar.setTitle(songs.getSonginfo().getTitle());
                showLrc(songs.getSonginfo().getLrclink());
                break;
            }
            case ConstUtils.TYPE_DOWNLOADSONGS:{

                DownLoadBean songs= (DownLoadBean) PlayMusicService.list.get(PlayMusicService.position);
               setBackGround(null);
                mToolbar.setSubtitle(songs.getAuthor());
                mToolbar.setTitle(songs.getName());
                showLrc(null);
                break;
            }
            case ConstUtils.TYPE_HOTSINGER: {
                SingerSongs.SonglistBean songlistBean = (SingerSongs.SonglistBean) PlayMusicService.list.get(PlayMusicService.position);
                String pic_url = songlistBean.getPic_big();
                setBackGround(pic_url);
                mToolbar.setSubtitle(songlistBean.getAuthor());
                mToolbar.setTitle(songlistBean.getTitle());
                showLrc(songlistBean.getLrclink());
                break;
            }
            case ConstUtils.TYPE_LOCAL: {
                LocalMusic localMusic = (LocalMusic) PlayMusicService.list.get(PlayMusicService.position);
                String pic_url = localMusic.getPic_big();
                setBackGround(pic_url);
                mToolbar.setSubtitle(localMusic.getAuthor());
                mToolbar.setTitle(localMusic.getTitle());
                showLrc(localMusic.getLrc_link());

                break;
            }
            case ConstUtils.TYPE_SERVERLIST: {
                MusicListItem musicListItem = (MusicListItem) PlayMusicService.list.get(PlayMusicService.position);
                String pic_url = musicListItem.getPic();
                setBackGround(pic_url);
                mToolbar.setSubtitle(musicListItem.getAuthor());
                mToolbar.setTitle(musicListItem.getTitle());
                showLrc(musicListItem.getLrc());
                break;
            }


        }

    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("changeView");
        filter.addAction("notifyViewPager");
        playMainReceiver = new PlayMainReceiver();
        registerReceiver(playMainReceiver, filter);
    }

    private void initListener() {
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouch = true;
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isTouch) {
                    String songId = null;
                    switch (PlayMusicService.type) {
                        case ConstUtils.TYPE_RANK: {
                            RankSongs.SongListBean songListBean = (RankSongs.SongListBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                            String pic_url = songListBean.getPic_big();
                            mToolbar.setSubtitle(songListBean.getAuthor());
                            mToolbar.setTitle(songListBean.getTitle());
                            setBackGround(pic_url);
                            showLrc(songListBean.getLrclink());
                            songId = songListBean.getSong_id();
                        }
                        break;
                        case ConstUtils.TYPE_NEWALUBM: {
                            AlbumSongs.SonglistBean albumSongs = (AlbumSongs.SonglistBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                            String pic_url = albumSongs.getPic_big();
                            mToolbar.setSubtitle(albumSongs.getAuthor());
                            mToolbar.setTitle(albumSongs.getTitle());
                            setBackGround(pic_url);
                            songId = albumSongs.getSong_id();
                            showLrc(albumSongs.getLrclink());
                            break;
                        }
                        case ConstUtils.TYPE_NEWSONG: {
                            NewMusic.SongListBean songs = (NewMusic.SongListBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                            String pic_url = songs.getPic_big();
                            mToolbar.setSubtitle(songs.getAuthor());
                            mToolbar.setTitle(songs.getTitle());
                            setBackGround(pic_url);
                            songId = songs.getSong_id();
                            showLrc(songs.getLrclink());
                            break;
                        }
                        case ConstUtils.TYPE_SEARCHSONGS: {
                            Songs songs = (Songs) PlayMusicService.list.get(position % PlayMusicService.list.size());
                            String pic_url = songs.getSonginfo().getPic_big();
                            mToolbar.setSubtitle(songs.getSonginfo().getAuthor());
                            mToolbar.setTitle(songs.getSonginfo().getTitle());
                            setBackGround(pic_url);
                            songId = songs.getBitrate().getFile_link();
                            showLrc(songs.getSonginfo().getLrclink());
                            break;
                        }
                        case ConstUtils.TYPE_HOTSINGER: {
                            SingerSongs.SonglistBean songlistBean = (SingerSongs.SonglistBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                            String pic_url = songlistBean.getPic_big();
                            mToolbar.setSubtitle(songlistBean.getAuthor());
                            mToolbar.setTitle(songlistBean.getTitle());
                            setBackGround(pic_url);
                            songId = songlistBean.getSong_id();
                            showLrc(songlistBean.getLrclink());
                            break;
                        }
                        case ConstUtils.TYPE_LOCAL: {
                            LocalMusic localMusic = (LocalMusic) PlayMusicService.list.get(position % PlayMusicService.list.size());
                            String pic_url = localMusic.getPic_big();
                            mToolbar.setSubtitle(localMusic.getAuthor());
                            mToolbar.setTitle(localMusic.getTitle());
                            setBackGround(pic_url);
                            songId = localMusic.getFile_link();
                            showLrc(localMusic.getLrc_link());
                            break;
                        }
                        case ConstUtils.TYPE_DOWNLOADSONGS: {
                            DownLoadBean localMusic = (DownLoadBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
//                            String pic_url = localMusic.getPic_big();
                            mToolbar.setSubtitle(localMusic.getAuthor());
                            mToolbar.setTitle(localMusic.getName());
                            setBackGround(null);
                            songId = localMusic.getSource();
                            showLrc(null);
                            break;
                        }
                        case ConstUtils.TYPE_SERVERLIST: {
                            MusicListItem musicListItem = (MusicListItem) PlayMusicService.list.get(position % PlayMusicService.list.size());
                            String pic_url = musicListItem.getPic();
                            mToolbar.setSubtitle(musicListItem.getAuthor());
                            mToolbar.setTitle(musicListItem.getTitle());
                            setBackGround(pic_url);
                            songId = musicListItem.getUrl();
                            showLrc(musicListItem.getLrc());
                            break;
                        }


                    }

                    PlayMusicService.position = position % PlayMusicService.list.size();
                    playBySongId(songId);


                }
                isTouch = false;
                viewPager.getChildAt(0).setTag(position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isTouch = true;
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                next.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_next));
            }
        });
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTouch = true;
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                last.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_last));
            }
        });
        playOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PlayMusicService.play_flag == false) {
                    if (PlayMusicService.isPause == true) {
                        PlayMusicService.mediaPlayer.start();
                        PlayMusicService.play_flag = true;
                        PlayMusicService.isPause = false;
                        playOrPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause));
                        sendBroadcast(new Intent("notifyChange"));
                        return;
                    } else {
//
                    }
                } else {
//
                    PlayMusicService.mediaPlayer.pause();
                    PlayMusicService.play_flag = false;
                    PlayMusicService.isPause = true;
                    playOrPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
                    sendBroadcast(new Intent("notifyChange"));
                }
//
            }
        });
        ic_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.popup_musiclist, null);
                final DisplayMetrics dm = getResources().getDisplayMetrics();
                btn_loop= (Button) view.findViewById(R.id.popup_musiclist_btn_loop);
                btn_loop.setText(MusicUtils.getPlayModeButtonText());
                btn_loop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List temp1=new ArrayList();
                        int i=0;
                        while(temp1.size() !=PlayMusicService.list.size() ){

                            temp1.add(PlayMusicService.randomList.get(i));
                            i++;
                        }
                        switch (PlayMusicService.playMod){

                            case ConstUtils.PLAYMOD_SEQUENCE:{
                                PlayMusicService.playMod=ConstUtils.PLAYMOD_DISORDER;
                                List temp2=new ArrayList();
                                int j=0;
                                while(temp2.size() !=PlayMusicService.randomList.size() ){

                                    temp2.add(PlayMusicService.randomList.get(j));
                                    j++;
                                }
                                PlayMusicService.list =MusicUtils.getRandomList(temp2);
                                int i1=viewPager.getCurrentItem()%PlayMusicService.list.size();
                                viewPager.setAdapter(new MyPlayActivityViewPagerAdapter(PlayMusicService.list,PlayActivity.this,PlayMusicService.type));
                                isTouch=false;
                                viewPager.setCurrentItem(MusicUtils.getRandomPosition(i1)+PlayMusicService.list.size()*10);
                                PlayMusicService.position = MusicUtils.getRandomPosition(i1);
                                break;}
                            case ConstUtils.PLAYMOD_DISORDER:{
                                PlayMusicService.playMod=ConstUtils.PLAYMOD_SINGLE;

                                int i2=viewPager.getCurrentItem()%PlayMusicService.list.size();
                                viewPager.setAdapter(new MyPlayActivityViewPagerAdapter(PlayMusicService.randomList,PlayActivity.this,PlayMusicService.type));
                                isTouch=false;
                                viewPager.setCurrentItem(MusicUtils.getSectionPosition(i2)+PlayMusicService.list.size()*10);
                                PlayMusicService.position = MusicUtils.getSectionPosition(i2);
                                PlayMusicService.list =temp1;
                                break;}
                            case ConstUtils.PLAYMOD_SINGLE:{
                                PlayMusicService.playMod=ConstUtils.PLAYMOD_SEQUENCE;
                                break;}

                        }
                        btn_loop.setText(MusicUtils.getPlayModeButtonText());

                    }
                });
                Button collect= (Button) view.findViewById(R.id.popup_musiclist_btn_collect);
                collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("onClick: ", "执行onclick");
                        if (PlayMusicService.user!=null){
                            setListDialog();
                            dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position==0){
                                        et_addList=new EditText(PlayActivity.this);
                                        et_addList.setHint("请输入歌单名");
                                        AlertDialog.Builder builder=new AlertDialog.Builder(PlayActivity.this);
                                        builder.setTitle("增加歌单");
                                        builder.setView(et_addList);
                                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            String url="http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/addMusicList.do";
                                            @Override
                                            public void onClick( DialogInterface dialog, int which) {

                                                RequestParams requestParams=new RequestParams(url);
                                                requestParams.addBodyParameter("listName",et_addList.getText().toString());
                                                requestParams.addBodyParameter("userId",PlayMusicService.user.getId()+"");
                                                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                                                    @Override
                                                    public void onSuccess(String result) {
                                                        if (result.equals("Y")){
                                                            Toast.makeText(PlayActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                                                            Map<String, Object> map=new HashMap<String, Object>() ;
                                                            map.put("musicList",new MusicList(et_addList.getText().toString()));
                                                            map.put("count",0);
                                                            dialogList.add(map);
                                                            myDialogListviewAdapter.notifyDataSetChanged();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Throwable ex, boolean isOnCallback) {
                                                        Log.e("onError: ","SHIBAI" );
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
                                        builder.setNegativeButton("取消", null);
                                        builder.create().show();

                                    }else{
                                        Map<String,Object> map=dialogList.get(position-1);
                                        try {
                                            MusicUtils.saveMusicList(PlayActivity.this,PlayMusicService.list,PlayMusicService.type,((MusicList)map.get("musicList")).getId());
                                            dialog.dismiss();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }

                    private void setListDialog() {
                        dialogList.clear();
                        dialog=new Dialog(PlayActivity.this);
                        dialog.setTitle("收藏到歌单");
                        dialog.setContentView(R.layout.dialog_collect);
                        Window dialogwindow=dialog.getWindow();
                        WindowManager.LayoutParams p=dialogwindow.getAttributes();
                        p.height= (int) (getResources().getDisplayMetrics().heightPixels*0.6);
                        p.width= (int) (getResources().getDisplayMetrics().widthPixels*0.9);
                        dialogwindow.setAttributes(p);
                        dialogListView= (ListView) dialog.findViewById(R.id.dialog_collect_listview);
                        myDialogListviewAdapter=new MyDialogListviewAdapter(PlayActivity.this,dialogList);
                        dialogListView.setAdapter(myDialogListviewAdapter);
                        dialog.show();
                        String url_1="http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/getMusicList.do?userId="+ PlayMusicService.user.getId();
                        RequestParams requestParams=new RequestParams(url_1);
                        x.http().post(requestParams, new Callback.CommonCallback<String>() {
                            List<MusicList> lists;

                            Map<String,Object> map;
                            @Override
                            public void onSuccess(String result) {
                                lists=new Gson().fromJson(result,new TypeToken<List<MusicList>>(){}.getType());
                                for (int i=0;i<lists.size();i++){
                                    String url_2="http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/getMusicCount.do?listId="+lists.get(i).getId();

                                    RequestParams req=new RequestParams(url_2);
                                    final int finalI = i;
                                    x.http().get(req, new CommonCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Log.e("onSuccess: ","返回成功" );
                                            map=new HashMap<String, Object>() ;
                                            map.put("musicList",lists.get(finalI));
                                            map.put("count",result);
                                            dialogList.add(map);
                                            if (finalI ==lists.size()-1){
                                                myDialogListviewAdapter.notifyDataSetChanged();

                                            }
                                        }

                                        @Override
                                        public void onError(Throwable ex, boolean isOnCallback) {
                                            Log.e("onError: ","失败" );
                                        }

                                        @Override
                                        public void onCancelled(CancelledException cex) {

                                        }

                                        @Override
                                        public void onFinished() {

                                        }
                                    });
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Log.e("onError: ","sshibai" );


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
                popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels / 5 * 3, true);
                popupWindow.setTouchable(true);
                ColorDrawable dw = new ColorDrawable(0xb0000000);
                popupWindow.setBackgroundDrawable(dw);
                popupWindow.setAnimationStyle(R.style.contextMenuAnim);
                popupWindow.showAtLocation(view, Gravity.BOTTOM,  0, 0);
                listView = (ListView) view.findViewById(R.id.popup_musiclist_listview);
                listView.setFocusable(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
                        isTouch=true;
                        int offSet = position-PlayMusicService.position;
                        PlayMusicService.position = position;
                        int nowPosition = viewPager.getCurrentItem()+offSet+PlayMusicService.list.size();
                        viewPager.setCurrentItem(nowPosition);
                        popupWindow.dismiss();
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        if (PlayMusicService.type==ConstUtils.TYPE_LOCAL||PlayMusicService.type==ConstUtils.TYPE_DOWNLOADSONGS){
                            return true;
                        }
                        AlertDialog.Builder builder=new AlertDialog.Builder(PlayActivity.this);
                        builder.setTitle("下载歌曲？");
                        builder.setNegativeButton("下载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final RankSongs.SongListBean songListBean= (RankSongs.SongListBean) PlayMusicService.randomList.get(position);

                                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songListBean.getSong_id());
                                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                                        }.getType());
                                        String url = songs.getBitrate().getFile_link();
                                        Intent intent=new Intent(PlayActivity.this, DownloadService.class);
                                        intent.putExtra("name",songListBean.getTitle());
                                        intent.putExtra("author",songListBean.getAuthor());
                                        intent.putExtra("url",url);
                                        intent.putExtra("pic",songs.getSonginfo().getPic_big());
                                        startService(intent);
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



//                                Map map=MusicUtils.getDownloadData(PlayMusicService.list,PlayMusicService.type,position);
//                                Intent intent=new Intent(getContext(), DownloadService.class);
//                                intent.putExtra("name",map.get("name")+"");
//                                intent.putExtra("url",map.get("url")+"");
//                                getContext().startService(intent);
                            }
                        });
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                        return true;
                    }
                });
                listviewAdapter = new MyPopupListviewAdapter(PlayMusicService.list, PlayActivity.this, PlayMusicService.type);
                listView.setAdapter(listviewAdapter);
                listView.setSelection(position % PlayMusicService.list.size());


            }

        });
        playOrPause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (PlayMusicService.play_flag == false) {
                    playOrPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause_on));
                } else {
                    playOrPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_on));
                }
                return false;
            }
        });
        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                next.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_next_on));
                return false;
            }
        });
        last.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                last.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_last_on));
                return false;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    lrcView.onDrag(progress);
                }
                if (progress<500){
                    lrcView.onDrag(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayMusicService.mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ic_playMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List temp1=new ArrayList();
                int i=0;
                while(temp1.size() !=PlayMusicService.list.size() ){

                    temp1.add(PlayMusicService.randomList.get(i));
                    i++;
                }
                switch (PlayMusicService.playMod){
                    case ConstUtils.PLAYMOD_SEQUENCE:{
                        PlayMusicService.playMod=ConstUtils.PLAYMOD_DISORDER;
                        ic_playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_mode_disorder));
                        List temp2=new ArrayList();
                        int j=0;
                        while(temp2.size() !=PlayMusicService.randomList.size() ){

                            temp2.add(PlayMusicService.randomList.get(j));
                            j++;
                        }
                        PlayMusicService.list =MusicUtils.getRandomList(temp2);
                        int i1=viewPager.getCurrentItem()%PlayMusicService.list.size();
                        viewPager.setAdapter(new MyPlayActivityViewPagerAdapter(PlayMusicService.list,PlayActivity.this,PlayMusicService.type));
                        isTouch=false;
                        viewPager.setCurrentItem(MusicUtils.getRandomPosition(i1)+PlayMusicService.list.size()*10);
                        PlayMusicService.position = MusicUtils.getRandomPosition(i1);
                        Toast.makeText(PlayActivity.this,"随机播放",Toast.LENGTH_SHORT).show();
                        break;}
                    case ConstUtils.PLAYMOD_DISORDER:{
                        PlayMusicService.playMod=ConstUtils.PLAYMOD_SINGLE;
                        ic_playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_mode_single));
                        int i2=viewPager.getCurrentItem()%PlayMusicService.list.size();
                        viewPager.setAdapter(new MyPlayActivityViewPagerAdapter(PlayMusicService.randomList,PlayActivity.this,PlayMusicService.type));
                        isTouch=false;
                        viewPager.setCurrentItem(MusicUtils.getSectionPosition(i2)+PlayMusicService.list.size()*10);
                        PlayMusicService.position = MusicUtils.getSectionPosition(i2);
                        PlayMusicService.list =temp1;
                        Toast.makeText(PlayActivity.this,"单曲循环",Toast.LENGTH_SHORT).show();
                        break;}
                    case ConstUtils.PLAYMOD_SINGLE:{
                        PlayMusicService.playMod=ConstUtils.PLAYMOD_SEQUENCE;
                        ic_playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_mode_shunxu));
                        Toast.makeText(PlayActivity.this,"列表循环",Toast.LENGTH_SHORT).show();
                        break;}
                }
            }
        });
        ic_playMode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (PlayMusicService.playMod){
                    case ConstUtils.PLAYMOD_SEQUENCE:
                        ic_playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_mode_shunxu_on));
                        break;
                    case ConstUtils.PLAYMOD_DISORDER:
                        ic_playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_mode_disorder_on));
                        break;
                    case ConstUtils.PLAYMOD_SINGLE:
                        ic_playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_mode_single_on));
                        break;
                }
                return false;
            }
        });
    }

    private void playBySongId(String songId) {
        Intent intent = new Intent();
        intent.setAction("playReceiveTwo");
        if (songId.length() > 20) {
            intent.putExtra("state", ConstUtils.STATE_LOCAL_CHANGE);
        } else {
            intent.putExtra("state", ConstUtils.STATE_CHANGE);
        }
        intent.putExtra("songId", songId);
        sendBroadcast(intent);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PlayActivity.class);
        context.startActivity(intent);
    }

    private void setBackGround(final String pic_url) {
        if (pic_url == null) {
            rootRelativeLayout.setBackgroundResource(R.drawable.ic_blackground);
        } else {


            new Thread() {
                @Override
                public void run() {
                    super.run();
                    ImageLoader.getInstance().loadImageSync(pic_url);
                    ImageLoader.getInstance().loadImage(pic_url, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rootRelativeLayout.setBackground(ConstUtils.getForegroundDrawable(loadedImage, PlayActivity.this));
                                }
                            });
                        }
                    });

                }
            }.start();
        }
    }

    class PlayMainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getAction();
            if (type.equals("changeView")) {
                switch (intent.getIntExtra("state", 0)) {
                    case ConstUtils.STATE_PLAY:
                        allTime.setText(MusicUtils.formatTime(intent.getIntExtra("duration", 0)));
                        playOrPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause));
                        break;
                    case ConstUtils.STATE_PAUSE:
                        playOrPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
                        break;
                    case ConstUtils.STATE_NEXT:
                        isTouch = true;
                        int nowPosition = viewPager.getCurrentItem();

                        if (PlayMusicService.playMod == ConstUtils.PLAYMOD_SINGLE) {
                            viewPager.setCurrentItem(nowPosition);
                        } else {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                        break;
                    case ConstUtils.STATE_LAST:
                        isTouch = true;
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        break;
                    case ConstUtils.STATE_CHANGE:

                    default:
                        seekBar.setMax(PlayMusicService.mediaPlayer.getDuration());
                        allTime.setText(MusicUtils.formatTime(PlayMusicService.mediaPlayer.getDuration()));
                        playOrPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause));
                        break;
                }
            }else{
//                viewPager.setAdapter(new MyPlayActivityViewPagerAdapter(PlayMusicService.list,PlayActivity.this,PlayMusicService.type));
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(playMainReceiver);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.playexit);
    }

    private void showLrc(String lrc_url) {
        RequestParams requestParams = new RequestParams(lrc_url);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                lrcView.loadLrc(result);

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
