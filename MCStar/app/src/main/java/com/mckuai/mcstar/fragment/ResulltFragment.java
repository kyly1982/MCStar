package com.mckuai.mcstar.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.activity.BaseActivity;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.utils.NetInterface;

import java.util.ArrayList;


public class ResulltFragment extends BaseFragment implements NetInterface.OnReportListener {

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
    private ArrayList<Integer> rightQuestionId;
    private ArrayList<Integer> wrongQuestionId;

    private boolean isLoading = false;



    public ResulltFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_resullt, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        score = bundle.getInt(getString(R.string.tag_score));
        rightQuestionId = bundle.getIntegerArrayList(getString(R.string.tag_rightquestion));
        wrongQuestionId = bundle.getIntegerArrayList(getString(R.string.tag_wrongquestion));
         if (null != view){
             if (null == mRank){
                initView();
             }
             showData(false);
         }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1){//返回RESULT_OK
            updateResult();
        }
    }

    private void showData(boolean isUpdated){
        mScore.setText(""+score);
    }

    private void updateResult(){
        if (mApplication.isLogined()){
            MCUser user = mApplication.user;
            NetInterface.uploadResult(getActivity(),user.getId(),score,rightQuestionId,wrongQuestionId,this);
            isLoading = true;
        }
    }

    private void initView(){
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
    }

    @Override
    public void onSuccess() {
        isLoading = false;
    }

    @Override
    public void onFalse(String msg) {
        isLoading = false;
    }
}
