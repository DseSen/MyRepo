package controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import jsonjavabean.DownLoadBean;
import modle.SqliteHelper;


/**
 * Created by 啊丁 on 2017/5/4.
 */

public class DatabaseController {
    SqliteHelper sqliteHelper;
    SQLiteDatabase sqLiteDatabase;
    public DatabaseController(Context context, int version){
        sqliteHelper=new SqliteHelper(context,"birthManager.db",null,version);
        sqLiteDatabase=sqliteHelper.getWritableDatabase();
    }
    public void closeDatabase(){
        sqLiteDatabase.close();
    }
    public void createBirth(){
        sqLiteDatabase.execSQL("create table if not exists birth (_id integer primary key autoincrement,name text,sex text,birth text,phone text,remind integer,relation text,pic blob)");
    }



    public void createDownload(){
        sqLiteDatabase.execSQL("create table if not exists download (_id integer primary key autoincrement,name text,progress text,max text,author text,source text)");
    }
    public void addDownload(String name,String author,String source){
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("author",author);
        contentValues.put("source",source);
        sqLiteDatabase.insert("download",null,contentValues);
    }
    public void updateProgress(String name,String progress,String max){
        ContentValues contentValues=new ContentValues();
        contentValues.put("progress",progress);
        contentValues.put("max",max);
        sqLiteDatabase.update("download",contentValues,"name=?",new String[]{name});
    }
    public List<DownLoadBean> getDownload(){
       Cursor cursor=sqLiteDatabase.query("download",null,null,null,null,null,null);

        List<DownLoadBean> list=new ArrayList<>();
        while (cursor.moveToNext()){
            DownLoadBean downLoadBean=new DownLoadBean();
            downLoadBean.setName(cursor.getString(cursor.getColumnIndex("name")));
            downLoadBean.setMax(cursor.getString(cursor.getColumnIndex("max")));
            downLoadBean.setProgress(cursor.getString(cursor.getColumnIndex("progress")));
            downLoadBean.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            downLoadBean.setSource(cursor.getString(cursor.getColumnIndex("source")));
            list.add(downLoadBean);
        }
        return list;
    }

    public List<DownLoadBean> getDownloading(){
      Cursor cursor=  sqLiteDatabase.rawQuery("select* from download where progress<max;",null);
        List<DownLoadBean> list=new ArrayList<>();
        while (cursor.moveToNext()){

            DownLoadBean downLoadBean=new DownLoadBean();
            downLoadBean.setName(cursor.getString(cursor.getColumnIndex("name")));
            downLoadBean.setMax(cursor.getString(cursor.getColumnIndex("max")));
            downLoadBean.setProgress(cursor.getString(cursor.getColumnIndex("progress")));
            list.add(downLoadBean);
        }
        return list;
    }
}
