package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.StateAdapter;
import com.hemaapp.xsm.model.Report;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/3/7.
 * 全国单
 */
public class TheSingleActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton add_button;
    private ImageButton next_button;
    private TextView search_input;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private StateAdapter adapter;//全国单的adapter
    private ViewPop viewPop;
    private String status = "0";
    private String district = "";
    private String industry = "";
    private String startdate1 = "";
    private String startdate2 = "";
    private String enddate1 = "";
    private String enddate2 = "";
    private Integer page = 0;
    private ArrayList<Report> reports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_the_single);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 0;
        inIt();
    }

    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().reportList(token, "1", "", status, district, industry, startdate1, startdate2,
                enddate1, enddate2, String.valueOf(page));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                status = data.getStringExtra("status");
                industry = data.getStringExtra("industryId");
                startdate1 = data.getStringExtra("up_date1");
                startdate2 = data.getStringExtra("up_date2");
                enddate1 = data.getStringExtra("down_date1");
                enddate2 = data.getStringExtra("down_date2");
                district = data.getStringExtra("district2");
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_LIST:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_LIST:
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_LIST:
                HemaPageArrayResult<Report> result = (HemaPageArrayResult<Report>) hemaBaseResult;
                ArrayList<Report> reports = result.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.reports.clear();
                    this.reports.addAll(reports);

                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (reports.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (reports.size() > 0)
                        this.reports.addAll(reports);
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
            adapter = new StateAdapter(mContext, reports, null);
            adapter.setEmptyString("暂无全国单信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无全国单信息");
            adapter.setReports(reports);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取全国单列表失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        add_button = (ImageButton) findViewById(R.id.add_button);
        next_button = (ImageButton) findViewById(R.id.next_button);
        search_input = (TextView) findViewById(R.id.search_input);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
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
        title_text.setText("全国单");

        //筛选
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(TheSingleActivity.this, StateSelectActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        String feeaccount = JhctmApplication.getInstance().getUser().getFeeaccount();
        if ("1".equals(feeaccount)) {
            add_button.setVisibility(View.INVISIBLE);
        } else
            add_button.setVisibility(View.VISIBLE);
        //刷新
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
        if (!"2".equals(JhctmApplication.getInstance().getUser().getFeeaccount()))
            add_button.setVisibility(View.INVISIBLE);
        else
            add_button.setVisibility(View.VISIBLE);
        //我要报备
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });
        //搜索
        search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra("keytype", "2");
                startActivity(intent);
            }
        });
    }

    //我要报备  我报备的
    private class ViewPop {
        TextView issue_reput;//我要报备
        TextView my_reput;//我报备的
    }

    private void showPop() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_blog_show, null);
        viewPop = new ViewPop();
        viewPop.issue_reput = (TextView) view.findViewById(R.id.issue_reput);
        viewPop.my_reput = (TextView) view.findViewById(R.id.my_reput);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //我要报备
        viewPop.issue_reput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(TheSingleActivity.this, WantReportActivity.class);
                startActivity(intent);
            }
        });
        //我报备的
        viewPop.my_reput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(TheSingleActivity.this, MyReportActivity.class);
                startActivity(intent);
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAsDropDown(findViewById(R.id.add_button));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        //  popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
