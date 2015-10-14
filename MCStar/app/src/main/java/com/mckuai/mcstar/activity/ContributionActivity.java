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
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.adapter.ContributionAdapter;
import com.mckuai.mcstar.bean.Page;
import com.mckuai.mcstar.bean.Question;

import java.util.ArrayList;

public class ContributionActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, OnMoreListener, SwipeRefreshLayout.OnRefreshListener {

    ArrayList<Question> mQuestions;
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
        Log.e("LT", "onResume");
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
        mList = (SuperRecyclerView) findViewById(R.id.questionlist);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mList.setLoadingMore(false);
//        mList.getRecyclerView().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        RecyclerView.ItemDecoration decoration = ItemDecorations.vertical(this).first(getResources().getDrawable(R.drawable.divider)).last(getResources().getDrawable(R.drawable.divider)).create();
//        mList.addItemDecoration(decoration);
        mList.addItemDecoration(new RecyclerView.ItemDecoration() {

            Drawable mDivider = getResources().getDrawable(R.drawable.divider);

            @Override
            public void onDraw(Canvas c, RecyclerView parent) {
//                super.onDraw(c, parent);
                final int top = parent.getPaddingTop();
                final int bottom = parent.getHeight() - parent.getPaddingBottom();

                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    final int left = child.getRight() + params.rightMargin;
                    final int right = left + mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }


            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
//                super.getItemOffsets(outRect, itemPosition, parent);
                    outRect.set(0,0,0,mDivider.getIntrinsicHeight());
            }
        });
        mList.getRecyclerView().setHasFixedSize(true);
        mList.setupMoreListener(this, 2);
        mList.setRefreshListener(this);
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText(R.string.contribution);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadData() {
        mQuestions = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Question question = new Question(i, "用户" + i, "题目题目题目题目题目题目", "这是正确的,这是错误的,这是错误的", "未知");
            mQuestions.add(question);
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
