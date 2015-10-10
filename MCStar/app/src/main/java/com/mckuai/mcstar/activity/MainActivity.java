package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mckuai.mcstar.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.btn_getpaper).setOnClickListener(this);
        setTitles(R.string.app_name,0,0);
    }

    @Override
    public void onClick(View v) {

    }

    /*    @Override
    public void onFABClicked() {
        Intent intent = new Intent(this,ExaminationActivity.class);
        startActivity(intent);
    }*/
}
