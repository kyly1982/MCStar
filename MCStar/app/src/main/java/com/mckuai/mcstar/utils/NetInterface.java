package com.mckuai.mcstar.utils;


import android.content.Context;
import android.hardware.usb.UsbRequest;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.QuoteSpan;
import android.util.Log;

import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mckuai.mcstar.R;
import com.mckuai.mcstar.activity.MCStar;
import com.mckuai.mcstar.bean.ContributionBean;
import com.mckuai.mcstar.bean.MCUser;
import com.mckuai.mcstar.bean.Page;
import com.mckuai.mcstar.bean.Paper;
import com.mckuai.mcstar.bean.Question;
import com.mckuai.mcstar.bean.RankingListBean;
import com.tencent.connect.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kyly on 2015/10/13.
 */
public class NetInterface {

    public static void loginServer(@NonNull final Context context, @NonNull final MCUser user, @NonNull String token, @NonNull final OnLoginServerListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_loginserver);
        RequestParams params = new RequestParams();
        params.put("accessToken", token);
        params.put("openId", user.getUserName());
        params.put("nickName", user.getNickName());
        params.put("gender", user.getSex());
        params.put("headImg", user.getHeadImg());
        MCStar.client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.e("NE", "" + response.toString());
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    Gson gson = new Gson();
                    MCUser userinfo = gson.fromJson(result.msg, MCUser.class);
                    if (null != userinfo && userinfo.getUserName().equals(user.getUserName())) {
                        listener.onSuccess(userinfo);
                    } else {
                        listener.onFalse(context.getString(R.string.error_parsefalse));
                    }
                } else {
                    listener.onFalse(result.msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });
    }

    public static void getQuestions(@NonNull final Context context, @NonNull final OnGetQrestionListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_getquestionlist);
        MCStar.client.get(context, url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    Gson gson = new Gson();
                    Paper paper = gson.fromJson(result.msg, Paper.class);
                    if (null != paper && null != paper.getQuestion()) {
                        listener.onSuccess(paper);
                    } else {
                        listener.onFalse(context.getString(R.string.error_parsefalse));
                    }
                } else {
                    listener.onFalse(result.msg);
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });
    }

    public static void uploadResult(@NonNull final Context context, final int userId, int score, @NonNull ArrayList<Integer> correct, @NonNull ArrayList<Integer> error, @NonNull final OnReportListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_report);
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("allScore", score);
        if (!correct.isEmpty()) {
            String p = "";
            for (Integer id : correct) {
                p += ("," + id);
            }
            p = p.substring(1);
            params.put("correct", p);
        }
        if (!error.isEmpty()) {
            String p = "";
            for (Integer id : error) {
                p += ("," + id);
            }
            p = p.substring(1);
            params.put("error", p);
        }
        MCStar.client.post(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    Gson gson = new Gson();
                    ArrayList<MCUser> users = gson.fromJson(result.msg, new TypeToken<ArrayList<MCUser>>() {
                    }.getType());
//                    MCUser user = gson.fromJson(result.msg,MCUser.class);
                    if (null != users && !users.isEmpty() && 4 > users.size()) {
                        MCUser myself, user_pre, user_next;
                        switch (users.size()) {
                            case 1:
                                if (users.get(0).getId() == userId) {
                                    listener.onSuccess(users.get(0), null, null);
                                } else {
                                    listener.onFalse(context.getString(R.string.error_userformat_noself));
                                }
                                break;
                            case 2:
                                if (userId == users.get(0).getId()) {
                                    listener.onSuccess(users.get(0), null, users.get(1));
                                } else if (userId == users.get(1).getId()) {
                                    listener.onSuccess(users.get(1), users.get(0), null);
                                } else {
                                    listener.onFalse(context.getString(R.string.error_userformat_noself));
                                }
                                break;
                            default:
                                if (userId == users.get(1).getId()){
                                    listener.onSuccess(users.get(1), users.get(0), users.get(2));
                                } else {
                                    listener.onFalse(context.getString(R.string.error_userformat_noself));
                                }
                                break;
                        }
                    } else {
                        listener.onFalse("返回的用户数量不正确！");
                    }
                } else {
                    listener.onFalse(result.msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });
    }

    public static void uploadQuestion(@NonNull final Context context, MCUser user, @NonNull Question questin, @Nullable String picUrl, @NonNull final OnUploadQuestionListener listener) {

        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_upload);
        RequestParams params = new RequestParams();
        params.put("authorId", user.getId());
        params.put("authorName", user.getNickName());
        params.put("questionType", questin.getQuestionType());
        params.put("title", questin.getTitle());
        if (null != picUrl) {
            params.put("icon", picUrl);
        }
        params.put("answerOne", questin.getAnswerOne());
        params.put("answerTwo", questin.getAnswerOne());
        if (questin.getQuestionType().equals("choice")) {
            params.put("answerThree", questin.getAnswerOne());
            params.put("answerFour", questin.getAnswerOne());
        }

        MCStar.client.post(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    listener.onSuccess();
                } else {
                    listener.onFalse(result.msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });

    }

    public static void uploadPic(@NonNull final Context context, @NonNull MCUser user, @NonNull String filepath, @NonNull final OnUploadPicsListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_upload_pic);
        File file = new File(filepath);
        if (null != file && file.exists() && file.isFile()) {
            RequestParams params = new RequestParams();
            params.put("", "");
            MCStar.client.post(context, url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    PretreatmentResult result = pretreatmentResponse(context, response);
                    if (result.isSuccess) {
                        listener.onSuccess("");
                    } else {
                        listener.onFalse(result.msg);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
                }
            });
        } else {
            listener.onFalse("图片不存在！");
        }


    }

    public static void getUserInfo(@NonNull final Context context, final int userId, @NonNull final OnGetUserInfoListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_getuserInfo);
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        MCStar.client.get(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    Gson gson = new Gson();
                    MCUser user = gson.fromJson(result.msg, MCUser.class);
                    if (null != user && user.getId() == userId) {
                        listener.onSuccess(user);
                    } else {
                        listener.onFalse(context.getString(R.string.error_parsefalse));
                    }
                } else {
                    listener.onFalse(result.msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });

    }

    public static void getContribution(@NonNull final Context context, int userId, int page, @NonNull final OnGetContributionListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_contribution);
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("page", page);
        MCStar.client.get(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    Gson gson = new Gson();
                    ContributionBean bean = gson.fromJson(result.msg,ContributionBean.class);
                    if (null != bean && null != bean.getData() && null != bean.getPageBean()){
                        listener.onSuccess(bean.getPageBean(),bean.getData());
                    } else {
                        listener.onFalse(context.getString(R.string.error_parsefalse));
                    }
                } else {
                    listener.onFalse(result.msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });

    }

    public static void getRankingList(@NonNull final Context context, int userId, int page, @NonNull final OnGetRankingListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_rankinglist);
        RequestParams params = new RequestParams();
        if (0 != userId) {
            params.put("userId", userId);
        }
        params.put("page", page);
        MCStar.client.get(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    Gson gson = new Gson();
                    RankingListBean bean = gson.fromJson(result.msg,RankingListBean.class);
                    if (null != bean && null != bean.getList() && null != bean.getList().getPageBean()){
                        listener.onSuccess(bean.getList().getPageBean(),bean.getUser(),bean.getList().getData());
                    } else {
                        listener.onFalse(context.getString(R.string.error_parsefalse));
                    }
                } else {
                    listener.onFalse(result.msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });
    }

    public static interface OnLoginServerListener {
        void onSuccess(MCUser user);

        void onFalse(String msg);
    }

    public static interface OnGetQrestionListener {
        void onSuccess(Paper paper);

        void onFalse(String msg);
    }


    public static interface OnReportListener {
        void onSuccess(@NonNull MCUser myself, @Nullable MCUser user_pre, @Nullable MCUser user_next);

        void onFalse(String msg);
    }

    public static interface OnGetUserInfoListener {
        void onSuccess(MCUser userInfo);

        void onFalse(String msg);
    }

    public static interface OnGetContributionListener {
        void onSuccess(Page page, ArrayList<Question> questions);

        void onFalse(String msg);
    }

    public static interface OnUploadQuestionListener {
        void onSuccess();

        void onFalse(String msg);
    }

    public static interface OnGetRankingListener {
        void onSuccess(Page page,MCUser myself,ArrayList<MCUser> rankingList);

        void onFalse(String msg);
    }

    public static interface OnUploadPicsListener {
        void onSuccess(String url);

        void onFalse(String msg);
    }

    static class PretreatmentResult {
        boolean isSuccess = false;
        String msg;
    }

    private static PretreatmentResult pretreatmentResponse(@NonNull Context context, JSONObject response) {
        PretreatmentResult result = new PretreatmentResult();
        if (null == response || 10 > response.toString().length()) {
            result.msg = context.getString(R.string.error_pretreatmentres_nullerror);
            return result;
        }
        if (response.has("state") && response.has("dataObject")) {
            try {
                result.msg = response.getString("dataObject");
                result.isSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                result.msg = context.getString(R.string.error_pretreatmentres_ponsefalse, e.getLocalizedMessage());
            }
        } else {
            if (response.has("msg")) {
                try {
                    result.msg = context.getString(R.string.error_serverfalse, response.getString("msg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                result.msg = context.getString(R.string.error_serverfalse_unknow);
            }
        }

        return result;
    }

}
