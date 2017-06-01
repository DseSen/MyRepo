package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication.R;

import adapter.MyRecommentListViewAdapter;
import service.PlayMusicService;
import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/3/29.
 */

public class RecommentMusicListFragment extends Fragment {
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recomment_musiclist,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView= (ListView) view.findViewById(R.id.recomment_musiclist_listview);
        listView.setAdapter(new MyRecommentListViewAdapter(getContext()));
        if (PlayMusicService.list!=null){
            listView.setPadding(0,0,0, ConstUtils.dip2px(getContext(),50));
        }
        super.onViewCreated(view, savedInstanceState);

    }
}
