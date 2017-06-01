package adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.myapplication.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import java.util.List;

import jsonjavabean.AlbumSongs;
import jsonjavabean.DownLoadBean;
import jsonjavabean.LocalMusic;
import jsonjavabean.MusicListItem;
import jsonjavabean.NewMusic;
import jsonjavabean.RankSongs;
import jsonjavabean.SingerSongs;
import jsonjavabean.Songs;
import mymusicplayer.PlayActivity;
import service.PlayMusicService;
import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/4/14.
 */

public class MyPlayActivityViewPagerAdapter extends PagerAdapter {
    private List songListBeens;
    private Context context;
    private int type;
    RoundedBitmapDrawable drawable3;
    ShapeDrawable drawable0;
    RoundedBitmapDrawable drawable1;
    ShapeDrawable drawable2;
    Drawable[] layers = new Drawable[4];
    View imageView,v;
    ObjectAnimator discObjectAnimator;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.nopic)// 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.nopic)
            // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.nopic)// 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(false)
            // 设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
            // 设置成圆角图片
            .build();
    public MyPlayActivityViewPagerAdapter(List songListBeens, Context context, int type) {
        this.songListBeens = songListBeens;
        this.context = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            container.removeView((View) object);

        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        v = LayoutInflater.from(context).inflate(R.layout.activity_play_viewpager_item,null);

        //最外部的半透明边线
        OvalShape ovalShape0 = new OvalShape();
        drawable0 = new ShapeDrawable(ovalShape0);
        drawable0.getPaint().setColor(0x10000000);
        drawable0.getPaint().setStyle(Paint.Style.FILL);
        drawable0.getPaint().setAntiAlias(true);

        //黑色唱片边框
        drawable1 = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.drawable.a9o));
        drawable1.setCircular(true);
        drawable1.setAntiAlias(true);

        //内层黑色边线
        OvalShape ovalShape2 = new OvalShape();
        drawable2 = new ShapeDrawable(ovalShape2);
        drawable2.getPaint().setColor(Color.BLACK);
        drawable2.getPaint().setStyle(Paint.Style.FILL);
        drawable2.getPaint().setAntiAlias(true);

        //最里面的图像

        imageView = v.findViewById(R.id.myView);
        switch (type) {
            case ConstUtils.TYPE_RANK: {
                RankSongs.SongListBean songListBean = (RankSongs.SongListBean) songListBeens.get(position % songListBeens.size());
                setBackGround(songListBean.getPic_big(),imageView);
                break;
            }
            case ConstUtils.TYPE_NEWALUBM: {
                AlbumSongs.SonglistBean songlistBean = (AlbumSongs.SonglistBean) songListBeens.get(position % songListBeens.size());
                setBackGround(songlistBean.getPic_big(),imageView);

                break;
            }
            case ConstUtils.TYPE_NEWSONG: {
                NewMusic.SongListBean songlistBean = (NewMusic.SongListBean) songListBeens.get(position % songListBeens.size());
                setBackGround(songlistBean.getPic_big(),imageView);
                break;
            }
            case ConstUtils.TYPE_SEARCHSONGS: {

                Songs songs = (Songs) songListBeens.get(position % songListBeens.size());
                setBackGround(songs.getSonginfo().getPic_big(),imageView);

//
                break;
            }
            case ConstUtils.TYPE_DOWNLOADSONGS:{

                DownLoadBean songs= (DownLoadBean) songListBeens.get(position%songListBeens.size());
                setBackGround(null,imageView);
//
                break;
            }
            case ConstUtils.TYPE_HOTSINGER: {
                SingerSongs.SonglistBean songlistBean = (SingerSongs.SonglistBean) songListBeens.get(position % songListBeens.size());
                setBackGround(songlistBean.getPic_big(),imageView);


            }
            break;
            case ConstUtils.TYPE_LOCAL: {
                LocalMusic songlistBean = (LocalMusic) songListBeens.get(position % songListBeens.size());
                setBackGround(songlistBean.getPic_big(),imageView);


            }
            break;
            case ConstUtils.TYPE_SERVERLIST: {
                MusicListItem songlistBean = (MusicListItem) songListBeens.get(position % songListBeens.size());
                setBackGround(songlistBean.getPic(),imageView);

            }
            break;
        }
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//        int width = 15;
//        LayerDrawable layerDrawable = new LayerDrawable(layers);
//        layerDrawable.setLayerInset(1, width , width, width, width );
//        layerDrawable.setLayerInset(2, width* 11, width * 11, width * 11, width * 11);
//        width=14;
//        layerDrawable.setLayerInset(3, width * 12, width * 12, width * 12, width * 12);
//        imageView.setBackgroundDrawable(layerDrawable);
//        container.addView(v);
//        discObjectAnimator = ObjectAnimator.ofFloat(v, "rotation", 0, 360);
//        discObjectAnimator.setDuration(20000);
//        //使ObjectAnimator动画匀速平滑旋转
//        discObjectAnimator.setInterpolator(new LinearInterpolator());
//        //无限循环旋转
//        discObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        discObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
//        new Thread(){
//            @Override
//            public void run() {
////                while (true){
//                    if(PlayMusicService.play_flag!=false){
//                        ((Activity)context).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (!discObjectAnimator.isRunning())
//                                discObjectAnimator.start();
//                            }
//                        });
//
//                    }
////                }
//            }
//        }.start();
        container.addView(v);
        return v;
    }

    private void setBackGround(final String pic_url,final View imageView) {

        if (pic_url == null) {
            Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.al_nopic);
            drawable3 = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            drawable3.setCircular(true);
            drawable3.setAntiAlias(true);
            layers[0] = drawable0;
            layers[1] = drawable1;
            layers[2] = drawable2;
            layers[3] = drawable3;
            final  LayerDrawable  layerDrawable = new LayerDrawable(layers);
            int width = 15;
            layerDrawable.setLayerInset(1, width, width, width, width);
            layerDrawable.setLayerInset(2, width * 11, width * 11, width * 11, width * 11);
            width = 14;
            layerDrawable.setLayerInset(3, width * 12, width * 12, width * 12, width * 12);
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setBackgroundDrawable(layerDrawable);
                }
            });
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Bitmap bitmap=ImageLoader.getInstance().loadImageSync(pic_url,options);
                    Drawable[] layers = new Drawable[4];
                    RoundedBitmapDrawable   drawable3 = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
                    drawable3.setCircular(true);
                    drawable3.setAntiAlias(true);
                    layers[0] = drawable0;
                    layers[1] = drawable1;
                    layers[2] = drawable2;
                    layers[3] = drawable3;
                    final  LayerDrawable  layerDrawable = new LayerDrawable(layers);
                    int width = 15;
                    layerDrawable.setLayerInset(1, width, width, width, width);
                    layerDrawable.setLayerInset(2, width * 11, width * 11, width * 11, width * 11);
                    width = 14;
                    layerDrawable.setLayerInset(3, width * 12, width * 12, width * 12, width * 12);
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setBackgroundDrawable(layerDrawable);
                        }
                    });
                    //imageView，你要显示的imageview控件对象，布局文件里面//配置的



                }
            }.start();

        }

    }


}
