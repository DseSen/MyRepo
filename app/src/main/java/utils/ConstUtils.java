package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


import java.lang.reflect.Field;

import jsonjavabean.User;
import mymusicplayer.MainActivity;
import mymusicplayer.PlayActivity;
import service.PlayMusicService;

/**
 * Created by 啊丁 on 2017/3/28.
 */

public class ConstUtils {
    public static final int STATE_PLAY = 0x110;
    public static final int STATE_PAUSE = 0x111;
    public static final int STATE_NEXT = 0x112;
    public static final int STATE_LAST = 0x128;
    public static final int STATE_BACK = 0x113;
    public static final int STATE_STOP = 0x114;
    public static final int STATE_NOTIFY=0x115;
    public static final int STATE_CHANGE=0x116;
    public static final int TYPE_RANK=0x117;
    public static final int TYPE_NEWALUBM=0x118;
    public static final int TYPE_NEWSONG=0x119;
    public static final int TYPE_HOTSINGER=0x120;
    public static final int TYPE_SEARCHSONGS=0x121;
    public static final int TYPE_DOWNLOADSONGS=0x132;
    public static final int TYPE_HEAD=0x122;
    public static final int TYPE_CREATELIST=0x123;
    public static final int TYPE_COLLECTLIST=0x124;
    public static final int TYPE_LOCAL=0x125;
    public static final int STATE_LOCAL_CHANGE=0x126;
    public static final int TYPE_SERVERLIST=0x127;
    public static final int TYPE_SONGLISTS=0x128;
    public static final int PLAYMOD_SEQUENCE=0x129;
    public static final int PLAYMOD_DISORDER=0x130;
    public static final int PLAYMOD_SINGLE=0x131;
    public static final String MYSERVER_URL="192.168.43.207";
    public static void login(final Context context, String loginname, String pwd){
        RequestParams requestParams=new RequestParams("http://"+MYSERVER_URL+":8080/musicPlayerServer/login.do?loginname="+loginname+"&pwd="+pwd);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (result.equals("")){
                    Toast.makeText(context,"用户名或密码错误",Toast.LENGTH_LONG).show();
                }else {
                   User user = new Gson().fromJson(result, new TypeToken<User>() {
                    }.getType());
                    PlayMusicService.isLogin=true;
                    PlayMusicService.user=user;
                    MainActivity.actionStart(context);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("onSuccess: ","网络请求失败" );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar =context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    /*设备屏幕宽度*/
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /*设备屏幕高度*/
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static Drawable getForegroundDrawable(Bitmap bitmap,Context context) {
    /*得到屏幕的宽高比，以便按比例切割图片一部分*/

        final float widthHeightSize = (float) (ConstUtils.getScreenWidth(context)
                *1.0 / ConstUtils.getScreenHeight(context) * 1.0);

//        Bitmap bitmap = getForegroundBitmap(lbitmap,context);
        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidth2 = (int) ((bitmap.getWidth() - cropBitmapWidth) /1.5);

    /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidth2, 0, cropBitmapWidth2,
                bitmap.getHeight());
    /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 20, bitmap
                .getHeight() / 30, false);
    /*模糊化*/
        final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 2, true);

        final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
    /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int px2dip(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

//    public static Bitmap getForegroundBitmap(Bitmap bitmap,Context context) {
//        int screenWidth = ConstUtils.getScreenWidth(context);
//        int screenHeight = ConstUtils.getScreenHeight(context);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        bitmap=options.inBitmap;
//        int imageWidth = bitmap.getWidth();
//        int imageHeight = bitmap.getHeight();
//
//        if (imageWidth < screenWidth && imageHeight < screenHeight) {
//            return BitmapFactory.decodeResource(context.getResources(), musicPicRes);
//        }
//
//        int sample = 2;
//        int sampleX = imageWidth / ConstUtils.getScreenWidth(context);
//        int sampleY = imageHeight / ConstUtils.getScreenHeight(context);
//
//        if (sampleX > sampleY && sampleY > 1) {
//            sample = sampleX;
//        } else if (sampleY > sampleX && sampleX > 1) {
//            sample = sampleY;
//        }
//
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = sample;
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//
//        return BitmapFactory.decodeResource(context.getResources(), musicPicRes, options);
//    }
}
