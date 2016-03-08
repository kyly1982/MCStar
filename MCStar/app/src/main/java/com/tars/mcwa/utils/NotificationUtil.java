package com.tars.mcwa.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.tars.mcwa.activity.MCStar;
import com.tars.mcwa.bean.Ad;
import com.tars.mcwa.service.DLSerivce;

/**
 * Created by kyly on 2016/3/9.
 */
public class NotificationUtil {
 /*   public static void notificationForDLAPK(Context context, String url) {
        notificationForDLAPK(context, url, Environment.getExternalStorageDirectory() + "/AigeStudio/");
    }

    public static void notificationForDLAPK(Context context, String url, String path) {
        Intent intent = new Intent(context, DLSerivce.class);
        intent.putExtra("url", url);
        intent.putExtra("path", path);
        intent.putExtra("id", (int) (Math.random() * 1024));
        context.startService(intent);
    }*/

    public static void notificationForDLAPK(Context context,Ad ad){
        Intent intent = new Intent(context, DLSerivce.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("AD",ad);
        //intent.putExtra("path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/");
        intent.putExtras(bundle);
        context.startService(intent);
    }

}
