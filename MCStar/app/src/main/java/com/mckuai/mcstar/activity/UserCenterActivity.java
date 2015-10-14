package com.mckuai.mcstar.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mckuai.mcstar.R;

public class UserCenterActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usercenter,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_playmusic:
                    if (mApplication.switchPlayPauseMusic()){
                        item.setTitle(R.string.music_disable);
                        item.setIcon(android.support.v7.appcompat.R.drawable.abc_ic_menu_selectall_mtrl_alpha);
                    } else {
                        item.setTitle(R.string.music_enable);
                        item.setIcon(android.support.v7.appcompat.R.drawable.abc_ic_menu_share_mtrl_alpha);
                    }
                break;
        }
        return false;
    }
}
