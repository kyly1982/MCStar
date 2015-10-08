package com.mckuai.mcstar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.activity.MCStar;

import java.util.ArrayList;

/**
 * Created by kyly on 2015/9/30.
 */
public class ADAdapter extends PagerAdapter {
    ArrayList<String> urls;
    NetworkImageView mImageView;
    Context mContext;
    MCStar application;
    public ADAdapter(Context context,ArrayList<String> urls){
        this.urls = urls;
        this.mContext = context;
        application = MCStar.getInstance();
        if (null != urls && !urls.isEmpty()) {
            mImageView = new NetworkImageView(mContext);
            mImageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageView.setBackgroundColor(0xff00ff);
        }
    }

    @Override
    public int getCount() {
        return null == urls ? 0:urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (null != mImageView && null != urls && !urls.isEmpty()){
            mImageView.setImageUrl(urls.get(position),application.loader);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ADA","点击了图片："+urls.get(position));
                }
            });
        }
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
