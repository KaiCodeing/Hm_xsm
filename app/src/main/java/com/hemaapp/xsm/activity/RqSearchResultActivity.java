package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.hemaapp.xsm.adapter.RqMediaAdapter;
import com.hemaapp.xsm.model.Media;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/5/26.
 * 二期媒体列表的搜索
 */
public class RqSearchResultActivity extends JhActivity implements View.OnClickListener {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private TextView search_word;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private LinearLayout select_layout;
    private TextView change;//编辑|
    private Integer page = 0;
    private String keyword = "";//搜索关键词
    private String status;//状态=0 全部=1 上刊=2 空档期
    private String district1;
    private String district2;
    private String district3;
    private String limitcontent;//=0 全部=1 不限=2 房地产
    private String change_flag;//默认传：不传是全部	=1 不可调整=2 可调整
    private String house_type;//默认传：不传是全部	=1 不限=2 住宅=3 别墅=4 商超=5 写字楼
    private String time_out;//=0或不传 全部=1 十天内到期=2 已到期
    private String house_pirce_min;//楼盘均价小--新增
    private String house_pirce_max;//楼盘均价大--新增
    private String people_num_min;//覆盖人数小--新增
    private String people_num_max;//覆盖人数大--新增
    private String park_num_min;//车位数量小--新增
    private String park_num_max;//车位数量大--新增
    private String lng;//	经度--新增
    private String lat;//纬度--新增
    private String range_val;//范围--新增
    private ArrayList<Media> medias = new ArrayList<>();
    private FrameLayout search_yes_fr;
    private LinearLayout no_search_layout;
    private String up_date1 = "";
    private String up_date2 = "";
    private String down_date1 = "";
    private String down_date2 = "";
    private ImageView all_buy_img;
    private String all_or_no = "0";
    private ViewHolder holder;
    private RqMediaAdapter adapter;
    private String closeType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rq_search_result);
        super.onCreate(savedInstanceState);
        inIt();
    }

    @Override
    protected void onResume() {
        inIt();
        super.onResume();
    }

    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().mediaList(token, "1", keyword, "", status, district1, district2, district3, limitcontent, change_flag,
                house_type, time_out, house_pirce_min, house_pirce_max, people_num_min, people_num_max,
                park_num_min, park_num_max, lng, lat, range_val, up_date1, up_date2, down_date1, down_date2, "",String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                showProgressDialog("获取搜索内容");
                break;
            case MEDIA_EDIT:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
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
                String count = String.valueOf(result.getTotalCount());
                search_word.setText("共" + count + "条搜索结果");
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
                } else {
                    freshData();
                }
                break;
            case MEDIA_EDIT:
                String id = hemaNetTask.getParams().get("id");
                for (Media media : this.medias) {
                    if (id.equals(media.getId()))
                        media.setStatus("2");

                }
                freshData();
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new RqMediaAdapter(RqSearchResultActivity.this, medias, closeType);
            adapter.setEmptyString("暂无媒体信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无媒体信息");
            adapter.setMedias(medias);
            adapter.setColseType(closeType);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
            case MEDIA_EDIT:
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
                showTextDialog("获取搜索内容失败，请稍后重试");
                break;
            case MEDIA_EDIT:
                showTextDialog("操作失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        search_word = (TextView) findViewById(R.id.search_word);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        select_layout = (LinearLayout) findViewById(R.id.select_layout);
        change = (TextView) findViewById(R.id.change);
        search_yes_fr = (FrameLayout) findViewById(R.id.search_yes_fr);
        no_search_layout = (LinearLayout) findViewById(R.id.no_search_layout);
        all_buy_img = (ImageView) findViewById(R.id.all_buy_img);
    }

    @Override
    protected void getExras() {
        keyword = mIntent.getStringExtra("keyword");
        if (isNull(keyword))
            keyword = "";
        status = mIntent.getStringExtra("status");
        if (isNull(status))
            status = "";
        district1 = mIntent.getStringExtra("district1");
        if (isNull(district1))
            district1 = "";
        district2 = mIntent.getStringExtra("district2");
        if (isNull(district2))
            district2 = "";
        district3 = mIntent.getStringExtra("district3");
        if (isNull(district3))
            district3 = "";
        limitcontent = mIntent.getStringExtra("limitcontent");
        if (isNull(limitcontent))
            limitcontent = "";
        change_flag = mIntent.getStringExtra("change_flag");
        if (isNull(change_flag))
            change_flag = "";
        house_type = mIntent.getStringExtra("house_type");
        if (isNull(house_type))
            house_type = "";
        time_out = mIntent.getStringExtra("time_out");
        if (isNull(time_out))
            time_out = "";
        house_pirce_min = mIntent.getStringExtra("house_pirce_min");
        if (isNull(house_pirce_min))
            house_pirce_min = "";
        house_pirce_max = mIntent.getStringExtra("house_pirce_max");
        if (isNull(house_pirce_max))
            house_pirce_max = "";
        people_num_min = mIntent.getStringExtra("people_num_min");
        if (isNull(people_num_min))
            people_num_min = "";
        people_num_max = mIntent.getStringExtra("people_num_max");
        if (isNull(people_num_max))
            people_num_max = "";
        park_num_min = mIntent.getStringExtra("park_num_min");
        if (isNull(park_num_min))
            park_num_min = "";
        park_num_max = mIntent.getStringExtra("park_num_max");
        if (isNull(park_num_max))
            park_num_max = "";
        lng = mIntent.getStringExtra("lng");
        if (isNull(lng))
            lng = "";
        lat = mIntent.getStringExtra("lat");
        if (isNull(lat))
            lat = "";
        range_val = mIntent.getStringExtra("range_val");
        if (isNull(range_val))
            range_val = "";
        up_date1 = mIntent.getStringExtra("up_date1");
        if (isNull(up_date1))
            up_date1 = "";
        up_date2 = mIntent.getStringExtra("up_date2");
        if (isNull(up_date2))
            up_date2 = "";
        down_date1 = mIntent.getStringExtra("down_date1");
        if (isNull(down_date1))
            down_date1 = "";
        down_date2 = mIntent.getStringExtra("down_date2");
        if (isNull(down_date2))
            down_date2 = "";
    }

    @Override
    protected void setListener() {
        title_text.setText("搜索结果");
        next_button.setText("批量操作");
        next_button.setOnClickListener(this);
        change.setOnClickListener(this);
        all_buy_img.setOnClickListener(this);
        //判断用户权限
        //判断用户权限显示控件
        String userType = JhctmApplication.getInstance().getUser().getFeeaccount();
        if ("3".equals(userType)) {
            //普通用户  判断是否媒体列表有编辑权限
            next_button.setVisibility(View.VISIBLE);

        } else
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
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //窗口
    private class ViewHolder {
        RadioGroup group_1;
        TextView compile_text;
        RadioButton radiobutton_1;
    }

    /**
     * 更改状态
     *
     * @param media
     */
    public void changeState(final Media media) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_change_rq, null);
        holder = new ViewHolder();
        holder.group_1 = (RadioGroup) view.findViewById(R.id.group_1);
        holder.compile_text = (TextView) view.findViewById(R.id.compile_text);
        holder.radiobutton_1 = (RadioButton) view.findViewById(R.id.radiobutton_1);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        if ("2".equals(media.getStatus()))
            holder.radiobutton_1.setChecked(true);
        else
            holder.radiobutton_1.setChecked(false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        holder.group_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                switch (checkedId) {
                    case R.id.radiobutton_1:
                        if ("1".equals(media.getStatus())) {
                            getNetWorker().mediaEdit(token, media.getId(), "2");
                            popupWindow.dismiss();
                        }
                        break;
                }
            }
        });
        //编辑
        holder.compile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RqChangeMediaActivity.class);
                intent.putExtra("mediaId",media.getId());
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //批量操作
            case R.id.next_button:
                if (medias == null || medias.size() == 0)
                    return;
                if ("批量操作".equals(next_button.getText().toString())) {
                    closeType = "1";
                    next_button.setText("取消");
                    back_button.setVisibility(View.INVISIBLE);
                    select_layout.setVisibility(View.VISIBLE);
                    int m = 0;
                    int n = 0;
                    for (Media media : medias
                            ) {
                        if ("1".equals(media.getIsedit())) {
                            n++;
                            if (!media.isCheck()) {
                                m++;
                            }
                        }
                    }
                    if (m != 0)
                        all_buy_img.setImageResource((R.mipmap.search_all_on_img));
                    else
                        all_buy_img.setImageResource((R.mipmap.search_all_off_img));
                } else {
                    closeType = "0";
                    next_button.setText("批量操作");
                    back_button.setVisibility(View.VISIBLE);
                    select_layout.setVisibility(View.GONE);
                }
                freshData();
                break;
            //编辑
            case R.id.change:
                StringBuffer bufferid = new StringBuffer();
                for (Media media : medias) {
                    //可编辑的
                    if (media.getIsedit().equals("1")) {
                        if (media.isCheck())
                            bufferid.append(media.getId()+",");
                    }
                }
                if (bufferid==null || bufferid.length()==0)
                {
                    showTextDialog("请选择要编辑的媒体");
                    return;
                }
                Intent intent = new Intent(mContext,RqChangeMediaActivity.class);
                intent.putExtra("mediaId",bufferid.substring(0,bufferid.length()-1));
                startActivity(intent);
                break;
            case R.id.back_button:
                finish();
                break;
            case R.id.all_buy_img:
                if ("0".equals(all_or_no)) {
                    all_or_no = "1";
                    all_buy_img.setImageResource((R.mipmap.search_all_off_img));
                    for (Media media : medias
                            ) {
                        if ("1".equals(media.getIsedit()))
                            media.setCheck(true);
                    }
                } else {
                    all_or_no = "0";
                    all_buy_img.setImageResource((R.mipmap.search_all_on_img));
                    for (Media media : medias
                            ) {
                        if ("1".equals(media.getIsedit()))
                            media.setCheck(false);
                    }
                }
                freshData();
                break;
        }
    }

    //对全选的方法
    public void changeAll(String id) {
        int i = 0;
        int n = 0;
        for (Media media : medias) {
            //可编辑的
            if (media.getIsedit().equals("1")) {
                n++;
                if (media.isCheck())
                    i++;
            }
        }
        if (i == n) {
            all_or_no = "1";
            all_buy_img.setImageResource((R.mipmap.search_all_off_img));
        } else {
            all_or_no = "0";
            all_buy_img.setImageResource((R.mipmap.search_all_on_img));
        }
        freshData();
    }
}
