package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.magiepooh.recycleritemdecoration.ItemDecorations;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.adapter.ContributionAdapter;
import com.mckuai.mcstar.bean.Page;
import com.mckuai.mcstar.bean.Question;
import com.mckuai.mcstar.utils.NetInterface;

import java.util.ArrayList;

public class ContributionActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener,NetInterface.OnGetContributionListener,UltimateRecyclerView.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener {

    ArrayList<Question> mQuestions = new ArrayList<>(20);
    Page mPage = new Page();
    UltimateRecyclerView mList;
    ContributionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null == mList) {
            initView();
            loadData();
        }
    }

    private void initView() {
        initToolBar();
        mList = (UltimateRecyclerView) findViewById(R.id.questionlist);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(manager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
//        mList.addItemDecoration(dividerItemDecoration);
        mList.enableLoadmore();
        mList.enableDefaultSwipeRefresh(true);
        mList.setOnLoadMoreListener(this);
        mList.setDefaultOnRefreshListener(this);
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText(R.string.contribution);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadData() {
        if (mApplication.isLogined()) {
            NetInterface.getContribution(this, mApplication.user.getId(),mPage.getNextPage(), this);
        }
    }

    private void showData() {
        if (null ==mAdapter) {
            mAdapter = new ContributionAdapter(this);
            mList.setAdapter(mAdapter);
        }
        mAdapter.setData(mQuestions);
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
    public void loadMore(int itemsCount, int maxLastVisiblePosition) {
        if (!mPage.EOF()){
            loadData();
        }
    }

    @Override
    public void onRefresh() {
        mPage.setPage(0);
        mQuestions.clear();
        loadData();
    }

    @Override
    public void onSuccess(Page page, ArrayList<Question> questions) {
        this.mPage = page;
        this.mQuestions.addAll(questions);
        showData();
    }

    @Override
    public void onFalse(String msg) {

    }
}
