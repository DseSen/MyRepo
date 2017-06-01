package service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import controller.DatabaseController;

/**
 * Created by 啊丁 on 2017/5/20.
 */


public class DownloadService extends IntentService {
    public static boolean isDownload;
    DatabaseController databaseController;
    String name;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public DownloadService(String name) {
        super(name);
    }
    public DownloadService() {
        super("DownloadService");
    }
    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {

        name=intent.getStringExtra("name");
        String source=Environment.getExternalStorageDirectory() + "/MyAppMusic" + "/"+name+".mp3";
        String url=intent.getStringExtra("url");
        databaseController=new DatabaseController(getApplicationContext(),1);
        databaseController.createDownload();
        databaseController.addDownload(name,intent.getStringExtra("author"),source);
        Log.e("onHandleIntent: ", url);
        Log.e("onHandleIntent: ", name);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(source);
        Callback.Cancelable cancelable = null;


            cancelable = x.http().get(requestParams, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
                    intent.setAction("success");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                    isDownload=true;
                    Toast.makeText(getApplicationContext(), "开始下载", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    databaseController.updateProgress(name,current+"",total+"");

                    intent.setAction("receive");
                    intent.putExtra("name",name);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }

            });


    }
}
