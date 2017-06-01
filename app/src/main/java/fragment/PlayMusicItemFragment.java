package fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.ViewGroup.LayoutParams;

import adapter.MyDialogListviewAdapter;
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

import mymusicplayer.MainActivity;
import mymusicplayer.PlayActivity;
import service.DownloadService;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.MusicUtils;

/**
 * Created by 啊丁 on 2017/3/29.
 */

public class PlayMusicItemFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ImageView btn_play;
    ImageView btn_list;
    boolean isPlay = false;
    boolean isTouch = false;
    Activity activity;
    ViewPager viewPager;
    ListView listView, dialogListView;
    Dialog dialog;
    List list;
    List<Map<String, Object>> dialogList = new ArrayList<Map<String, Object>>();
    MyPlayItemViewPagerAdapter pagerAdapter;
    MyPopupListviewAdapter listviewAdapter;
    EditText et_addList;
    int position;
    MyDialogListviewAdapter myDialogListviewAdapter;
    Button btn_loop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playmusicitem, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.playmusicitemfragment_linerlayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.actionStart(getActivity());
                getActivity().overridePendingTransition(R.anim.playenter, R.anim.noaction);
            }
        });
        btn_play = (ImageView) view.findViewById(R.id.palyitem_btn_play);
        btn_list = (ImageView) view.findViewById(R.id.palyitem_btn_list);
        view.findViewById(R.id.playmusicitemfragment_linerlayout).getBackground().mutate().setAlpha(248);
        activity = getActivity();

        init(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.palyitem_btn_play: {
                Intent intent = new Intent();
                if (isPlay == false) {
                    if (PlayMusicService.isPause == true) {
                        PlayMusicService.mediaPlayer.start();
                        PlayMusicService.play_flag = true;
                        PlayMusicService.isPause = false;
                        btn_play.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.btn_pause));
                        isPlay = true;
                        getContext().sendBroadcast(new Intent("notifyChange"));
                        break;
                    } else {
//                        intent.putExtra("play", ConstUtils.STATE_PLAY);
                    }
                } else {
//                    intent.putExtra("play", ConstUtils.STATE_PAUSE);
//                    PlayMusicService.isPause=true;
                    PlayMusicService.mediaPlayer.pause();
                    PlayMusicService.play_flag = false;
                    PlayMusicService.isPause = true;
                    isPlay = false;
                    btn_play.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.btn_play));
                    getContext().sendBroadcast(new Intent("notifyChange"));

                }
