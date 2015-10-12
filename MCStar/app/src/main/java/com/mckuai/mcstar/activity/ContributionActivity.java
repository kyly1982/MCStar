package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.adapter.ContributionAdapter;
import com.mckuai.mcstar.bean.Page;
import com.mckuai.mcstar.bean.Questin;

import java.util.ArrayList;

public class ContributionActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, OnMoreListener, SwipeRefreshLayout.OnRefreshListener {

    ArrayList<Questin> mQuestions;
    Page mPage;
    SuperRecyclerView mList;
    ContributionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }


    @Override
    protected void onResume() {
        Log.e("LT","onResume");
        super.onResume();
        if (null == mList) {
            initView();
        }
        if (null == mQuestions) {
            loadData();
        }
    }

    private void initView() {
        initToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitle.setText(R.string.contribution);
        mList = (SuperRecyclerView) findViewById(R.id.questionlist);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mList.getRecyclerView().setHasFixedSize(true);
        mList.setLoadingMore(true);
        mList.setupMoreListener(this, 2);
        mList.setRefreshListener(this);
    }

    private void loadData() {
        mQuestions = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Questin questin = new Questin(i, "用户" + i, i + ".题目题目题目题目题目题目", "这是正确的,这是错误的,这是错误的", "未知");
            mQuestions.add(questin);
        }
        showData();
    }

    private void showData() {
        if (null ==mAdapter) {
            mAdapter = new ContributionAdapter(this);
            mList.setAdapter(mAdapter);
        }
        mAdapter.setData(mQuestions);
        Log.e("SD", "显示");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contribution, menu);
        mToolBar.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, QuestionActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

    }

    @Override
    public void onRefresh() {

    }

}
