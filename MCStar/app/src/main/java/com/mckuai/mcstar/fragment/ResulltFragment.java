package com.mckuai.mcstar.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.activity.BaseActivity;
import com.mckuai.mcstar.activity.LoginActivity;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.utils.NetInterface;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class ResulltFragment extends BaseFragment implements NetInterface.OnReportListener, View.OnClickListener {

    private View view;
    private AppCompatTextView mRank_pre;
    private AppCompatTextView mRank_next;
    private AppCompatTextView mRank;
    private AppCompatTextView mScore_pre;
    private AppCompatTextView mScore_next;
    private AppCompatTextView mScore;
    private ImageView mCover_pre;
    private ImageView mCover_next;
    private ImageView mCover;

    private RelativeLayout user_pre;
    private RelativeLayout user_next;
    private RelativeLayout user_mine;


    private int score;
    private boolean isLoading = false;
    private ArrayList<Integer> rightQuestionId;
    private ArrayList<Integer> wrongQuestionId;

    private MCUser user_p,user_n;
    private ImageLoader mLoader;


    public ResulltFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resullt, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        score = bundle.getInt(getString(R.string.tag_score));
        rightQuestionId = bundle.getIntegerArrayList(getString(R.string.tag_rightquestion));
        wrongQuestionId = bundle.getIntegerArrayList(getString(R.string.tag_wrongquestion));
        if (null != view) {
            if (null == mRank) {
                initView();
            }
            showData(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {//返回RESULT_OK
            updateResult();
        }
    }

    private void showData(boolean isUploading) {
        if (!isUploading) {
            mScore.setText("" + score);
        } else {
            if (null != user_p){
                showUserInfo(user_p,mRank_pre,mScore_pre,mCover_pre);
                user_pre.setVisibility(View.VISIBLE);
            }
            if (null != user_n){
                showUserInfo(user_n,mRank_next,mScore_next,mCover_next);
                user_next.setVisibility(View.VISIBLE);
            }
            showUserInfo(mApplication.user, mRank, mScore, mCover);
            user_mine.setVisibility(View.VISIBLE);
        }
    }

    private void showUserInfo(MCUser user,AppCompatTextView rank,AppCompatTextView score,ImageView cover){
        rank.setText(user.getRanking()+"");
        score.setText(user.getAllScore()+"");
        if (null != user.getHeadImg() && 10 < user.getHeadImg().length()) {
            mLoader.displayImage(user.getHeadImg(), cover);
        }
    }

    private void updateResult() {
        if (!isLoading && mApplication.isLogined()) {
            MCUser user = mApplication.user;
            mApplication.user.setAllScore(user.getAllScore() + score);
            mApplication.user.setAnswerNum(user.getAnswerNum() + 1);
            NetInterface.uploadResult(getActivity(), mApplication.user.getId(), score, rightQuestionId, wrongQuestionId, this);
            isLoading = true;
        }
    }

    private void initView() {
        mScore_pre = (AppCompatTextView) view.findViewById(R.id.score_pre);
        mScore_next = (AppCompatTextView) view.findViewById(R.id.score_next);
        mScore = (AppCompatTextView) view.findViewById(R.id.score_mine);
        mRank_pre = (AppCompatTextView) view.findViewById(R.id.ranking_pre);
        mRank_next = (AppCompatTextView) view.findViewById(R.id.ranking_next);
        mRank = (AppCompatTextView) view.findViewById(R.id.ranking_mine);
        mCover_pre = (ImageView) view.findViewById(R.id.cover_pre);
        mCover_next = (ImageView) view.findViewById(R.id.cover_next);
        mCover = (ImageView) view.findViewById(R.id.cover_mine);
        user_pre = (RelativeLayout) view.findViewById(R.id.user_pre);
        user_next = (RelativeLayout) view.findViewById(R.id.user_next);
        user_mine = (RelativeLayout) view.findViewById(R.id.layout_ranking_mine);
        mCover.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!isLoading && mApplication.isLogined()) {
            if (!isLoading) {
                updateResult();
            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onSuccess(@NonNull MCUser myself, @Nullable MCUser user_pre, @Nullable MCUser user_next) {
        isLoading = false;
        mApplication.user = myself;
        this.user_p = user_pre;
        this.user_n = user_next;
        showData(true);
    }

    @Override
    public void onFalse(String msg) {
        isLoading = false;
    }
}
