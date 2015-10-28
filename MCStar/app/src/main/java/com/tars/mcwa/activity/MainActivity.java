package com.tars.mcwa.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tars.mcwa.R;
import com.tars.mcwa.bean.Paper;
import com.tars.mcwa.utils.CircleBitmap;
import com.tars.mcwa.utils.NetInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends BaseActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener, NetInterface.OnGetQrestionListener {
    private boolean isLoading = false;
    private static final int REQUEST_USERCENTER = 1;
    private static final int REQUEST_CONTRIBUTION = 2;
    private static final int REQUEST_RANKING = 3;
    private static final int REQUEST_ANSWER = 4;

    private ImageButton button;
    private AppCompatTextView hint ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        initView();
        showUserInfo();
        YoYo.with(Techniques.Swing);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onStop() {
        mApplication.saveProfile();
        super.onStop();
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
                MobclickAgent.onEvent(this,"click_RL");
                if (!mApplication.isLogined()) {
                    callLogin(REQUEST_CONTRIBUTION);
                } else {
                    intent = new Intent(this, ContributionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.action_ranking:
                MobclickAgent.onEvent(this,"click_CL");
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
                MobclickAgent.onEvent(this,"click_UC");
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
        MobclickAgent.onEvent(this,"reqPaper");
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
        MobclickAgent.onEvent(this,"reqPaper_S");
        isLoading = false;
        Intent intent = new Intent(this, ExaminationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.tag_paper), paper);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_ANSWER);
    }

    @Override
    public void onFalse(String msg) {
        feedback_false();
        MobclickAgent.onEvent(this, "reqPaper_F");
        isLoading = false;
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
