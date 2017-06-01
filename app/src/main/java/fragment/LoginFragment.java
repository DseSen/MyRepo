package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import jsonjavabean.User;
import mymusicplayer.MainActivity;
import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/4/10.
 */

public class LoginFragment extends Fragment {
    EditText loginname,pwd;
    Button login;
    TextView regist;
    User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login_fragment_login,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loginname= (EditText) view.findViewById(R.id.activity_login_loginname);
        pwd= (EditText) view.findViewById(R.id.activity_login_pwd);
        login= (Button) view.findViewById(R.id.activity_login_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstUtils.login(getContext(),loginname.getText()+"",pwd.getText()+"");
            }
        });
        regist= (TextView) view.findViewById(R.id.activity_login_regist);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_login_framelayout,new RegistFragment()).addToBackStack(null).commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
