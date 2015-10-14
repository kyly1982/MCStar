package com.mckuai.mcstar.utils;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.activity.MCStar;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.bean.Paper;

import org.json.JSONObject;
import org.apache.http.Header;

/**
 * Created by kyly on 2015/10/13.
 */
public class NetInterface {

    public static void loginServer(@NonNull Context context,@NonNull final MCUser user,@NonNull String token,@NonNull final OnLoginServerListener listener){
        String url = context.getString(R.string.interface_domain)+context.getString(R.string.interface_loginserver);
        RequestParams params = new RequestParams();
        params.put("accessToken",token);
        params.put("openId", user.getUserName());
        params.put("nickName", user.getNickName());
        params.put("gender", user.getSex());
        params.put("headImg", user.getHeadImg());
        MCStar.client.get(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (null != response && response.has("dataObject")){
                        Gson gson = new Gson();
                        MCUser user1 = gson.fromJson(response.getString("dataObject"),MCUser.class);
                        if (null != user1 && user1.getUserName().equals(user.getUserName())){
                            listener.onSuccess(user1);
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                listener.onFalse("解析失败!");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.onFalse(throwable.getLocalizedMessage());
            }
        });
    }

    public static void getQuestions(@NonNull Context context,@NonNull final OnGetQrestionListener listener){
        String url = context.getString(R.string.interface_domain)+context.getString(R.string.interface_getquestionlist);
        MCStar.client.get(context,url,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                String result = parseResopnse(response,"dataObject");
                if (null != response){
                    Gson gson = new Gson();
                    Paper paper = gson.fromJson(result,Paper.class);
                    listener.onSuccess(paper);
                }
                else {
                    listener.onFalse("解析失败!");
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.onFalse(throwable.getLocalizedMessage());
            }
        });
    }

    public static void uploadResult(@NonNull Context context,@NonNull OnReportListener listener){
        String url = context.getString(R.string.interface_domain)+context.getString(R.string.interface_loginserver);

    }

    public static void uploadQuestion(@NonNull Context context,@NonNull OnUploadQuestionListener listener){
        String url = context.getString(R.string.interface_domain)+context.getString(R.string.interface_loginserver);

    }

    public static void getUserInfo(@NonNull Context context, int userId,@NonNull OnGetUserInfoListener listener){
        String url = context.getString(R.string.interface_domain)+context.getString(R.string.interface_loginserver);

    }

    public static void getContribution(@NonNull Context context,int page,@NonNull OnGetContributionListener listener){
        String url = context.getString(R.string.interface_domain)+context.getString(R.string.interface_loginserver);

    }

    public static void getRankingList(@NonNull Context context,int page,@NonNull OnGetRankingListener listener){
        String url = context.getString(R.string.interface_domain)+context.getString(R.string.interface_loginserver);

    }

//    public static interface OnLoginListener{
//        void onResult(MCUser user);
//        void onFalse();
//    }

    public static interface OnLoginServerListener{
        void onSuccess(MCUser user);
        void onFalse(String msg);
    }

    public static interface OnGetQrestionListener{
        void onSuccess(Paper paper);
        void onFalse(String msg);
    }


    public static interface OnReportListener{
        void onSuccess();
        void onFalse();
    }

    public static interface OnGetUserInfoListener{
        void onSuccess();
        void onFalse();
    }

    public static interface OnGetContributionListener{
        void onSuccess();
        void onFalse();
    }

    public static interface OnUploadQuestionListener{
        void onSuccess();
        void onFalse();
    }

    public static interface OnGetRankingListener{
        void onSuccess();
        void onFalse();
    }

    private static String parseResopnse(@NonNull JSONObject result,@NonNull String name){
        try {
            if (result.has("state") && result.getString("state").equals("ok")) {
                if (name.equals("dataObject")){
                    return result.getString(name);
                } else {
                    return result.getJSONObject("dataObject").getString(name);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
