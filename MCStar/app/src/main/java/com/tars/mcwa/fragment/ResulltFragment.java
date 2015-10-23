package com.tars.mcwa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tars.mcwa.R;
import com.tars.mcwa.activity.LoginActivity;
import com.tars.mcwa.bean.MCUser;
import com.tars.mcwa.utils.NetInterface;
import com.tars.mcwa.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.ArrayList;


public class ResulltFragment extends BaseFragment implements NetInterface.OnReportListener, View.OnClickListener {

    private View view;
    private AppCompatTextView mRank_pre;
    private AppCompatTextView mRank_next;
    private AppCompatTextView mRank;
    private AppCompatTextView mScore_pre;
    private AppCompatTextView mScore_next;
    private AppCompatTextView mScore;
    private ImageView mCover_pre;
    private ImageView mCover_next;
    private ImageView mCover;
    private AppCompatTextView mReExam;
    private AppCompatTextView mShare;

    private RelativeLayout user_pre;
    private RelativeLayout user_next;
    private RelativeLayout user_mine;


    private int score;
    private boolean isLoading = false;
    private boolean isUploaded = false;
    private ArrayList<Integer> rightQuestionId;
    private ArrayList<Integer> wrongQuestionId;

    private MCUser user_p, user_n;
    private ImageLoader mLoader;
    private com.umeng.socialize.controller.UMSocialService mShareService;
    private DisplayImageOptions options;


