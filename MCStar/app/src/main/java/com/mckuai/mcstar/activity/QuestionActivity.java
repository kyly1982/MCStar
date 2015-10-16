package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.bean.Question;
import com.mckuai.mcstar.utils.NetInterface;

public class QuestionActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, NetInterface.OnUploadQuestionListener {

    private RadioGroup type;
    private TextInputLayout topic;
    private TextInputLayout option_a;
    private TextInputLayout option_b;
    private TextInputLayout option_c;
    private TextInputLayout option_d;
    private ImageView image;
    private AppCompatButton submit;

    private Question mQuestion = new Question();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == type) {
            initView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            uploadQuestion();
        }
    }

    private void initView() {
        type = (RadioGroup) findViewById(R.id.rg_questiontype);
        topic = (TextInputLayout) findViewById(R.id.questiontopic);
        option_a = (TextInputLayout) findViewById(R.id.option_a);
        option_b = (TextInputLayout) findViewById(R.id.option_b);
        option_c = (TextInputLayout) findViewById(R.id.option_c);
        option_d = (TextInputLayout) findViewById(R.id.option_d);
        submit = (AppCompatButton) findViewById(R.id.submit);
        image = (ImageView) findViewById(R.id.questionimage);
        type.setOnCheckedChangeListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.type_choice:
                option_c.setVisibility(View.VISIBLE);
                option_d.setVisibility(View.VISIBLE);
                mQuestion.setQuestionType("choice");
                break;
            case R.id.type_judgment:
                option_c.setVisibility(View.GONE);
                option_d.setVisibility(View.GONE);
                option_c.getEditText().setText("");
                ;
                option_d.getEditText().setText("");
                mQuestion.setQuestionType("judge");
                break;
        }
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText(getString(R.string.title_makequestion));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void uploadQuestion() {
        if (mApplication.isLogined()) {
            NetInterface.uploadQuestion(this, mApplication.user, mQuestion, null, this);
        } else {
            callLogin(1);
        }
    }

    private void getQuestion() {
        mQuestion.setTitle(topic.getEditText().getText().toString());
        mQuestion.setAnswerOne(option_a.getEditText().getText().toString());
        mQuestion.setAnswerTwo(option_b.getEditText().getText().toString());
        if (mQuestion.getQuestionType().equals("choice")) {
            mQuestion.setAnswerThree(option_c.getEditText().getText().toString());
            mQuestion.setAnswerFour(option_d.getEditText().getText().toString());
        }
    }

    private boolean checkParams() {
        boolean result = false;
        String temp = mQuestion.getTitle();
        if (null != temp && 5 < temp.length() && 51 > temp.length()) {
            temp = mQuestion.getAnswerOne();
            if (null != temp && 0 < temp.length() && 11 > temp.length()) {
                temp = mQuestion.getAnswerTwo();
                if (null != temp && 0 < temp.length() && 11 > temp.length()) {
                    if (mQuestion.getQuestionType().equals("judge")) {
                        result = true;
                    } else {
                        temp = mQuestion.getAnswerThree();
                        if (null != temp && 0 < temp.length() && 11 > temp.length()) {
                            temp = mQuestion.getAnswerFour();
                            if (null != temp && 0 < temp.length() && 11 > temp.length()) {
                                result = true;
                            } else {
                                //D
                            }
                        } else {
                            //C
                        }
                    }
                } else {
                    //B
                }
            } else {
                //答案A长度不正确
            }

        } else {
            //标题长度不正确
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                getQuestion();
                if (checkParams()) {
                    uploadQuestion();
                } else {
                    Log.e("UQ", "参数不正确");
                }
                break;
        }
    }

    @Override
    public void onSuccess() {
        mApplication.user.setUploadNum(mApplication.user.getUploadNum()+1);
        this.finish();
    }

    @Override
    public void onFalse(String msg) {
        Log.e("UQ", msg);
    }
}
