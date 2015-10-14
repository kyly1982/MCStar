package com.mckuai.mcstar.activity;

import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.mckuai.mcstar.R;

public class QuestionActivity extends BaseActivity {

    RadioGroup rg_type;
    RadioGroup rg_options;
    TextInputLayout topic;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == rg_type){
            initView();
        }
    }

    private void initView(){
        initToolBar();
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText(getString(R.string.title_makequestion));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
