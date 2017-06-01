package mymusicplayer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myapplication.R;

import adapter.MyFragmentAdapter;
import adapter.MySideslipListViewAdapter;
import fragment.FriendFragment;
import fragment.PlayFragment;
import fragment.PlayMusicItemFragment;
import fragment.RecommentFragment;
import jsonjavabean.MusicList;
import jsonjavabean.User;
import search.SearchMusic;
import service.PlayMusicService;
import utils.ConstUtils;
import utils.MusicUtils;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    ViewPager viewPager;
    FragmentManager fm;
    List<Fragment> list;
    MySideslipListViewAdapter adapter;
    User user=PlayMusicService.user;
    Button btn_toPlayPage, btn_toMusicList, btn_toFriend;
    DrawerLayout drawerLayout;
    ListView sideslip_listView;
    List<Map<String, Object>> sideslip_list = new ArrayList<Map<String, Object>>();
    int[] images = {R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher, R.mipmap.ic_launcher_round};
    String[] sets = {"", "", "", "退出"};


//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        user= (User) intent.getSerializableExtra("user");
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        MyFragmentAdapter fadapter = new MyFragmentAdapter(fm, list);
        viewPager.setAdapter(fadapter);
        viewPager.setCurrentItem(1);



    }

    @Override
    protected void onStart() {
        super.onStart();
        createSideslip();


    }

    private void createSideslip() {
        sideslip_list=new ArrayList<Map<String, Object>>();
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", 0);
            map.put("image", PlayMusicService.user.getHead());
            map.put("text", PlayMusicService.user.getUsername());
            sideslip_list.add(map);
        }
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("text", sets[i]);
            map.put("type", 1);
            sideslip_list.add(map);
        }
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", 0);
            map.put("text", 0+"");
            map.put("type", 2);
            sideslip_list.add(map);

        }
        for (int i = 2; i < 4; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("text", sets[i]);
            map.put("type", 1);
            sideslip_list.add(map);
        }
        adapter = new MySideslipListViewAdapter(this, sideslip_list);
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) sideslip_listView.getLayoutParams();
        layoutParams.topMargin = -ConstUtils.getStatusBarHeight(MainActivity.this);
        sideslip_listView.setAdapter(adapter);
        sideslip_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 5:
                        sendBroadcast(new Intent("closeNotifi"));
                        stopService(new Intent(MainActivity.this, PlayMusicService.class));
                        finish();

                        break;
                }
            }
        });

    }
    @Override
    protected void onResume() {
        if (PlayMusicService.list!=null){
            fm.beginTransaction().replace(R.id.main_framelaout_playitem, new PlayMusicItemFragment(), "inMain").commit();
        }
        super.onResume();

    }

    public void init() {
        Intent intent = new Intent(this, PlayMusicService.class);
        startService(intent);

        sideslip_listView = (ListView) findViewById(R.id.sideslip_listview);

        viewPager = (ViewPager) findViewById(R.id.mainviewpager);
        viewPager.addOnPageChangeListener(this);

        btn_toPlayPage = (Button) findViewById(R.id.title_btn_play);
        btn_toFriend = (Button) findViewById(R.id.title_btn_friend);
        btn_toMusicList = (Button) findViewById(R.id.title_btn_musiclist);
        btn_toFriend.setOnClickListener(this);
        btn_toPlayPage.setOnClickListener(this);
        btn_toMusicList.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer);
        sideslip_listView = (ListView) findViewById(R.id.sideslip_listview);

        fm = getSupportFragmentManager();
        list = new ArrayList<Fragment>();
        list.add(new PlayFragment());
        list.add(new RecommentFragment());
//        list.add(new FriendFragment());
//        fm.beginTransaction().add(R.id.main_framelaout_playitem, new PlayMusicItemFragment(), "inMain").commit();

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
//        ((Activity)context).finish();

    }
//    public static void actionStart(Context context, User user) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("user",user);
//        context.startActivity(intent);
//        ((Activity)context).finish();

//    }

    public void onToSearch(View v) {
        SearchMusic.actionStart(this);
        overridePendingTransition(0, 0);
    }

    public void onSideslip(View v) {
        drawerLayout.openDrawer(Gravity.START);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        click(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void click(int id) {
        if (id == R.id.title_btn_play || id == 0) {
            viewPager.setCurrentItem(0);
            btn_toPlayPage.setTextColor(getResources().getColor(R.color.colorClick));
            btn_toMusicList.setTextColor(getResources().getColor(R.color.colorUnClick));
            btn_toFriend.setTextColor(getResources().getColor(R.color.colorUnClick));
        } else if (id == R.id.title_btn_musiclist || id == 1) {
            viewPager.setCurrentItem(1);
            btn_toPlayPage.setTextColor(getResources().getColor(R.color.colorUnClick));
            btn_toMusicList.setTextColor(getResources().getColor(R.color.colorClick));
            btn_toFriend.setTextColor(getResources().getColor(R.color.colorUnClick));
        } else if (id == R.id.title_btn_friend || id == 2) {
            viewPager.setCurrentItem(2);
            btn_toPlayPage.setTextColor(getResources().getColor(R.color.colorUnClick));
            btn_toMusicList.setTextColor(getResources().getColor(R.color.colorUnClick));
            btn_toFriend.setTextColor(getResources().getColor(R.color.colorClick));
        }
    }

    @Override
    public void onClick(View v) {
        click(v.getId());
    }


}
