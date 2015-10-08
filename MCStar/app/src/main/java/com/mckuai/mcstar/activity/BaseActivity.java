package com.mckuai.mcstar.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mckuai.mcstar.R;

/**
 * Created by kyly on 2015/9/29.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    int mContentViewId;
    MCStar mApplication = MCStar.getInstance();
    Toolbar mToolBar;
    FloatingActionButton mFloatingActionButton;
    android.support.v7.app.ActionBar mActionBar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        View view = LayoutInflater.from(this).inflate(layoutResID,null,false);
        mContentViewId = view.getId();
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);//图标可点击
        mActionBar.setDisplayShowHomeEnabled(true);  //显示左上角图标
//     mActionBar.setDisplayHomeAsUpEnabled(true);//左上角图标左边添加返回图标


        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(this);
    }

    public void showMessage(final int level,String msg){
        Snackbar.make(null, msg, 0 == level ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
    }

    public void showMessage(final int level,final String msg,final String actionName,final View.OnClickListener listener){
        Snackbar.make(null,msg,Snackbar.LENGTH_LONG).setAction(actionName, listener).show();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            if (onMenuKeyPressed()){
                return true;
            }
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK){
            if (onBackKeyPressed()){
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                onFABClicked();
                break;

        }
    }

    public boolean onMenuKeyPressed(){
        return false;
    }

    public boolean onBackKeyPressed() {
        return false;
    }

    public void onFABClicked(){

    }
}
