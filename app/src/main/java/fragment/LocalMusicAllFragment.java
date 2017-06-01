package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.MyLocalMusicAdapter;
import adapter.MyRankSongsListViewAdapter;
import jsonjavabean.LocalMusic;
import jsonjavabean.MusicList;
import mymusicplayer.PlayActivity;
import mymusicplayer.RankSongsActivity;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.MusicUtils;
import utils.SortByName;


public class LocalMusicAllFragment extends Fragment {
    View view;
    List<LocalMusic> localMusicList;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.localmusic_fragment,null);

        listView = (ListView) view.findViewById(R.id.localmusic_fragment_listview);
        localMusicList= MusicUtils.getMusicData(getContext());
        listView.setAdapter(new MyRankSongsListViewAdapter(localMusicList,getContext(), ConstUtils.TYPE_LOCAL));
        listView.setDivider(getContext().getResources().getDrawable(R.color.listLineColor));
        listView.setDividerHeight(1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url=localMusicList.get(position).getFile_link();
                Intent intent=new Intent();
                intent.setAction("playReceive");
                intent.putExtra("play", ConstUtils.STATE_PLAY);
                intent.putExtra("position", position);
                intent.putExtra("url", url);
                PlayMusicService.randomList = localMusicList;
                if (PlayMusicService.playMod==ConstUtils.PLAYMOD_DISORDER){
                    List temp=new ArrayList();
                    int i=0;
                    while(temp.size() !=localMusicList.size() ){

                        temp.add(localMusicList.get(i));
                        i++;
                    }
                    PlayMusicService.list =MusicUtils.getRandomList(temp);
                    intent.putExtra("position", MusicUtils.getRandomPosition(position));
                }else{
                    PlayMusicService.list =localMusicList;
                    intent.putExtra("position", position);
                }
                PlayMusicService.type = ConstUtils.TYPE_LOCAL;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.localmusic_framelayout, new PlayMusicItemFragment(), "inLocalSongs").commit();
                getContext().sendBroadcast(intent);
//                PlayActivity.actionStart(getContext());
//                getActivity().overridePendingTransition(R.anim.playenter,R.anim.noaction);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PlayMusicService.list!=null){
            listView.setPadding(0,0,0, ConstUtils.dip2px(getContext(),50));
        }
    }
}
