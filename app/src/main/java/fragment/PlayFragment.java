package fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.MyDialogListviewAdapter;
import adapter.MyPlayFragmentListViewAdapter;
import jsonjavabean.LocalMusic;
import jsonjavabean.MusicList;
import mymusicplayer.DownloadActivity;
import mymusicplayer.LocalMusicActivity;
import mymusicplayer.PlayActivity;
import mymusicplayer.RankSongsActivity;
import service.PlayMusicService;
import utils.ConstUtils;

public class PlayFragment extends Fragment {
    ListView listViewSet, listviewList;
    int[] head = {R.drawable.ic_mymusic_local_normal, R.drawable.ic_mymusic_download_normal};
    String[] func = {"本地音乐", "下载管理"};
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> listl = new ArrayList<Map<String, Object>>();
    EditText et_addList;
    MyDialogListviewAdapter myDialogListviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_play, null);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        myDialogListviewAdapter = new MyDialogListviewAdapter(getContext(), listl);
        listviewList.setAdapter(myDialogListviewAdapter);
        if (PlayMusicService.user != null) {
            Log.e("onViewCreated: ", "执行");
            getMusicList();
            listviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        et_addList = new EditText(getContext());
                        et_addList.setHint("请输入歌单名");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("增加歌单");
                        builder.setView(et_addList);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            String url = "http://" + ConstUtils.MYSERVER_URL + ":8080/musicPlayerServer/addMusicList.do";

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                RequestParams requestParams = new RequestParams(url);
                                requestParams.addBodyParameter("listName", et_addList.getText().toString());
                                requestParams.addBodyParameter("userId", PlayMusicService.user.getId() + "");
                                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        if (result.equals("Y")) {
                                            Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("musicList", new MusicList(et_addList.getText().toString()));
                                            map.put("count", 0);
                                            listl.add(map);
                                            myDialogListviewAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {
                                        Log.e("onError: ", "SHIBAI");
                                    }

                                    @Override
                                    public void onCancelled(CancelledException cex) {

                                    }

                                    @Override
                                    public void onFinished() {

                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.create().show();

                    } else {
                        Map<String, Object> map = listl.get(position - 1);
                        RankSongsActivity.actionStart(getContext(), ConstUtils.TYPE_SERVERLIST, ((MusicList) map.get("musicList")).getId() + "");
                    }
                }
            });
            listviewList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setTitle("删除列表？");
                    builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url="http://" + ConstUtils.MYSERVER_URL + ":8080/musicPlayerServer/deleteMusicList.do";
                            RequestParams requestParams=new RequestParams();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    return true;
                }
            });
        }
    }

    private void getMusicList() {
        String url_2 = "http://" + ConstUtils.MYSERVER_URL + ":8080/musicPlayerServer/getMusicList.do?userId=" + PlayMusicService.user.getId();
        RequestParams requestParams = new RequestParams(url_2);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            List<MusicList> lists;
            Map<String, Object> map;

            @Override
            public void onSuccess(String result) {
                lists = new Gson().fromJson(result, new TypeToken<List<MusicList>>() {
                }.getType());
                for (int i = 0; i < lists.size(); i++) {
                    String url_1 = "http://" + ConstUtils.MYSERVER_URL + ":8080/musicPlayerServer/getMusicCount.do?listId=" + lists.get(i).getId();
                    RequestParams req = new RequestParams(url_1);
                    final int finalI = i;
                    x.http().get(req, new CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            map = new HashMap<String, Object>();
                            map.put("musicList", lists.get(finalI));
                            map.put("count", result);
                            listl.add(map);
                            if (finalI == lists.size() - 1) {
                                myDialogListviewAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e("onError: ", "失败");
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("onError: ", "sshibai");


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewSet = (ListView) view.findViewById(R.id.fragment_play_listview);
        listviewList = (ListView) view.findViewById(R.id.fragment_play_musiclist_listview);
        for (int i = 0; i < func.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("head", head[i]);
            map.put("func", func[i]);
            list.add(map);
        }
        MyPlayFragmentListViewAdapter myPlayFragmentListViewAdapter = new MyPlayFragmentListViewAdapter(getContext(), list, ConstUtils.TYPE_HEAD);
        listViewSet.setAdapter(myPlayFragmentListViewAdapter);
        listViewSet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
//                        RankSongsActivity.actionStart(getContext(), ConstUtils.TYPE_LOCAL, 0 + "");
                        LocalMusicActivity.actionStart(getContext());
                        getActivity().overridePendingTransition(0, 0);
                        break;
                    }

                    case 1: {
                        DownloadActivity.actionStart(getContext());
                        getActivity().overridePendingTransition(0, 0);
                    }
                }
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();

        listl.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        list.clear();
    }
}
