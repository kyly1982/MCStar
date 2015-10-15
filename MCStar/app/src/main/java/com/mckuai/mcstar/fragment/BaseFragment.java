package com.mckuai.mcstar.fragment;

import android.app.Fragment;

import com.mckuai.mcstar.activity.MCStar;
import com.umeng.analytics.MobclickAgent;


public class BaseFragment extends Fragment {

    MCStar mApplication = MCStar.getInstance();
    String pageName;


    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null == pageName) {
            pageName = getArguments().getString("NAME");
        }
        MobclickAgent.onPageStart(pageName == null ?getClass().getName():pageName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(pageName == null ?getClass().getName():pageName);
    }
}
