package com.tars.mcwa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;

import com.tars.mcwa.R;
import com.tars.mcwa.bean.MCUser;
import com.tars.mcwa.utils.NetInterface;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, NetInterface.OnLoginServerListener {

    private static MCUser user;
    private static Button login;
    private static AppCompatTextView hint;
    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        login = (Button) findViewById(R.id.login);
        hint = (AppCompatTextView) findViewById(R.id.hint_login);
        login.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText(R.string.title_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                MobclickAgent.onEvent(this, "wxLogin");
                login.setEnabled(false);
                anonymousLogin();
                break;
            default:
                setResult(RESULT_CANCELED);
                this.finish();
                break;
        }
    }

    private void anonymousLogin(){
        NetInterface.anonymousLogin(this,this);
        hint.setText(getString(R.string.hint_loginserver));
        hint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(MCUser user) {
        MobclickAgent.onEvent(this, "login_S");
        if (null == mApplication.user){
            mApplication.user = user;
        } else {
            mApplication.user.clone(user);
        }
        mApplication.saveProfile();
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onFalse(String msg) {
        feedback(false,false);
        login.setEnabled(true);
        hint.setText(msg);
        hint.setVisibility(View.VISIBLE);
        MobclickAgent.onEvent(this, "login_F");
        setResult(RESULT_CANCELED);
    }
}

