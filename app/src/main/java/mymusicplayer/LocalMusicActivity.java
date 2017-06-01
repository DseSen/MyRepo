package mymusicplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentAdapter;
import fragment.LocalMusicAllFragment;
import fragment.LocalMusicAlubmFragment;
import fragment.LocalMusicFolderFragment;
import fragment.LocalMusicSingerFragment;
import fragment.PlayMusicItemFragment;
import jsonjavabean.LocalMusic;
import service.PlayMusicService;
import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/4/12.
 */

public class LocalMusicActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{
    private RadioGroup radioGroup;
    //红色的线
    private RelativeLayout navProgress;
    private ViewPager viewPager;
    private TextView textView;
    private int width;
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localmusic);
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new LocalMusicAllFragment());
        fragments.add(new LocalMusicSingerFragment());
        fragments.add(new LocalMusicAlubmFragment());
        fragments.add(new LocalMusicFolderFragment());
        viewPager = (ViewPager) findViewById(R.id.activity_localmusic_viewpager);
        navProgress = (RelativeLayout) findViewById(R.id.activity_localmusic_nav_progress);
        radioGroup = (RadioGroup) findViewById(R.id.activity_localmusic_rg);
        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
        setRbColor();
        radioGroup.setOnCheckedChangeListener(this);
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
        //添加红色那个滚动条
        textView = new TextView(this);
        //获取屏幕的宽
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        width = point.x;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width / 4, 5);
        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        textView.setLayoutParams(layoutParams);
        navProgress.addView(textView);

        toolbar= (Toolbar) findViewById(R.id.activity_localmusic_toolbar);
        toolbar.setTitle("本地音乐");
        toolbar.setNavigationIcon(R.drawable.lz);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int left = (position*width+positionOffsetPixels)/4;
        navProgress.setPadding(left,0,0,0);
    }

    @Override
    public void onPageSelected(int position) {
        ((RadioButton)radioGroup.getChildAt(position)).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    /**
     * 根据是否选中的状态设置显示字体的颜色
     * */
    private void  setRbColor(){
        for (int i=0;i<radioGroup.getChildCount();i++){
            RadioButton radioButton = ((RadioButton)radioGroup.getChildAt(i));
            if(radioButton.isChecked()){
                radioButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            }else{
                radioButton.setTextColor(getResources().getColor(R.color.colorUnClick2));
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.activity_localmusic_rb_all:
                viewPager.setCurrentItem(0);
                setRbColor();
                break;
            case R.id.activity_localmusic_rb_singer:
                viewPager.setCurrentItem(1);
                setRbColor();
                break;
            case R.id.activity_localmusic_rb_album:
                viewPager.setCurrentItem(2);
                setRbColor();
                break;
            case R.id.activity_localmusic_rb_folder:
                viewPager.setCurrentItem(3);
                setRbColor();
                break;
        }
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context, LocalMusicActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PlayMusicService.list != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.localmusic_framelayout, new PlayMusicItemFragment(), "inLockSongs").commit();

        }
    }
}
