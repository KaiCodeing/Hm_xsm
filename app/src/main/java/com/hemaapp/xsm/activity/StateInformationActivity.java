package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.Report;

/**
 * Created by lenovo on 2017/3/8.
 * 全国单详情
 */
public class StateInformationActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView company_name;
    private TextView issue_type;
    private TextView address_text;
    private TextView position_text;
    private TextView position_number;
    private TextView cause_text;
    private TextView client_name;
    private TextView client_position;
    private TextView alliance_company;
    private TextView alliance_user;
    private TextView alliance_tel;
    private TextView alliance_zw;
    private TextView alliance_time;
    private ImageView tel_call;
    private TextView content_state;
    private String reportId;
    private Report report;
    private boolean frist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_state_information);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().reportGet(token, reportId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_GET:
                showProgressDialog("获取全国单详情");
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
            case REPORT_GET:
            case COLLECT_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_GET:
                HemaArrayResult<Report> result = (HemaArrayResult<Report>) hemaBaseResult;
                report = result.getObjects().get(0);
                setData();
                break;
            case COLLECT_SAVE:
                String keytype = hemaNetTask.getParams().get("flag");
                if ("1".equals(keytype)) {
                    showTextDialog("收藏成功");
                    report.setIscollect("1");
                    next_button.setImageResource(R.mipmap.coolect_on_img);
                } else {
                    showTextDialog("取消收藏成功");
                    report.setIscollect("0");
                    next_button.setImageResource(R.mipmap.collect_case_img);
                }
                frist = true;
                break;
        }
    }

    /**
     * 填充数据
     */
    private void setData() {
        company_name.setText(report.getName());
        address_text.setText("投放区域:" + report.getArea());
        position_text.setText("所属行业:" + report.getIndustry_text());
        if ("2".equals(report.getStatus())) {
            position_number.setText("投放数量: " + report.getPutnum());
            cause_text.setText("投放时间: " + report.getStartdate() + "--" + report.getEnddate());
        } else {
            position_number.setText("预计投放数量: " + report.getPutnum());
            cause_text.setText("预计投放时间: " + report.getStartdate() + "--" + report.getEnddate());
        }

        client_name.setText("客户联系人: " + report.getLinkname());
        client_position.setText("联系人职务: " + report.getLinkpost());
        alliance_company.setText("联盟公司名称:" + report.getCompony());
        alliance_user.setText("联盟公司联系人:" + report.getNickname());
        alliance_tel.setText("联系人电话:" + report.getMobile());
        alliance_zw.setText("联系人职务:" + report.getLinkpost());
        alliance_time.setText("发布时间:" + report.getRegdate());
        //跟进情况
        if (report.getCon() == null || report.getCon().size() == 0) {
        } else {
            String content = "";
            for (int i = report.getCon().size() - 1; i > -1; i--) {
                content = content + report.getCon().get(i).getRegtime() + report.getCon().get(i).getContent() + "\n";
            }
            content_state.setText(content);
        }
        //电话
        tel_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + report.getMobile()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //发布类型判断
        if ("1".equals(report.getStatus())) {
            issue_type.setBackgroundResource(R.drawable.sh_text_doom);
            issue_type.setText("已报备");
        } else if ("2".equals(report.getStatus())) {
            issue_type.setBackgroundResource(R.drawable.sh_text_doom);
            issue_type.setText("已发布");
        } else {
            issue_type.setBackgroundResource(R.drawable.sh_text_gate);
            issue_type.setText("已过期");
        }
        //判断是否收藏
        if ("0".equals(report.getIscollect())) {
            next_button.setImageResource(R.mipmap.collect_case_img);
        } else
            next_button.setImageResource(R.mipmap.coolect_on_img);
        //收藏操作
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                if ("0".equals(report.getIscollect())) {
                    getNetWorker().collectSave(token, "1", "2", reportId);
                } else
                    getNetWorker().collectSave(token, "2", "2", reportId);
            }
        });
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_GET:
            case COLLECT_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_GET:
                showTextDialog("获取全国单详情失败，请稍后重试");
                break;
            case COLLECT_SAVE:
                showTextDialog("保存操作失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        company_name = (TextView) findViewById(R.id.company_name);
        issue_type = (TextView) findViewById(R.id.issue_type);
        address_text = (TextView) findViewById(R.id.address_text);
        position_text = (TextView) findViewById(R.id.position_text);
        position_number = (TextView) findViewById(R.id.position_number);
        cause_text = (TextView) findViewById(R.id.cause_text);
        client_name = (TextView) findViewById(R.id.client_name);
        client_position = (TextView) findViewById(R.id.client_position);
        alliance_company = (TextView) findViewById(R.id.alliance_company);
        alliance_user = (TextView) findViewById(R.id.alliance_user);
        alliance_zw = (TextView) findViewById(R.id.alliance_zw);
        alliance_time = (TextView) findViewById(R.id.alliance_time);
        tel_call = (ImageView) findViewById(R.id.tel_call);
        content_state = (TextView) findViewById(R.id.content_state);
        alliance_tel = (TextView) findViewById(R.id.alliance_tel);
    }

    @Override
    protected void getExras() {
        reportId = mIntent.getStringExtra("reportId");
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
        title_text.setText("全国单详情");
        next_button.setImageResource(R.mipmap.coolect_on_img);
    }

    @Override
    protected boolean onKeyBack() {
        if (frist)
            setResult(RESULT_OK, mIntent);
        finish();
        return true;
    }
}
