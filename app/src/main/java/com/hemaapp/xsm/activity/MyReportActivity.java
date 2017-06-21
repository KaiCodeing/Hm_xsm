package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
 * 我的报备
 */
public class MyReportActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private StateAdapter adapter;//全国单的adapter
    private ViewHolder holder;
    private ArrayList<Report> reports = new ArrayList<>();
    private Integer page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_report);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().reportList(token, "2", "", "", "", "", "", "", "", "", String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_LIST:
                showProgressDialog("获取个人报备信息");
                break;
            case REPORT_RM:
                showProgressDialog("删除全国单");
                break;
            case DEAL_SAVE:
                showProgressDialog("发布全国单");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                break;
            case REPORT_RM:
            case DEAL_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        final JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPORT_LIST:
                HemaPageArrayResult<Report> result = (HemaPageArrayResult<Report>) hemaBaseResult;
                ArrayList<Report> reports = result.getObjects();
                final String page2 = hemaNetTask.getParams().get("page");
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
            case REPORT_RM:
                showTextDialog("删除全国单成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        inIt();
                    }
                }, 1000);
                break;
            case DEAL_SAVE:
                showTextDialog("发布全国单成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        inIt();
                    }
                }, 1000);
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new StateAdapter(mContext, reports, "1");
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
            case REPORT_RM:
            case DEAL_SAVE:
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
            case REPORT_RM:
                showTextDialog("删除全国单失败，请稍后重试");
                break;
            case DEAL_SAVE:
                showTextDialog("发布全国单失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
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
        title_text.setText("我报备的");
        next_button.setImageResource(R.mipmap.issue_blog_img);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyReportActivity.this, WantReportActivity.class);
                startActivity(intent);
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
    }

    private class ViewHolder {
        LinearLayout layout_genjin;
        TextView content_follow;
        TextView typekey;
        TextView compile_text;
        TextView delect_text;
    }

    //展示图片
    public void showPop(final Report report) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_genjin_view, null);
        holder = new ViewHolder();
        holder.layout_genjin = (LinearLayout) view.findViewById(R.id.layout_genjin);
        holder.content_follow = (TextView) view.findViewById(R.id.content_follow);
        holder.typekey = (TextView) view.findViewById(R.id.typekey);
        holder.compile_text = (TextView) view.findViewById(R.id.compile_text);
        holder.delect_text = (TextView) view.findViewById(R.id.delect_text);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        //跟进进度
        if (report.getCon() == null || report.getCon().size() == 0) {
        } else
            holder.content_follow.setText(report.getCon().get(0).getContent());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //进度
        holder.layout_genjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(report.getStatus()) || "2".equals(report.getStatus()) ){
                    popupWindow.dismiss();
                    Intent intent = new Intent(MyReportActivity.this, FollowUpInforActivity.class);
                    intent.putExtra("reportId", report.getId());
                    startActivity(intent);
                }
                else
                {}
            }
        });
        if ("1".equals(report.getStatus()))
            holder.typekey.setText("发布");
        else if ("2".equals(report.getStatus()))
            holder.typekey.setText("已发布");
        else
            holder.typekey.setText("已报备");
        //进度状态
        holder.typekey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                if ("1".equals(report.getStatus())) {
                    getNetWorker().dealSave(token, report.getId(), "2");
                    popupWindow.dismiss();
                } else {
                }
            }
        });
        //编辑
        holder.compile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(report.getStatus()) || "2".equals(report.getStatus())) {
                    Intent intent = new Intent(MyReportActivity.this, WantReportActivity.class);
                    intent.putExtra("reportId", report.getId());
                    startActivity(intent);
                    popupWindow.dismiss();
                }
                else {

                }
            }
        });
        //删除
        holder.delect_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().reportRm(token, report.getId());

            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //   popupWindow.showAsDropDown(findViewById(R.id.add_button));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
