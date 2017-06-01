package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import adapter.MyRankListViewAdapter;
import jsonjavabean.RankList;
import mymusicplayer.RankSongsActivity;
import service.PlayMusicService;
import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/3/29.
 */

public class RecommentRanksFragment extends Fragment implements ListView.OnItemClickListener{
    private ListView listView;
    private List<RankList.ContentBeanX> contentBeanXList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recomment_ranks,container,false);
        listView= (ListView) view.findViewById(R.id.recomment_ranks_listview);
        listView.setOnItemClickListener(this);
        init();
        return view;
    }
    private void init(){
        if (PlayMusicService.list!=null){
            listView.setPadding(0,0,0, ConstUtils.dip2px(getContext(),50));
        }
        RequestParams requestParams=new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=ppzs&operator=0&method=baidu.ting.billboard.billCategory&format=json&kflag=2");
        requestParams.addQueryStringParameter("name", "values");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                RankList rankList = new Gson().fromJson(result,new TypeToken<RankList>(){}.getType());
                contentBeanXList=rankList.getContent();
                MyRankListViewAdapter adapter=new MyRankListViewAdapter(getContext(),contentBeanXList);
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

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RankSongsActivity.actionStart(getContext(),ConstUtils.TYPE_RANK,contentBeanXList.get(position).getType()+"");
    }
}
