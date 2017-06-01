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
import android.widget.ListView;

import com.example.myapplication.R;

import java.util.List;

import adapter.MyDownloadListViewAdapter;
import controller.DatabaseController;
import jsonjavabean.DownLoadBean;

/**
 * Created by 啊丁 on 2017/5/20.
 */

public class DownloadingFragment extends Fragment {
    ListView listView;
    MyDownloadListViewAdapter downloadListViewAdapter;
    List<DownLoadBean> list;
    DatabaseController databaseController;
    LocalBroadcastManager localBroadcastManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        databaseController=new DatabaseController(getContext(),1);
        localBroadcastManager= LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter=new IntentFilter();
        filter.addAction("receive");
        MyReceiver myReceiver= new MyReceiver();

        localBroadcastManager.registerReceiver(myReceiver,filter);
        return inflater.inflate(R.layout.fragment_download,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= (ListView) view.findViewById(R.id.downlaod_listview);
        databaseController=new DatabaseController(getContext(),1);
        list=databaseController.getDownloading();
        downloadListViewAdapter=new MyDownloadListViewAdapter(getContext(),list);
        listView.setAdapter(downloadListViewAdapter);
    }
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e( "onReceive: ","收到广播" );
            String name=intent.getStringExtra("name");
            list= databaseController.getDownloading();
            downloadListViewAdapter=new MyDownloadListViewAdapter(getContext(),list);
            listView.setAdapter(downloadListViewAdapter);
//            downloadListViewAdapter.notifyDataSetChanged();
        }
    }
}
