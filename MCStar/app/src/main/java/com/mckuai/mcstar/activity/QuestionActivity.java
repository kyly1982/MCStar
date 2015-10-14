package com.mckuai.mcstar.activity;

import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.bean.Question;

public class QuestionActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    RadioGroup type;
//    AppCompatRadioButton choice;
//    AppCompatRadioButton judgment;
    TextInputLayout topic;
    TextInputLayout option_a;
    TextInputLayout option_b;
    TextInputLayout option_c;
    TextInputLayout option_d;
    ImageView image;
    AppCompatButton submit;

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
        if (null == type){
            initView();
        }
    }

    private void initView(){
        type = (RadioGroup) findViewById(R.id.rg_questiontype);
//        choice = (AppCompatRadioButton) findViewById(R.id.type_choice);
//        judgment = (AppCompatRadioButton) findViewById(R.id.type_judgment);
        topic = (TextInputLayout) findViewById(R.id.questiontopic);
        option_a = (TextInputLayout) findViewById(R.id.option_a);
        option_b = (TextInputLayout) findViewById(R.id.option_b);
        option_c = (TextInputLayout) findViewById(R.id.option_c);
        option_d = (TextInputLayout) findViewById(R.id.option_d);
        type.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.type_choice:
                option_c.setVisibility(View.VISIBLE);
                option_d.setVisibility(View.VISIBLE);
                mQuestion.setQuestionType("choice");
                break;
            case R.id.type_judgment:
                option_c.setVisibility(View.GONE);
                option_d.setVisibility(View.GONE);
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
}
