package adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import jsonjavabean.HotSinger;
import jsonjavabean.NewAlbum;
import jsonjavabean.NewMusic;
import mymusicplayer.RecommentActivity;
import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/4/1.
 */

public class MyRecommentListViewAdapter extends BaseAdapter {
    private Context context;
    private List list;

    public MyRecommentListViewAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.recomment_musiclist_listview_item, null);
            holder.recyclerView = (RecyclerView) convertView.findViewById(R.id.recomment_musiclist_listview_item_recyclerview);
            holder.textView = (TextView) convertView.findViewById(R.id.recomment_musiclist_listview_item_text);
            holder.button = (Button) convertView.findViewById(R.id.recomment_musiclist_listview_item_btn);
            holder.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int pos=parent.getChildAdapterPosition(view);
                    if(pos==1||pos==4){
                        outRect.left=6;
                        outRect.right=6;
                    }
                    outRect.bottom=10;
                }
            });
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String url = null;
        switch (position) {
            case 0:
                url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=ppzs&operator=0&method=baidu.ting.plaza.getRecommendAlbum&format=json&offset=0&limit=6";
                break;
            case 1:
                url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.plaza.getNewSongs&format=json&limit=6";
                break;
            case 2:
                url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.9.1&channel=ppzs&operator=0&method=baidu.ting.artist.getList&format=json&offset=0&limit=6&order=1&area=0&sex=0";
                break;

        }


        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("wd", "xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                switch (position) {
                    case 0: {
                        NewAlbum newAlbum = new Gson().fromJson(result, new TypeToken<NewAlbum>() {
                        }.getType());
                        list = newAlbum.getPlaze_album_list().getRM().getAlbum_list().getList();
                        setViewPlace("新碟上架",position,holder,list);
                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RecommentActivity.actionStart(context, ConstUtils.TYPE_NEWALUBM);
                            }
                        });
                    }
                    break;
                    case 1: {
                        NewMusic newMusic=new Gson().fromJson(result,new TypeToken<NewMusic>(){}.getType());
                        list=newMusic.getSong_list();
                        setViewPlace("新歌速递",position,holder,list);
                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RecommentActivity.actionStart(context, ConstUtils.TYPE_NEWSONG);
                            }
                        });
                    }
                    break;
                    case 2: {
                        HotSinger hotSinger=new Gson().fromJson(result,new TypeToken<HotSinger>(){}.getType());
                        list=hotSinger.getArtist();
                        setViewPlace("热门歌手",position,holder,list);
                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RecommentActivity.actionStart(context, ConstUtils.TYPE_HOTSINGER);
                            }
                        });
                    }
                    break;
                    default:
                        break;
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


        return convertView;
    }

    
    class ViewHolder {
        RecyclerView recyclerView;
        TextView textView;
        Button button;
    }

    private void setViewPlace(String title,int position,ViewHolder holder,List list){


        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        gridLayoutManager.setOrientation(GridLayout.VERTICAL);

        holder.recyclerView.setLayoutManager(gridLayoutManager);
        holder.recyclerView.setAdapter(new MyRecommentListViewRecyclerViewAdapter(context, list, position));
        holder.textView.setText(title);
    }
}
