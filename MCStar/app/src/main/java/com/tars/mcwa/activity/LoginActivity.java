package com.tars.mcwa.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tars.mcwa.R;
import com.tars.mcwa.bean.MCUser;
import com.tars.mcwa.utils.NetInterface;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,NetInterface.OnLoginServerListener{

    private static MCUser user;
    private static Tencent mTencent;
    private static String mQQToken;
    private IUiListener loginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTencent = Tencent.createInstance("1104907496", getApplicationContext());
        if (null == mApplication.readPreference()){
            user = new MCUser();
        } else {
            user = mApplication.user;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN){
            mTencent.handleLoginData(data, loginListener);
        }
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
        switch (v.getId()){
            case R.id.login:
                if (null == user || user.getUserName() == null){
                    loginQQ();
                } else {
                    loginServer();
                }
                break;
            default:
                setResult(RESULT_CANCELED);
                this.finish();
                break;
        }
    }

    private void loginQQ(){
        MobclickAgent.onEvent(this, "qqlogin");
        if (!mTencent.isSessionValid()) {
            loginListener = new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject values) {
                    super.doComplete(values);
                    initOpenidAndToken(values);
                }
            };
            mTencent.login(this, "all", loginListener);
        } else {
            logoutQQ();
        }
    }

    private void loginServer(){
        MobclickAgent.onEvent(this,"login");
        NetInterface.loginServer(this, user, mQQToken, this);
    }

    private static boolean initOpenidAndToken(JSONObject jsonObject) {
        Log.e("111","initOpenidAndToken");
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
            return false;
        }
        return true;
    }


    private class BaseUiListener implements IUiListener {
        public BaseUiListener(){
        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            logoutQQ();
            setResult(RESULT_CANCELED);
            LoginActivity.this.finish();
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            if (null == response) {
                logoutQQ();
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null == jsonResponse || jsonResponse.length() == 0) {
                logoutQQ();
                return;
            }
            doComplete(jsonResponse);
            updateUserInfo();
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            logoutQQ();
        }

        protected  void doComplete(JSONObject values){

        }
    }


    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {
                    logoutQQ();
                }

                @Override
                public void onComplete(final Object response) {
                    Log.e("updateUserInfo","onComplete");
                    JSONObject json = (JSONObject) response;
                    if (json.has("nickname")) {
                        try {
                            user.setNickName(json.getString("nickname"));
                            user.setHeadImg(json.getString("figureurl_2"));// 取空间头像做为头像
                            user.setSex(json.getString("gender"));
                            mApplication.user = user;
                        } catch (Exception e) {
                            // TODO: handle exception
                            logoutQQ();
                            return;
                        }
                        MobclickAgent.onEvent(LoginActivity.this, "qqLogin_S");
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
        MobclickAgent.onEvent(this,"qqLogin_F");
        if (null != mTencent) {
            mTencent.logout(LoginActivity.this);
        }
    }

    @Override
    public void onSuccess(MCUser user) {
        MobclickAgent.onEvent(this,"login_S");
        MCStar.user.clone(user);
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onFalse(String msg) {
        feedback_false();
        MobclickAgent.onEvent(this, "login_F");
        setResult(RESULT_CANCELED);
        this.finish();
    }
}

