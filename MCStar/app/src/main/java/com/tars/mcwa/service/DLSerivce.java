package com.tars.mcwa.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.tars.mcwa.R;
import com.tars.mcwa.bean.Ad;

import java.io.File;

import cn.aigestudio.downloader.bizs.DLManager;
import cn.aigestudio.downloader.interfaces.SimpleDListener;

public class DLSerivce extends Service {
    private NotificationManager nm;
    private NotificationCompat.Builder builder;
    private String path;

    public DLSerivce() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Ad ad = (Ad) intent.getSerializableExtra("AD");
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/";
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(ad.getDownName())
                .setContentText("正在为你下载，请稍候...")
                .setProgress(100, 0, false)
                .setOngoing(true);


        final int[] length = new int[1];
        DLManager.getInstance(this).dlStart(ad.getDownUrl(), path, null, null, new SimpleDListener() {
            @Override
            public void onStart(String fileName, String realUrl, int fileLength) {
                //builder.setContentTitle(fileName);
                length[0] = fileLength;
            }

            @Override
            public void onProgress(int progress) {
                builder.setProgress(length[0], progress, false);
                nm.notify(ad.getId(), builder.build());
            }

            @Override
            public void onFinish(File file) {
                builder.setProgress(0, 0, false);
                builder.setContentIntent(getIntent(file));
                //nm.cancel(ad.getId());
            }

        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private PendingIntent getIntent(File file){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        return PendingIntent.getActivity(this,0,intent,0);
    }

}
