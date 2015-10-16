package com.mckuai.mcstar.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.adapter.RankingAdapter;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.bean.Page;
import com.mckuai.mcstar.utils.NetInterface;
import com.mckuai.mcstar.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RankingActivity extends BaseActivity implements UltimateRecyclerView.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener,NetInterface.OnGetRankingListener {

    private AppCompatTextView mRank;
//    private AppCompatTextView mName;
    private AppCompatTextView mScore;
    private ImageView mCover;
    private UltimateRecyclerView mList;
    private RelativeLayout mLayout;

    private RankingAdapter mAdapter;
    private Page mPage = new Page();
    private ArrayList<MCUser> mUsers = new ArrayList<>(20);

    private ImageLoader mLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
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
//        mName = (AppCompatTextView) findViewById(R.id.myname);
        mScore = (AppCompatTextView) findViewById(R.id.myscore);
        mCover = (ImageView) findViewById(R.id.mycover);
        mList = (UltimateRecyclerView) findViewById(R.id.rankinglist);
        mLayout = (RelativeLayout) findViewById(R.id.myinfo);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setHasFixedSize(true);
        mList.enableLoadmore();
        mList.enableDefaultSwipeRefresh(true);
        mList.setOnLoadMoreListener(this);
        mList.setDefaultOnRefreshListener(this);
    }

    private void loadData() {
        if (mApplication.isLogined()){
            NetInterface.getRankingList(this,mApplication.user.getId(),mPage.getNextPage(),this);
        } else {
            NetInterface.getRankingList(this,0,mPage.getNextPage(),this);
        }
    }

    private void showData() {
        if (mApplication.isLogined()) {
            MCUser user = mApplication.user;
            if (null != user && 0 != user.getId()) {
                mRank.setText((user.getRanking() +1) + "");
                //mName.setText(user.getNickName() + "");
                mScore.setText(getString(R.string.scores,user.getAllScore()));
            }
            if (null != user.getHeadImg() && 10 < user.getHeadImg().length()) {
                mLoader.displayImage(user.getHeadImg(),mCover,options);
            }
            mLayout.setVisibility(View.VISIBLE);
        }
        if (null == mAdapter){
            mAdapter = new RankingAdapter(this);
            mList.setAdapter(mAdapter);
        }
        mAdapter.setData(mUsers);
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
    public void onSuccess(Page page, MCUser myself,ArrayList<MCUser> users) {
        mPage.clone(page);
        if (null !=myself) {
            mApplication.user.clone(myself);
        }
        mUsers.addAll(users);
        mUsers.addAll(users);
        mUsers.addAll(users);
        mUsers.addAll(users);
        showData();
    }

    @Override
    public void onFalse(String msg) {
        Log.e("RA", msg);
        mList.setRefreshing(false);
    }
}
