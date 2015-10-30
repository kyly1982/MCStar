package com.tars.mcwa.fragment;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.View;

import com.tars.mcwa.R;
import com.tars.mcwa.activity.MCStar;
import com.umeng.analytics.MobclickAgent;


public class BaseFragment extends Fragment {

    protected MCStar mApplication = MCStar.getInstance();
    protected String pageName;
    protected Vibrator vibrator;
    protected SoundPool soundPool;


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


    protected void feedback(final boolean isSuccess,final boolean needMusic) {
        if (null == vibrator) {
            vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (vibrator.hasVibrator() && isSuccess) {
            long[] pattern1 = {100, 300, 100, 300};
            vibrator.vibrate(pattern1, -1);
        }
        if (needMusic) {
            playSound(true == isSuccess ? 1 : 0);
        }

    }

    protected void playBackgroundMusic(){
        playSound(2);
    }

    protected void playSound(int type){
        if (null == soundPool) {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
        }
        int id;
        switch (type){
            case 0:
                id =soundPool.load(getActivity(), R.raw.music,2);
                soundPool.play(id,1,1,2,0,1);
                break;
            case 1:
                id =soundPool.load(getActivity(), R.raw.music,2);
                soundPool.play(id,1,1,2,0,1);
                break;
            case 2:
                id =soundPool.load(getActivity(), R.raw.music,2);
                soundPool.play(id,0.5f,0.5f,2,0,1);
                break;
        }
    }
}
