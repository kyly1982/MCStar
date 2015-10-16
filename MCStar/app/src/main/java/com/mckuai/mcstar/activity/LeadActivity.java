package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mckuai.mcstar.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class LeadActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mImageView;
    private AppCompatButton mBtn_Next;
    private ArrayList<ImageView> mIndications;
    private ArrayList<String> urls;
    int mLastPosition = 0;
    static final String TAG = "LA";
    private CountDownTimer timer;
    private ImageLoader mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mApplication.init();
        init();
        if (!mApplication.isFirstBoot) {
            countTime();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void countTime(){
        timer = new CountDownTimer(2500,500) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBtn_Next.setText(getString(R.string.skip,millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                showMainActivity();
            }
        };
        timer.start();
    }

    private void showMainActivity(){
        timer.cancel();
        mLoader.cancelDisplayTask(mImageView);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();

    }

    private void init(){
        urls = new ArrayList<>(10);
        urls.add("http://cdn.mckuai.com/uploadimg/talkContImg/20151008/92001444245157450.png");
/*        urls.add("http://cdn.mckuai.com/uploadimg/talkContImg/20151008/42301444245394430.png");
        urls.add("http://cdn.mckuai.com/uploadimg/talkContImg/20151008/75731444246261854.png");*/
        mImageView = (ImageView) findViewById(R.id.ads);
        mBtn_Next = (AppCompatButton) findViewById(R.id.btn_next);
        mBtn_Next.setOnClickListener(this);
        showImage();

        int count = urls.size();
        //画点
        LinearLayoutCompat indicationRoot = (LinearLayoutCompat) findViewById(R.id.ll_indication);
        if ( 1 < count) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30,30);
            params.setMargins(5,5,5,5);
            mIndications = new ArrayList<>(count);

            for (int i = 0;i < count;i++){
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.drawable.icon_circle_gray);
                mIndications.add(imageView);
                indicationRoot.addView(imageView);
            }
            mIndications.get(mLastPosition).setImageResource(R.drawable.icon_circle_blue);
        }
        else {
            indicationRoot.setVisibility(View.GONE);
        }
    }

    private void showImage(){
        if (urls.size() > 1) {
            mIndications.get(mLastPosition).setImageResource(R.drawable.icon_circle_gray);
            mLastPosition++;
            mIndications.get(mLastPosition).setImageResource(R.drawable.icon_circle_blue);
        }
        mLoader = ImageLoader.getInstance();
        mLoader.displayImage(urls.get(mLastPosition),mImageView);
        if (mLastPosition == urls.size() -1){
            mBtn_Next.setText(R.string.enter);
        }
    }

    @Override
    public void onClick(View v) {
        if (mLastPosition == urls.size() - 1) {
            showMainActivity();
        }else {
            showImage();
        }
    }
}
