package com.mckuai.mcstar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by kyly on 2015/9/30.
 */
public class ADAdapter extends PagerAdapter {
    ArrayList<String> urls;
    ImageView mImageView;
    Context mContext;
    public ADAdapter(Context context,ArrayList<String> urls){
        this.urls = urls;
        this.mContext = context;
        if (null != urls && !urls.isEmpty()) {
            mImageView = new ImageView(mContext);
            mImageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
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
    public Object instantiateItem(ViewGroup container, int position) {
        if (null != mImageView){

        }
        return super.instantiateItem(container, position);
    }
}
