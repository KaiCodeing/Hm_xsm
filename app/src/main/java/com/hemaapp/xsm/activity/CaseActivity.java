package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.CaseAdapter;
import com.hemaapp.xsm.model.DemoList;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/3/6.
 * 案例展示
 * CaseAdapter 案例的adapter
 */
public class CaseActivity extends JhActivity {
    private TextView search_input;
    private ImageButton back_button;
    private Button next_button;
    private XtomListView listview;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ProgressBar progressbar;
    private CaseAdapter adapter;
    private Integer page = 0;
    private String industry = "";
    private ArrayList<DemoList> demoLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_case);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 0;
        inIt();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            industry="";
            return;
        }
        switch (requestCode) {
            case 1:
                industry = data.getStringExtra("industryId");
                break;
        }
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().demoList(token, industry, "", "1", String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation infomation = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case DEMO_LIST:
                showProgressDialog("获取案例列表");

                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation infomation = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case DEMO_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation infomation = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case DEMO_LIST:
                HemaPageArrayResult<DemoList> result = (HemaPageArrayResult<DemoList>) hemaBaseResult;
                ArrayList<DemoList> demoLists = result.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.demoLists.clear();
                    this.demoLists.addAll(demoLists);

                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (demoLists.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (demoLists.size() > 0)
                        this.demoLists.addAll(demoLists);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                freshData();
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new CaseAdapter(mContext, demoLists);
            adapter.setEmptyString("暂无案例信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无案例信息");
            adapter.setDemoLists(demoLists);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation infomation = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case DEMO_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation infomation = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case DEMO_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取案例列表失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        search_input = (TextView) findViewById(R.id.search_input);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        listview = (XtomListView) findViewById(R.id.listview);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
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
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseActivity.this, SelectIndustryActivity.class);

                startActivityForResult(intent, 1);
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                inIt();
            }
        });
        //搜索
        search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseActivity.this, SearchActivity.class);
                intent.putExtra("keytype","3");
                startActivity(intent);
            }
        });
    }
}
