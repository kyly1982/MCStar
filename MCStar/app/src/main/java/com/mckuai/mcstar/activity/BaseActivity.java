package com.mckuai.mcstar.activity;

import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mckuai.mcstar.R;

/**
 * Created by kyly on 2015/9/29.
 */
public class BaseActivity extends AppCompatActivity {
    int mContentViewId;
    MCStar mApplication = MCStar.getInstance();
    static Toolbar mToolBar;
    static TextView mTitle;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        View view = LayoutInflater.from(this).inflate(layoutResID, null, false);
        mContentViewId = view.getId();
        mTitle = (TextView) findViewById(R.id.title);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
    }

    public void showMessage(final int level, String msg) {
        Snackbar.make(null, msg, 0 == level ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
    }

    public void showMessage(final int level, final String msg, final String actionName, final View.OnClickListener listener) {
        Snackbar.make(null, msg, Snackbar.LENGTH_LONG).setAction(actionName, listener).show();
    }

    public static void setmTitle(final String title) {
        if (null != title) {
            mTitle.setText(title);
        }
    }

    public static void setToolBarTitle(final String title) {
        if (null != title) {
            mToolBar.setTitle(title);
        }
    }

    public static void setToolBarSubTitle(final String subTitle) {
        if (null != subTitle) {
            mToolBar.setSubtitle(subTitle);
        }
    }

    public static void setTitles(final String title, final String toolbarTitle, final String toolbarSubTitle) {
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

    public static void setToolBarListener(Toolbar.OnMenuItemClickListener itemClickListener, View.OnClickListener navigationClickListener) {
        if (null != itemClickListener) {
            mToolBar.setOnMenuItemClickListener(itemClickListener);
        }

        if (null != navigationClickListener) {
            mToolBar.setNavigationOnClickListener(navigationClickListener);
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
        return super.onKeyUp(keyCode, event);
    }

    public boolean onMenuKeyPressed() {
        return false;
    }

    public boolean onBackKeyPressed() {
        return false;
    }

}
