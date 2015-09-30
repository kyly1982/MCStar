package com.mckuai.mcstar.activity;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.mckuai.mcstar.bean.MCUser;

/**
 * Created by kyly on 2015/9/29.
 */
public class MCStar extends Application{
    static MCUser mUserInfo;
    static final String TAG = "application";
    static MCStar instance;

    static MCStar getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.e(TAG,"onCreate");
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
