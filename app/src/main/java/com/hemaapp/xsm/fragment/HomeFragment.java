package com.hemaapp.xsm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.xsm.JhFragment;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhUtil;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.CaseActivity;
import com.hemaapp.xsm.activity.City0Activity;
import com.hemaapp.xsm.activity.MediaActivity;
import com.hemaapp.xsm.activity.MediaOnlyMapActivity;
import com.hemaapp.xsm.activity.MessageActivity;
import com.hemaapp.xsm.activity.RQSearchActivity;
import com.hemaapp.xsm.activity.TheSingleActivity;
import com.hemaapp.xsm.adapter.MediaAdapter;
import com.hemaapp.xsm.adapter.TopAdAdapter;
import com.hemaapp.xsm.model.AdList;
import com.hemaapp.xsm.model.Media;
import com.hemaapp.xsm.view.JhViewPager;
import com.hemaapp.xsm.view.MyListView;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/3/2.
 * 首页的
 * 首页adapter  mediaAdapter
 */
public class HomeFragment extends JhFragment{
    private TextView loaction_text;//地址
    private TextView search_input;//搜索
    private ImageView message_to;//消息
    private TextView message_number;//消息数量
    private JhViewPager adviewpager;
    private FrameLayout media_view;
    private FrameLayout state_view;
    private FrameLayout case_view;
    private FrameLayout city_view;
    private MyListView listview;
    private String cityName;
    private Integer page = 0;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ProgressBar progressbar;
    private ArrayList<Media> medias = new ArrayList<>();
    private MediaAdapter adapter;
    private TopAdAdapter adAdapter;
    private ArrayList<AdList> adLists = new ArrayList<>();//广告
    private FrameLayout vp_top;
    private RadioGroup radiogroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_home);
        super.onCreate(savedInstanceState);
        log_i("++++++++++++++++++++++++++++++" + XtomSharedPreferencesUtil.get(getActivity(), "city"));
        if (isNull(XtomSharedPreferencesUtil.get(getActivity(), "city")))
            cityName = "未定位";
        else
            cityName = XtomSharedPreferencesUtil.get(getActivity(), "city");
        //写定位城市
        loaction_text.setText(cityName);
        log_i("_________" + cityName);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNull(XtomSharedPreferencesUtil.get(getActivity(), "cityId"))) {
            Intent intent = new Intent(getActivity(), City0Activity.class);
            intent.putExtra("keytype", "1");
            HomeFragment.this.startActivityForResult(intent, 1);
        } else {
            inIt();
        }
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().noticeUnread(token);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity().RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 1:
                String cityName = data.getStringExtra("name");
                loaction_text.setText(cityName);
                XtomSharedPreferencesUtil.save(getContext(), "cityName", cityName);
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:

                break;
            case AD_LIST:
             //   showProgressDialog("获取广告信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                break;
            case AD_LIST:
                cancelProgressDialog();
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
                        XtomToastUtil.showShortToast(getActivity(), "已经到最后啦");
                    }
                }
                freshData();

                break;
            case AD_LIST:
                HemaPageArrayResult<AdList> result1 = (HemaPageArrayResult<AdList>) hemaBaseResult;
                adLists = result1.getObjects();
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().mediaList(token, "1", "", "1", "", XtomSharedPreferencesUtil.get(getActivity(), "cityId"), "", "", "",
                        "", "", "", String.valueOf(page));
                break;
            case NOTICE_UNREAD:
                HemaArrayResult<String> result2 = (HemaArrayResult<String>) hemaBaseResult;
                String num = result2.getObjects().get(0);
                if (isNull(num) || "0".equals(num)) {
                    message_number.setVisibility(View.GONE);
                } else {
                    message_number.setVisibility(View.VISIBLE);
                    message_number.setText(num);
                }
                getNetWorker().adList();
                break;

        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new MediaAdapter(getContext(), medias, null);
            adapter.setEmptyString("暂无媒体信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无媒体信息");
            adapter.setMedias(medias);
            adapter.notifyDataSetChanged();
        }
        double width = JhUtil.getScreenWidth(getActivity());
        double height = width / 640 * 266;
        ViewGroup.LayoutParams params1 = adviewpager.getLayoutParams();
        params1.width = (int) width;
        params1.height = (int) height;
        adviewpager.setLayoutParams(params1);
        //广告列表
        adAdapter = new TopAdAdapter(HomeFragment.this, radiogroup, vp_top, adLists);
        adviewpager.setAdapter(adAdapter);
        adviewpager.setOnPageChangeListener(new PageChangeListener());
    }
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            if (adAdapter != null) {
                ViewGroup indicator = adAdapter.getIndicator();
                if (indicator != null) {
                    RadioButton rbt = (RadioButton) indicator.getChildAt(arg0);
                    if (rbt != null)
                        rbt.setChecked(true);
                }
            }
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
            case AD_LIST:
            case NOTICE_UNREAD:
                showTextDialog(hemaBaseResult.getMsg());
                break;

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
                showTextDialog("获取媒体列表失败，请稍后重试");
                break;
            case AD_LIST:
                showTextDialog("获取广告信息失败，请稍后重试");
                break;
            case NOTICE_UNREAD:
                showTextDialog("获取未读通知条数失败，请稍后重试");
                break;

        }
    }

    @Override
    protected void findView() {
        loaction_text = (TextView) findViewById(R.id.loaction_text);
        search_input = (TextView) findViewById(R.id.search_input);
        message_to = (ImageView) findViewById(R.id.message_to);
        message_number = (TextView) findViewById(R.id.message_number);
        adviewpager = (JhViewPager) findViewById(R.id.adviewpager);
        media_view = (FrameLayout) findViewById(R.id.media_view);
        state_view = (FrameLayout) findViewById(R.id.state_view);
        case_view = (FrameLayout) findViewById(R.id.case_view);
        city_view = (FrameLayout) findViewById(R.id.city_view);
        listview = (MyListView) findViewById(R.id.listview);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        vp_top = (FrameLayout) findViewById(R.id.vp_top);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
    }

    @Override
    protected void setListener() {
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
        //选城市
        loaction_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), City0Activity.class);
                HomeFragment.this.startActivityForResult(intent, 1);
            }
        });
        //媒体档期
        media_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MediaActivity.class);
                startActivity(intent);
            }
        });
        //全国单
        state_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断账号角色
                if (!"1".equals(JhctmApplication.getInstance().getUser().getFeeaccount()))
                {
                    Intent intent = new Intent(getActivity(), TheSingleActivity.class);
                    startActivity(intent);
                }
                else
                {
                    showTextDialog("您不具备访问权限");
                }

            }
        });
        //案例展示
        case_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaseActivity.class);
                startActivity(intent);
            }
        });
        //消息
        message_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        });
        //搜索
        search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RQSearchActivity.class);
//                intent.putExtra("keytype","1");
                startActivity(intent);
            }
        });
        /**
         * 加盟城市
         */
        city_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), MediaOnlyMapActivity.class);
              //  Intent intent = new Intent(getActivity(), UpLoadActivity.class);
                intent.putExtra("keytype","3");
                startActivity(intent);
            }
        });
        listview.setFocusable(false);
    }


}