//                intent.putExtra("url", "null");
//                intent.setAction("playReceive");
//                intent.putExtra("position", -1);
//                activity.sendBroadcast(intent);

            }
            break;

            case R.id.palyitem_btn_list: {
                final View view = activity.getLayoutInflater().inflate(R.layout.popup_musiclist, null);
                btn_loop = (Button) view.findViewById(R.id.popup_musiclist_btn_loop);
                btn_loop.setText(MusicUtils.getPlayModeButtonText());
                btn_loop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List temp1 = new ArrayList();
                        int i = 0;
                        while (temp1.size() != list.size()) {

                            temp1.add(list.get(i));
                            i++;
                        }
                        switch (PlayMusicService.playMod) {

                            case ConstUtils.PLAYMOD_SEQUENCE: {
                                PlayMusicService.playMod = ConstUtils.PLAYMOD_DISORDER;
                                List temp2 = new ArrayList();
                                int j = 0;
                                while (temp2.size() != list.size()) {

                                    temp2.add(list.get(j));
                                    j++;
                                }
                                PlayMusicService.list = MusicUtils.getRandomList(temp2);
                                int i1 = viewPager.getCurrentItem() % PlayMusicService.list.size();
                                viewPager.setAdapter(new MyPlayItemViewPagerAdapter(PlayMusicService.list, getContext(), PlayMusicService.type));
                                isTouch = false;
                                viewPager.setCurrentItem(MusicUtils.getRandomPosition(i1) + PlayMusicService.list.size() * 10);
                                PlayMusicService.position = MusicUtils.getRandomPosition(i1);
                                break;
                            }
                            case ConstUtils.PLAYMOD_DISORDER: {
                                PlayMusicService.playMod = ConstUtils.PLAYMOD_SINGLE;

                                int i2 = viewPager.getCurrentItem() % PlayMusicService.list.size();
                                viewPager.setAdapter(new MyPlayItemViewPagerAdapter(PlayMusicService.randomList, getContext(), PlayMusicService.type));
                                isTouch = false;
                                viewPager.setCurrentItem(MusicUtils.getSectionPosition(i2) + PlayMusicService.list.size() * 10);
                                PlayMusicService.position = MusicUtils.getSectionPosition(i2);
                                PlayMusicService.list = temp1;
                                break;
                            }
                            case ConstUtils.PLAYMOD_SINGLE: {
                                PlayMusicService.playMod = ConstUtils.PLAYMOD_SEQUENCE;
                                break;
                            }

                        }

                        btn_loop.setText(MusicUtils.getPlayModeButtonText());
                    }
                });
                final DisplayMetrics dm = getResources().getDisplayMetrics();
                Button collect = (Button) view.findViewById(R.id.popup_musiclist_btn_collect);
                collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("onClick: ", "执行onclick");
                        if (PlayMusicService.user != null) {
                            setListDialog();
                            dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                        et_addList = new EditText(getContext());
                                        et_addList.setHint("请输入歌单名");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("增加歌单");
                                        builder.setView(et_addList);
                                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            String url = "http://" + ConstUtils.MYSERVER_URL + ":8080/musicPlayerServer/addMusicList.do";

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                RequestParams requestParams = new RequestParams(url);
                                                requestParams.addBodyParameter("listName", et_addList.getText().toString());
                                                requestParams.addBodyParameter("userId", PlayMusicService.user.getId() + "");
                                                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                                                    @Override
                                                    public void onSuccess(String result) {
                                                        if (result.equals("Y")) {
                                                            Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                                            Map<String, Object> map = new HashMap<String, Object>();
                                                            map.put("musicList", new MusicList(et_addList.getText().toString()));
                                                            map.put("count", 0);
                                                            dialogList.add(map);
                                                            myDialogListviewAdapter.notifyDataSetChanged();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Throwable ex, boolean isOnCallback) {
                                                        Log.e("onError: ", "SHIBAI");
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

                                    } else {
                                        Map<String, Object> map = dialogList.get(position - 1);
                                        try {
                                            MusicUtils.saveMusicList(getContext(), PlayMusicService.list, PlayMusicService.type, ((MusicList) map.get("musicList")).getId());
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
                        dialog = new Dialog(getContext());
                        dialog.setTitle("收藏到歌单");
                        dialog.setContentView(R.layout.dialog_collect);
                        Window dialogwindow = dialog.getWindow();
                        WindowManager.LayoutParams p = dialogwindow.getAttributes();
                        p.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
                        p.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
                        dialogwindow.setAttributes(p);
                        dialogListView = (ListView) dialog.findViewById(R.id.dialog_collect_listview);
                        myDialogListviewAdapter = new MyDialogListviewAdapter(getContext(), dialogList);
                        dialogListView.setAdapter(myDialogListviewAdapter);
                        dialog.show();
                        String url_1 = "http://" + ConstUtils.MYSERVER_URL + ":8080/musicPlayerServer/getMusicList.do?userId=" + PlayMusicService.user.getId();
                        RequestParams requestParams = new RequestParams(url_1);
                        x.http().post(requestParams, new Callback.CommonCallback<String>() {
                            List<MusicList> lists;

                            Map<String, Object> map;

                            @Override
                            public void onSuccess(String result) {
                                lists = new Gson().fromJson(result, new TypeToken<List<MusicList>>() {
                                }.getType());
                                for (int i = 0; i < lists.size(); i++) {
                                    String url_2 = "http://" + ConstUtils.MYSERVER_URL + ":8080/musicPlayerServer/getMusicCount.do?listId=" + lists.get(i).getId();

                                    RequestParams req = new RequestParams(url_2);
                                    final int finalI = i;
                                    x.http().get(req, new CommonCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            map = new HashMap<String, Object>();
                                            map.put("musicList", lists.get(finalI));
                                            map.put("count", result);
                                            dialogList.add(map);
                                            if (finalI == lists.size() - 1) {
                                                myDialogListviewAdapter.notifyDataSetChanged();

                                            }
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
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }


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

                Button clearList = (Button) view.findViewById(R.id.popup_musiclist_btn_delete);
//                clearList.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        getActivity().sendBroadcast(new Intent("closeNotifi"));
//                        getActivity().stopService(new Intent(getActivity(), PlayMusicService.class));
//                        getActivity().sendBroadcast(new Intent("closeAll"));
//                    }
//                });

                final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, dm.heightPixels / 5 * 3, true);
                popupWindow.setTouchable(true);
                ColorDrawable dw = new ColorDrawable(0xb0000000);
                popupWindow.setBackgroundDrawable(dw);
                popupWindow.setAnimationStyle(R.style.contextMenuAnim);
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                listView = (ListView) view.findViewById(R.id.popup_musiclist_listview);
                listView.setFocusable(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        int offSet = MusicUtils.getRandomPosition(position) - PlayMusicService.position;
//                        PlayMusicService.position = MusicUtils.getRandomPosition(position);
                        int nowPosition = viewPager.getCurrentItem() + offSet + PlayMusicService.list.size();
//                        PlayMusicService.position = nowPosition%PlayMusicService.list.size();
                        isTouch = true;
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
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("下载歌曲？");
                        builder.setNegativeButton("下载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (PlayMusicService.type){
                                    case ConstUtils.TYPE_RANK:{
                                        final RankSongs.SongListBean songListBean= (RankSongs.SongListBean) PlayMusicService.randomList.get(position);

                                        RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songListBean.getSong_id());
                                        x.http().get(requestParams, new Callback.CommonCallback<String>() {
                                            @Override
                                            public void onSuccess(String result) {
                                                Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                                                }.getType());
                                                String url = songs.getBitrate().getFile_link();
                                                Intent intent=new Intent(getContext(), DownloadService.class);
                                                intent.putExtra("name",songListBean.getTitle());
                                                intent.putExtra("author",songListBean.getAuthor());
                                                intent.putExtra("url",url);
                                                intent.putExtra("pic",songs.getSonginfo().getPic_big());
                                                getContext().startService(intent);
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

                                        break;
                                    }
                                    case ConstUtils.TYPE_NEWALUBM: {

                                        final AlbumSongs.SonglistBean songListBean= (AlbumSongs.SonglistBean) PlayMusicService.randomList.get(position);

                                        RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songListBean.getSong_id());
                                        x.http().get(requestParams, new Callback.CommonCallback<String>() {
                                            @Override
                                            public void onSuccess(String result) {
                                                Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                                                }.getType());
                                                String url = songs.getBitrate().getFile_link();
                                                Intent intent=new Intent(getContext(), DownloadService.class);
                                                intent.putExtra("name",songListBean.getTitle());
                                                intent.putExtra("author",songListBean.getAuthor());
                                                intent.putExtra("url",url);
                                                intent.putExtra("pic",songs.getSonginfo().getPic_big());
                                                getContext().startService(intent);
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
                                    case ConstUtils.TYPE_NEWSONG:{
                                        final NewMusic.SongListBean songListBean= (NewMusic.SongListBean) PlayMusicService.randomList.get(position);

                                        RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songListBean.getSong_id());
                                        x.http().get(requestParams, new Callback.CommonCallback<String>() {
                                            @Override
                                            public void onSuccess(String result) {
                                                Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                                                }.getType());
                                                String url = songs.getBitrate().getFile_link();
                                                Intent intent=new Intent(getContext(), DownloadService.class);
                                                intent.putExtra("name",songListBean.getTitle());
                                                intent.putExtra("author",songListBean.getAuthor());
                                                intent.putExtra("url",url);
                                                intent.putExtra("pic",songs.getSonginfo().getPic_big());
                                                getContext().startService(intent);
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
                                    case ConstUtils.TYPE_SEARCHSONGS:{

                                        final Songs songListBean= (Songs) PlayMusicService.randomList.get(position);
                                        String url = songListBean.getBitrate().getFile_link();
                                        Intent intent=new Intent(getContext(), DownloadService.class);
                                        intent.putExtra("name",songListBean.getSonginfo().getTitle());
                                        intent.putExtra("author",songListBean.getSonginfo().getAuthor());
                                        intent.putExtra("url",url);
                                        intent.putExtra("pic",songListBean.getSonginfo().getPic_big());
                                        getContext().startService(intent);

                                        break;
                                    }
                                    case ConstUtils.TYPE_HOTSINGER:
                                    {
                                        final SingerSongs.SonglistBean songListBean= (SingerSongs.SonglistBean) PlayMusicService.randomList.get(position);

                                        RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songListBean.getSong_id());
                                        x.http().get(requestParams, new Callback.CommonCallback<String>() {
                                            @Override
                                            public void onSuccess(String result) {
                                                Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                                                }.getType());
                                                String url = songs.getBitrate().getFile_link();
                                                Intent intent=new Intent(getContext(), DownloadService.class);
                                                intent.putExtra("name",songListBean.getTitle());
                                                intent.putExtra("author",songListBean.getAuthor());
                                                intent.putExtra("url",url);
                                                intent.putExtra("pic",songs.getSonginfo().getPic_big());
                                                getContext().startService(intent);
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
                                    break;
                                    }
                                        case ConstUtils.TYPE_LOCAL:
                                    {


                                    }
                                    break;
                                    case ConstUtils.TYPE_SERVERLIST:
                                    {
                                        final MusicListItem songListBean= (MusicListItem) PlayMusicService.randomList.get(position);

                                        String url = songListBean.getUrl();
                                        Intent intent=new Intent(getContext(), DownloadService.class);
                                        intent.putExtra("name",songListBean.getTitle());
                                        intent.putExtra("author",songListBean.getAuthor());
                                        intent.putExtra("url",url);
                                        intent.putExtra("pic",songListBean.getPic());
                                        getContext().startService(intent);

                                    }
                                    break;
                                }



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
                getSongListView();
                Toast.makeText(activity, "显示列表", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("playReceive");
        if (PlayMusicService.mediaPlayer != null && PlayMusicService.play_flag == true) {
            isPlay = true;
            btn_play.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.btn_pause));
        } else if (PlayMusicService.mediaPlayer == null || PlayMusicService.play_flag == false) {
            isPlay = false;
            btn_play.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.btn_play));
        }
        if (PlayMusicService.list != null) {

            getSongList();
        }

    }

    public void init(View view) {

        viewPager = (ViewPager) view.findViewById(R.id.playmusicitem_viewpager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.actionStart(getContext());
            }
        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouch = true;
                return false;
            }
        });
//        viewPager.setOffscreenPageLimit(1);
        btn_list.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("changeView");
        filter.addAction("notifyViewPager");
        PlaySurfaceBroadCast playSurfaceBroadCast = new PlaySurfaceBroadCast();
        activity.registerReceiver(playSurfaceBroadCast, filter);


    }

    @Override
    public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {


    }


    @Override
    public void onPageSelected(final int position) {
        if (isTouch) {
            String songId = null;
            switch (PlayMusicService.type) {
                case ConstUtils.TYPE_RANK: {
                    RankSongs.SongListBean songListBean = (RankSongs.SongListBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    songId = songListBean.getSong_id();
                }
                break;
                case ConstUtils.TYPE_NEWALUBM: {
                    AlbumSongs.SonglistBean albumSongs = (AlbumSongs.SonglistBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    songId = albumSongs.getSong_id();
                    break;
                }
                case ConstUtils.TYPE_NEWSONG: {
                    NewMusic.SongListBean songs = (NewMusic.SongListBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    songId = songs.getSong_id();
                    break;
                }
                case ConstUtils.TYPE_SEARCHSONGS: {
                    Songs songs = (Songs) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    songId = songs.getSonginfo().getSong_id();
                    break;
                }
                case ConstUtils.TYPE_HOTSINGER: {
                    SingerSongs.SonglistBean songlistBean = (SingerSongs.SonglistBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    songId = songlistBean.getSong_id();
                    break;
                }
                case ConstUtils.TYPE_LOCAL: {
                    LocalMusic localMusic = (LocalMusic) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    songId = localMusic.getFile_link();
                    break;
                }
                case ConstUtils.TYPE_DOWNLOADSONGS: {
                    DownLoadBean localMusic = (DownLoadBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
//                            String pic_url = localMusic.getPic_big();

                    songId = localMusic.getSource();

                    break;
                }
                case ConstUtils.TYPE_SERVERLIST: {
                    MusicListItem musicListItem = (MusicListItem) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    songId = musicListItem.getUrl();
                    break;
                }


            }
            if (listView != null) {
                listView.setSelection(MusicUtils.getSectionPosition(position % PlayMusicService.list.size()));
            }
            playBySongId(songId);
            PlayMusicService.position = position % PlayMusicService.list.size();


        }
        isTouch = false;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class PlaySurfaceBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getAction();
            if (type.equals("changeView")) {
                switch (intent.getIntExtra("state", 0)) {
                    case ConstUtils.STATE_PLAY:
                        getSongList();
                        btn_play.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_pause));
                        isPlay = true;
                        break;
                    case ConstUtils.STATE_PAUSE:
//                    getSongList();
                        btn_play.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_play));
                        isPlay = false;
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
//                    btn_play.setText("暂停");
//                    isPlay = true;
//                    break;
//                case ConstUtils.STATE_LOCAL_CHANGE:


                    default:
                        btn_play.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_pause));
                        isPlay = true;
                        break;
                }
            }else{
//                viewPager.setAdapter(new MyPlayItemViewPagerAdapter(PlayMusicService.list, getContext(), PlayMusicService.type));
            }
        }


    }

    private void getSongList() {
        position = PlayMusicService.position;
//        list = PlayMusicService.randomList;
        pagerAdapter = new MyPlayItemViewPagerAdapter(PlayMusicService.list, getContext(), PlayMusicService.type);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position + PlayMusicService.list.size() * 10);

    }

    private void getSongListView() {
        position = PlayMusicService.position;
        list = PlayMusicService.randomList;
        listviewAdapter = new MyPopupListviewAdapter(PlayMusicService.randomList, getContext(), PlayMusicService.type);
        listView.setAdapter(listviewAdapter);
        listView.setSelection(MusicUtils.getSectionPosition(position % PlayMusicService.list.size()));

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
        activity.sendBroadcast(intent);
    }


}
