package com.hemaapp.xsm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.SelectIndustryAdapter;
import com.hemaapp.xsm.model.Industry;
import com.hemaapp.xsm.view.JhctmGridView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/7.
 * 选择行业
 * SelectIndustryAdapter
 */
public class SelectIndustryActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private JhctmGridView gridview;
    private SelectIndustryAdapter adapter;
    private ArrayList<Industry> industries = new ArrayList<>();
    private String industryId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_industry);
        super.onCreate(savedInstanceState);
        inIt();
    }
    private void inIt()
    {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().industryList(token);
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
                showProgressDialog("获取行业列表");
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
                HemaArrayResult<Industry> result = (HemaArrayResult<Industry>) hemaBaseResult;
                industries = result.getObjects();
                if (industries == null || industries.size() == 0) {
                } else {
                    industries.get(0).setCheck(true);
                }
                freshData();
                break;
        }
    }
    private void freshData() {
        if (adapter == null) {
            adapter = new SelectIndustryAdapter(mContext, industries);
            gridview.setAdapter(adapter);
        } else {
            adapter.setIndustries(industries);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
                showTextDialog("获取行业列表失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        gridview = (JhctmGridView) findViewById(R.id.gridview);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
    back_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
        title_text.setText("行业筛选");
        next_button.setText("确定");
        //确定
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < industries.size(); i++) {
                    if (industries.get(i).isCheck())
                        industryId = industries.get(i).getId();
                }
                mIntent.putExtra("industryId", industryId);
                setResult(RESULT_OK, mIntent);
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                finish();
            }
        });
    }
}
