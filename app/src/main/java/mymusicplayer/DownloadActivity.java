package mymusicplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentAdapter;
import fragment.DownloadedFragment;
import fragment.DownloadingFragment;
import fragment.PlayMusicItemFragment;
import service.PlayMusicService;

/**
 * Created by 啊丁 on 2017/5/20.
 */

public class DownloadActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private RadioGroup radioGroup;
    //红色的线
    private RelativeLayout navProgress;
    private ViewPager viewPager;
    private TextView textView;
    private int width;
    Toolbar toolbar;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initView();
    }
    private void initView(){
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new DownloadedFragment());
        fragments.add(new DownloadingFragment());
        viewPager = (ViewPager) findViewById(R.id.activity_download_viewPager);
        navProgress = (RelativeLayout) findViewById(R.id.activity_download_nav);
        radioGroup = (RadioGroup) findViewById(R.id.activity_download_radiogroup);
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width / 2, 5);
        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        textView.setLayoutParams(layoutParams);
        navProgress.addView(textView);

        toolbar= (Toolbar) findViewById(R.id.activity_download_toolbar);
        toolbar.setTitle("下载管理");
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
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.activity_download_downloaded:
                viewPager.setCurrentItem(0);
                setRbColor();
                break;
            case R.id.activity_download_downloading:
                viewPager.setCurrentItem(1);
                setRbColor();
                break;
        }
    }
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int left = (position*width+positionOffsetPixels)/2;
        navProgress.setPadding(left,0,0,0);
    }

    @Override
    public void onPageSelected(int position) {
        ((RadioButton)radioGroup.getChildAt(position)).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context, DownloadActivity.class);
        context.startActivity(intent);

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (PlayMusicService.list != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.download_framelayout, new PlayMusicItemFragment(), "inDownloadSongs").commit();

        }
    }
}
