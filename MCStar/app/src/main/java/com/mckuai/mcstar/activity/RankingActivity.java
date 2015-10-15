package com.mckuai.mcstar.activity;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.ImageView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.adapter.RankingAdapter;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.bean.Page;
import com.mckuai.mcstar.bean.Question;
import com.mckuai.mcstar.utils.NetInterface;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RankingActivity extends BaseActivity implements UltimateRecyclerView.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener,NetInterface.OnGetRankingListener {

    private AppCompatTextView mRank;
    private AppCompatTextView mName;
    private AppCompatTextView mScore;
    private ImageView mCover;
    private UltimateRecyclerView mList;

    private RankingAdapter mAdapter;
    private Page mPage = new Page();
    private ArrayList<MCUser> mUsers = new ArrayList<>(20);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        if (null == mList){
            initView();
            loadData();
        }
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText(R.string.title_rankinglist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        mRank = (AppCompatTextView) findViewById(R.id.myranking);
        mName = (AppCompatTextView) findViewById(R.id.myname);
        mScore = (AppCompatTextView) findViewById(R.id.myscore);
        mCover = (ImageView) findViewById(R.id.mycover);
        mList = (UltimateRecyclerView) findViewById(R.id.rankinglist);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mList.setLayoutManager(manager);
        mList.enableLoadmore();
        mList.enableDefaultSwipeRefresh(true);
        mList.setOnLoadMoreListener(this);
        mList.setDefaultOnRefreshListener(this);
    }

    private void loadData() {
        if (mApplication.isLogined()){
            NetInterface.getRankingList(this,mApplication.user.getId(),mPage.getNextPage(),this);
        }
    }

    private void showData() {
        if (null == mAdapter){
            mAdapter = new RankingAdapter(this);
        }
        mList.setAdapter(mAdapter);
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
        mUsers.clear();
        loadData();
    }

    @Override
    public void onSuccess(Page page, ArrayList<MCUser> users) {
        mPage = page;
        mUsers.addAll(users);
        showData();
    }

    @Override
    public void onFalse(String msg) {

    }
}
