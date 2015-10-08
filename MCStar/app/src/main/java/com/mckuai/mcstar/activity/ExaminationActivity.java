package com.mckuai.mcstar.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ViewFlipper;

import com.mckuai.mcstar.R;

public class ExaminationActivity extends BaseActivity {
    FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        mActionBar.setDisplayHomeAsUpEnabled(true);//左上角图标左边添加返回图标
        mFragmentManager = getFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void switchFragment(int position){

    }


    @Override
    public void onFABClicked() {

    }
}
