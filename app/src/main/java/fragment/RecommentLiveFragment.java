package fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import adapter.MySongListsRecyclerAdapter;
import jsonjavabean.MvLists;
import jsonjavabean.SongLists;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.LoadMoreAdapter;

/**
 * Created by 啊丁 on 2017/3/29.
 */

public class RecommentLiveFragment extends Fragment {
    RecyclerView recyclerView;
    MySongListsRecyclerAdapter mySongListsRecyclerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recomment_live,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= (RecyclerView) view.findViewById(R.id.recomment_live_recyclerview);
        RequestParams requestParams=new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=ppzs&operator=0&provider=11%2C12&method=baidu.ting.mv.searchMV&format=json&order=1&page_num=1&page_size=50&query=%E5%85%A8%E9%83%A8");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("onSuccess: ","bbbbbbb" );
                MvLists songLists=new Gson().fromJson(result,new TypeToken<MvLists>(){}.getType());
                mySongListsRecyclerAdapter=new MySongListsRecyclerAdapter(getContext(),songLists.getResult().getMv_list(),ConstUtils.TYPE_SONGLISTS);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                gridLayoutManager.setOrientation(GridLayout.VERTICAL);
                recyclerView.setLayoutManager(gridLayoutManager);
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
                recyclerView.setAdapter(mySongListsRecyclerAdapter);
                if (PlayMusicService.list!=null){
                    recyclerView.setPadding(0,0,0, ConstUtils.dip2px(getContext(),50));
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
}
