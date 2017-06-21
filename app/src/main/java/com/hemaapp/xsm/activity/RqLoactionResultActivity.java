package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.PTAdapter;
import com.hemaapp.xsm.model.Media;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/6/6.
 * 搜索点位的列表
 */
public class RqLoactionResultActivity extends JhActivity {
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
    private String closeType = "0";
    private String cus_industry;
    private String keytype;
    private PTAdapter adapter;
    private String typekey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rq_search_result);
        super.onCreate(savedInstanceState);
        inIt();
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
//        getNetWorker().pointList(token, "1", keyword, status, district1, district2, district3, limitcontent, change_flag, house_type,
//                time_out, house_pirce_min, house_pirce_max, people_num_min, people_num_max, park_num_min, park_num_max, lng, lat, range_val, up_date1, up_date2, down_date1,
//                down_date2, cus_industry, String.valueOf(page));
        getNetWorker().mediaList(token,typekey,keyword,"",status,district1,district2,district3,limitcontent,
                change_flag,house_type,time_out,house_pirce_min,house_pirce_max,people_num_min,people_num_max,
                park_num_min,park_num_max,lng,lat,range_val,up_date1,up_date2,down_date1,down_date2,cus_industry,String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("获取点位列表");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
        refreshLoadmoreLayout.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
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

    }

    private void freshData() {
        if (adapter == null) {
            adapter = new PTAdapter(mContext, medias, keytype);
            adapter.setEmptyString("暂无点位信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无点位信息");
            adapter.setMedias(medias);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("获取失败，请稍后重试");
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
        cus_industry = mIntent.getStringExtra("cus_industry");
        if (isNull(cus_industry))
            cus_industry = "";
        keytype = mIntent.getStringExtra("keytype");
        typekey = mIntent.getStringExtra("typekey");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("搜索结果");
        if ("1".equals(keytype))
            next_button.setVisibility(View.INVISIBLE);
        else
            next_button.setText("确定");
        //确定
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medias == null || medias.size() == 0)
                    finish();
                else {
                    int i = 0;
                    for (Media media:medias)
                    {
                        if (media.isCheck())
                            i++;
                    }
                    if (i==0)
                    {
                        showTextDialog("请选择点位");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("medias", medias);
                    setResult(RESULT_OK, intent);
                    finish();
                }
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
}
