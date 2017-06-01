package mymusicplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import adapter.MySongListsRecyclerAdapter;
import fragment.PlayMusicItemFragment;
import jsonjavabean.HotSinger;
import jsonjavabean.NewAlbum;
import jsonjavabean.NewMusic;
import service.PlayMusicService;
import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/4/18.
 */

public class RecommentActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    Toolbar toolbar;
    int type;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomment);
        recyclerView= (RecyclerView) findViewById(R.id.activity_recomment_recyclerview);
        toolbar= (Toolbar) findViewById(R.id.activity_recomment_toolbar);
        toolbar.setNavigationIcon(R.drawable.lz);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        type=getIntent().getIntExtra("type",0);
        switch (type){
            case ConstUtils.TYPE_NEWALUBM:{
                RequestParams requestParams=new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=ppzs&operator=0&method=baidu.ting.plaza.getRecommendAlbum&format=json&offset=0&limit=50");
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        NewAlbum newAlbum=new Gson().fromJson(result,new TypeToken<NewAlbum>(){}.getType());
                        setRecyclerView(newAlbum.getPlaze_album_list().getRM().getAlbum_list().getList(),ConstUtils.TYPE_NEWALUBM,"新碟上架");
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
            case ConstUtils.TYPE_HOTSINGER:{
                RequestParams requestParams=new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.9.1&channel=ppzs&operator=0&method=baidu.ting.artist.getList&format=json&offset=0&limit=50&order=1&area=0&sex=0");
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        HotSinger hotSinger=new Gson().fromJson(result,new TypeToken<HotSinger>(){}.getType());
                        setRecyclerView(hotSinger.getArtist(),ConstUtils.TYPE_HOTSINGER,"热门歌手");
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
                Log.e( "onCreate: ", "TYPE_NEWSONG");
                RequestParams requestParams=new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.plaza.getNewSongs&format=json&limit=20");
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e( "onSuccess: ", "TYPE_NEWSONG");
                        NewMusic newMusic=new Gson().fromJson(result,new TypeToken<NewMusic>(){}.getType());
                        setRecyclerView(newMusic.getSong_list(),ConstUtils.TYPE_NEWSONG,"热门歌曲");
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
        }
        if (PlayMusicService.list!=null){
            recyclerView.setPadding(0,0,0, ConstUtils.dip2px(this,50));
        }
    }
    public static void actionStart(Context context,int type){
        Intent intent=new Intent(context,RecommentActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    private void setRecyclerView(List list,int type,String toolbarTitle){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(RecommentActivity.this, 2);
        gridLayoutManager.setOrientation(GridLayout.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new MySongListsRecyclerAdapter(RecommentActivity.this,list,type));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos=parent.getChildAdapterPosition(view);
                if(pos%2==0){
                    outRect.right=3;
                }else if (pos%2==1){
                    outRect.left=3;
                }
                outRect.top=10;
                outRect.bottom=10;
            }
        });
        toolbar.setTitle(toolbarTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PlayMusicService.list != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_recomment_framelayout, new PlayMusicItemFragment(), "inRecommentSongs").commit();
        }
    }
}
