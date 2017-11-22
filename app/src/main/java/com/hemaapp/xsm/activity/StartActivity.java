package com.hemaapp.xsm.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetWorker;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.SysInitInfo;
import com.hemaapp.xsm.model.User;

import java.net.MalformedURLException;
import java.net.URL;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/13.
 * 启动页
 */
public class StartActivity extends JhActivity implements AMapLocationListener{

    private ImageView imageView;
    private SysInitInfo sysInitInfo;
    private User user;
    private String fromNotification;

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_start);
        super.onCreate(savedInstanceState);
        sysInitInfo = getApplicationContext().getSysInitInfo();
        user = getApplicationContext().getUser();
        init();
        if (locationPermissionEnable()) {
            startLocation();
        }
    }
    private boolean locationPermissionEnable() {
        if ((ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {//判断是否拥有定位权限
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意授权
                startLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onDestroy() {

        stopLocation();
        super.onDestroy();
    }
    private void startLocation() {
        if (locationClient == null) {
            locationClient = new AMapLocationClient(mContext);
            locationOption = new AMapLocationClientOption();
            //设置定位监听
            locationClient.setLocationListener(this);
            //设置为高精度定位模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationOption.setOnceLocation(true);
            //设置定位参数
            locationClient.setLocationOption(locationOption);
            locationClient.startLocation();
        }
    }

    private void stopLocation() {
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    private void init() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo);
        animation.setAnimationListener(new StartAnimationListener());
        imageView.startAnimation(animation);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        // TODO Auto-generated method stub
        JhHttpInformation information = (JhHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                HemaArrayResult<SysInitInfo> sResult = (HemaArrayResult<SysInitInfo>) baseResult;
                sysInitInfo = sResult.getObjects().get(0);
                log_i("KKKKKKKKKKKKKKKKKKKKKKKKKK");
                //setStartImage();
                getApplicationContext().setSysInitInfo(sysInitInfo);
                String adsshow = XtomSharedPreferencesUtil.get(mContext, "adsshow");
                if (isNull(adsshow)) {
                    finish();
                    toAds();
                }
                else {
                    checkLogin();
                }
                // toAds();
                break;
            case CLIENT_LOGIN:
                HemaArrayResult<User> uResult = (HemaArrayResult<User>) baseResult;
                user = uResult.getObjects().get(0);
                getApplicationContext().setUser(user);
                toMain();
                break;
            default:
                break;
        }
    }

    /**
     *
     * @方法名称: toAds
     * @功能描述: TODO跳转到广告页
     * @返回值: void
     */
    private void toAds() {
        Intent intent = new Intent(StartActivity.this, AdsActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @方法名称: setStartImage
     * @功能描述: 获取启动图片地址，并显示
     * @返回值: void
     */
    private void setStartImage() {
        String StartImage = null;
        if (sysInitInfo != null) {
            StartImage = sysInitInfo.getStart_img();
        }
        if (!isNull(StartImage)) {
            URL url;
            try {
                url = new URL(StartImage);
                imageWorker.loadImage(new ImageTask(imageView, url, mContext));
            } catch (MalformedURLException e) {

                imageView.setImageResource(R.mipmap.startimg);
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
                 stopLocation();
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) { //定位成功
            Double lat = aMapLocation.getLatitude();
            Double lng = aMapLocation.getLongitude();
            JhctmApplication application = JhctmApplication.getInstance();
            log_i("定位成功：lng=" + lng.toString() + " lat=" + lat.toString());
            XtomSharedPreferencesUtil.save(application, "lat", lat.toString());
            XtomSharedPreferencesUtil.save(application, "lng", lng.toString());
            XtomSharedPreferencesUtil.save(application, "dizhi", aMapLocation.getAddress());
            XtomSharedPreferencesUtil.save(application, "city_name", aMapLocation.getCity());
            XtomSharedPreferencesUtil.save(application, "city", aMapLocation.getCity());
            XtomSharedPreferencesUtil.save(application, "district", aMapLocation.getProvince()+aMapLocation.getCity());
            XtomSharedPreferencesUtil.save(application, "position", aMapLocation.getProvince()+","+aMapLocation.getCity()+","+aMapLocation.getDistrict());
        } else {
            startLocation();
        }
    }

    private class ImageTask extends XtomImageTask {

        public ImageTask(ImageView imageView, URL url, Object context) {
            super(imageView, url, context);
        }

        @Override
        public void beforeload() {
            imageView.setImageResource(R.mipmap.startimg);
        }

        @Override
        public void failed() {
            log_w("Get image " + path + " failed!!!");
            imageView.setImageResource(R.mipmap.startimg);
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        // TODO Auto-generated method stub
        JhHttpInformation information = (JhHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                getInitFailed();
                break;
            case CLIENT_LOGIN:
                int key = baseResult.getError_code();
//                if (key==102 || key==104 || key==106) {
//                    JhctmApplication.getInstance().setUser(null);
//                    XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
//                    XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
//                }
                toLogin();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub
        JhHttpInformation information = (JhHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                getInitFailed();
                break;
            case CLIENT_LOGIN:
                JhctmApplication.getInstance().setUser(null);
//                XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
//                XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
                toLogin();
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        // TODO Auto-generated method stub
        imageView = (ImageView) findViewById(R.id.imageview);
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub
        fromNotification = mIntent.getStringExtra("fromNotification");
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }

    /**
     *
     * @方法名称: getInitFailed
     * @功能描述: 初始化失败提示
     * @返回值: void
     */
    private void getInitFailed() {
        if (sysInitInfo != null) {
            checkLogin();
        } else {
            showTextDialog("获取系统初始化信息失败啦\n请检查网络连接重试");
        }
    }

    private void checkLogin() {
        if (isAutoLogin()) {
            String username = XtomSharedPreferencesUtil.get(this, "username");
            String password = XtomSharedPreferencesUtil.get(this, "password");
            if (!isNull(username) && !isNull(password)) {
                JhNetWorker netWorker = getNetWorker();
                netWorker.clientLogin(username, password);
            } else {
                toLogin();
            }
        } else {
            toLogin();
        }

    }

    /**
     *
     * @方法名称: isAutoLogin
     * @功能描述: 检测是否是自动登录
     * @return
     * @返回值: boolean
     */
    private boolean isAutoLogin() {
        String autologin = XtomSharedPreferencesUtil.get(mContext, "autoLogin");
        boolean no = "no".equals(autologin);
        return !no;
    }

    /**
     *
     * @方法名称: toLogin
     * @功能描述: 跳转到登录界面
     * @返回值: void
     */
    private void toLogin() {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     *
     * @方法名称: toMain
     * @功能描述: 跳转到主界面
     * @返回值: void
     */
    private void toMain() {
      Intent intent = new Intent(StartActivity.this, MainActivity.class);
        //Intent intent = new Intent(StartActivity.this, UpLoadActivity.class);
        intent.putExtra("fromNotification", fromNotification);
        log_i("启动页的信息是什么-----------"+fromNotification);
        finish();
        startActivity(intent);
    }

    private class StartAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            JhNetWorker netWorker = getNetWorker();
            netWorker.inIt();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }
    }


}
