package com.tars.mcwa.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tars.mcwa.R;
import com.tars.mcwa.bean.Question;
import com.tars.mcwa.utils.NetInterface;
import com.umeng.analytics.MobclickAgent;

public class QuestionActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, NetInterface.OnUploadQuestionListener, NetInterface.OnUploadPicsListener, View.OnLongClickListener, View.OnFocusChangeListener {

    private RadioGroup type;
    private TextInputLayout topic;
    private TextInputLayout option_a;
    private TextInputLayout option_b;
    private TextInputLayout option_c;
    private TextInputLayout option_d;
    private ImageButton image;
    private AppCompatButton submit;
//    private AppCompatRadioButton choice;
//    private AppCompatRadioButton judge;
    private AppCompatTextView hint;
    private Question mQuestion = new Question();

    private static final int REQUEST_LOGIN = 1;
    private static final int REQUEST_GETPIC = 2;
    private static final int REQUEST_UPLOADPIC = 3;
    private static final int REQUEST_UPLOADQUESTION = 4;
    private static String pic;
    private Bitmap bitmap;
//    private ImageLoader mLoader = ImageLoader.getInstance();
    private Vibrator vibrator;//振动


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == type) {
            initView();
        }
    }

    @Override
    protected void onStop() {
        if (null != vibrator) {
            vibrator.cancel();
            vibrator = null;
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                if (requestCode == REQUEST_GETPIC){
                    getPic(data);
                } else {
                    uploadQuestion();
                }
                break;
            default:
                if (requestCode == REQUEST_GETPIC){
                    MobclickAgent.onEvent(this,"addQP_F");
                }
                break;
        }
        /*if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GETPIC:
                    getPic(data);
                    break;
                case REQUEST_LOGIN:
                    uploadQuestion();
                    break;
            }
        }*/
    }

    private void getPic(Intent data) {
        if (null != data) {
            // 取出所选图片的路径
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picuri = cursor.getString(columnIndex);
            cursor.close();


            // 将图片贴到控件上
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picuri, options);
            int width = options.outWidth;
            int height = options.outHeight;
            if (100 > width || 40 > height) {
                //图片分辨率太低
                Toast.makeText(this, getString(R.string.question_imagetoosmall), Toast.LENGTH_SHORT).show();
                MobclickAgent.onEvent(this,"addQP_F");
                return;
            }


            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picuri, opts);
            opts.inSampleSize = computeSampleSize(opts, -1, 700 * 272);
            opts.inJustDecodeBounds = false;
            try {
                bitmap = BitmapFactory.decodeFile(picuri, opts);
            } catch (OutOfMemoryError err) {
                // showNotification("图片过大!");
                MobclickAgent.onEvent(this,"addQP_F");
                Toast.makeText(this, getString(R.string.question_imagetoolarge), Toast.LENGTH_SHORT).show();
                return;
            }
            MobclickAgent.onEvent(this,"addQP_S");
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            image.setImageBitmap(bitmap);
            hint.setText(getString(R.string.question_removeimage_hint));
        }
    }

    private void initView() {
        type = (RadioGroup) findViewById(R.id.rg_questiontype);
        topic = (TextInputLayout) findViewById(R.id.questiontopic);
        option_a = (TextInputLayout) findViewById(R.id.option_a);
        option_b = (TextInputLayout) findViewById(R.id.option_b);
        option_c = (TextInputLayout) findViewById(R.id.option_c);
        option_d = (TextInputLayout) findViewById(R.id.option_d);
        submit = (AppCompatButton) findViewById(R.id.submit);
//        choice = (AppCompatRadioButton) findViewById(R.id.type_choice);
//        judge = (AppCompatRadioButton) findViewById(R.id.type_judgment);
        hint = (AppCompatTextView) findViewById(R.id.questionimage_hint);

        image = (ImageButton) findViewById(R.id.questionimage);
        type.setOnCheckedChangeListener(this);
        image.setOnClickListener(this);
        image.setOnLongClickListener(this);
        submit.setOnClickListener(this);
        topic.getEditText().setOnFocusChangeListener(this);
        option_a.getEditText().setOnFocusChangeListener(this);
        option_b.getEditText().setOnFocusChangeListener(this);
        option_c.getEditText().setOnFocusChangeListener(this);
        option_d.getEditText().setOnFocusChangeListener(this);

        topic.setHint(getString(R.string.question_titlehint));
        option_a.setHint(getString(R.string.result_right));
        option_b.setHint(getString(R.string.result_false));
        option_c.setHint(getString(R.string.result_false));
        option_d.setHint(getString(R.string.result_false));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.type_choice:
                option_b.setVisibility(View.VISIBLE);
                option_c.setVisibility(View.VISIBLE);
                option_d.setVisibility(View.VISIBLE);
                mQuestion.setQuestionType("choice");
                break;
            case R.id.type_judgment:
                option_b.setVisibility(View.GONE);
                option_c.setVisibility(View.GONE);
                option_d.setVisibility(View.GONE);
                option_b.getEditText().setText("");
                option_c.getEditText().setText("");
                option_d.getEditText().setText("");
                mQuestion.setQuestionType("judge");
                break;
        }
        topic.getEditText().requestFocus();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edt_topic:
                if (hasFocus) {
                    topic.setErrorEnabled(true);
                    topic.setError(getString(R.string.worldscount, 50));
                } else {
                    topic.setErrorEnabled(false);
                }
                break;
            case R.id.edt_option_a:
                if (hasFocus) {
                    option_a.setErrorEnabled(true);
                    if (mQuestion.getQuestionType().equals("judge")) {
                        option_a.setError(getString(R.string.correct_or_wrong));
                    } else {
                        option_a.setError(getString(R.string.worldscount, 10));
                    }
                } else {
                    option_a.setErrorEnabled(false);
                }
                break;
            case R.id.edt_option_b:
                if (hasFocus) {
                    option_b.setErrorEnabled(true);
                    if (mQuestion.getQuestionType().equals("judge")) {
                        option_b.setError(getString(R.string.correct_or_wrong));
                    } else {
                        option_b.setError(getString(R.string.worldscount, 10));
                    }
                } else {
                    option_b.setErrorEnabled(false);
                }
                break;
            case R.id.edt_option_c:
                if (hasFocus) {
                    option_c.setErrorEnabled(true);
                    option_c.setError(getString(R.string.worldscount, 10));
                } else {
                    option_c.setErrorEnabled(false);
                }
                break;
            case R.id.edt_option_d:
                if (hasFocus) {
                    option_d.setErrorEnabled(true);
                    option_d.setError(getString(R.string.worldscount, 10));
                } else {
                    option_d.setErrorEnabled(false);
                }
                break;
        }
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText(getString(R.string.title_makequestion));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void uploadQuestion() {
        if (mApplication.isLogined()) {
            if (null != bitmap && null == pic) {
                MobclickAgent.onEvent(this,"uploadQP");
                NetInterface.uploadPic(this, mApplication.user, bitmap, this);
                return;
                //有图片但未上传图片
            }
            if (mQuestion.getQuestionType().equals("choice")){
                MobclickAgent.onEvent(this,"uploadChoice");
            } else {
                MobclickAgent.onEvent(this,"uploadJudge");
            }
            NetInterface.uploadQuestion(this, mApplication.user, mQuestion, pic, this);
        } else {
            callLogin(REQUEST_LOGIN);
        }
    }

    private void getQuestion() {
        mQuestion.setTitle(topic.getEditText().getText().toString());
        mQuestion.setAnswerOne(option_a.getEditText().getText().toString());
        if (mQuestion.getQuestionType().equals("choice")) {
            mQuestion.setAnswerTwo(option_b.getEditText().getText().toString());
            mQuestion.setAnswerThree(option_c.getEditText().getText().toString());
            mQuestion.setAnswerFour(option_d.getEditText().getText().toString());
        } else if (mQuestion.getAnswerOne().equals(getString(R.string.correct))){
            mQuestion.setAnswerTwo(getString(R.string.wrong));
        } else if (mQuestion.getAnswerOne().equals(getString(R.string.wrong))){
            mQuestion.setAnswerTwo(getString(R.string.correct));
        }
    }

    private boolean checkParams() {
        boolean result = false;
        if (null != mQuestion.getTitle() && 5 < mQuestion.getTitle().length() && 51 > mQuestion.getTitle().length()) {
            if (null != mQuestion.getAnswerOne() && 0 < mQuestion.getAnswerOne().length() && 11 > mQuestion.getAnswerOne().length()) {
                if (mQuestion.getQuestionType().equals("judge")) {
                    if (mQuestion.getAnswerOne().equals(getString(R.string.wrong)) || mQuestion.getAnswerOne().equals(getString(R.string.correct))) {
                        return true;
                    } else {
                        option_a.getEditText().requestFocus();
                        return false;
                    }
                } else {
                    if (null != mQuestion.getAnswerTwo() && 0 < mQuestion.getAnswerTwo().length() && 11 > mQuestion.getAnswerTwo().length()) {
                        if (null != mQuestion.getAnswerThree() && 0 < mQuestion.getAnswerThree().length() && 11 > mQuestion.getAnswerThree().length()) {
                            if (null != mQuestion.getAnswerFour() && 0 < mQuestion.getAnswerFour().length() && 11 > mQuestion.getAnswerFour().length()) {
                                result = true;
                            } else {
                                //D
                                option_d.getEditText().requestFocus();
                            }
                        } else {
                            //C
                            option_c.getEditText().requestFocus();
                        }
                    } else {
                        //B
                        option_b.getEditText().requestFocus();
                    }
                }
            } else {
                //答案A长度不正确
                option_a.getEditText().requestFocus();
            }

        } else {
            //标题长度不正确
            topic.getEditText().requestFocus();
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                getQuestion();
                if (checkParams()) {
                    uploadQuestion();
                } else {
                    Log.e("UQ", "参数不正确");
                    feedback_false();
                }
                break;
            case R.id.questionimage:
                MobclickAgent.onEvent(this,"clickQP");
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GETPIC);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.questionimage:
                if (null != bitmap) {
                    PopupMenu popupMenu = new PopupMenu(this, image);
                    popupMenu.getMenuInflater().inflate(R.menu.popupmenu_deleteimage, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.deleteimage:
                                    bitmap.recycle();
                                    bitmap = null;
                                    image.setScaleType(ImageView.ScaleType.CENTER);
                                    image.setImageDrawable(getResources().getDrawable(R.mipmap.img_questionimage));
                                    hint.setText(getString(R.string.question_addimage_hint));
                                    return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }

                break;
        }
        return false;
    }

    @Override
    public void onSuccess() {
        MobclickAgent.onEvent(this,"upload_S");
        mApplication.user.setUploadNum(mApplication.user.getUploadNum() + 1);
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onSuccess(String url) {
        MobclickAgent.onEvent(this,"uploadQP_S");
        pic = url;
        uploadQuestion();
    }

    @Override
    public void onFalse(int requestCode, String msg) {
        feedback_false();
        switch (requestCode) {
            case REQUEST_UPLOADPIC:
                MobclickAgent.onEvent(this,"uploadQP_F");
                break;
            case REQUEST_UPLOADQUESTION:
                MobclickAgent.onEvent(this,"upload_F");
                break;
        }
        Log.e("UQ", msg);
    }

    // 加载大图时,计算缩放比例,以免出现OOM
    public static int computeSampleSize(BitmapFactory.Options options, int width, int height) {
        int initialSize = computeInitialSampleSize(options, width, height);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int w = options.outWidth;
        int h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
