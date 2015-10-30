package com.tars.mcwa.activity;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.tars.mcwa.R;
import com.tars.mcwa.bean.MCUser;
import com.tars.mcwa.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

/**
 * Created by kyly on 2015/9/29.
 */
public class MCStar extends Application {
    static final String TAG = "application";
    static MCStar instance;
    public static MCUser user;
    public static Gson gson;
    public long mWXToken_Birthday;
    public long mWXToken_Expires;
    public String mWXToken;
    public boolean isFirstBoot = true;


    private final int IMAGE_POOL_SIZE = 3;// 线程池数量
    private final int CONNECT_TIME = 15 * 1000;// 连接时间
    private final int TIME_OUT = 30 * 1000;// 超时时间

    private String mCacheDir;
    private MediaPlayer mPlayer;
    private DisplayImageOptions mCircleOption;
    private DisplayImageOptions mNormalOption;


    public static MCStar getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void init() {
        gson = new Gson();
        initImageLoader();
        readPreference();
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
    }

    public void setUser(MCUser user) {
        this.user.clone(user);
    }

    public boolean LogOut() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_filename), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(getString(R.string.preferences_tokenexpires), 0);
        editor.apply();
        editor.commit();
        user = null;
        return true;
    }

    public boolean isLogined() {
        return null != user;
    }


    public MCUser readPreference() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_filename), 0);
        isFirstBoot = preferences.getBoolean(getString(R.string.preferences_isFirstBoot), true);
        if (!isFirstBoot) {
            mWXToken_Birthday = preferences.getLong(getString(R.string.preferences_tokentime), 0);
            mWXToken_Expires = preferences.getLong(getString(R.string.preferences_tokenexpires), 0);
            // 检查qq的token是否有效如果在有效期内则获取qqtoken
            if (verificationTokenLife(mWXToken_Birthday, mWXToken_Expires)) {
                user = new MCUser();
                mWXToken = preferences.getString(getString(R.string.preferences_accesstoken_wx),null);
                user.setUserName(preferences.getString(getString(R.string.preferences_username), null)); //姓名,实为wx的access_token
                user.setNickName(preferences.getString(getString(R.string.preferences_nickname), null));// 显示名
                user.setHeadImg(preferences.getString(getString(R.string.preferences_cover), null));// 用户头像
                user.setSex(preferences.getString(getString(R.string.preferences_six), null));// 性别
                user.setId(preferences.getInt(getString(R.string.preferences_id), 0));                    //id
                user.setLevel(preferences.getInt(getString(R.string.preferences_level), 0));           //level
                user.setAllScore(preferences.getLong(getString(R.string.preferences_score), 0));     //分数
                user.setRanking(preferences.getLong(getString(R.string.preferences_ranking), 0));
                user.setScoreRank(preferences.getInt(getString(R.string.preferences_scorerank), 0));
                user.setAnswerNum(preferences.getInt(getString(R.string.preferences_answercount), 0));  //ac
                user.setUploadNum(preferences.getInt(getString(R.string.preferences_uploadcount), 0));
            }
        }
        return user;
    }

    public void saveProfile() {
        if (null != user) {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_filename), 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.preferences_isFirstBoot),false);
            editor.putLong(getString(R.string.preferences_tokentime), mWXToken_Birthday);
            editor.putLong(getString(R.string.preferences_tokenexpires), mWXToken_Expires);
            editor.putInt(getString(R.string.preferences_scorerank), user.getScoreRank());
            editor.putLong(getString(R.string.preferences_ranking), user.getRanking());
            editor.putInt(getString(R.string.preferences_id), user.getId());         //id
            editor.putInt(getString(R.string.preferences_level), user.getLevel());       //level
            editor.putLong(getString(R.string.preferences_score), user.getAllScore()); //分数
            editor.putInt(getString(R.string.preferences_answercount), user.getAnswerNum());//ac
            editor.putInt(getString(R.string.preferences_uploadcount), user.getUploadNum());        //uc
            editor.putString(getString(R.string.preferences_username), user.getUserName() + "");
            editor.putString(getString(R.string.preferences_cover), user.getHeadImg() + "");
            editor.putString(getString(R.string.preferences_nickname), user.getNickName() + "");
            editor.putString(getString(R.string.preferences_six), user.getSex() + "");
            editor.commit();
        }
    }

    public void playMusic() {
        if (null == mPlayer) {
            mPlayer = MediaPlayer.create(this, R.raw.background);
        }
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayer.start();
            }
        });
    }

    public void stopMusic() {
        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    public boolean switchPlayPauseMusic() {
        if (null != mPlayer) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                return false;
            } else {
                mPlayer.start();
                return true;
            }
        }
        return false;
    }

    public void pauseMusic() {
        if (null != mPlayer) {
            mPlayer.pause();
        }
    }

    public DisplayImageOptions getCircleOption() {
        if (null == mCircleOption) {
            mCircleOption = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
        }
        return mCircleOption;
    }

    public DisplayImageOptions getNormalOption() {
        if (null == mNormalOption) {
            mNormalOption = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        }
        return mNormalOption;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e(TAG, "onTerminate");
    }

    public String getImageCacheDir() {
        return getCacheRoot() + File.separator + getString(R.string.imagecache_dir) + File.separator;
    }

    private String getCacheRoot() {
        if (null == mCacheDir) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                if (null != getExternalCacheDir()) {
                    mCacheDir = getExternalCacheDir().getPath();
                } else {
                    mCacheDir = getCacheDir().getPath();
                }
            } else {
                mCacheDir = getCacheDir().getPath();
            }
        }
        return mCacheDir;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(750, 480)
                .threadPoolSize(IMAGE_POOL_SIZE)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                        // 对于同一url只缓存一个图
                        //.memoryCache(new UsingFreqLimitedMemoryCache(MEM_CACHE_SIZE)).memoryCacheSize(MEM_CACHE_SIZE)
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO)
                .discCache(new UnlimitedDiskCache(new File(getImageCacheDir())))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), CONNECT_TIME, TIME_OUT))
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(configuration);
    }

    private boolean verificationTokenLife(Long birthday, long expires) {
        return (System.currentTimeMillis() - birthday) < expires * 1000;
    }
}
