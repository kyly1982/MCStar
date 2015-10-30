package com.tars.mcwa.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.tars.mcwa.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.local.UmengNotificationBuilder;

/**
 * Created by kyly on 2015/9/29.
 */
public class BaseActivity extends AppCompatActivity {
    protected static MCStar mApplication = MCStar.getInstance();
    protected static Toolbar mToolBar;
    protected static TextView mTitle;
    protected Vibrator vibrator;
    protected SoundPool soundPool;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initToolBar() {
        mTitle = (TextView) findViewById(R.id.title);
        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
    }

    public void callLogin(int requestCode) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, requestCode);
    }

    public void showMessage(final int level, String msg) {
        Snackbar.make(null, msg, 0 == level ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
    }

    public void showMessage(final int level, final String msg, final String actionName, final View.OnClickListener listener) {
        Snackbar.make(null, msg, Snackbar.LENGTH_LONG).setAction(actionName, listener).show();
    }

    public void AddNotification() {
        UmengNotificationBuilder builder = new UmengNotificationBuilder();
    }

    public void deleteNotification() {

    }


    public static void setTitles(@StringRes final int titleId, @StringRes final int toolbarTitleId, @StringRes final int toolbarSubTitleId) {
        if (0 != titleId) {
            mTitle.setText(titleId);
        } else {
            mTitle.setText("");
        }

        if (0 != toolbarTitleId) {
            mToolBar.setTitle(toolbarTitleId);
        } else {
            mToolBar.setTitle("");
        }

        if (0 != toolbarSubTitleId) {
            mToolBar.setSubtitle(toolbarSubTitleId);
        } else {
            mToolBar.setSubtitle("");
        }
    }



    protected void feedback(final boolean isSuccess, final boolean needMusic) {
        if (null == vibrator) {
            vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (vibrator.hasVibrator() && isSuccess) {
            long[] pattern1 = {100, 300, 100, 300};
            vibrator.vibrate(pattern1, -1);
        }
        if (needMusic) {
            playSound(true == isSuccess ? 1 : 0);
        }
    }

    protected void playBackgroundMusic(){
        playSound(2);
    }


    protected void playSound(int type){
        if (null == soundPool) {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
        }
        int id;
        switch (type){
            case 0:
                id =soundPool.load(this, R.raw.music,2);
                soundPool.play(id,1,1,2,0,1);
                break;
            case 1:
                id =soundPool.load(this, R.raw.music,2);
                soundPool.play(id,1,1,2,0,1);
                break;
            case 2:
                id =soundPool.load(this, R.raw.music,1);
                soundPool.play(id,0.5f,0.5f,2,0,1);
                break;
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (onMenuKeyPressed()) {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onBackKeyPressed()) {
                return true;
            }
        }
        Log.e("BA","onKeyUp");
        return super.onKeyUp(keyCode, event);
    }

    public boolean onMenuKeyPressed() {
        return false;
    }

    public boolean onBackKeyPressed() {
        return false;
    }

}
