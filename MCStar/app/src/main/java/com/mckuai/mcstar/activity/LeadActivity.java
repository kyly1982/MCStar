package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.mckuai.mcstar.R;

import java.util.ArrayList;

public class LeadActivity extends BaseActivity{
    NetworkImageView mImageView;
    ArrayList<ImageView> mIndications;
    ArrayList<String> urls;
    int mLastPosition = 0;
    static final String TAG = "LA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_lead);
        mActionBar.hide();
    }


    @Override
    protected void onResume() {
        super.onResume();
        init();
        Log.e(TAG, "onResume");
    }

    private void readPrefiles(){

    }

    private void savePrefiles(){

    }

    private void init(){
        urls = new ArrayList<>(10);
        urls.add("http://cdn.mckuai.com/uploadimg/talkContImg/20151008/92001444245157450.png");
        urls.add("http://cdn.mckuai.com/uploadimg/talkContImg/20151008/42301444245394430.png");
        urls.add("http://cdn.mckuai.com/uploadimg/talkContImg/20151008/75731444246261854.png");
        mImageView = (NetworkImageView) findViewById(R.id.ads);

        mImageView.setImageUrl(urls.get(mLastPosition),mApplication.loader);

        int count = urls.size();
        //画点
        if ( 1 < count) {
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
    public void onFABClicked() {
        if (mLastPosition == urls.size() - 1) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }else {
            mIndications.get(mLastPosition).setImageResource(R.drawable.icon_circle_gray);
            mLastPosition++;
            mIndications.get(mLastPosition).setImageResource(R.drawable.icon_circle_blue);
            mImageView.setImageUrl(urls.get(mLastPosition),mApplication.loader);
        }
    }
}
