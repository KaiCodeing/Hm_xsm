package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.AdsAdapter;
import com.hemaapp.xsm.model.User;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/19.
 * 引导页
 */
public class AdsActivity extends JhActivity {


    private ViewPager viewpager;// 换页
    private TextView tomain;// 进入首页
    private RadioGroup rg;//
    private AdsAdapter adapter;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_ads);
        super.onCreate(savedInstanceState);
        XtomSharedPreferencesUtil.save(mContext, "adsshow", "1");

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

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void findView() {
        // TODO Auto-generated method stub
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tomain = (TextView) findViewById(R.id.tomain);
        rg = (RadioGroup) findViewById(R.id.rg);
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

        tomain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                toLogin();
            }
        });
        tomain.setVisibility(View.GONE);
        user = getApplicationContext().getUser();
        adapter = new AdsAdapter(AdsActivity.this, rg, viewpager,tomain);
        viewpager.setAdapter(adapter);
    }
    /**
     *
     * @方法名称: toLogin
     * @功能描述: 跳转到登录界面
     * @返回值: void
     */
    private void toLogin() {
        Intent intent = new Intent(AdsActivity.this, LoginActivity.class);
        intent.putExtra("type","type");
        startActivity(intent);
        finish();
    }
    private void toMain() {
        Intent intent = new Intent(AdsActivity.this, MainActivity.class);
//        intent.putExtra("fromNotification", fromNotification);
//        log_i("启动页的信息是什么-----------"+fromNotification);
        startActivity(intent);
        finish();

    }
}
