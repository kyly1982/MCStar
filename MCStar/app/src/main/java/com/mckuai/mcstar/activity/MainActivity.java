package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.bean.Paper;
import com.mckuai.mcstar.utils.NetInterface;

public class MainActivity extends BaseActivity implements View.OnClickListener,Toolbar.OnMenuItemClickListener,NetInterface.OnGetQrestionListener {
    private boolean isLoading = false;

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
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mToolBar.setNavigationIcon(R.mipmap.ic_menu_navigataion);
        mToolBar.setOnMenuItemClickListener(this);
        mToolBar.setNavigationOnClickListener(this);
        mTitle.setText(R.string.title_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mToolBar.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_contribution:
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
        switch (v.getId()){
            case R.id.btn_getpaper:
                if (!isLoading) {
                    loadData();
                }
                break;
            default:
                if (!mApplication.isLogined()){
                   callLogin();
                } else {
                    Intent intent = new Intent(this,UserCenterActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void loadData(){
        NetInterface.getQuestions(this,this);
        isLoading = true;
    }

    @Override
    public void onSuccess(Paper paper) {
        isLoading = false;
        Intent  intent = new Intent(this,ExaminationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.tag_paper),paper);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onFalse(String msg) {
        isLoading = false;
        Log.e("LD",""+msg);
    }
}
