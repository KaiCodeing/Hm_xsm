package com.hemaapp.xsm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.MyReportIndustryAdapter;
import com.hemaapp.xsm.model.Industry;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2017/3/8.
 * 我要报备的选择行业
 */
public class MyReportIndustryActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
 //   private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private MyReportIndustryAdapter adapter;
    private ArrayList<Industry> industries=  new ArrayList<>();
    private String industryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_report_industry);
        super.onCreate(savedInstanceState);
        inIt();
    }
    //初始化
    private void inIt()
    {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().industryList(token);
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case INDUSTRY_LIST:
                showProgressDialog("获取行业列表");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case INDUSTRY_LIST:
             cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case INDUSTRY_LIST:
                HemaArrayResult<Industry> result = (HemaArrayResult<Industry>) hemaBaseResult;
                industries = result.getObjects();
                if (!isNull(industryId))
                    for (Industry industry:industries)
                    {
                        if (industry.getId().equals(industryId))
                            industry.setCheck(true);
                        else
                            industry.setCheck(false);
                    }
                freshData();
                break;
        }
    }
    private void freshData() {
        if (adapter == null) {
            adapter = new MyReportIndustryAdapter(mContext, industries);
            adapter.setEmptyString("暂无行业信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无行业信息");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case INDUSTRY_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case INDUSTRY_LIST:
                showTextDialog("行业列表获取失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
     //   refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void getExras() {
        industryId = mIntent.getStringExtra("industryId");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("所属行业");
        next_button.setVisibility(View.INVISIBLE);
        //选择行业
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIntent.putExtra("industry",industries.get(position).getId());
                mIntent.putExtra("industry_text",industries.get(position).getName());
                setResult(RESULT_OK,mIntent);
                finish();
            }
        });
    }
}
