package com.hemaapp.xsm.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhFragmentActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhUpGrade;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.fragment.HomeFragment;
import com.hemaapp.xsm.fragment.MyFragment;
import com.hemaapp.xsm.fragment.ServiceFragment;
import com.hemaapp.xsm.model.SysInitInfo;
import com.hemaapp.xsm.model.User;
import com.hemaapp.xsm.push.PushUtils;

import java.util.List;

import xtom.frame.util.XtomToastUtil;

//主界面
public class MainActivity extends JhFragmentActivity {
    private FrameLayout content_frame;
    private RadioGroup radiogroup;
    private JhUpGrade upGrade;
    private RadioButton radiobutton0;
    private RadioButton radiobutton2;
    private long exitTime = 0;
    private TextView main_point;
    private long checkTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        toogleFragment(HomeFragment.class);
        // 个推相关 start
        startGeTuiPush();
        // 个推相关 end
//        upGrade = new JhUpGrade(this);

    }
    @Override
    protected void onResume() {
        super.onResume();
//        if (upGrade != null) {
//            upGrade.check();
//        }
        log_i("跳页----------------1.31");
        long currentTime = System.currentTimeMillis();
        boolean isCanCheck = checkTime == 0
                || currentTime - checkTime > 1000 * 60 * 60 * 24;
        if (isCanCheck) {
            SysInitInfo sysInitInfo = JhctmApplication.getInstance().getSysInitInfo();
            checkTime = System.currentTimeMillis();
            //   @SuppressWarnings("unchecked")
            //   HemaArrayResult<SysInitInfo> sResult = (HemaArrayResult<SysInitInfo>) baseResult;
            //    sysInitInfo = sResult.getObjects().get(0);
            String sysVersion = sysInitInfo.getAndroid_last_version();
            String version = HemaUtil.getAppVersionForSever(mContext);
            if (HemaUtil.isNeedUpDate(version, sysVersion)) {
                log_i("跳页----------------2");
                Intent intent = new Intent(mContext, UpLoadActivity.class);
                startActivity(intent);
            }

        }
    }

    @Override
    protected void findView() {
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);

        main_point = (TextView) findViewById(R.id.main_point);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        radiogroup.setOnCheckedChangeListener(new OnTabListener());
    }
    private class OnTabListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radiobutton0:// 策略库
                    toogleFragment(HomeFragment.class);
                    break;
                case R.id.radiobutton1:// 名师榜
                    toogleFragment(ServiceFragment.class);
                    break;
                case R.id.radiobutton2:// 朋友圈

                    toogleFragment(MyFragment.class);
                    break;
            }
        }
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case DEVICE_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }
    /**
     * 显示或更换Fragment
     *
     * @param c
     */
    public void toogleFragment(Class<? extends Fragment> c) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = c.getName();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);

        if (fragment == null) {
            try {
                fragment = c.newInstance();
                log_i("第一次+++++++++++++++++++++");
                // 替换时保留Fragment,以便复用
                transaction.add(R.id.content_frame, fragment, tag);
            } catch (Exception e) {
                // ignore
            }
        } else {
            // nothing
        }
        // 遍历存在的Fragment,隐藏其他Fragment
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null)
            for (Fragment fm : fragments)
                if (!fm.equals(fragment))
                    transaction.hide(fm);

        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                XtomToastUtil.showShortToast(mContext, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                // moveTaskToBack(false);
                finish();
                NotificationManager nm = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancelAll();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /* 推送相关 */
    private PushReceiver pushReceiver;

    private void startPush() {
        User user = getApplicationContext().getUser();
        if (user == null) {
            log_i("未登录，无需启动推送服务");
            return;
        }
        if (!PushUtils.hasBind(getApplicationContext())) {
            PushManager.startWork(getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    PushUtils.getMetaValue(this, "api_key"));
            // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
            // PushManager.enableLbs(getApplicationContext());
        } else {
            saveDevice();
        }
        registerPushReceiver();
    }

    private void stopPush() {
        PushManager.stopWork(getApplicationContext());
        PushUtils.setBind(mContext, false);
        unregisterPushReceiver();
    }

    private void startGeTuiPush() {
        com.igexin.sdk.PushManager.getInstance().initialize(mContext);
        registerPushReceiver();
    }

    private void stopGeTuiPush() {
        unregisterPushReceiver();
    }

    private void registerPushReceiver() {
        if (pushReceiver == null) {
            pushReceiver = new PushReceiver();
            IntentFilter mFilter = new IntentFilter("com.hemaapp.push.connect");
            mFilter.addAction("com.hemaapp.push.msg");
            mFilter.addAction("com.hemaapp.car.number");
            registerReceiver(pushReceiver, mFilter);
        }
    }

    private void unregisterPushReceiver() {
        if (pushReceiver != null)
            unregisterReceiver(pushReceiver);
    }

    private class PushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handleEvent(intent);
        }

        private void handleEvent(Intent intent) {
            String action = intent.getAction();

            if ("com.hemaapp.push.connect".equals(action)) {
                saveDevice();

            } else if ("com.hemaapp.push.msg".equals(action)) {
                boolean unread = PushUtils.getmsgreadflag(
                        getApplicationContext(), "2");
                if (unread) {
                    // showNoticePoint();
                    log_i("有未读推送");
                } else {
                    log_i("无未读推送");
                    // hideNoticePoint();
                }
            }
            else if("com.hemaapp.car.number".equals(action))
            {
//                //获取购物车商品数量
//                if (!isNull(XtomSharedPreferencesUtil.get(MainActivity.this,"username")))
//                {  String token = DtywApplication.getInstance().getUser().getToken();
//                    getNetWorker().getCartgoodsnum(token);
//                }
//                else
//                {
//                    main_point.setVisibility(View.INVISIBLE);
//                }
            }
        }
    }

    public void saveDevice() {
        User user = getApplicationContext().getUser();
        if (user == null) {
            return;
        }
        getNetWorker().deviceSave(user.getToken(),
                PushUtils.getUserId(mContext), "2",
                PushUtils.getChannelId(mContext));
    }

	/* 推送相关end */
}
