package com.mckuai.mcstar.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mckuai.mcstar.R;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.utils.NetInterface;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.proguard.Z;

import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,NetInterface.OnLoginServerListener{

    private static MCUser user;
//    private static long mQQToken_Birthday;
//    private static long mQQToken_Expires;
    private static Tencent mTencent;
    private static boolean isLogin = false;
    private static String mQQToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        findViewById(R.id.login).setOnClickListener(this);

        if (null == mApplication.readPreference()){
            user = new MCUser();
        } else {
            user = mApplication.user;
        }
        mApplication.playMusic();
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
        switch (v.getId()){
            case R.id.login:
                if (null == user || user.getUserName() == null){
                    loginQQ();
                } else {
                    loginServer();
                }
                break;
            default:
                handleResult();
                break;
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void loginQQ(){
//        initTencent();
        if (null == mTencent) {
            mTencent = mApplication.mTencent;
        }
        mTencent.login(this,"all",new BaseUiListener()) ;
    }

    private void loginServer(){
        NetInterface.loginServer(this, user,mQQToken, this);
    }

    private void handleResult(){
        setResult(null != user ? RESULT_OK : RESULT_CANCELED);
        this.finish();
    }



    private void initTencent(){
        if (null == mTencent) {
            mTencent = Tencent.createInstance("101155101", getApplicationContext());
        }
    }


    private static boolean initOpenidAndToken(JSONObject jsonObject) {
        try {
            mQQToken = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            user.setUserName(openId);

            mApplication.mQQToken_Birthday = System.currentTimeMillis() - 600000;
            mApplication.mQQToken_Expires = jsonObject.getLong(Constants.PARAM_EXPIRES_IN);
            if (!TextUtils.isEmpty(mQQToken) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(mQQToken, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
            isLogin = false;
            return false;
        }
        return true;
    }


    private class BaseUiListener implements IUiListener {

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            logoutQQ();
            MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            if (null == response) {
                logoutQQ();
                MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                logoutQQ();
                MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
                return;
            }
            initOpenidAndToken(jsonResponse);
            updateUserInfo();
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            mTencent.logout(LoginActivity.this);
            MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
        }
    }


    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {
                    MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
                    logoutQQ();
                }

                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject) response;
                    if (json.has("nickname")) {
                        try {
                            user.setNickName(json.getString("nickname"));
                            user.setHeadImg(json.getString("figureurl_2"));// 取空间头像做为头像
                            user.setSex(json.getString("gender"));
                            mApplication.user = user;
                        } catch (Exception e) {
                            // TODO: handle exception
                            MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
                            mTencent.logout(LoginActivity.this);
                            return;
                        }
                        MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Success");
                        loginServer();           //登录到服务器
                    }
                }

                @Override
                public void onCancel() {
                    logoutQQ();
                }
            };
            UserInfo mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {
            logoutQQ();
        }
    }


    private void logoutQQ() {
        if (null != mTencent) {
            isLogin = false;
            mTencent.logout(LoginActivity.this);
        }
    }

    @Override
    public void onSuccess(MCUser user) {
        MCStar.user = user;
        handleResult();
    }

    @Override
    public void onFalse(String msg) {
        setResult(null != user ? RESULT_OK : RESULT_CANCELED);
    }
}

