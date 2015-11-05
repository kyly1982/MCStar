package com.tars.mcwa.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.tars.mcwa.R;
import com.tars.mcwa.bean.ContributionBean;
import com.tars.mcwa.bean.MCUser;
import com.tars.mcwa.bean.Page;
import com.tars.mcwa.bean.Paper;
import com.tars.mcwa.bean.Question;
import com.tars.mcwa.bean.RankingListBean;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by kyly on 2015/10/13.
 */
public class NetInterface {
    private static AsyncHttpClient mClient = new AsyncHttpClient();

    public static void loginServer(@NonNull final Context context, @NonNull final MCUser user, @NonNull String token, @NonNull final OnLoginServerListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_loginserver);
        RequestParams params = new RequestParams();
        params.put("accessToken", token);
        params.put("openId", user.getUserName());
        params.put("nickName", user.getNickName());
        params.put("gender", user.getSex());
        params.put("headImg", user.getHeadImg());
        mClient.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
                listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });
    }

    public static void getQuestions(@NonNull final Context context, @NonNull final OnGetQrestionListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_getquestionlist);
        mClient.get(context, url, new JsonHttpResponseHandler() {

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
        mClient.post(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    Gson gson = new Gson();
                    ArrayList<MCUser> users = gson.fromJson(result.msg, new TypeToken<ArrayList<MCUser>>() {
                    }.getType());
                    if (null != users && !users.isEmpty() && 4 > users.size()) {
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
                                if (userId == users.get(1).getId()) {
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
        params.put("answerTwo", questin.getAnswerTwo());
        if (questin.getQuestionType().equals("choice")) {
            params.put("answerThree", questin.getAnswerThree());
            params.put("answerFour", questin.getAnswerFour());
        }

        mClient.post(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    listener.onSuccess();
                } else {
                    listener.onFalse(4, result.msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.onFalse(4, context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });

    }

    public static void uploadPic(@NonNull final Context context, @NonNull MCUser user, @NonNull Bitmap bitmap, @NonNull final OnUploadPicsListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_upload_pic);
        if (null != bitmap) {
            RequestParams params = new RequestParams();
            params.put("upload", Bitmap2IS(bitmap), "01.jpg", "image/jpeg");
            mClient.post(context, url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    PretreatmentResult result = pretreatmentResponse(context, response);
                    if (result.isSuccess) {
                        listener.onSuccess(result.msg);
                    } else {
                        listener.onFalse(3, result.msg);
                    }
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    if (totalSize > 0) {
                        int progress = (int)(bytesWritten * 100 / totalSize);
                        listener.onProgress(progress);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    listener.onFalse(3, context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
                }
            });
        } else {
            listener.onFalse(3, "图片不存在！");
        }


    }

    public static void getUserInfo(@NonNull final Context context, final int userId, @NonNull final OnGetUserInfoListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_getuserInfo);
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        mClient.get(context, url, params, new JsonHttpResponseHandler() {
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
                listener.onFalse(context.getString(R.string.error_requestfalse, throwable.getLocalizedMessage()));
            }
        });

    }

    public static void getContribution(@NonNull final Context context, int userId, int page, @NonNull final OnGetContributionListener listener) {
        String url = context.getString(R.string.interface_domain) + context.getString(R.string.interface_contribution);
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("page", page);
        mClient.get(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    Gson gson = new Gson();
                    ContributionBean bean = gson.fromJson(result.msg, ContributionBean.class);
                    if (null != bean && null != bean.getData() && null != bean.getPageBean()) {
                        listener.onSuccess(bean.getPageBean(), bean.getData());
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
        mClient.get(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                PretreatmentResult result = pretreatmentResponse(context, response);
                if (result.isSuccess) {
                    Gson gson = new Gson();
                    RankingListBean bean = gson.fromJson(result.msg, RankingListBean.class);
                    if (null != bean && null != bean.getList() && null != bean.getList().getPageBean()) {
                        listener.onSuccess(bean.getList().getPageBean(), bean.getUser(), bean.getList().getData());
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

    public interface OnLoginServerListener {
        void onSuccess(MCUser user);

        void onFalse(String msg);
    }

    public interface OnGetQrestionListener {
        void onSuccess(Paper paper);

        void onFalse(String msg);
    }


    public interface OnReportListener {
        void onSuccess(@NonNull MCUser myself, @Nullable MCUser user_pre, @Nullable MCUser user_next);

        void onFalse(String msg);
    }

    public interface OnGetUserInfoListener {
        void onSuccess(MCUser userInfo);

        void onFalse(String msg);
    }

    public interface OnGetContributionListener {
        void onSuccess(Page page, ArrayList<Question> questions);

        void onFalse(String msg);
    }

    public interface OnUploadQuestionListener {
        void onSuccess();

        void onFalse(int requestCode, String msg);
    }

    public interface OnGetRankingListener {
        void onSuccess(Page page, MCUser myself, ArrayList<MCUser> rankingList);

        void onFalse(String msg);
    }

    public interface OnUploadPicsListener {
        void onSuccess(String url);
        void onFalse(int requestCode, String msg);
        void onProgress(int progress);
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
        if (response.has("state")) {
            try {
                if (response.getString("state").equals("ok")) {
                    result.isSuccess = true;
                    if (response.has("dataObject")) {
                        result.msg = response.getString("dataObject");
                    }
                } else {
                    if (response.has("msg")) {
                        result.msg = response.getString("msg");
                    } else {
                        result.msg = context.getString(R.string.error_serverfalse_unknow);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.msg = context.getString(R.string.error_pretreatmentres_ponsefalse, e.getLocalizedMessage());
            }
        } else {
            result.msg = context.getString(R.string.error_serverreturn_unkonw);
        }

        return result;
    }

    private static InputStream Bitmap2IS(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

}
