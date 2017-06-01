package jsonjavabean;

import java.io.Serializable;

/**
 * Created by 啊丁 on 2017/4/11.
 */

public class MusicList implements Serializable{

    /**
     * id : 1
     * pic : null
     * listName : 我的最爱
     * userId : 1
     */

    private int id;
    private String pic;
    private String listName;
    private int userId;

    public MusicList(String listName){
        this.listName=listName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
