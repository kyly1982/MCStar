package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mckuai.mcstar.R;

public class ContributionActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution);
    }



    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        mTitle.setText(R.string.contribution);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contribution,menu);
        mToolBar.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Intent intent = new Intent(this,QuestionActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }
}
