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
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.tag_name),getString(R.string.title_ready));
                paperFragment.setArguments(bundle);
                mFragmentManager.beginTransaction().replace(R.id.context,paperFragment,getString(R.string.title_ready)).commit();
                break;
            case 1:
                mToolBar.setVisibility(View.GONE);
                AnswerFragment questionFragment = new AnswerFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString(getString(R.string.tag_name),getString(R.string.title_answer));
                questionFragment.setArguments(bundle1);
                //mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag("PAPER")).commit();
                mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag(getString(R.string.title_ready))).replace(R.id.context,questionFragment,getString(R.string.title_answer)).commit();
                break;
            case 2:
                mToolBar.setVisibility(View.VISIBLE);
                mTitle.setText(R.string.title_achievement);
                ResulltFragment resulltFragment = new ResulltFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString(getString(R.string.tag_name),getString(R.string.title_achievement));
                mFragmentManager.beginTransaction().replace(R.id.context,resulltFragment,getString(R.string.title_achievement)).commit();
                break;
        }
    }
}
