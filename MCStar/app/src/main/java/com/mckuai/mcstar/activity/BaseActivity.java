package com.mckuai.mcstar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.mckuai.mcstar.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.local.UmengLocalNotification;
import com.umeng.message.local.UmengNotificationBuilder;

/**
 * Created by kyly on 2015/9/29.
 */
public class BaseActivity extends AppCompatActivity {
    protected static MCStar mApplication = MCStar.getInstance();
    protected static Toolbar mToolBar;
    protected static TextView mTitle;
    protected Vibrator vibrator;

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



    protected void feedback_false(){
        feedback_affirm(1,null);
    }

    protected void feedback_success(){
        feedback_affirm(0,null);
    }

    protected void feedback_affirm(final int type,  View view) {
        if (null == vibrator) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (vibrator.hasVibrator()) {
            switch (type) {
                case 0:
                    long[] pattern = {100, 500};
                    vibrator.vibrate(pattern, -1);
                    break;
                case 1:
                    long[] pattern1 = {100, 300, 100, 300};
                    vibrator.vibrate(pattern1, -1);
                    if (null != view){
                        shake(view);
                    }
                    break;
            }
        }

    }

    private void shake(@NonNull View view) {
        view.setAnimation(makeShakeAnimation());
    }

    private AnimationSet makeShakeAnimation(){

        AnimationSet shake = new AnimationSet(true);
        for (int i = 0;i < 16;i++){
            TranslateAnimation translateAnimation = new TranslateAnimation(0,-15,0,0);
        }

        return  shake;
    }
/*
    public static void setToolBarClickListener(Toolbar.OnMenuItemClickListener itemClickListener, View.OnClickListener navigationClickListener) {
        if (null != itemClickListener) {
            mToolBar.setOnMenuItemClickListener(itemClickListener);
        }

        if (null != navigationClickListener) {
            mToolBar.setNavigationOnClickListener(navigationClickListener);
        }
    }*/

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
        return super.onKeyUp(keyCode, event);
    }

    public boolean onMenuKeyPressed() {
        return false;
    }

    public boolean onBackKeyPressed() {
        return false;
    }

}
