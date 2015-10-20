package com.mckuai.mcstar.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Vibrator;

import com.mckuai.mcstar.activity.MCStar;
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

    protected void feedback_No() {
        if (null == vibrator) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        }
        long[] pattern = {100, 300, 100, 300};
        vibrator.vibrate(pattern, -1);
    }

    protected void  feedback_affirm(){
        if (null == vibrator) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (vibrator.hasVibrator()){
            long[] pattern = {100, 500};
            vibrator.vibrate(pattern, -1);
        }
    }
}
