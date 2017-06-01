package fragment;

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
import jsonjavabean.LocalMusic;
import mymusicplayer.PlayActivity;
import mymusicplayer.RankSongsActivity;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.MusicUtils;
import utils.SortByName;


public class LocalMusicAlubmFragment extends Fragment {
    View view;
    List<LocalMusic> musicList;
    List<String> folderName;
    ListView listView;
    Map<String, List<LocalMusic>> fileList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.localmusic_fragment,container,false);
        musicList= MusicUtils.getMusicData(getContext());
        folderName=new ArrayList<String>();
        listView = (ListView) view.findViewById(R.id.localmusic_fragment_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<LocalMusic> list= (ArrayList<LocalMusic>) fileList.get(folderName.get(position));
                RankSongsActivity.actionStart(getContext(), ConstUtils.TYPE_LOCAL,folderName.get(position),list);
            }
        });

        try {
            fileList= (Map<String, List<LocalMusic>>) groupSinger(musicList);
            listView.setAdapter(new MyLocalMusicAdapter(getContext(), folderName,fileList,1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
    private Map<String, List<LocalMusic>> groupSinger(List<LocalMusic> musics) throws Exception {
        Map<String, List<LocalMusic>> resultMap = new HashMap<String, List<LocalMusic>>();

        try {
            for (LocalMusic music : musics) {
//                String[] s=music.getPath().toString().split("/");
//                String path="";
//                for (int i=0;i<s.length-1;i++) {
//                   path +=s[i]+"/";
//                }
                String path=music.getAlbum();

                if (resultMap.containsKey(path) ){
                    //map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中
                    //把相同文件夹的歌曲放入同一个集合中
                    resultMap.get(path).add(music);
                } else {
                    //map中不存在，新建key，用来存放数据
                    List<LocalMusic> musicList = new ArrayList<LocalMusic>();
                    folderName.add(path);
                    musicList.add(music);
                    resultMap.put(path, musicList);
                }
            }
            Collections.sort(folderName,new SortByName());
        } catch (Exception e) {
            throw new Exception("按照异常批次号对已开单数据进行分组时出现异常", e);
        }
        return resultMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PlayMusicService.list!=null){
            listView.setPadding(0,0,0, ConstUtils.dip2px(getContext(),50));
        }
    }
}
