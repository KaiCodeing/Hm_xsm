package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.config.XsmConfig;
import com.hemaapp.xsm.model.User;
import com.hemaapp.xsm.view.JhctmImageWay;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/3/2.
 * 注册填写信息
 */
public class UserInformationActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private RoundedImageView user_img;
    private EditText user_name;
    private EditText user_tel;
    private EditText user_position;
    private EditText user_company;
    private TextView login_text;
    private TextView man_text;
    private TextView woman_text;
    private RadioGroup check_sex;
    private String username;
    private String tempToken;
    private String password;
    private String imagePathCamera;
    private String tempPath = "";
    private JhctmImageWay imageWay;
    private String keytype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_information);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            imageWay = new JhctmImageWay(mContext, 1, 2);
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new JhctmImageWay(mContext, 1, 2);
        }
        if ("1".equals(keytype)) {
            String token = JhctmApplication.getInstance().getUser().getToken();
            String userid = JhctmApplication.getInstance().getUser().getId();
            getNetWorker().clientGet(token, userid);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;

        }
        switch (requestCode) {
            case 1:
                album(data);
                break;
            case 2:
                camera();
                break;
            case 3:
                user_img.setCornerRadius(100);
                imageWorker.loadImage(new XtomImageTask(user_img, tempPath,
                        mContext, new XtomImageTask.Size(180, 180)));
                break;

            default:
                break;
        }
    }

    private void album(Intent data) {
        if (data == null)
            return;
        Uri selectedImageUri = data.getData();
        startPhotoZoom(selectedImageUri, 3);
    }

    private void editImage(String path, int requestCode) {
        File file = new File(path);
        startPhotoZoom(Uri.fromFile(file), requestCode);
    }

    private void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", XsmConfig.IMAGE_WIDTH);
        intent.putExtra("aspectY", XsmConfig.IMAGE_WIDTH);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", XsmConfig.IMAGE_WIDTH);
        intent.putExtra("outputY", XsmConfig.IMAGE_WIDTH);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        String savedir = XtomFileUtil.getTempFileDir(mContext);
        File dir = new File(savedir);
        if (!dir.exists())
            dir.mkdirs();
        // 保存入sdCard
        tempPath = savedir + XtomBaseUtil.getFileName() + ".jpg";// 保存路径
        File file = new File(tempPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return file;
    }

    private void camera() {
//		imagePathCamera = null;
//		if (imagePathCamera == null) {
//			imagePathCamera = imageWay.getCameraImage();
//		}
        String path = imageWay.getCameraImage();
        if (!isNull(path))
            imagePathCamera = path;
        log_i("imagePathCamera=" + imagePathCamera);
        editImage(imagePathCamera, 3);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageWay != null)
            outState.putString("imagePathCamera", imageWay.getCameraImage());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
                showProgressDialog("保存注册信息");
                break;
            case FILE_UPLOAD:
                showProgressDialog("保存图片");
                break;
            case CLIENT_LOGIN:
                showProgressDialog("登录");
                break;
            case CLIENT_GET:
                showProgressDialog("获取用户信息");
                break;
            case CLIENT_SAVE:
                showProgressDialog("保存用户信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGIN:
            case CLIENT_GET:
            case CLIENT_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String token = result.getObjects().get(0);
                if (isNull(tempPath) || tempPath.equals("")) {
                    getNetWorker().clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(password)));
                } else {
                    getNetWorker().fileUpload(token, "1", "0", "0", "0", "无", tempPath);
                }
                break;
            case FILE_UPLOAD:
                if ("1".equals(keytype)) {
                    showTextDialog("修改用户信息成功!");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else
                    getNetWorker().clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(password)));
                break;
            case CLIENT_LOGIN:
                HemaArrayResult<User> result2 = (HemaArrayResult<User>) hemaBaseResult;
                User user = result2.getObjects().get(0);
                JhctmApplication.getInstance().setUser(user);
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password)));

                XtomActivityManager.finishAll();
                Intent it1 = new Intent(mContext, MainActivity.class);
                startActivity(it1);
                break;
            case CLIENT_GET:
                HemaArrayResult<User> result1 = (HemaArrayResult<User>) hemaBaseResult;
                User user1 = result1.getObjects().get(0);
                setData(user1);
                break;
            case CLIENT_SAVE:
                if (isNull(tempPath) || tempPath.equals("")) {
                    showTextDialog("修改用户信息成功!");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else {
                    String token1 = JhctmApplication.getInstance().getUser().getToken();
                    getNetWorker().fileUpload(token1, "1", "0", "0", "0", "无", tempPath);
                }
                break;
        }
    }

    /**
     * 填充数据
     */
    private void setData(User user) {
        String path = user.getAvatarbig();
        user_img.setCornerRadius(100);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.my_fr_user_img)
                .showImageForEmptyUri(R.mipmap.my_fr_user_img)
                .showImageOnFail(R.mipmap.my_fr_user_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, user_img, options);
        user_name.setText(user.getNickname());
        if ("男".equals(user.getSex())) {
            check_sex.check(R.id.man_text);
        } else
            check_sex.check(R.id.woman_text);
        user_tel.setText(user.getMobile());
        user_position.setText(user.getSelfsign());
        user_company.setText(user.getCompany());
        if (!"1".equals(user.getFeeaccount()))
            user_company.setEnabled(false);
        else
            user_company.setEnabled(true);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
            case FILE_UPLOAD:
            case CLIENT_LOGIN:
            case CLIENT_GET:
            case CLIENT_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_ADD:
                showTextDialog("保存注册信息失败，请稍后重试");
                break;
            case FILE_UPLOAD:
                showTextDialog("上传图片失败，请稍后重试");
                break;
            case CLIENT_LOGIN:
                showTextDialog("登录失败，请稍后重试");
                break;
            case CLIENT_GET:
                showTextDialog("获取用户详情失败，请稍后重试");
                break;
            case CLIENT_SAVE:
                showTextDialog("保存用户信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        user_img = (RoundedImageView) findViewById(R.id.user_img);
        user_name = (EditText) findViewById(R.id.user_name);
        user_tel = (EditText) findViewById(R.id.user_tel);
        user_position = (EditText) findViewById(R.id.user_position);
        user_company = (EditText) findViewById(R.id.user_company);
        login_text = (TextView) findViewById(R.id.login_text);
        man_text = (TextView) findViewById(R.id.man_text);
        woman_text = (TextView) findViewById(R.id.woman_text);
        check_sex = (RadioGroup) findViewById(R.id.check_sex);

    }

    @Override
    protected void getExras() {
        username = mIntent.getStringExtra("username");
        tempToken = mIntent.getStringExtra("tempToken");
        password = mIntent.getStringExtra("password");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if ("1".equals(keytype)) {
            title_text.setText("编辑个人信息");
            login_text.setText("保存");
        } else {
            title_text.setText("注册");
            login_text.setText("提交");
        }
        //提交
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = user_name.getText().toString().trim();
                String usertel = user_tel.getText().toString().trim();
                String position = user_position.getText().toString().trim();
                String company = user_company.getText().toString().trim();
                String sex = "男";
                if (check_sex.getCheckedRadioButtonId() == R.id.man_text) {
                    sex = "男";
                } else
                    sex = "女";
                if (isNull(nickname)) {
                    showTextDialog("请填写您的姓名");
                    return;
                }
                if (isNull(usertel)) {
                    showTextDialog("请填写您的电话");
                    return;
                }
//                if (isNull(position)) {
//                    showTextDialog("请填写您的职位");
//                    return;
//                }
//                if (isNull(company)) {
//                    showTextDialog("请填写您所在单位");
//                    return;
//                }
                if (isNull(tempPath) || tempPath.equals("")) {
                    showTextDialog("请选择头像");
                    return;
                }
                if ("1".equals(keytype)) {
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    getNetWorker().clientSave(token, nickname, sex, position, company, usertel, token);
                } else
                    getNetWorker().clientAdd(tempToken, username, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(password)), nickname, sex, position, company, usertel);
            }
        });
        //选择照片
        //照片
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                imageWay.show();
            }
        });
    }
}
