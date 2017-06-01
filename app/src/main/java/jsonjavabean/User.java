package jsonjavabean;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by 啊丁 on 2017/4/10.
 */

public class User implements Serializable{

    /**
     * id : 1
     * loginname : dse
     * pwd : 1994620
     * sex : 男
     * username : 1756fg
     * background : null
     * head : null
     * university : null
     * adress : null
     */

    private int id;
    private String loginname;
    private String pwd;
    private String sex;
    private String username;
    private Object background;
    private Object head;
    private Object university;
    private Object adress;
    private List<Map<String, Object>> musicList;

    public List<Map<String, Object>> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Map<String, Object>> musicList) {
        this.musicList = musicList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getBackground() {
        return background;
    }

    public void setBackground(Object background) {
        this.background = background;
    }

    public Object getHead() {
        return head;
    }

    public void setHead(Object head) {
        this.head = head;
    }

    public Object getUniversity() {
        return university;
    }

    public void setUniversity(Object university) {
        this.university = university;
    }

    public Object getAdress() {
        return adress;
    }

    public void setAdress(Object adress) {
        this.adress = adress;
    }
}
