package com.mckuai.mcstar.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.bean.MCUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    MCUser user = mApplication.mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        mTitle.setText(R.string.title_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                if (null == user || user.getIsServerActor() == 0){
                    loginQQ();
                } else {
                    loginServer();
                }
                break;
            default:
                returnResult();
                break;
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void loginQQ(){
        returnResult();
    }

    private void loginServer(){
        returnResult();
    }

    private void returnResult(){
        setResult(null != user ?RESULT_OK:RESULT_CANCELED );
        this.finish();
    }
}

