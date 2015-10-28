package com.tars.mcwa.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.View;

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



    protected void feedback_false(){
        feedback_affirm(1, null);
    }

    protected void feedback_success(){
        feedback_affirm(0, null);
    }

    protected void feedback_affirm(final int type,  View view) {
        if (null == vibrator) {
            vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (vibrator.hasVibrator()) {
            switch (type) {
                case 0:
                    long[] pattern = {100, 500};
                    vibrator.vibrate(pattern, -1);
                    break;
                case 1:
                    long[] pattern1 = {100, 300, 100, 300};
                    vibrator.vibrate(pattern1, -1);
                    if (null != view){
                        shake(view);
                    }
                    break;
            }
        }

    }

    private void shake(@NonNull View view) {
//        view.setAnimation(makeShakeAnimation());
    }
}