    public ResulltFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resullt, container, false);;
        mShareService = UMServiceFactory.getUMSocialService("com.umeng.share");
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
        mLoader = ImageLoader.getInstance();
        configPlatforms();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != view && null == mRank) {
            Bundle bundle = getArguments();
            score = bundle.getInt(getString(R.string.tag_score));
            rightQuestionId = bundle.getIntegerArrayList(getString(R.string.tag_rightquestion));
            wrongQuestionId = bundle.getIntegerArrayList(getString(R.string.tag_wrongquestion));
            MobclickAgent.onEvent(getActivity(), "ans_S", rightQuestionId.size());
            MobclickAgent.onEvent(getActivity(), "ans_F", wrongQuestionId.size());
            initView();
        }

        showData(false);
        if (!isUploaded){
            updateResult();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        Log.e("RESULT","onActivityResult");
        UMSsoHandler ssoHandler = mShareService.getConfig().getSsoHandler(requestCode);
        if (null != ssoHandler){
            ssoHandler.authorizeCallBack(requestCode,resultCode,data);
        }
        if (resultCode == -1) {//返回RESULT_OK
            updateResult();
        }
    }



    private void showData(boolean isUploading) {
        if (!isUploading) {
            mScore.setText("" + score);
            if (mApplication.isLogined() && null != mApplication.user.getHeadImg()){
                mLoader.displayImage(mApplication.user.getHeadImg(),mCover,options);
                mRank.setText(mApplication.user.getScoreRank()+"");
            }
        } else {
            if (null != user_p) {
                showUserInfo(user_p, mRank_pre, mScore_pre, mCover_pre);
                user_pre.setVisibility(View.VISIBLE);
            }
            if (null != user_n) {
                showUserInfo(user_n, mRank_next, mScore_next, mCover_next);
                user_next.setVisibility(View.VISIBLE);
            }
            showUserInfo(mApplication.user, mRank, mScore, mCover);
            user_mine.setVisibility(View.VISIBLE);
        }
    }

    private void showUserInfo(MCUser user, AppCompatTextView rank, AppCompatTextView score, ImageView cover) {
        rank.setText((user.getScoreRank() ) +"");
        score.setText(user.getAllScore() + "");
        if (null != user.getHeadImg() && 10 < user.getHeadImg().length()) {
            mLoader.displayImage(user.getHeadImg(), cover, options);
        }
    }

    private void updateResult() {
        if (!isLoading && !isUploaded && mApplication.isLogined()) {
            MCUser user = mApplication.user;
            mApplication.user.setAllScore(user.getAllScore() + score);
            mApplication.user.setAnswerNum(user.getAnswerNum() + 1);
            NetInterface.uploadResult(getActivity(), mApplication.user.getId(), score, rightQuestionId, wrongQuestionId, this);
            isLoading = true;
        }
    }

    private void initView() {
        mScore_pre = (AppCompatTextView) view.findViewById(R.id.score_pre);
        mScore_next = (AppCompatTextView) view.findViewById(R.id.score_next);
        mScore = (AppCompatTextView) view.findViewById(R.id.score_mine);
        mRank_pre = (AppCompatTextView) view.findViewById(R.id.ranking_pre);
        mRank_next = (AppCompatTextView) view.findViewById(R.id.ranking_next);
        mRank = (AppCompatTextView) view.findViewById(R.id.ranking_mine);
        mCover_pre = (ImageView) view.findViewById(R.id.cover_pre);
        mCover_next = (ImageView) view.findViewById(R.id.cover_next);
        mCover = (ImageView) view.findViewById(R.id.cover_mine);
        mReExam = (AppCompatTextView) view.findViewById(R.id.reexam);
        mShare = (AppCompatTextView) view.findViewById(R.id.sharescore);
        user_pre = (RelativeLayout) view.findViewById(R.id.user_pre);
        user_next = (RelativeLayout) view.findViewById(R.id.user_next);
        user_mine = (RelativeLayout) view.findViewById(R.id.layout_ranking_mine);
        mCover.setOnClickListener(this);
        mReExam.setOnClickListener(this);
        mShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cover_mine:
                if (!isLoading && mApplication.isLogined()) {
                    if (!isLoading && !isUploaded) {
                        MobclickAgent.onEvent(getActivity(),"uploadSC_M");
                        updateResult();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.reexam:
                MobclickAgent.onEvent(getActivity(),"click_RE");
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
            case R.id.sharescore:
                MobclickAgent.onEvent(getActivity(), "share_E");

                View v1 = this.view.findViewById(R.id.layout_result);
                v1.setDrawingCacheEnabled(true);
                v1.buildDrawingCache();
                Bitmap bitmap = v1.getDrawingCache();
                shareScore(bitmap);
                break;
        }

    }

    @Override
    public void onSuccess(@NonNull MCUser myself, @Nullable MCUser user_pre, @Nullable MCUser user_next) {
        MobclickAgent.onEvent(getActivity(),"uploadSC_S");
        isLoading = false;
        isUploaded = true;
        mApplication.user = myself;
        this.user_p = user_pre;
        this.user_n = user_next;
        showData(true);
    }

    @Override
    public void onFalse(String msg) {
        MobclickAgent.onEvent(getActivity(),"uploadSC_F");
        isLoading = false;
        feedback_false();
    }

    private void shareScore(Bitmap bitmap){
//        configPlatforms();
        if (isUploaded){
            mShareService.setShareContent(getString(R.string.share_title_rank));
            mShareService.setShareContent(getString(R.string.share_content_rank, mApplication.user.getScoreRank()));
        } else {
            mShareService.setShareContent(getString(R.string.share_title_score));
            mShareService.setShareContent(getString(R.string.share_content_score, score));
        }
        if (null != bitmap){
            UMImage image = new UMImage(getActivity(),bitmap);
            mShareService.setShareImage(image);
        }
        mShareService.openShare(getActivity(),false);
    }

    private void configPlatforms()
    {
/*        String targetUrl = "http://www.mckuai.com/thread-" + post.getId() + ".html";
        String title = "麦块for我的世界盒子";
        String context = post.getTalkTitle();
        UMImage image;
        if (null != post.getMobilePic() && 10 < post.getMobilePic().length())
        {
            image = new UMImage(this, post.getMobilePic());
        } else
        {
            image = new UMImage(this, R.drawable.icon_share_default);
        }
//         添加内容和图片
        mShareService.setShareContent(context);
        mShareService.setShareMedia(image);*/

        String appID_QQ = "101155101";
        String appAppKey_QQ = "78b7e42e255512d6492dfd135037c91c";
        // 添加qq
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appID_QQ, appAppKey_QQ);
//        qqSsoHandler.setTargetUrl(targetUrl);
//        qqSsoHandler.setTitle(title);
        qqSsoHandler.addToSocialSDK();
        // 添加QQ空间参数
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), appID_QQ, appAppKey_QQ);
//        qZoneSsoHandler.setTargetUrl(targetUrl);
        qZoneSsoHandler.addToSocialSDK();

        String appIDWX = "wx49ba2c7147d2368d";
        String appSecretWX = "85aa75ddb9b37d47698f24417a373134";
        // 添加微信
        UMWXHandler wxHandler = new UMWXHandler(getActivity(), appIDWX, appSecretWX);
//        wxHandler.setTargetUrl(targetUrl);
//        wxHandler.setTitle(title);
        wxHandler.showCompressToast(false);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appIDWX, appSecretWX);
//        wxCircleHandler.setTitle(title);
//        wxCircleHandler.setTargetUrl(targetUrl);
        wxCircleHandler.setToCircle(true);
        wxHandler.showCompressToast(false);
        wxCircleHandler.addToSocialSDK();
        // 移除多余平台
        mShareService.getConfig().removePlatform(SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);
    }
}
