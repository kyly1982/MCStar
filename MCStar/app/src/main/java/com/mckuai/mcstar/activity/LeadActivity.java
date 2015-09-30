package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mckuai.mcstar.R;

import java.util.ArrayList;

public class LeadActivity extends BaseActivity implements ViewPager.OnPageChangeListener,View.OnClickListener {

    ViewPager mViewPager;
    ArrayList<ImageView> mIndications;
    int mLastPosition = 0;
    static final String TAG = "LA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_lead);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    private void init(){
        mViewPager = (ViewPager) findViewById(R.id.pager);

        initIndication(3);
        findViewById(R.id.skip).setOnClickListener(this);
    }

    private void initIndication(int count){
        if ( 0 < count && 5 > count) {
            LinearLayout indicationRoot = (LinearLayout) findViewById(R.id.ll_indication);
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
    }







    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mIndications.get(mLastPosition).setImageResource(R.drawable.icon_circle_gray);
        mIndications.get(position).setImageResource(R.drawable.icon_circle_blue);
        mLastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }
}
