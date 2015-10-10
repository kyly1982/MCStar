package com.mckuai.mcstar.activity;

import android.content.Intent;
import android.os.Bundle;
import com.mckuai.mcstar.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        mActionBar.setDisplayHomeAsUpEnabled(true);//左上角图标左边添加返回图标
    }



 /*   @Override
    public void onFABClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }*/
}

