package com.tars.mcwa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.tars.mcwa.R;
import com.tars.mcwa.adapter.ContributionAdapter;
import com.tars.mcwa.bean.Page;
import com.tars.mcwa.bean.Question;
import com.tars.mcwa.utils.NetInterface;
import com.umeng.analytics.MobclickAgent;

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
        }
        if (mQuestions.isEmpty()){
            loadData();
        }
    }

    private void initView() {
        initToolBar();
        mList = (UltimateRecyclerView) findViewById(R.id.questionlist);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(manager);
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
                MobclickAgent.onEvent(this,"clickAQ");
                Intent intent = new Intent(this, QuestionActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public void loadMore(int itemsCount, int maxLastVisiblePosition) {
        if (!mPage.EOF()){
            MobclickAgent.onEvent(this,"reqCL_More");
            loadData();
        }
    }

    @Override
    public void onRefresh() {
        MobclickAgent.onEvent(this,"reqRefCL");
        mPage.setPage(0);
        mQuestions.clear();
        loadData();
    }

    @Override
    public void onSuccess(Page page, ArrayList<Question> questions) {
        MobclickAgent.onEvent(this,"reqCL_S");
        this.mPage.clone(page) ;
        this.mQuestions.addAll(questions);
        showData();
    }

    @Override
    public void onFalse(String msg) {
        MobclickAgent.onEvent(this,"reqCL_F");
        feedback_false();
    }
}
