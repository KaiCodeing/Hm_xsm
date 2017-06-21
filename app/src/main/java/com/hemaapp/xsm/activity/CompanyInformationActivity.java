package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhUtil;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.GoodAdAdapter;
import com.hemaapp.xsm.model.Union;
import com.hemaapp.xsm.view.JhViewPager;

/**
 * Created by lenovo on 2017/3/10.
 * 公司简介
 */
public class CompanyInformationActivity extends JhActivity {
    private ImageButton back_button;
    private ImageButton add_button;//收藏
    private JhViewPager adviewpager;
    private RadioGroup radiogroup;
    private TextView company_name;//公司名称
    private TextView issue_tel;//联系电话
    private TextView issue_user;//发布人
    private TextView issue_time;//公司位置
    private HemaWebView webview;
    private TextView go_tel;//一键拨打
    private TextView go_loaction;//查看地图
    private String unionId;
    private Union union;
    private FrameLayout vp_top;
    private GoodAdAdapter adAdapter;
    private TextView title_text;
    private boolean frist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_infor);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().unionGet(token, unionId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case UNION_GET:
                showProgressDialog("获取城市详情");
                break;
            case COLLECT_SAVE:
                showProgressDialog("保存收藏操作");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case UNION_GET:
            case COLLECT_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case UNION_GET:
                HemaArrayResult<Union> result = (HemaArrayResult<Union>) hemaBaseResult;
                union = result.getObjects().get(0);
                if ("1".equals(union.getType()))
                    setData();
                else {
                    showTextDialog("即将加入的公司暂无详情\n敬请期待");
                    back_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                }
                break;
            case COLLECT_SAVE:
                String key = hemaNetTask.getParams().get("flag");
                if ("1".equals(key)) {
                    union.setIscollect("1");
                    add_button.setImageResource(R.mipmap.coolect_on_img);
                } else {
                    union.setIscollect("2");
                    add_button.setImageResource(R.mipmap.collect_case_img);
                }
                frist = true;
                break;
        }
    }

    /**
     * 填充数据
     */
    private void setData() {
        double width = JhUtil.getScreenWidth(this);
        double height = width / 1 * 1;
        ViewGroup.LayoutParams params1 = adviewpager.getLayoutParams();
        params1.width = (int) width;
        params1.height = (int) height;
        adviewpager.setLayoutParams(params1);
        adAdapter = new GoodAdAdapter(mContext, radiogroup, vp_top, union.getImgitem());
        adviewpager.setAdapter(adAdapter);
        if (union.getImgitem() != null)
            adviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (adAdapter != null) {
                        ViewGroup indicator = adAdapter.getIndicator();
                        if (indicator != null) {
                            RadioButton rbt = (RadioButton) indicator.getChildAt(position);
                            if (rbt != null)
                                rbt.setChecked(true);
                        }
                    }
                }
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        //公司名称
        company_name.setText(union.getName());
        //发布人
        issue_user.setText("联系人: " + union.getLinkman());
        //联系电话
        issue_tel.setText("联系电话 :" + union.getLinknum());
        //公司位置
        issue_time.setText("公司位置:" + union.getAddress());
        //公司介绍
        String sys_web_service = getApplicationContext().getSysInitInfo()
                .getSys_web_service();
        String path = sys_web_service + "webview/parm/uni_" + unionId;
        webview.loadUrl(path);
        log_i("++++++++" + path);
        //判断是否收藏
        //判断是否收藏
        if ("0".equals(union.getIscollect())) {
            add_button.setImageResource(R.mipmap.collect_case_img);
        } else
            add_button.setImageResource(R.mipmap.coolect_on_img);
        //收藏操作
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                if ("0".equals(union.getIscollect())) {
                    getNetWorker().collectSave(token, "1", "4", unionId);
                } else
                    getNetWorker().collectSave(token, "2", "4", unionId);
            }
        });
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case UNION_GET:
            case COLLECT_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case UNION_GET:
                showTextDialog("获取城市详情失败，请稍后重试");
                break;
            case COLLECT_SAVE:
                showTextDialog("收藏操作失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        add_button = (ImageButton) findViewById(R.id.add_button);
        adviewpager = (JhViewPager) findViewById(R.id.adviewpager);
        vp_top = (FrameLayout) findViewById(R.id.vp_top);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        company_name = (TextView) findViewById(R.id.company_name);
        issue_tel = (TextView) findViewById(R.id.issue_tel);
        issue_user = (TextView) findViewById(R.id.issue_user);
        issue_time = (TextView) findViewById(R.id.issue_time);
        webview = (HemaWebView) findViewById(R.id.webview);
        go_tel = (TextView) findViewById(R.id.go_tel);
        go_loaction = (TextView) findViewById(R.id.go_loaction);
        title_text = (TextView) findViewById(R.id.title_text);
    }

    @Override
    protected void getExras() {
        unionId = mIntent.getStringExtra("unionId");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frist)
                    setResult(RESULT_OK, mIntent);
                finish();
            }
        });
        title_text.setText("公司简介");
        //一键拨打
        go_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + union.getLinknum()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        go_loaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyInformationActivity.this, MediaOnlyMapActivity.class);
                intent.putExtra("keytype", "2");
                //    intent.putExtra("media",medias);
                startActivity(intent);
            }
        });
    }

    @Override
    protected boolean onKeyBack() {
        if (frist)
            setResult(RESULT_OK, mIntent);
        finish();
        return true;
    }
}
