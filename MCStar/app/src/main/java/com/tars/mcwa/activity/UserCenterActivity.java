package com.tars.mcwa.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.tars.mcwa.R;
import com.tars.mcwa.bean.MCUser;
import com.tars.mcwa.utils.NetInterface;
import com.tars.mcwa.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class UserCenterActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, NetInterface.OnGetUserInfoListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

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
        configPlatforms();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMSsoHandler ssoHandler = mShareService.getConfig().getSsoHandler(requestCode);
        if (null != ssoHandler){
            ssoHandler.authorizeCallBack(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void share(Bitmap bitmap) {
        String content = getString(R.string.share_content_usercenter, mApplication.user.getAnswerNum(),mApplication.user.getAllScore());
        mShareService.setShareContent(getString(R.string.share_title_usercenter));
        mShareService.setShareContent(content);
        if (null != bitmap) {
            UMImage image = new UMImage(this, bitmap);
            mShareService.setShareImage(image);
        }
        mShareService.openShare(this, false);
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
            mLoader.displayImage(user.getHeadImg(), cover, options);
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
                    MobclickAgent.onEvent(this, "enableMusic");
                    item.setIcon(R.mipmap.ic_menu_music_enable);
                    item.setTitle(R.string.music_disable);
                } else {
                    MobclickAgent.onEvent(this, "disableMusic");
                    item.setTitle(R.string.music_enable);
                    item.setIcon(R.mipmap.ic_menu_music_disable);
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                MobclickAgent.onEvent(this, "share_UC");
                View view = v.getRootView();
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();
                share(bitmap);
                break;
        }
    }

    @Override
    public void onSuccess(MCUser userInfo) {
        MobclickAgent.onEvent(this, "reqUC_S");
        refreshLayout.setRefreshing(false);
        isLoading = false;
        mApplication.user.clone(userInfo);
        showData();
    }

    @Override
    public void onFalse(String msg) {
        MobclickAgent.onEvent(this, "reqUC_F");
        feedback_false();
        refreshLayout.setRefreshing(false);
        isLoading = false;
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            MobclickAgent.onEvent(this, "reqRefUC");
            loadData();
        }
    }

    private void configPlatforms() {
/*        String targetUrl = "http://www.mckuai.com/thread-" + post.getId() + ".html";
        String title = "麦块for我的世界盒子";
        String context = post.getTalkTitle();
        UMImage image;
        if (null != post.getMobilePic() && 10 < post.getMobilePic().length())
        {
            image = new UMImage(this, post.getMobilePic());
        } else
        {
            image = new UMImage(this, R.drawable.icon_share_default);
        }
//         添加内容和图片
        mShareService.setShareContent(context);
        mShareService.setShareMedia(image);*/

        String title = "MC哇";
        String url = "www.mckuai.com";
        String appID_QQ = "1104907496";
        String appAppKey_QQ = "78b7e42e255512d6492dfd135037c91c";
        // 添加qq
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appID_QQ, appAppKey_QQ);
        qqSsoHandler.setTargetUrl(url);
        qqSsoHandler.setTitle(title);
        qqSsoHandler.addToSocialSDK();
        // 添加QQ空间参数
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appID_QQ, appAppKey_QQ);
//        qZoneSsoHandler.setTargetUrl(targetUrl);
        qZoneSsoHandler.addToSocialSDK();

//        String appIDWX = "wx49ba2c7147d2368d";
        String appIDWX = "wxc49b6a0e3c78364d";
        String appSecretWX = "d4624c36b6795d1d99dcf0547af5443d";
        // 添加微信
        UMWXHandler wxHandler = new UMWXHandler(this, appIDWX, appSecretWX);
        wxHandler.setTargetUrl(url);
        wxHandler.setTitle(title);
        wxHandler.showCompressToast(false);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, appIDWX, appSecretWX);
//        wxCircleHandler.setTitle(title);
//        wxCircleHandler.setTargetUrl(targetUrl);
        wxCircleHandler.setToCircle(true);
        wxHandler.showCompressToast(false);
        wxCircleHandler.addToSocialSDK();
        // 移除多余平台
        mShareService.getConfig().removePlatform(SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);
    }
}
