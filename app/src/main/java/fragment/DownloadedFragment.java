package fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import adapter.MyDownloadListViewAdapter;
import adapter.MyRankSongsListViewAdapter;
import controller.DatabaseController;
import jsonjavabean.DownLoadBean;
import mymusicplayer.PlayActivity;
import mymusicplayer.RankSongsActivity;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.MusicUtils;

/**
 * Created by 啊丁 on 2017/5/20.
 */

public class DownloadedFragment extends Fragment {
    ListView listView;
    DatabaseController databaseController;
    LocalBroadcastManager localBroadcastManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        localBroadcastManager= LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter=new IntentFilter();
        filter.addAction("success");
        DownloadSuccessReceiver myReceiver= new DownloadSuccessReceiver();

        localBroadcastManager.registerReceiver(myReceiver,filter);

        return inflater.inflate(R.layout.fragment_download,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseController=new DatabaseController(getContext(),1);
        listView= (ListView) view.findViewById(R.id.downlaod_listview);
        listView.setAdapter(new MyRankSongsListViewAdapter(databaseController.getDownload(),getContext(), ConstUtils.TYPE_DOWNLOADSONGS));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<DownLoadBean> songs=databaseController.getDownload();
                String url = songs.get(position).getSource();
                Intent intent = new Intent();
                intent.setAction("playReceive");
                intent.putExtra("play", ConstUtils.STATE_PLAY);
                intent.putExtra("url", url);
                PlayMusicService.randomList = songs;
                if (PlayMusicService.playMod==ConstUtils.PLAYMOD_DISORDER){
                    List temp=new ArrayList();
                    int i=0;
                    while(temp.size() !=songs.size() ){

                        temp.add(songs.get(i));
                        i++;
                    }
                    PlayMusicService.list = MusicUtils.getRandomList(temp);
                    intent.putExtra("position", MusicUtils.getRandomPosition(position));
                }else{
                    PlayMusicService.list =songs;
                    intent.putExtra("position", position);
                }
                PlayMusicService.type = ConstUtils.TYPE_DOWNLOADSONGS;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.download_framelayout, new PlayMusicItemFragment(), "inDownloadSongs").commit();
                getContext().sendBroadcast(intent);
//                PlayActivity.actionStart(getContext());
                getActivity().overridePendingTransition(R.anim.playenter,R.anim.noaction);
            }
        });
    }
    class DownloadSuccessReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e( "onReceive: ","收到广播" );
            listView.setAdapter(new MyRankSongsListViewAdapter(databaseController.getDownload(),getContext(), ConstUtils.TYPE_DOWNLOADSONGS));
        }
    }
}
