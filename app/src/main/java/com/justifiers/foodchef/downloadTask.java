package com.justifiers.foodchef;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.downloader.PRDownloader;
import androidx.core.content.ContextCompat;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.Progress;
import com.downloader.Status;
import java.io.File;
import java.net.URI;
import java.nio.file.FileStore;

import static android.content.Context.DOWNLOAD_SERVICE;

public class downloadTask {
    private Context context;

    private String url = "";
    private String downloadFileName = "";
    private Uri uri;

    public static String dirPath;

    public downloadTask(Context context, String url){
        this.context = context;
        this.url = url;
        this.downloadFileName = url.substring(url.lastIndexOf('/'), url.length());
        uri = Uri.parse(url);

        dirPath = "C:\\Users\\rudra\\Desktop\\Android Project\\FoodChef\\app\\src\\main\\assets";

        PRDownloader.initialize(context);
    }
    public int download(){
        int downloadId = PRDownloader.download(url, dirPath, downloadFileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Toast.makeText(context,"Downloading Started",Toast.LENGTH_SHORT);
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Toast.makeText(context,"Download Completed",Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(context,"Error",Toast.LENGTH_SHORT);
                    }
                });
        return downloadId;
    }
}
