package jsonjavabean;

/**
 * Created by 啊丁 on 2017/4/12.
 */

public class MusicListItem {

    private int id;
    private String title;
    private String author;
    private String url;
    private String pic;
    private String lrc;
    private int listid;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getLrc() {
        return lrc;
    }
    public void setLrc(String lrc) {
        this.lrc = lrc;
    }
    public int getListid() {
        return listid;
    }
    public void setListid(int listid) {
        this.listid = listid;
    }
}
