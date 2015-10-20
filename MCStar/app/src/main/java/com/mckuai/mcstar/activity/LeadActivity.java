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
import com.mckuai.mcstar.utils.AutoUpgrade.AppUpdate;
import com.mckuai.mcstar.utils.AutoUpgrade.AppUpdateService;
import com.mckuai.mcstar.utils.AutoUpgrade.ResponseParser;
import com.mckuai.mcstar.utils.AutoUpgrade.Version;
import com.mckuai.mcstar.utils.AutoUpgrade.internal.SimpleJSONParser;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URLEncoder;
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
        checkUpgrade();
        mImageView = (ImageView) findViewById(R.id.ads);
        mBtn_Next = (AppCompatButton) findViewById(R.id.btn_next);
        if (!mApplication.isFirstBoot) {
            countTime();
        } else {
            init();
        }
    }

    @Override
    protected void onPause() {
        timer.cancel();
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
        if (null != mLoader) {
            mLoader.cancelDisplayTask(mImageView);
        }

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

    private void checkUpgrade(){
        AppUpdate updateService = AppUpdateService.getAppUpdate(this);
        String url = getString(R.string.interface_domain) + getString(R.string.interface_checkupgread);
        url = url + "&pushMan=" + URLEncoder.encode(getString(R.string.channel));
        updateService.checkLatestVersionQuiet(url, new MyJsonParser());
    }

    @Override
    public void onClick(View v) {
        if (mLastPosition == urls.size() - 1) {
            showMainActivity();
        }else {
            showImage();
        }
    }

    static class MyJsonParser extends SimpleJSONParser implements ResponseParser
    {
        @Override
        public Version parser(String response)
        {
            try
            {
                JSONTokener jsonParser = new JSONTokener(response);
                JSONObject json = (JSONObject) jsonParser.nextValue();
                Version version = null;
                if (json.has("state") && json.has("dataObject"))
                {
                    JSONObject dataField = json.getJSONObject("dataObject");
                    int code = dataField.getInt("code");
                    String name = dataField.getString("name");
                    String feature = dataField.getString("feature");
                    String targetUrl = dataField.getString("targetUrl");
                    version = new Version(code, name, feature, targetUrl);
                }
                return version;
            } catch (JSONException exp)
            {
                exp.printStackTrace();
                return null;
            }
        }
    }

}
