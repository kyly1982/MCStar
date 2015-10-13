package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mckuai.mcstar.R;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends BaseActivity implements View.OnClickListener,Toolbar.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        findViewById(R.id.btn_getpaper).setOnClickListener(this);
        mTitle.setText(R.string.title_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mToolBar.setOnMenuItemClickListener(this);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_edit:
                intent = new Intent(this,ContributionActivity.class);
                startActivity(intent);
                break;
            case R.id.action_ranking:
                intent = new Intent(this,RankingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,ExaminationActivity.class);
        startActivity(intent);
    }
}
