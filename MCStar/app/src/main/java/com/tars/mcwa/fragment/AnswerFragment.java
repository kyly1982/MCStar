package com.tars.mcwa.fragment;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tars.mcwa.R;
import com.tars.mcwa.activity.ExaminationActivity;
import com.tars.mcwa.bean.Question;
import com.tars.mcwa.utils.CircleBitmap;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerFragment extends BaseFragment implements View.OnClickListener {
    private ImageButton mCover;
    private AppCompatTextView mQuestionTitle;
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
    private final String TAG="AnswerFragment";

    private ImageLoader mLoader = ImageLoader.getInstance();
    private CountDownTimer mTimer;
    private boolean isTimeOut = false;




    public AnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer, container, false);
        initView(view);
//        Log.e(TAG,"onCreateView");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e(TAG, "onResume");
        mQuestions = (ArrayList<Question>) getArguments().getSerializable(getString(R.string.tag_questions));
        if (null != mQuestions && !mQuestions.isEmpty()) {
            mIndex = 0;
            showQuestion();
        }
        showUserInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.e(TAG, "onPause");
        mTimer.cancel();
        handler.removeMessages(2);
        handler.removeMessages(3);
    }

    private void initView(View view) {
//        Log.e(TAG, "initView");
        mCover = (ImageButton) view.findViewById(R.id.usercover);
        mScore = (AppCompatTextView) view.findViewById(R.id.score);
        mScore_question = (AppCompatTextView) view.findViewById(R.id.questionscore);
        mQuestionIndex = (AppCompatTextView) view.findViewById(R.id.questionIndex);
        mTime = (AppCompatTextView) view.findViewById(R.id.timer);
        mQuestionTitle = (AppCompatTextView) view.findViewById(R.id.questionContent);
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
        if (!isChecked) {
            mTimer.cancel();
            switch (v.getId()) {
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
//        Log.e(TAG, "showQuestion");
        isChecked = false;
        Question question = mQuestions.get(mIndex);
        ArrayList<String> answer = question.getOptionsEx();
        mOption_A.setChecked(false);
        mOption_B.setChecked(false);
        mOption_C.setChecked(false);
        mOption_D.setChecked(false);
        mOption_A.setText(answer.get(0));
        mOption_B.setText(answer.get(1));
        mOption_A.setBackgroundDrawable(getResources().getDrawable(R.drawable.option_selector));
        mOption_B.setBackgroundDrawable(getResources().getDrawable(R.drawable.option_selector));
        switch (question.getQuestionType()) {
            case "choice":
                mOption_C.setText(answer.get(2));
                mOption_D.setText(answer.get(3));
                mOption_C.setBackgroundDrawable(getResources().getDrawable(R.drawable.option_selector));
                mOption_D.setBackgroundDrawable(getResources().getDrawable(R.drawable.option_selector));
                mOption_C.setVisibility(View.VISIBLE);
                mOption_D.setVisibility(View.VISIBLE);
                break;
            case "judge":
                mOption_C.setVisibility(View.GONE);
                mOption_D.setVisibility(View.GONE);
                break;
        }
        mQuestionIndex.setText((mIndex + 1) + ".");
        mScore_question.setText(getString(R.string.addscore, question.getScore()));
        mContributioner.setText(getString(R.string.contributor, question.getAuthorName()));
        mQuestionTitle.setText(question.getTitle() + "");
        if (null != question.getIcon() && 10 < question.getIcon().length()) {
            mLoader.displayImage(question.getIcon(), mImage);
            mImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mImage.setVisibility(View.VISIBLE);
        } else {
            mImage.setVisibility(View.GONE);
        }
        mTime.setText("10");
        isTimeOut = false;
        countTime(10);
        isTimeOut = false;
    }

    private void processResult(AppCompatCheckedTextView answer) {
//        Log.e(TAG, "processResult");
        Question question = mQuestions.get(mIndex);
        String rightAnswer = question.getRightAnswer();
        answer.setChecked(true);
        if (answer.getText().equals(rightAnswer)) {
            //答对
            rightQuestions.add(question.getId());
            score += question.getScore();
            mScore.setText(score + "");
            handler.sendEmptyMessageDelayed(3, LOCKTIME_RIGHT);
        } else {
            //答错
            wrongQuestions.add(question.getId());
            answer.setBackgroundColor(getResources().getColor(R.color.red));
            processWrong(question, answer);
            handler.sendEmptyMessageDelayed(2, LOCKTIME_WRONG);
        }
    }

    private void processWrong(Question question, AppCompatCheckedTextView answer) {
//        Log.e(TAG, "processWrong");
        feedback_false();
        if (null != answer) {
            YoYo.with(Techniques.Shake).playOn(answer);
            answer.setBackgroundColor(getResources().getColor(R.color.red));
        }
        wrongQuestions.add(question.getId());
        String rightAnswer = question.getRightAnswer();
        if (mOption_A.getText().equals(rightAnswer)) {
            mOption_A.setBackgroundColor(getResources().getColor(R.color.green));
        } else if (mOption_B.getText().equals(rightAnswer)) {
            mOption_B.setBackgroundColor(getResources().getColor(R.color.green));
        } else if (mOption_C.getText().equals(rightAnswer)) {
            mOption_C.setBackgroundColor(getResources().getColor(R.color.green));
        } else if (mOption_D.getText().equals(rightAnswer)) {
            mOption_D.setBackgroundColor(getResources().getColor(R.color.green));
        }
        handler.sendEmptyMessageDelayed(2, LOCKTIME_WRONG);
    }

    private void checkExaminationFinished(boolean isUserAction) {
//        Log.e(TAG, "checkExaminationFinished");
        mIndex++;
        if (mIndex < mQuestions.size()) {
            showQuestion();
        } else {
            //已做完
//            Log.e("AQ","问题已做完");
            finishExamination(isUserAction);
        }
    }

    private void finishExamination(boolean isUserFinished) {
//        Log.e(TAG, "finishExamination");
        ExaminationActivity activity = (ExaminationActivity) getActivity();

        ExaminationActivity.score = this.score;
        ExaminationActivity.wrongQuestions = this.wrongQuestions;
        ExaminationActivity.rightQuestions = this.rightQuestions;

        if (!isUserFinished) {
            //显示时间到的信息
//            Log.e(TAG, "finishExamination,time out");
        }
        ((ExaminationActivity) getActivity()).showNextFragment();
    }

    private void countTime(int time) {
//        Log.e(TAG, "countTime");
        if (null == mTimer) {
            mTimer = new CountDownTimer(time * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
//                    Log.e(TAG, "onTick:"+millisUntilFinished / 1000);
                    mTime.setText(millisUntilFinished / 1000 + "");
                }

                @Override
                public void onFinish() {
//                    Log.e(TAG, "ans_TimeOut");
                    MobclickAgent.onEvent(getActivity(),"ans_TimeOut");
                    mTime.setText("0");
                    isChecked = true;
                    isTimeOut = true;
                    processWrong(mQuestions.get(mIndex), null);
                }
            };
        }
        mTime.setText(time + "");
        mTimer.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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

    private void showUserInfo() {
//        Log.e(TAG, "showUserInfo");
        if (mApplication.isLogined()) {
            final String cover = mApplication.user.getHeadImg();
            if (null != cover && 10 < cover.length()) {
                ImageLoader loader = ImageLoader.getInstance();
                loader.loadImage(cover, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (null != loadedImage) {
                            mCover.setImageBitmap(CircleBitmap.getCircleBitmap(loadedImage, getResources().getDimensionPixelSize(R.dimen.usercover_diameter_small)));
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
                return;
            }
        }
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher);
        assert drawable != null;
        mCover.setImageBitmap(CircleBitmap.getCircleBitmap(drawable.getBitmap(), getResources().getDimensionPixelSize(R.dimen.usercover_diameter_small)));
    }

}
