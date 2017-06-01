package mymusicplayer;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.myapplication.R;

import fragment.LoginFragment;

/**
 * Created by 啊丁 on 2017/4/10.
 */

public class LoginActivity extends AppCompatActivity {
    Toolbar mToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_login_framelayout,new LoginFragment()).commit();
        mToolbar= (Toolbar) findViewById(R.id.activity_login_toolbar);
        mToolbar.setTitle("登陆");
        mToolbar.setNavigationIcon(R.drawable.lz);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(mToolbar);
    }


}

