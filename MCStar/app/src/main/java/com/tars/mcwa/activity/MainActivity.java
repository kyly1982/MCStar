package com.tars.mcwa.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tars.mcwa.R;
import com.tars.mcwa.bean.Ad;
import com.tars.mcwa.bean.Paper;
import com.tars.mcwa.utils.CircleBitmap;
import com.tars.mcwa.utils.NetInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tars.mcwa.utils.NotificationUtil;
import com.tars.mcwa.widget.ExitDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


public class MainActivity extends BaseActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener, NetInterface.OnGetQrestionListener,NetInterface.OnGetAdResponse {
    private boolean isLoading = false;
    private static final int REQUEST_USERCENTER = 1;
    private static final int REQUEST_CONTRIBUTION = 2;
    private static final int REQUEST_RANKING = 3;
    private static final int REQUEST_ANSWER = 4;

    private ImageButton button;
    private AppCompatTextView hint;
    private boolean isCheckUpadte = false;

    private ExitDialog exitDialog;
    private Ad ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PushAgent.getInstance(this).enable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exitDialog = new ExitDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        if (null == hint) {
            initView();
        }
        showUserInfo();
        YoYo.with(Techniques.Swing).playOn(hint);
        if (!isCheckUpadte) {
            checkUpgrade();
            isCheckUpadte = true;
        }
        mApplication.playMusic();
        if (null == ad){
            NetInterface.getAd(this, this);
        }
    }


    @Override
    public boolean onBackKeyPressed() {
        /*showMessage(hint, R.string.exit, R.string.action_exit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplication.saveProfile();
                mApplication.stopMusic();
                MainActivity.this.finish();
            }
        });*/
        MobclickAgent.onEvent(this, "showExitDialog");
        exitDialog.init("是否退出MC哇", "http://pic1.nipic.com/2008-12-09/200812910493588_2.jpg", "下载", new ExitDialog.OnClickListener() {
            @Override
            public void onCanclePressed() {
                MobclickAgent.onEvent(MainActivity.this,"ExitDialog_C");
                exitDialog.dismiss();
            }

            @Override
            public void onExitPressed() {
                MobclickAgent.onEvent(MainActivity.this,"ExitDialog_E");
                exitDialog.dismiss();
                finish();
            }

            @Override
            public void onDownloadPressed() {
                //showMessage(hint, R.string.exit, R.string.action_exit, null);
                MobclickAgent.onEvent(MainActivity.this,"ExitDialog_D");
                NotificationUtil.notificationForDLAPK(MainActivity.this,ad);
                exitDialog.dismiss();
                finish();
            }
        });
        exitDialog.show(getFragmentManager(), "exit");
        return true;
    }

    private void initView() {
        button = (ImageButton) findViewById(R.id.btn_getpaper);
        RelativeLayout shando = (RelativeLayout) findViewById(R.id.rl_shandow);
        button.setOnClickListener(this);
        hint = (AppCompatTextView) findViewById(R.id.hint_getpaper);

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float scal = screenWidth / 720f;
        float left = 122f * scal;
        float top = 159f * scal;
        float right = 124f * scal;
        float bottom = 87f * scal;
        float width = 474f * scal;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) width, (int) width);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(screenWidth, screenWidth);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.setMargins((int) left, (int) top, (int) right, (int) bottom);

        button.setLayoutParams(params);
        shando.setLayoutParams(params1);
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
        switch (item.getItemId()) {
            case R.id.action_contribution:
                MobclickAgent.onEvent(this, "click_RL");
                if (!mApplication.isLogined()) {
                    callLogin(REQUEST_CONTRIBUTION);
                } else {
                    intent = new Intent(this, ContributionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.action_ranking:
                MobclickAgent.onEvent(this, "click_CL");
                intent = new Intent(this, RankingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getpaper:
                if (!isLoading) {
                    loadData();
                }
                break;
            default:
                MobclickAgent.onEvent(this, "click_UC");
                if (!mApplication.isLogined()) {
                    callLogin(REQUEST_USERCENTER);
                } else {
                    Intent intent = new Intent(this, UserCenterActivity.class);
                    startActivityForResult(intent, 1);
                }
                break;
        }
    }

    private void loadData() {
        MobclickAgent.onEvent(this, "reqPaper");
        NetInterface.getQuestions(this, this);
        isLoading = true;
    }

    private void showUserInfo() {
        if (mApplication.isLogined()) {
            final String cover = mApplication.user.getHeadImg();
            if (null != cover && 10 < cover.length()) {
                ImageLoader loader = ImageLoader.getInstance();
                loader.loadImage(cover, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (null != loadedImage) {
                            mToolBar.setNavigationIcon(new BitmapDrawable(getResources(), CircleBitmap.getCircleBitmap(loadedImage, getResources().getDimensionPixelSize(R.dimen.usercover_diameter_small))));
//                            mToolBar.setNavigationIcon(new BitmapDrawable(getResources(),CircleBitmap.getCircleBitmapWithStroke(MainActivity.this, loadedImage, getResources().getDimensionPixelSize(R.dimen.usercover_diameter_small),0xffffff)));

                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
                return;
            }
        }
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher);
        mToolBar.setNavigationIcon(new BitmapDrawable(getResources(), CircleBitmap.getCircleBitmap(drawable.getBitmap(), getResources().getDimensionPixelSize(R.dimen.usercover_diameter_small))));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent;
            switch (requestCode) {
                case REQUEST_CONTRIBUTION:
                    intent = new Intent(this, ContributionActivity.class);
                    startActivity(intent);
                    break;
                case REQUEST_RANKING:
                    intent = new Intent(this, RankingActivity.class);
                    startActivity(intent);
                    break;
                case REQUEST_USERCENTER:
                    intent = new Intent(this, UserCenterActivity.class);
                    startActivity(intent);
                    break;
                case REQUEST_ANSWER:
                    loadData();
            }
        }
    }

    @Override
    public void onSuccess(Paper paper) {
        MobclickAgent.onEvent(this, "reqPaper_S");
        isLoading = false;
        Intent intent = new Intent(this, ExaminationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.tag_paper), paper);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_ANSWER);
    }

    @Override
    public void onFalse(String msg) {
        feedback(false, false);
        MobclickAgent.onEvent(this, "reqPaper_F");
        isLoading = false;
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetAdSuccess(Ad ad) {
        this.ad = ad;
    }

    @Override
    public void onGetAdFailure(String msg) {

    }

    private void checkUpgrade() {
        BDAutoUpdateSDK.uiUpdateAction(this, new UICheckUpdateCallback() {
            @Override
            public void onCheckComplete() {
                Log.e("MA", "uiUpdateAction_onCheckComplete");
            }
        });
        isCheckUpadte = true;
    }

}
