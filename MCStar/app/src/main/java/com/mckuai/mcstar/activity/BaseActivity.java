package com.mckuai.mcstar.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by kyly on 2015/9/29.
 */
public class BaseActivity extends AppCompatActivity {
    MCStar mApplication = MCStar.getInstance();


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

    public boolean onMenuKeyPressed(){
        return false;
    }

    protected boolean onBackKeyPressed() {
        return false;
    }
}
