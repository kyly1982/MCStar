package com.mckuai.mcstar.activity;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.utils.LruImageCache;

/**
 * Created by kyly on 2015/9/29.
 */
public class MCStar extends Application{
    static MCUser mUserInfo;
    static final String TAG = "application";
    static MCStar instance;
    public RequestQueue queue;
    public ImageLoader loader;

    public static MCStar getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.e(TAG,"onCreate");
    }

    public void init(){
        queue = Volley.newRequestQueue(this);
        loader = new ImageLoader(queue,new LruImageCache());
    }

    public void readPreference(){

    }

    public void writePreference(){

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e(TAG, "onTerminate");
    }
}
