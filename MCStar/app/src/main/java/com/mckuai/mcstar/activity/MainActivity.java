package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;

import com.mckuai.mcstar.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFABClicked() {
        Intent intent = new Intent(this,ExaminationActivity.class);
        startActivity(intent);
    }
}
