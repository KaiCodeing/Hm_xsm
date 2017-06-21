package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetTaskExecuteListener;
import com.hemaapp.xsm.JhNetWorker;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.MyCollectActivity;
import com.hemaapp.xsm.model.DemoList;
import com.hemaapp.xsm.model.Media;
import com.hemaapp.xsm.model.Report;
import com.hemaapp.xsm.model.Union;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/3/3.
 * 我的收藏的page
 * MediaAdapter 媒体档期
 * CaseAdapter 案例展示
 * UnionCompanyActivity 联盟公司
 * StateAdapter 全国单
 */
public class CollecttPageAdapter extends PagerAdapter {
    private ArrayList<Params> paramses;
    private MyCollectActivity activity;
    private ArrayList<RefreshLoadmoreLayout> layouts = new ArrayList<>();
    private int posit;
    private boolean isFrist = true;

    public CollecttPageAdapter(ArrayList<Params> paramses, MyCollectActivity activity, int position) {
        this.paramses = paramses;
        this.activity = activity;
        this.posit = position;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        Params params = paramses.get(position);
        if (params.isFirstSetPrimary) {
            View view = (View) object;
            RefreshLoadmoreLayout layout = (RefreshLoadmoreLayout) view
                    .findViewById(R.id.refreshLoadmoreLayout);
            layout.getOnStartListener().onStartRefresh(layout);
            params.isFirstSetPrimary = false;
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
//        View view = container.getChildAt(position);
//        if (view == null) {
        Log.i("ss", "position---" + position);
        if (isFrist && posit > 0) {
            isFrist = false;
            for (int i = 0; i < posit; i++) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                View view = inflater.inflate(R.layout.pageritem_rllistview_progress,
                        null);
                container.addView(view, i);
            }
        }
        View view = container.getChildAt(position);
        Params params = paramses.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            view = inflater.inflate(R.layout.pageritem_rllistview_progress,
                    null);
            RefreshLoadmoreLayout layout = (RefreshLoadmoreLayout) view
                    .findViewById(R.id.refreshLoadmoreLayout);
            layouts.add(layout);
            JhNetWorker netWorker = new JhNetWorker(activity);
            netWorker.setOnTaskExecuteListener(new OnTaskExecuteListener(
                    activity, view, params));
            layout.setOnStartListener(new OnStartListener(params, netWorker,
                    view));
            container.addView(view);
        } else {
            if (params.isFirstSetPrimary) {
                RefreshLoadmoreLayout layout = (RefreshLoadmoreLayout) view
                        .findViewById(R.id.refreshLoadmoreLayout);
                JhNetWorker netWorker = new JhNetWorker(activity);
                netWorker.setOnTaskExecuteListener(new OnTaskExecuteListener(
                        activity, view, params));
                layout.setOnStartListener(new OnStartListener(params, netWorker,
                        view));
            }
        }

//        }
        return view;
    }

    private class OnStartListener implements
            xtom.frame.view.XtomRefreshLoadmoreLayout.OnStartListener {

        private Integer current_page = 0;
        private Params params;
        private JhNetWorker netWorker;
        private XtomRefreshLoadmoreLayout layout;
        private XtomListView listView;
        private ProgressBar progressBar;
        private LinearLayout layout_show;

        public OnStartListener(Params params, JhNetWorker netWorker, View v) {
            this.params = params;
            this.netWorker = netWorker;
            this.layout = (XtomRefreshLoadmoreLayout) v
                    .findViewById(R.id.refreshLoadmoreLayout);
            this.progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
            this.listView = (XtomListView) layout.findViewById(R.id.listview);

        }

        @Override
        public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
            // TODO Auto-generated method stub
            current_page = 0;
            JhctmApplication application = JhctmApplication.getInstance();
            String token = application.getUser().getToken();
            if (params.keytype.equals("1")) {
                netWorker.mediaList(token, "3", "", "", "", "", "", "", "", "", "", "", String.valueOf(current_page));
            } else if (params.keytype.equals("2"))
                netWorker.reportList(token, "3", "", "", "", "", "", "", "", "", String.valueOf(current_page));
            else if (params.keytype.equals("3"))
                netWorker.demoList(token, "", "", "2", String.valueOf(current_page));
            else
                netWorker.unionList(token, "2", String.valueOf(current_page));
        }

        @Override
        public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
            // TODO Auto-generated method stub
            current_page++;
            JhctmApplication application = JhctmApplication.getInstance();
            String token = application.getUser().getToken();
            if (params.keytype.equals("1")) {
                netWorker.mediaList(token, "3", "", "", "", "", "", "", "", "", "", "", String.valueOf(current_page));
            } else if (params.keytype.equals("2"))
                netWorker.reportList(token, "3", "", "", "", "", "", "", "", "", String.valueOf(current_page));
            else if (params.keytype.equals("3"))
                netWorker.demoList(token, "", "", "2", String.valueOf(current_page));
            else
                netWorker.unionList(token, "2", String.valueOf(current_page));
        }

    }

    private class OnTaskExecuteListener extends JhNetTaskExecuteListener {

        private XtomRefreshLoadmoreLayout layout;
        private XtomListView listView;
        private ProgressBar progressBar;
        private Params params;
        // private GridView gridview;
        private ViewPager vp;
        private TextView content;
        private MediaAdapter mediaAdapter;
        private ArrayList<Media> medias = new ArrayList<>();
        private StateAdapter stateAdapter;
        private ArrayList<Report> reports = new ArrayList<>();
        private CaseAdapter caseAdapter;
        private ArrayList<DemoList> demoLists = new ArrayList<>();
        private UnionAdapter unionAdapter;
        private ArrayList<Union> unions = new ArrayList<>();
        private JhNetWorker netWorker;

        // private View allitem;
        public OnTaskExecuteListener(Context context, View view, Params params) {
            super(context);
            // TODO Auto-generated constructor stub
            layout = (XtomRefreshLoadmoreLayout) view
                    .findViewById(R.id.refreshLoadmoreLayout);
            listView = (XtomListView) view.findViewById(R.id.listview);
            progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
            progressBar.setVisibility(View.GONE);
            this.params = params;
       //     this.netWorker = netWorker;
            medias = new ArrayList<>();
            reports = new ArrayList<>();
            demoLists = new ArrayList<>();
            unions = new ArrayList<>();
        }

        @Override
        public void onPreExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            // TODO Auto-generated method stub
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                default:
                    activity.showProgressDialog("获取数据...");
                    break;
            }
        }

        @Override
        public void onPostExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            // TODO Auto-generated method stub
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                default:
                    activity.cancelProgressDialog();
                    progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onServerSuccess(HemaNetWorker netWorker,
                                    HemaNetTask netTask, HemaBaseResult baseResult) {
            // TODO Auto-generated method stub
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case MEDIA_LIST:
                    HemaPageArrayResult<Media> result = (HemaPageArrayResult<Media>) baseResult;
                    ArrayList<Media> medias = result.getObjects();
                    String page2 = netTask.getParams().get("page");
                    if ("0".equals(page2)) {// 刷新
                        layout.refreshSuccess();
                        this.medias.clear();
                        this.medias.addAll(medias);

                        JhctmApplication application = JhctmApplication.getInstance();
                        int sysPagesize = application.getSysInitInfo()
                                .getSys_pagesize();
                        if (medias.size() < sysPagesize) {
                            layout.setLoadmoreable(false);
                            // leftRE = false;
                        } else {
                            layout.setLoadmoreable(true);
                            // leftRE = true;
                        }
                    } else {// 更多
                        layout.loadmoreSuccess();
                        if (medias.size() > 0)
                            this.medias.addAll(medias);
                        else {
                            layout.setLoadmoreable(false);
                            // leftRE = false;
                            XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                        }
                    }
                    if (mediaAdapter == null) {
                        mediaAdapter = new MediaAdapter(mContext, this.medias, null, "1");
                        mediaAdapter.setEmptyString("暂无媒体信息");
                        listView.setAdapter(mediaAdapter);
                    } else {
                        mediaAdapter.setEmptyString("暂无媒体信息");
                        mediaAdapter.setMedias(this.medias);
                        mediaAdapter.notifyDataSetChanged();
                    }
                    break;
                case REPORT_LIST:
                    HemaPageArrayResult<Report> result1 = (HemaPageArrayResult<Report>) baseResult;
                    ArrayList<Report> reports = result1.getObjects();
                    String page22 = netTask.getParams().get("page");
                    if ("0".equals(page22)) {// 刷新
                        layout.refreshSuccess();
                        this.reports.clear();
                        this.reports.addAll(reports);

                        JhctmApplication application = JhctmApplication.getInstance();
                        int sysPagesize = application.getSysInitInfo()
                                .getSys_pagesize();
                        if (reports.size() < sysPagesize) {
                            layout.setLoadmoreable(false);
                            // leftRE = false;
                        } else {
                            layout.setLoadmoreable(true);
                            // leftRE = true;
                        }
                    } else {// 更多
                        layout.loadmoreSuccess();
                        if (reports.size() > 0)
                            this.reports.addAll(reports);
                        else {
                            layout.setLoadmoreable(false);
                            // leftRE = false;
                            XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                        }
                    }
                    if (stateAdapter == null) {
                        stateAdapter = new StateAdapter(mContext, this.reports, null, "1");
                        stateAdapter.setEmptyString("暂无全国单信息");
                        listView.setAdapter(stateAdapter);
                    } else {
                        stateAdapter.setEmptyString("暂无全国单信息");
                        stateAdapter.setReports(this.reports);
                        stateAdapter.notifyDataSetChanged();
                    }
                    break;
                case DEMO_LIST:
                    HemaPageArrayResult<DemoList> result3 = (HemaPageArrayResult<DemoList>) baseResult;
                    ArrayList<DemoList> demoLists = result3.getObjects();
                    String page3 = netTask.getParams().get("page");
                    if ("0".equals(page3)) {// 刷新
                        layout.refreshSuccess();
                        this.demoLists.clear();
                        this.demoLists.addAll(demoLists);

                        JhctmApplication application = JhctmApplication.getInstance();
                        int sysPagesize = application.getSysInitInfo()
                                .getSys_pagesize();
                        if (demoLists.size() < sysPagesize) {
                            layout.setLoadmoreable(false);
                            // leftRE = false;
                        } else {
                            layout.setLoadmoreable(true);
                            // leftRE = true;
                        }
                    } else {// 更多
                        layout.loadmoreSuccess();
                        if (demoLists.size() > 0)
                            this.demoLists.addAll(demoLists);
                        else {
                            layout.setLoadmoreable(false);
                            // leftRE = false;
                            XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                        }
                    }
                    if (caseAdapter == null) {
                        caseAdapter = new CaseAdapter(mContext, this.demoLists, "1");
                        caseAdapter.setEmptyString("暂无案例信息");
                        listView.setAdapter(caseAdapter);
                    } else {
                        caseAdapter.setEmptyString("暂无案例信息");
                        caseAdapter.setDemoLists(this.demoLists);
                        caseAdapter.notifyDataSetChanged();
                    }
                    break;
                case UNION_LIST:
                    HemaPageArrayResult<Union> result4 = (HemaPageArrayResult<Union>) baseResult;
                    ArrayList<Union> unions = result4.getObjects();
                    String page4 = netTask.getParams().get("page");
                    if ("0".equals(page4)) {// 刷新
                        layout.refreshSuccess();
                        this.unions.clear();
                        this.unions.addAll(unions);

                        JhctmApplication application = JhctmApplication.getInstance();
                        int sysPagesize = application.getSysInitInfo()
                                .getSys_pagesize();
                        if (unions.size() < sysPagesize) {
                            layout.setLoadmoreable(false);
                            // leftRE = false;
                        } else {
                            layout.setLoadmoreable(true);
                            // leftRE = true;
                        }
                    } else {// 更多
                        layout.loadmoreSuccess();
                        if (unions.size() > 0)
                            this.unions.addAll(unions);
                        else {
                            layout.setLoadmoreable(false);
                            // leftRE = false;
                            XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                        }
                    }
                    if (unionAdapter == null) {
                        unionAdapter = new UnionAdapter(mContext, this.unions, "1");
                        unionAdapter.setEmptyString("暂无案例信息");
                        listView.setAdapter(unionAdapter);
                    } else {
                        unionAdapter.setEmptyString("暂无案例信息");
                        unionAdapter.setUnions(this.unions);
                        unionAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onServerFailed(HemaNetWorker netWorker,
                                   HemaNetTask netTask, HemaBaseResult baseResult) {
            // TODO Auto-generated method stub
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                default:
                    String page5 = netTask.getParams().get("page");
                    if ("0".equals(page5)) { // 刷新
                        layout.refreshFailed();
                    } else { // 更多
                        layout.loadmoreFailed();
                    }
                    break;
            }
        }

        @Override
        public void onExecuteFailed(HemaNetWorker netWorker,
                                    HemaNetTask netTask, int failedType) {
            // TODO Auto-generated method stub
            JhHttpInformation information = (JhHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                default:
                    String page12 = netTask.getParams().get("page");
                    if ("0".equals(page12)) { // 刷新
                        layout.refreshFailed();
                    } else { // 更多
                        layout.loadmoreFailed();
                    }
                    break;
            }
        }

    }

    public static class Params extends XtomObject {
        boolean isFirstSetPrimary = true;// 第一次显示时需要自动加载数据
        String keytype;
        String keyid;

        public Params(String keytype, String keyid) {
            super();
            this.keytype = keytype;
            this.keyid = keyid;
        }
    }

    public void freshAll() {
        for (RefreshLoadmoreLayout layout : layouts) {
            if (layout != null)
                layout.getOnStartListener().onStartRefresh(layout);
        }
    }

}
