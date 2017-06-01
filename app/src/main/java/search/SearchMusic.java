package search;


import android.app.ActionBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import adapter.MyRankSongsListViewAdapter;
import fragment.PlayMusicItemFragment;
import jsonjavabean.NewMusic;
import jsonjavabean.SearchAdvice;
import jsonjavabean.SearchSongs;
import jsonjavabean.Songs;
import mymusicplayer.MainActivity;
import service.PlayMusicService;
import utils.ConstUtils;

public class SearchMusic extends AppCompatActivity implements TextWatcher {
    EditText searchEdit;
    PopupWindow popupWindow;
    ListView popupListView, searchListView;
    SearchSongs searchSongs;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        view = getLayoutInflater().inflate(R.layout.search_ppopup, null);
        init();
    }

    @Override
    protected void onResume() {
        if (PlayMusicService.list != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.search_framelaout_playitem, new PlayMusicItemFragment(), "inSearch").commit();
        }
        super.onResume();
    }

    private void init() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        searchEdit = (EditText) findViewById(R.id.search_et);
        searchEdit.addTextChangedListener(this);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (searchEdit.getText().toString().length() == 0) {
                    Log.e("onEditorAction: ", "触发");
                } else {
                    if (actionId == EditorInfo.IME_ACTION_SEND
                            || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        Log.e("onEditorAction: ", "触发2");
                        RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query=" + searchEdit.getText() + "&page_no=1&page_size=30");
                        getSongList(requestParams);
                    }
                }
                return true;
            }
        });
        searchListView = (ListView) findViewById(R.id.search_listview);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final SearchSongs.SongListBean songListBean = searchSongs.getSong_list().get(position);
                String songId = songListBean.getSong_id();
                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&callback=&from=webapp_music&method=baidu.ting.song.playAAC&songid=" + songId);
                requestParams.addQueryStringParameter("name", "value");
                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("onSuccess: ", "网络成功");
                        Songs songs = new Gson().fromJson(result, new TypeToken<Songs>() {
                        }.getType());
                        String url = songs.getBitrate().getFile_link();
                        Intent intent = new Intent();
                        intent.setAction("playReceive");
                        intent.putExtra("play", ConstUtils.STATE_PLAY);
                        intent.putExtra("position", 0);
                        intent.putExtra("url", url);
                        List<Songs> songses = new ArrayList<Songs>();
                        songses.add(songs);
                        PlayMusicService.list = songses;
                        PlayMusicService.randomList = songses;
                        PlayMusicService.type = ConstUtils.TYPE_SEARCHSONGS;
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_framelaout_playitem, new PlayMusicItemFragment(), "inSearchSongs").commit();
                        Log.e("onSuccess: ", "转换成功");
                        sendBroadcast(intent);


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
        });
        popupListView = (ListView) view.findViewById(R.id.search_popup_listview);
        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) ((ViewGroup) parent.getChildAt(position)).getChildAt(0);
                RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query=" + tv.getText() + "&page_no=1&page_size=30");
                getSongList(requestParams);
            }
        });
    }

    public void onBackToMain(View v) {
        finish();
        overridePendingTransition(0, 0);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchMusic.class);
        context.startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (popupWindow == null) {

            DisplayMetrics dm = getResources().getDisplayMetrics();
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(true);
            ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
            popupWindow.setBackgroundDrawable(dw);
            popupWindow.setAnimationStyle(R.style.contextMenuAnim2);
            popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindow.showAsDropDown(searchEdit, 0, 0);
        } else {
            popupWindow.showAsDropDown(searchEdit, 0, 0);
        }
        if (searchEdit.getText().toString().length() == 0) {
            popupWindow.dismiss();
        } else {
            RequestParams requestParams = new RequestParams("http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.suggestion&query=" + searchEdit.getText() + "&format=json&from=ios&version=2.1.1");
            requestParams.addQueryStringParameter("name", "value");
            x.http().get(requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    SearchAdvice searchAdvice = new Gson().fromJson(result, new TypeToken<SearchAdvice>() {
                    }.getType());
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SearchMusic.this, R.layout.search_popup_listview_item, R.id.search_popup_listview_text, searchAdvice.getSuggestion_list());
                    Log.e("onSuccess: ", searchAdvice.getSuggestion_list().get(0));
                    popupListView.setAdapter(arrayAdapter);
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

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void getSongList(RequestParams requestParams) {

        requestParams.addQueryStringParameter("name", "value");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                searchSongs = new Gson().fromJson(result, new TypeToken<SearchSongs>() {
                }.getType());
                MyRankSongsListViewAdapter myRankSongsListViewAdapter = new MyRankSongsListViewAdapter(searchSongs.getSong_list(), SearchMusic.this, ConstUtils.TYPE_SEARCHSONGS);
                searchListView.setAdapter(myRankSongsListViewAdapter);
                popupWindow.dismiss();
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
