package com.mckuai.mcstar.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.activity.ExaminationActivity;
import com.mckuai.mcstar.bean.Question;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerFragment extends BaseFragment implements View.OnClickListener {
    private ImageButton mCover;
    private AppCompatCheckedTextView mOption_A;
    private AppCompatCheckedTextView mOption_B;
    private AppCompatCheckedTextView mOption_C;
    private AppCompatCheckedTextView mOption_D;
    private AppCompatTextView mContributioner;
    private AppCompatTextView mScore;
    private AppCompatTextView mScore_question;
    private AppCompatTextView mTime;
    private AppCompatTextView mQuestionIndex;
    private ImageView mImage;

    private boolean isChecked = false;
    private int mIndex = 0;
    private int score = 0;
    private final int LOCKTIME_RIGHT = 1000;
    private final int LOCKTIME_WRONG = 2000;
    private ArrayList<Question> mQuestions;
    private ArrayList<Integer> wrongQuestions = new ArrayList<>();
    private ArrayList<Integer> rightQuestions = new ArrayList<>();

    private ImageLoader mLoader = ImageLoader.getInstance();




    public AnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mQuestions = (ArrayList<Question>) getArguments().getSerializable(getString(R.string.tag_questions));
        if (null != mQuestions && !mQuestions.isEmpty()){
            Log.e("AS=",""+mQuestions.size());
            mIndex = 0;
            showQuestion();
        }
        //mQuestions = ((ExaminationActivity) getActivity()).paper.getQuestion();

    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(3);
    }

    private void initView(View view) {
        mCover = (ImageButton) view.findViewById(R.id.usercover);
        mScore = (AppCompatTextView) view.findViewById(R.id.score);
        mScore_question = (AppCompatTextView) view.findViewById(R.id.questionscore);
        mQuestionIndex = (AppCompatTextView) view.findViewById(R.id.questionIndex);
        mTime = (AppCompatTextView) view.findViewById(R.id.timer);
        mContributioner = (AppCompatTextView) view.findViewById(R.id.contributioner);
        mOption_A = (AppCompatCheckedTextView) view.findViewById(R.id.answerA);
        mOption_B = (AppCompatCheckedTextView) view.findViewById(R.id.answerB);
        mOption_C = (AppCompatCheckedTextView) view.findViewById(R.id.answerC);
        mOption_D = (AppCompatCheckedTextView) view.findViewById(R.id.answerD);
        mImage = (ImageView) view.findViewById(R.id.answer_questionimage);
        mScore.setText("0");

        mOption_A.setOnClickListener(this);
        mOption_B.setOnClickListener(this);
        mOption_C.setOnClickListener(this);
        mOption_D.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!isChecked){
            switch (v.getId()){
                case R.id.answerA:
                    processResult(mOption_A);
                    break;
                case R.id.answerB:
                    processResult(mOption_B);
                    break;
                case R.id.answerC:
                    processResult(mOption_C);
                    break;
                case R.id.answerD:
                    processResult(mOption_D);
                    break;
            }
            isChecked = true;
        }
    }


    private void showQuestion() {
        isChecked = false;
        Question question = mQuestions.get(mIndex);
        ArrayList<String> answer = question.getOptionsEx();
        mOption_A.setBackgroundDrawable(getResources().getDrawable(R.drawable.option_selector));
        mOption_B.setBackgroundDrawable(getResources().getDrawable(R.drawable.option_selector));
        mOption_A.setChecked(false);
        mOption_B.setChecked(false);
        switch (question.getQuestionType()) {
            case "choice":
                mOption_A.setText(answer.get(0));
                mOption_B.setText(answer.get(1));
                mOption_C.setText(answer.get(2));
                mOption_D.setText(answer.get(3));
                mOption_C.setBackgroundDrawable(getResources().getDrawable(R.drawable.option_selector));
                mOption_D.setBackgroundDrawable(getResources().getDrawable(R.drawable.option_selector));
                mOption_C.setChecked(false);
                mOption_D.setChecked(false);
                mOption_C.setVisibility(View.VISIBLE);
                mOption_D.setVisibility(View.VISIBLE);
                break;
            case "judge":
                mOption_A.setText(answer.get(0));
                mOption_B.setText(answer.get(1));
                mOption_C.setVisibility(View.GONE);
                mOption_D.setVisibility(View.GONE);
                break;
        }
        mQuestionIndex.setText((mIndex+1)+".");
        mScore_question.setText(getString(R.string.addscore,question.getScore()));
        mContributioner.setText(getString(R.string.contributor,question.getAuthorName()));
        if (null != question.getIcon() && 10 < question.getIcon().length()){
            mLoader.displayImage(question.getIcon(),mImage);
            mImage.setVisibility(View.VISIBLE);
        } else {
            //mImage.setVisibility(View.GONE);
        }
    }

    private void processResult(AppCompatCheckedTextView answer) {
        Question question = mQuestions.get(mIndex);
        String rightAnswer = question.getRightAnswer();
        answer.setChecked(false);
        if (answer.getText().equals(rightAnswer)) {
            //答对
            rightQuestions.add(question.getId());
            score += question.getScore();
            mScore.setText(score + "");
            handler.sendEmptyMessageDelayed(3,LOCKTIME_RIGHT);
        } else {
            //答错
            wrongQuestions.add(question.getId());
            answer.setBackgroundColor(getResources().getColor(R.color.red));

            if (mOption_A.getText().equals(rightAnswer)){
                mOption_A.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (mOption_B.getText().equals(rightAnswer)){
                mOption_B.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (mOption_C.getText().equals(rightAnswer)){
                mOption_C.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (mOption_D.getText().equals(rightAnswer)){
                mOption_D.setBackgroundColor(getResources().getColor(R.color.green));
            }
            handler.sendEmptyMessageDelayed(2,LOCKTIME_WRONG);
        }
    }

    private void checkExaminationFinished(boolean isUserAction){
        mIndex++;
        if (mIndex < mQuestions.size()) {
            showQuestion();
        } else {
            //已做完
            finishExamination(isUserAction);
        }
    }

    private void finishExamination(boolean isUserFinished) {
        ExaminationActivity activity = (ExaminationActivity) getActivity();

        ExaminationActivity.score = this.score;
        ExaminationActivity.wrongQuestions = this.wrongQuestions;
        ExaminationActivity.rightQuestions = this.rightQuestions;

        if (!isUserFinished) {
            //显示时间到的信息
        }
        ((ExaminationActivity) getActivity()).showNextFragment();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    handler.removeMessages(1);
                    ((ExaminationActivity) getActivity()).showNextFragment();
                    break;
                case 2:
                    handler.removeMessages(2);
                    checkExaminationFinished(false);
                    break;
                case 3:
                    handler.removeMessages(3);
                    checkExaminationFinished(true);
            }
        }
    };
}
