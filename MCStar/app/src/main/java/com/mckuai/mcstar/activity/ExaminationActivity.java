package com.mckuai.mcstar.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.bean.Paper;
import com.mckuai.mcstar.fragment.ReadyFragment;
import com.mckuai.mcstar.fragment.AnswerFragment;
import com.mckuai.mcstar.fragment.ResulltFragment;

import java.util.ArrayList;

public class ExaminationActivity extends BaseActivity {
    FragmentManager mFragmentManager;
    private int mPosition = 0;  //页面
    public Paper paper;

    public static int score = 0;
    public static ArrayList<Integer> rightQuestions;
    public static ArrayList<Integer> wrongQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == paper) {
            paper = (Paper) getIntent().getSerializableExtra(getString(R.string.tag_paper));
            mPosition = 0;
        }
        if (null != paper && null != paper.getQuestion()) {
            switchFragment(mPosition);
        } else {
            this.finish();
        }
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showNextFragment() {
        mPosition++;
        switchFragment(mPosition);
    }

    private void switchFragment(int position) {
        if (null == mFragmentManager){
            mFragmentManager = getFragmentManager();
        }
        switch (position) {
            case 0:
                ReadyFragment paperFragment = new ReadyFragment();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.tag_name), getString(R.string.title_ready));
                bundle.putSerializable(getString(R.string.tag_users),paper.getUser());
                paperFragment.setArguments(bundle);
                mFragmentManager.beginTransaction().replace(R.id.context, paperFragment, getString(R.string.title_ready)).commit();
                break;
            case 1:
                mToolBar.setVisibility(View.GONE);
                AnswerFragment questionFragment = new AnswerFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString(getString(R.string.tag_name), getString(R.string.title_answer));
                bundle1.putSerializable(getString(R.string.tag_questions),paper.getQuestion());
                questionFragment.setArguments(bundle1);
                mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag(getString(R.string.title_ready))).replace(R.id.context, questionFragment, getString(R.string.title_answer)).commit();
                break;
            case 2:
                mToolBar.setVisibility(View.VISIBLE);
                mTitle.setText(R.string.title_achievement);
                ResulltFragment resulltFragment = new ResulltFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString(getString(R.string.tag_name), getString(R.string.title_achievement));
                bundle2.putInt(getString(R.string.tag_score), score);
                bundle2.putIntegerArrayList(getString(R.string.tag_wrongquestion), wrongQuestions);
                bundle2.putIntegerArrayList(getString(R.string.tag_rightquestion),rightQuestions);
                resulltFragment.setArguments(bundle2);
                mFragmentManager.beginTransaction().replace(R.id.context, resulltFragment, getString(R.string.title_achievement)).commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_examination,menu);
        return true;
    }
}
