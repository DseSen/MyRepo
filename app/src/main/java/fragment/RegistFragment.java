package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import utils.ConstUtils;

/**
 * Created by 啊丁 on 2017/4/10.
 */

public class RegistFragment extends Fragment {
    EditText logingname,pwd,repwd,username;
    RadioGroup wm;
    Button regist;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login_fragment_regist,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        logingname= (EditText) view.findViewById(R.id.activity_login_fragment_regist_loginname);
        pwd= (EditText) view.findViewById(R.id.activity_login_fragment_regist_pwd);
        repwd= (EditText) view.findViewById(R.id.activity_login_fragment_regist_repwd);
        username= (EditText) view.findViewById(R.id.activity_login_fragment_regist_username);
        wm= (RadioGroup) view.findViewById(R.id.activity_login_fragment_regist_mw);
        regist= (Button) view.findViewById(R.id.activity_login_fragment_regist_btn);
        super.onViewCreated(view, savedInstanceState);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams requestParams=new RequestParams("http://"+ ConstUtils.MYSERVER_URL+":8080/musicPlayerServer/regist.do?loginname="+logingname.getText()+"&pwd="+pwd.getText()+"&sex="+((RadioButton)wm.findViewById(wm.getCheckedRadioButtonId())).getText()+"&username="+username.getText());
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("Y")){
                            ConstUtils.login(getContext(),logingname.getText()+"",pwd.getText()+"");
                            Toast.makeText(getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"注册失败",Toast.LENGTH_SHORT).show();
                        }
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
    }
}
