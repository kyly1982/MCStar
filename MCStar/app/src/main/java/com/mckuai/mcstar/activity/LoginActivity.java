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
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.proguard.Z;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,NetInterface.OnLoginServerListener{

    private static MCUser user;
    private static Tencent mTencent;
    private static boolean isLogin = false;
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
        Log.e("loginQQ","start");
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
            Log.e("loginQQ","SessionInvalid");
        }
    }

    private void loginServer(){
        NetInterface.loginServer(this, user, mQQToken, this);
    }

    private void handleResult(){
        if (isLogin && null != user && 0 < user.getId() && !user.getUserName().isEmpty()){
            setResult(RESULT_OK);
            mApplication.saveProfile();
        } else {
            setResult(RESULT_CANCELED);
        }
        this.finish();
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
            isLogin = false;
            return false;
        }
        return true;
    }


    private class BaseUiListener implements IUiListener {
        public BaseUiListener(){
            Log.e("BaseUiListener","create");
        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            logoutQQ();
            Log.e("BaseUiListener", "onCancel");
            setResult(RESULT_CANCELED);
            LoginActivity.this.finish();
            MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Log.e("BaseUiListener","onComplete");
            if (null == response) {
                logoutQQ();
                MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null == jsonResponse || jsonResponse.length() == 0) {
                logoutQQ();
                MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
                return;
            }
            doComplete(jsonResponse);
            updateUserInfo();
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            mTencent.logout(LoginActivity.this);
            MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
            Log.e("BaseUiListener","onError");
        }

        protected  void doComplete(JSONObject values){

        }
    }


    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {
                    Log.e("updateUserInfo","onError");
                    MobclickAgent.onEvent(LoginActivity.this, "qqLogin_Failure");
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
            Log.e("updateUserInfo","else");
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
        isLogin = true;
        MCStar.user.clone(user);
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onFalse(String msg) {
        feedback_false();
        Log.e("登录到服务器时失败!", msg);
        setResult(RESULT_CANCELED);
        this.finish();
    }
}

