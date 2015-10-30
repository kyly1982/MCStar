package com.tars.mcwa.fragment;

import android.app.Fragment;
import android.os.Vibrator;

import com.tars.mcwa.activity.MCStar;
import com.umeng.analytics.MobclickAgent;


public class BaseFragment extends Fragment {

    protected MCStar mApplication = MCStar.getInstance();
    protected String pageName;
    protected Vibrator vibrator;


    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null == pageName) {
            pageName = getArguments().getString("NAME");
        }
        MobclickAgent.onPageStart(pageName == null ? getClass().getName() : pageName);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != vibrator){
            vibrator.cancel();
        }
        MobclickAgent.onPageEnd(pageName == null ? getClass().getName() : pageName);
    }
}
