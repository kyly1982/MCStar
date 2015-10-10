package com.mckuai.mcstar.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.fragment.PaperFragment;
import com.mckuai.mcstar.fragment.QuestionFragment;

public class ExaminationActivity extends BaseActivity {
    FragmentManager mFragmentManager;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
//        mActionBar.setDisplayHomeAsUpEnabled(true);//左上角图标左边添加返回图标
        mFragmentManager = getFragmentManager();
//        mActionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchFragment(position);
    }

    private void switchFragment(int position){
        switch (position){
            case 0:
                PaperFragment paperFragment = new PaperFragment();
                mFragmentManager.beginTransaction().replace(R.id.ll_context,paperFragment,"PAPER").commit();
                break;
            case 1:
                QuestionFragment questionFragment = new QuestionFragment();
                mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag("PAPER")).commit();
                mFragmentManager.beginTransaction().replace(R.id.ll_context,questionFragment,"QUESTION").commit();
                break;
            case 2:
                break;
        }
    }
}
