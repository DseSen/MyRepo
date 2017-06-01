package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import jsonjavabean.AlbumSongs;
import jsonjavabean.DownLoadBean;
import jsonjavabean.LocalMusic;
import jsonjavabean.MusicListItem;
import jsonjavabean.NewMusic;
import jsonjavabean.RankSongs;

import jsonjavabean.SingerSongs;
import jsonjavabean.Songs;
import jsonjavabean.User;
import mymusicplayer.PlayActivity;
import utils.ConstUtils;

public class PlayMusicService extends Service {
    public static MediaPlayer mediaPlayer;
    public static boolean play_flag = false;
    public static int position;
    public static List list;
    public static List randomList;
    public static int type;
    public static boolean isPause = false;
    public static boolean isLogin = false;
    public static User user = new User();
    public static int playMod = ConstUtils.PLAYMOD_SEQUENCE;
    PlayBroadCastReceiver playReceiver;
    PlayBroadCastReceiverTwo playReceiver2;
    NotificationBroadCastReceiver notifyReceiver;
    Notification notification;
    NotificationManager nManager;
    RemoteViews remoteView;
    String url;

    @Override
    public void onCreate() {
        /**
         * 动态注册三个广播，用于接收音乐控制命令
         */
        playReceiver = new PlayBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("playReceive");
        registerReceiver(playReceiver, filter);

        playReceiver2 = new PlayBroadCastReceiverTwo();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("playReceiveTwo");
        registerReceiver(playReceiver2, filter2);

        notifyReceiver = new NotificationBroadCastReceiver();
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction("notifyReceiverNext");
        filter3.addAction("notifyReceiverLast");
        filter3.addAction("notifyReceiverPlayPause");
        filter3.addAction("notifyChangePlayState");
        filter3.addAction("notifyChange");
        filter3.addAction("closeNotifi");
        registerReceiver(notifyReceiver, filter3);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        /**
         * 服务销毁后置空变量释放内存，并将广播注销，
         */
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        list = null;
        randomList = null;
        unregisterReceiver(notifyReceiver);
        unregisterReceiver(playReceiver);
        unregisterReceiver(playReceiver2);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
        处理网络异步请求
     */
    public void setImageInNotify(String pic_url, final String title, final String author, final Context context) {
        new AsyncTask<String, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... params) {
                if (params[0] == null) {
                    Log.e("doInBackground: ", "npic");
                    return BitmapFactory.decodeResource(context.getResources(), R.drawable.nopic);
                }

                return ImageLoader.getInstance().loadImageSync(params[0]);
            }

            /*
                开启通知栏
             */
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                remoteView = new RemoteViews(getPackageName(), R.layout.notification_play);

                remoteView.setImageViewBitmap(R.id.notification_play_image, bitmap);
                remoteView.setTextViewText(R.id.notification_play_title, title);
                remoteView.setTextViewText(R.id.notification_play_author, author);

                Intent intent = new Intent("notifyReceiverNext");
                PendingIntent nextIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteView.setOnClickPendingIntent(R.id.notification_play_next, nextIntent);
                Intent intent2 = new Intent("notifyReceiverLast");
                PendingIntent lastIntent = PendingIntent.getBroadcast(context, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteView.setOnClickPendingIntent(R.id.notification_play_last, lastIntent);
                Intent intent3 = new Intent("notifyReceiverPlayPause");
                PendingIntent ppIntent = PendingIntent.getBroadcast(context, 2, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteView.setOnClickPendingIntent(R.id.notification_play_playOrPause, ppIntent);
                remoteView.setImageViewBitmap(R.id.notification_play_playOrPause, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_pause));
                builder.setSmallIcon(R.mipmap.ic_launcher);
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.lz);

                builder.setLargeIcon(icon);
                builder.setTicker("音乐正在播放");
                builder.setWhen(System.currentTimeMillis());
                builder.setCustomBigContentView(remoteView);
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, PlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pIntent);
                notification = builder.build();
                notification.flags = Notification.FLAG_NO_CLEAR;
                nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                nManager.notify(0, notification);
            }
        }.execute(pic_url);
    }

    /**
     * 此广播接收器主要接收第一次播放音乐时的广播
     */
    class PlayBroadCastReceiver extends BroadcastReceiver implements MediaPlayer.OnCompletionListener {
        Intent intent1;

        @Override
        public void onReceive(Context context, Intent intent) {

            int state = intent.getIntExtra("play", 0);
            if (intent.getStringExtra("url").equals("null") || intent.getIntExtra("position", 0) == -1) {

            } else {
                url = intent.getStringExtra("url");
                position = intent.getIntExtra("position", 0);
            }
            intent1 = new Intent();
            intent1.setAction("changeView");
            intent1.putExtra("state", state);

            setNotificationState(context);


            switch (state) {
                case ConstUtils.STATE_PLAY:
                    play(url);
                    play_flag = true;

                    sendBroadcast(intent1);
                    break;
                case ConstUtils.STATE_PAUSE:
                    mediaPlayer.pause();
                    play_flag = false;
                    Log.e("onReceive: ", "pause");
                    remoteView.setImageViewBitmap(R.id.notification_play_playOrPause, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_play));
                    nManager.notify(0, notification);
                    sendBroadcast(intent1);
                    break;

                default:
                    break;
            }


        }

        private void play(String url) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(this);
            } else {
                mediaPlayer.reset();
            }
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPause = false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /*
            监听播放状态并进行相应操作
         */
        @Override
        public void onCompletion(MediaPlayer mp) {
            sendBroadcast(new Intent("notifyReceiverNext"));
        }


    }

    private void setNotificationState(Context context) {
        switch (type) {
            case ConstUtils.TYPE_RANK: {

                RankSongs.SongListBean songListBean = (RankSongs.SongListBean) list.get(position);

                setImageInNotify(songListBean.getPic_big(), songListBean.getTitle(), songListBean.getAuthor(), context);

            }
            break;
            case ConstUtils.TYPE_NEWALUBM: {
                AlbumSongs.SonglistBean songlistBean = (AlbumSongs.SonglistBean) list.get(position);
                setImageInNotify(songlistBean.getPic_big(), songlistBean.getTitle(), songlistBean.getAuthor(), context);

            }
            break;
            case ConstUtils.TYPE_NEWSONG: {
                NewMusic.SongListBean songlistBean = (NewMusic.SongListBean) list.get(position);
                setImageInNotify(songlistBean.getPic_big(), songlistBean.getTitle(), songlistBean.getAuthor(), context);

                break;
            }
            case ConstUtils.TYPE_SEARCHSONGS: {

                Songs songs = (Songs) list.get(position);
                setImageInNotify(songs.getSonginfo().getPic_big(), songs.getSonginfo().getTitle(), songs.getSonginfo().getAuthor(), context);

                break;
            }
            case ConstUtils.TYPE_HOTSINGER: {
                SingerSongs.SonglistBean songlistBean = (SingerSongs.SonglistBean) list.get(position);
                setImageInNotify(songlistBean.getPic_big(), songlistBean.getTitle(), songlistBean.getAuthor(), context);


            }
            break;
            case ConstUtils.TYPE_LOCAL: {
                LocalMusic songlistBean = (LocalMusic) list.get(position);
                setImageInNotify(songlistBean.getPic_big(), songlistBean.getTitle(), songlistBean.getAuthor(), context);
            }
            break;
            case ConstUtils.TYPE_DOWNLOADSONGS: {
                DownLoadBean localMusic = (DownLoadBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
//                            String pic_url = localMusic.getPic_big();
                setImageInNotify(null, localMusic.getName(), localMusic.getAuthor(), context);
                break;
            }
            case ConstUtils.TYPE_SERVERLIST: {
                MusicListItem songlistBean = (MusicListItem) list.get(position);
                setImageInNotify(songlistBean.getPic(), songlistBean.getTitle(), songlistBean.getAuthor(), context);


            }
            break;
        }
    }

    /**
     * 此广播接收器主要处理程序界面和音乐的动态变化
     */
    class PlayBroadCastReceiverTwo extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            int state = intent.getIntExtra("state", 0);


            Intent intent1 = new Intent();
            intent1 = new Intent();
            intent1.setAction("changeView");
            intent1.putExtra("state", state);
            switch (state) {
                case ConstUtils.STATE_CHANGE: {
                    String songId = intent.getStringExtra("songId");
                    RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songId);
                    requestParams.addQueryStringParameter("name", "value");
                    final Intent finalIntent = intent1;
                    x.http().get(requestParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                            }.getType());
                            String url = songs.getBitrate().getFile_link();
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            try {
                                mediaPlayer.setDataSource(url);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                isPause = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            play_flag = true;
                            setDuration(mediaPlayer, intent);
                            sendBroadcast(finalIntent);

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
                break;
                case ConstUtils.STATE_LOCAL_CHANGE: {
                    String songId = intent.getStringExtra("songId");
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(songId);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        isPause = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    play_flag = true;
                    setDuration(mediaPlayer, intent1);
                    sendBroadcast(intent1);
                    break;
                }
            }
            setNotificationState(context);

        }


    }

    /**
     * 此音乐播放器处理通知栏发送过来的控制命令
     */
    class NotificationBroadCastReceiver extends BroadcastReceiver {
        String songId;

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intentState = new Intent();
            intentState.setAction("changeView");

            String type = intent.getAction();
            if (type.equals("notifyReceiverNext")) {


                if (PlayMusicService.playMod == ConstUtils.PLAYMOD_SINGLE) {

                } else {
                    position = (position + 1) % list.size();
                }

                intentState.putExtra("state", ConstUtils.STATE_NEXT);
            } else if (type.equals("notifyReceiverLast")) {
                if (position - 1 < 0) {
                    position = list.size() - 1;
                } else {
                    position = (position - 1);
                }
                intentState.putExtra("state", ConstUtils.STATE_LAST);
            } else if (type.equals("notifyReceiverPlayPause")) {
                Intent intent1 = new Intent("changeView");
                if (mediaPlayer.isPlaying()) {
                    remoteView.setImageViewBitmap(R.id.notification_play_playOrPause, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_play));
                    mediaPlayer.pause();
                    play_flag = false;
                    isPause = true;
                    intent1.putExtra("state", ConstUtils.STATE_PAUSE);
                } else {
                    play_flag = true;
                    isPause = false;
                    remoteView.setImageViewBitmap(R.id.notification_play_playOrPause, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_pause));
                    mediaPlayer.start();
                    intent1.putExtra("state", ConstUtils.STATE_PLAY);

                }

                sendBroadcast(intent1);
                nManager.notify(0, notification);
                return;
            } else if (type.equals("notifyChange")) {
                if (mediaPlayer.isPlaying()) {
                    remoteView.setImageViewBitmap(R.id.notification_play_playOrPause, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_pause));
                } else {
                    remoteView.setImageViewBitmap(R.id.notification_play_playOrPause, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_play));
                }
                nManager.notify(0, notification);
                return;
            } else if (type.equals("closeNotifi")) {
                nManager.cancel(0);
                return;
            }

            switch (PlayMusicService.type) {
                case ConstUtils.TYPE_RANK: {

                    RankSongs.SongListBean songListBean = (RankSongs.SongListBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    String pic_url = songListBean.getPic_big();
                    setImageInNotify(pic_url, songListBean.getTitle(), songListBean.getAuthor(), context);
                    songId = songListBean.getSong_id();
                    break;
                }

                case ConstUtils.TYPE_NEWALUBM: {
                    AlbumSongs.SonglistBean albumSongs = (AlbumSongs.SonglistBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    String pic_url = albumSongs.getPic_big();
                    setImageInNotify(pic_url, albumSongs.getTitle(), albumSongs.getAuthor(), context);
                    songId = albumSongs.getSong_id();
                    break;
                }
                case ConstUtils.TYPE_NEWSONG: {
                    NewMusic.SongListBean songs = (NewMusic.SongListBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    String pic_url = songs.getPic_big();
                    setImageInNotify(pic_url, songs.getTitle(), songs.getAuthor(), context);
                    songId = songs.getSong_id();
                    break;
                }
                case ConstUtils.TYPE_SEARCHSONGS: {
                    Songs songs = (Songs) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    String pic_url = songs.getSonginfo().getPic_big();
                    setImageInNotify(pic_url, songs.getSonginfo().getTitle(), songs.getSonginfo().getAuthor(), context);
                    songId = songs.getBitrate().getFile_link();
                    break;
                }
                case ConstUtils.TYPE_HOTSINGER: {
                    SingerSongs.SonglistBean songlistBean = (SingerSongs.SonglistBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    String pic_url = songlistBean.getPic_big();
                    setImageInNotify(pic_url, songlistBean.getTitle(), songlistBean.getAuthor(), context);

                    songId = songlistBean.getSong_id();
                    break;
                }
                case ConstUtils.TYPE_LOCAL: {
                    LocalMusic localMusic = (LocalMusic) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    String pic_url = localMusic.getPic_big();
                    setImageInNotify(pic_url, localMusic.getTitle(), localMusic.getAuthor(), context);
                    songId = localMusic.getFile_link();
                    break;
                }
                case ConstUtils.TYPE_DOWNLOADSONGS: {
                    DownLoadBean localMusic = (DownLoadBean) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    songId = localMusic.getSource();
                    setImageInNotify(null, localMusic.getName(), localMusic.getAuthor(), context);

                    break;
                }
                case ConstUtils.TYPE_SERVERLIST: {
                    MusicListItem musicListItem = (MusicListItem) PlayMusicService.list.get(position % PlayMusicService.list.size());
                    String pic_url = musicListItem.getPic();
                    setImageInNotify(pic_url, musicListItem.getTitle(), musicListItem.getAuthor(), context);
                    songId = musicListItem.getUrl();
                    break;
                }

            }

            Intent playIntent = new Intent();
            playIntent.setAction("playReceiveTwo");
            if (songId.length() > 20) {
                playIntent.putExtra("state", ConstUtils.STATE_LOCAL_CHANGE);
            } else {
                playIntent.putExtra("state", ConstUtils.STATE_CHANGE);
            }
            playIntent.putExtra("songId", songId);
            sendBroadcast(playIntent);

            sendBroadcast(intentState);
        }

    }

    private void setDuration(MediaPlayer mediaPlayer, Intent intent) {

        int duration = mediaPlayer.getDuration();
        intent.putExtra("duration", duration);

    }

}
