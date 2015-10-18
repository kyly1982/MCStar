package com.mckuai.mcstar.activity;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.utils.NetInterface;
import com.mckuai.mcstar.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.connect.UserInfo;
import com.umeng.socialize.controller.UMServiceFactory;

public class UserCenterActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, NetInterface.OnGetUserInfoListener, SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    private AppCompatTextView name;
    private AppCompatTextView count;
    private AppCompatTextView score;
    private AppCompatTextView score_avg;
    private AppCompatTextView contribution;
    private AppCompatButton share;
    private ImageView cover;
    private SwipeRefreshLayout refreshLayout;

    private ImageLoader mLoader;
    private DisplayImageOptions options;

    private boolean isLoading = false;
    private com.umeng.socialize.controller.UMSocialService mShareService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
        mShareService = UMServiceFactory.getUMSocialService("com.umeng.share");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        if (null == name) {
            initView();
            mLoader = ImageLoader.getInstance();
        }
        showData();
        mApplication.playMusic();
    }

    @Override
    protected void onPause() {
        mApplication.pauseMusic();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mApplication.stopMusic();
        super.onDestroy();
    }

    private void initView() {
        name = (AppCompatTextView) findViewById(R.id.username);
        count = (AppCompatTextView) findViewById(R.id.papercount);
        contribution = (AppCompatTextView) findViewById(R.id.contributioncount);
        score = (AppCompatTextView) findViewById(R.id.totlescore);
        score_avg = (AppCompatTextView) findViewById(R.id.avgscore);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        cover = (ImageView) findViewById(R.id.usercover);
        refreshLayout.setOnRefreshListener(this);
        share = (AppCompatButton) findViewById(R.id.btn_share);
        share.setOnClickListener(this);
    }

    private void loadData() {
        NetInterface.getUserInfo(this, mApplication.user.getId(), this);
        refreshLayout.setRefreshing(true);
        isLoading = true;
    }

    private void share(){
            mShareService.setShareContent(getString(R.string.share_title_scorewithrank));
            mShareService.setShareContent(getString(R.string.share_content_scorewithrank,mApplication.user.getRanking()));
//        mShareService.setShareImage();
        mShareService.openShare(this,false);
    }

    private void showData() {
        MCUser user = mApplication.user;
        name.setText(user.getNickName() + "");
        count.setText(user.getAnswerNum() + "");
        contribution.setText(user.getUploadNum() + "");
        score.setText(user.getAllScore() + "");
        if (0 < user.getAnswerNum()) {
            score_avg.setText((int) (user.getAllScore() / user.getAnswerNum()) + "");
        }
        if (null != user.getHeadImg() && 10 < user.getHeadImg().length()) {
            mLoader.displayImage(user.getHeadImg(),cover,options);
        }
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
        getMenuInflater().inflate(R.menu.menu_usercenter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_playmusic:
                if (mApplication.switchPlayPauseMusic()) {
                    item.setTitle(R.string.music_disable);
                    item.setIcon(R.mipmap.ic_menu_music_disable);
                } else {
                    item.setTitle(R.string.music_enable);
                    item.setIcon(R.mipmap.ic_menu_music_enable);
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_share:
                share();
                break;
        }
    }

    @Override
    public void onSuccess(MCUser userInfo) {
        refreshLayout.setRefreshing(false);
        isLoading = false;
        mApplication.user = userInfo;
        showData();
    }

    @Override
    public void onFalse(String msg) {
        refreshLayout.setRefreshing(false);
        isLoading = false;
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            loadData();
        }
    }
}
