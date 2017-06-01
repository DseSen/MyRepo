package utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import adapter.MyDialogListviewAdapter;
import jsonjavabean.AlbumSongs;
import jsonjavabean.LocalMusic;
import jsonjavabean.MusicList;
import jsonjavabean.MusicListItem;
import jsonjavabean.NewAlbum;
import jsonjavabean.NewMusic;
import jsonjavabean.RankSongs;
import jsonjavabean.SearchSongs;
import jsonjavabean.SingerSongs;
import jsonjavabean.Songs;
import service.PlayMusicService;

/**
 * Created by 啊丁 on 2017/4/11.
 */

public class MusicUtils {

    public static List<LocalMusic> getMusicData(Context context) {
        List<LocalMusic> list = new ArrayList<LocalMusic>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.Media.TITLE);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                LocalMusic song = new LocalMusic();

                song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                song.setAuthor( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setFile_link(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                song.setMusicId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                list.add(song);
            }

            cursor.close();
        }

        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }

    public static void saveMusicList(final Context context, final List list, int type,int listId) throws UnsupportedEncodingException {
        switch (type){
            case ConstUtils.TYPE_LOCAL:{
                for (int i=0;i<list.size();i++){
                    LocalMusic localMusic= (LocalMusic) list.get(i);
                    String title = URLEncoder.encode(localMusic.getTitle(),"utf-8");
                    String author = URLEncoder.encode(localMusic.getAuthor(),"UTF-8");
                    String url = URLEncoder.encode(localMusic.getFile_link(),"utf-8");
                    RequestParams requestParams=new RequestParams("http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/addMusicToListNoPicLrc.do?title="+title+"&author="+author+"&listid="+listId+"&url="+url);
                    final int finalI = i;
                    x.http().post(requestParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (finalI ==list.size()-1){
                                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e("onError: ", "失败了");
                            ex.printStackTrace();
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
            case ConstUtils.TYPE_RANK:{
                for (int i=0;i<list.size();i++){
                    RankSongs.SongListBean songListBeans= (RankSongs.SongListBean) list.get(i);
                    RequestParams requestParams=new RequestParams("http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/addMusicToListNoPicLrc.do");
                    requestParams.addBodyParameter("title",songListBeans.getTitle());
                    requestParams.addBodyParameter("author",songListBeans.getAuthor());
                    requestParams.addBodyParameter("listid",listId+"");
                    requestParams.addBodyParameter("url",songListBeans.getSong_id());
                    requestParams.addBodyParameter("pic",songListBeans.getPic_big());
                    requestParams.addBodyParameter("lrc",songListBeans.getLrclink());
                    final int finalI = i;
                    x.http().post(requestParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (finalI ==list.size()-1){
                                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e("onError: ", "失败了");
                            ex.printStackTrace();
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
            }
            case ConstUtils.TYPE_SEARCHSONGS:{
                for (int i=0;i<list.size();i++){
                    Songs songListBeans= (Songs) list.get(i);
                    RequestParams requestParams=new RequestParams("http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/addMusicToListNoPicLrc.do");
                    requestParams.addBodyParameter("title",songListBeans.getSonginfo().getTitle());
                    requestParams.addBodyParameter("author",songListBeans.getSonginfo().getAuthor());
                    requestParams.addBodyParameter("listid",listId+"");
                    requestParams.addBodyParameter("url",songListBeans.getBitrate().getFile_link());
                    requestParams.addBodyParameter("pic",songListBeans.getSonginfo().getPic_big());
                    requestParams.addBodyParameter("lrc",songListBeans.getSonginfo().getLrclink());
                    final int finalI = i;
                    x.http().post(requestParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (finalI ==list.size()-1){
                                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e("onError: ", "失败了");
                            ex.printStackTrace();
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
            }
            case ConstUtils.TYPE_NEWALUBM:{
                for (int i=0;i<list.size();i++){
                    AlbumSongs.SonglistBean songListBeans= (AlbumSongs.SonglistBean) list.get(i);
                    RequestParams requestParams=new RequestParams("http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/addMusicToListNoPicLrc.do");
                    requestParams.addBodyParameter("title",songListBeans.getTitle());
                    requestParams.addBodyParameter("author",songListBeans.getAuthor());
                    requestParams.addBodyParameter("listid",listId+"");
                    requestParams.addBodyParameter("url",songListBeans.getSong_id());
                    requestParams.addBodyParameter("pic",songListBeans.getPic_big());
                    requestParams.addBodyParameter("lrc",songListBeans.getLrclink());
                    final int finalI = i;
                    x.http().post(requestParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (finalI ==list.size()-1){
                                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e("onError: ", "失败了");
                            ex.printStackTrace();
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
            }
            case ConstUtils.TYPE_HOTSINGER:{
                for (int i=0;i<list.size();i++){
                    SingerSongs.SonglistBean songListBeans= (SingerSongs.SonglistBean) list.get(i);
                    RequestParams requestParams=new RequestParams("http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/addMusicToListNoPicLrc.do");
                    requestParams.addBodyParameter("title",songListBeans.getTitle());
                    requestParams.addBodyParameter("author",songListBeans.getAuthor());
                    requestParams.addBodyParameter("listid",listId+"");
                    requestParams.addBodyParameter("url",songListBeans.getSong_id());
                    requestParams.addBodyParameter("pic",songListBeans.getPic_big());
                    requestParams.addBodyParameter("lrc",songListBeans.getLrclink());
                    final int finalI = i;
                    x.http().post(requestParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (finalI ==list.size()-1){
                                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e("onError: ", "失败了");
                            ex.printStackTrace();
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
            }
            case ConstUtils.TYPE_NEWSONG:{
                for (int i=0;i<list.size();i++){
                    NewMusic.SongListBean songListBeans= (NewMusic.SongListBean) list.get(i);
                    RequestParams requestParams=new RequestParams("http://"+ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/addMusicToListNoPicLrc.do");
                    requestParams.addBodyParameter("title",songListBeans.getTitle());
                    requestParams.addBodyParameter("author",songListBeans.getAuthor());
                    requestParams.addBodyParameter("listid",listId+"");
                    requestParams.addBodyParameter("url",songListBeans.getSong_id());
                    requestParams.addBodyParameter("pic",songListBeans.getPic_big());
                    requestParams.addBodyParameter("lrc",songListBeans.getLrclink());
                    final int finalI = i;
                    x.http().post(requestParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (finalI ==list.size()-1){
                                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e("onError: ", "失败了");
                            ex.printStackTrace();
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
            }
        }
    }

    public static String getPlayModeButtonText(){
        switch (PlayMusicService.playMod){
            case ConstUtils.PLAYMOD_SEQUENCE:
                return "顺序播放("+PlayMusicService.list.size()+")";
            case ConstUtils.PLAYMOD_DISORDER:
                return "随机播放("+PlayMusicService.list.size()+")";
            case ConstUtils.PLAYMOD_SINGLE:
                return "单曲循环("+PlayMusicService.list.size()+")";
            default:
                return "";
        }
    }

    public static List getRandomList(List sourceList){

        List random = new ArrayList(sourceList.size());

        do {
            int index = Math.abs(new Random().nextInt(sourceList.size()));
            random.add(sourceList.remove(index));

        } while (sourceList.size() !=0);

        return random;
    }

    public static int getRandomPosition(int position){
        int index=0;
        for (int j = 0; j < PlayMusicService.randomList.size(); j++) {
            if (PlayMusicService.randomList.get(position)==PlayMusicService.list.get(j)) {
                index=j;
            }
        }
        Log.e("getRandomPosition: ",index+"");
        return index;
    }
    public static int getSectionPosition(int position){
        int index=0;
        for (int j = 0; j < PlayMusicService.randomList.size(); j++) {
            if (PlayMusicService.list.get(position)==PlayMusicService.randomList.get(j)) {
                index=j;
            }
        }
        Log.e("getRandomPosition: ",index+"");
        return index;
    }
//    public static List<MusicList> getMusicList(){
//        final String url_1 = "http://192.168.191.1:8080/musicPlayerServer/getMusicCount.do?listId=1";
//        String url_2 = "http://192.168.191.1:8080/musicPlayerServer/getMusicList.do?userId=" + PlayMusicService.user.getId();
//        RequestParams requestParams = new RequestParams(url_2);
//        x.http().post(requestParams, new Callback.CommonCallback<String>() {
//            List<MusicList> lists;
//            Map<String, Object> map;
//            @Override
//            public void onSuccess(String result) {
//                lists = new Gson().fromJson(result, new TypeToken<List<MusicList>>() {
//                }.getType());
//                return lists;
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.e("onError: ", "sshibai");
//
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//    }

    public static void setHead(ImageView imageView, int musicId) {


        ImageLoader imageLoader=ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.nopic)
                .cacheInMemory(true)
                .cacheOnDisk(true)

                .build();
        imageLoader.displayImage(ImageDownloader.Scheme.CONTENT.wrap("media/external/audio/media/" + musicId + "/albumart"),imageView,options);

    }
    public static Map getDownloadData(List songListBeens,int type,int position){
        final Map map=new HashMap();
        switch (type){
            case ConstUtils.TYPE_RANK:{
                RankSongs.SongListBean songListBean= (RankSongs.SongListBean) songListBeens.get(position%songListBeens.size());
                map.put("name",songListBean.getTitle());
                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songListBean.getSong_id());
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                        }.getType());
                        String url = songs.getBitrate().getFile_link();
                        Log.e("onSuccess: ", url);
                        map.put("url",url);
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
                AlbumSongs.SonglistBean songlistBean = (AlbumSongs.SonglistBean) songListBeens.get(position % songListBeens.size());
                map.put("name",songlistBean.getTitle());
                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songlistBean.getSong_id());
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                        }.getType());
                        String url = songs.getBitrate().getFile_link();
                        map.put("url",url);
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
            case ConstUtils.TYPE_NEWSONG:{
                NewMusic.SongListBean songlistBean= (NewMusic.SongListBean) songListBeens.get(position%songListBeens.size());
                map.put("name",songlistBean.getTitle());
                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songlistBean.getSong_id());
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                        }.getType());
                        String url = songs.getBitrate().getFile_link();
                        map.put("url",url);
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
            case ConstUtils.TYPE_SEARCHSONGS:{

                Songs songs= (Songs) songListBeens.get(position%songListBeens.size());
                map.put("name",songs.getSonginfo().getTitle());
                map.put("url",songs.getBitrate().getFile_link());

                break;
            }
            case ConstUtils.TYPE_HOTSINGER:
            {
                SingerSongs.SonglistBean songlistBean= (SingerSongs.SonglistBean) songListBeens.get(position%songListBeens.size());
                map.put("name",songlistBean.getTitle());
                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songlistBean.getSong_id());
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                        }.getType());
                        String url = songs.getBitrate().getFile_link();
                        map.put("url",url);
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
            case ConstUtils.TYPE_LOCAL:
            {
                LocalMusic songlistBean= (LocalMusic) songListBeens.get(position%songListBeens.size());
                map.put("name",songlistBean.getTitle());


            }
            break;
            case ConstUtils.TYPE_SERVERLIST:
            {
                MusicListItem songlistBean= (MusicListItem) songListBeens.get(position%songListBeens.size());
                map.put("name",songlistBean.getTitle());
                map.put("url",songlistBean.getUrl());

            }
            break;
        }
        return map;
    }
}


