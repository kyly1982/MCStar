package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
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
    static MCStar mApplication = MCStar.getInstance();
    static Toolbar mToolBar;
    static TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
//        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    public void initToolBar(){
        mTitle = (TextView) findViewById(R.id.title);
        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
    }

    public void callLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    public void showMessage(final int level, String msg) {
        Snackbar.make(null, msg, 0 == level ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
    }

    public void showMessage(final int level, final String msg, final String actionName, final View.OnClickListener listener) {
        Snackbar.make(null, msg, Snackbar.LENGTH_LONG).setAction(actionName, listener).show();
    }

    public void AddNotification(){
        UmengNotificationBuilder builder = new UmengNotificationBuilder();
    }

    public void deleteNotification(){

    }


  /*  public static void setTitles(final String title, final String toolbarTitle, final String toolbarSubTitle) {
        if (null != title) {
            mTitle.setText(title);
        }
        if (null != toolbarTitle) {
            mToolBar.setTitle(toolbarTitle);
        }
        if (null != toolbarSubTitle) {
            mToolBar.setSubtitle(toolbarSubTitle);
        }
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
