package com.tars.mcwa.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tars.mcwa.R;
import com.tars.mcwa.bean.MCUser;
import com.tars.mcwa.utils.NetInterface;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.List;
import java.util.Map;

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
    }

    @Override
    public void onSuccess(MCUser user) {
        MobclickAgent.onEvent(this, "login_S");
        MCStar.user.clone(user);
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

