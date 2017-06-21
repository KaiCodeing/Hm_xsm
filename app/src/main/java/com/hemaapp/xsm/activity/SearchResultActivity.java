package com.hemaapp.xsm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.hemaapp.xsm.adapter.MediaAdapter;
import com.hemaapp.xsm.adapter.StateAdapter;
import com.hemaapp.xsm.model.DemoList;
import com.hemaapp.xsm.model.Media;
import com.hemaapp.xsm.model.Report;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/3/21.
 * 搜索结果
 * / 1 媒体 2 全国单 3 案例
 */
public class SearchResultActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private TextView search_word;
    private LinearLayout no_search_layout;
    private FrameLayout search_yes_fr;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private String keytype; //1 媒体 2 全国单 3 案例
    private Integer page = 0;
    private ArrayList<Media> medias = new ArrayList<>();
    private ArrayList<DemoList> demoLists = new ArrayList<>();
    private ArrayList<Report> reports = new ArrayList<>();
    private String keyword;
    private MediaAdapter adapter;
    private StateAdapter adapter2;//全国单的adapter
    private CaseAdapter adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_result);
        super.onCreate(savedInstanceState);
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        if ("1".equals(keytype))
            getNetWorker().mediaList(token, "1", keyword, "", "", "", "", "", "", "", "", "", String.valueOf(page));
        else if ("2".equals(keytype))
            getNetWorker().reportList(token, "1", keyword, "", "", "", "", "", "", "", String.valueOf(page));
        else
            getNetWorker().demoList(token, "", keyword, "1", String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:

            case REPORT_LIST:
            case DEMO_LIST:
                showProgressDialog("获取搜索内容");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
            case REPORT_LIST:
            case DEMO_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                HemaPageArrayResult<Media> result = (HemaPageArrayResult<Media>) hemaBaseResult;
                ArrayList<Media> medias = result.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.medias.clear();
                    this.medias.addAll(medias);

                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (medias.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (medias.size() > 0)
                        this.medias.addAll(medias);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                if (this.medias == null || this.medias.size() == 0) {
                    no_search_layout.setVisibility(View.VISIBLE);
                    search_yes_fr.setVisibility(View.GONE);
                } else
                    freshData();
                break;
            case REPORT_LIST:
                HemaPageArrayResult<Report> result1 = (HemaPageArrayResult<Report>) hemaBaseResult;
                ArrayList<Report> reports = result1.getObjects();
                String page21 = hemaNetTask.getParams().get("page");
                if ("0".equals(page21)) {// 刷新
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
                if (this.reports == null || this.reports.size() == 0) {
                    no_search_layout.setVisibility(View.VISIBLE);
                    search_yes_fr.setVisibility(View.GONE);
                } else
                    freshData2();
                break;
            case DEMO_LIST:
                HemaPageArrayResult<DemoList> result2 = (HemaPageArrayResult<DemoList>) hemaBaseResult;
                ArrayList<DemoList> demoLists = result2.getObjects();
                String page22 = hemaNetTask.getParams().get("page");
                if ("0".equals(page22)) {// 刷新
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
                if (this.demoLists == null || this.demoLists.size() == 0) {
                    no_search_layout.setVisibility(View.VISIBLE);
                    search_yes_fr.setVisibility(View.GONE);
                } else
                    freshData3();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case REPORT_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case DEMO_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    //案例
    private void freshData3() {
        if (adapter3 == null) {
            adapter3 = new CaseAdapter(mContext, demoLists);
            adapter3.setEmptyString("暂无案例信息");
            listview.setAdapter(adapter3);
        } else {
            adapter3.setEmptyString("暂无案例信息");
            adapter3.setDemoLists(demoLists);
            adapter3.notifyDataSetChanged();
        }
    }

    //全国单
    private void freshData2() {
        if (adapter2 == null) {
            adapter2 = new StateAdapter(mContext, reports, null);
            adapter2.setEmptyString("暂无全国单信息");
            listview.setAdapter(adapter2);
        } else {
            adapter2.setEmptyString("暂无全国单信息");
            adapter2.setReports(reports);
            adapter2.notifyDataSetChanged();
        }
    }

    //媒体
    private void freshData() {
        if (adapter == null) {
            adapter = new MediaAdapter(mContext, medias, null);
            adapter.setEmptyString("暂无媒体信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无媒体信息");
            adapter.setMedias(medias);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取搜索内容失败，请稍后重试");
                break;
            case REPORT_LIST:
                String page1 = hemaNetTask.getParams().get("page");
                if ("0".equals(page1)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取搜索内容失败，请稍后重试");
                break;
            case DEMO_LIST:
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取搜索内容失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        search_word = (TextView) findViewById(R.id.search_word);
        no_search_layout = (LinearLayout) findViewById(R.id.no_search_layout);
        search_yes_fr = (FrameLayout) findViewById(R.id.search_yes_fr);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
        keyword = mIntent.getStringExtra("keyword");
    }

    @Override
    protected void setListener() {
        search_word.setText("当前搜索: " + keyword);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("搜索结果");
        next_button.setVisibility(View.INVISIBLE);
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
}
