package jsonjavabean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;

/**
 * Created by 啊丁 on 2017/3/31.
 */

public class LocalMusic implements Parcelable{
    private String file_link;
//    private String pic_small;
    private String pic_big;
    private String lrc_link;
    private String title;
    private String author;
    private String album;
    private int musicId;

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    private int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    //    public Music( String pic_big, String lrc_link, String title, String author) {
//        this.pic_big = pic_big;
//        this.lrc_link = lrc_link;
//        this.title = title;
//        this.author = author;
//    }
//
    public String getFile_link() {
        return file_link;
    }
    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getLrc_link() {
        return lrc_link;
    }

    public void setLrc_link(String lrc_link) {
        this.lrc_link = lrc_link;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(file_link);
        dest.writeString(pic_big);
        dest.writeString(album);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(lrc_link);
        dest.writeInt(musicId);
    }
    public static final Parcelable.Creator<LocalMusic> CREATOR=new Parcelable.Creator<LocalMusic>(){

        @Override
        public LocalMusic createFromParcel(Parcel source) {
            LocalMusic localMusic=new LocalMusic();
            localMusic.file_link=source.readString();
            localMusic.pic_big=source.readString();
            localMusic.album=source.readString();
            localMusic.author=source.readString();
            localMusic.title=source.readString();
            localMusic.lrc_link=source.readString();
            localMusic.musicId=source.readInt();
            return localMusic;
        }

        @Override
        public LocalMusic[] newArray(int size) {
            return new LocalMusic[size];
        }
    };
}
