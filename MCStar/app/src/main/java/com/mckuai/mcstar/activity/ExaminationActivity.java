package com.mckuai.mcstar.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.fragment.ReadyFragment;
import com.mckuai.mcstar.fragment.AnswerFragment;
import com.mckuai.mcstar.fragment.ResulltFragment;

public class ExaminationActivity extends BaseActivity {
    FragmentManager mFragmentManager;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        mFragmentManager = getFragmentManager();
        initToolBar();
        mTitle.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchFragment(position);
    }

    public  void showNextFragment(){
        position++;
        switchFragment(position);
    }

    private void switchFragment(int position){
        switch (position){
            case 0:
                ReadyFragment paperFragment = new ReadyFragment();
                mFragmentManager.beginTransaction().replace(R.id.context,paperFragment,"PAPER").commit();
                break;
            case 1:
                mToolBar.setVisibility(View.GONE);
                AnswerFragment questionFragment = new AnswerFragment();
                //mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag("PAPER")).commit();
                mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag("PAPER")).replace(R.id.context,questionFragment,"QUESTION").commit();
                break;
            case 2:
                mToolBar.setVisibility(View.VISIBLE);
                mTitle.setText(R.string.title_achievement);
                ResulltFragment resulltFragment = new ResulltFragment();
                mFragmentManager.beginTransaction().replace(R.id.context,resulltFragment,"RESULT").commit();
                break;
        }
    }
}
