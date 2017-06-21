package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.hemaapp.xsm.adapter.RQMedia2Adapter;
import com.hemaapp.xsm.model.Media;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/3/8.
 * 媒体档期
 */
public class MediaActivity extends JhActivity {
    private ImageButton back_button;
    private TextView search_input;
    private ImageButton next_button;
    private Button sx_button;
    private Button price_button;
    private Button loction_button;
    private XtomListView listview;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ProgressBar progressbar;
    private RQMedia2Adapter adapter;
    private ViewHolder holder;
    private String status = "0";//展示状态 =0 全部=1 上刊=2 空档期
    private String limitcontent = "0";//限制内容 =0 全部=1 房地产=2 医疗
    private String district1 = "";
    private String district2 = "";
    private String district3 = "";
    private String up_date1 = "";//上刊开始
    private String up_date2 = "";//上刊结束
    private String down_date1 = "";//下刊开始
    private String down_date2 = "";//下刊结束
    private Integer page = 0;
    private ArrayList<Media> medias = new ArrayList<>();
    private RadioGroup radiogroup;
    private String type = "0";
    private Button yd_button;
    private ViewHolder1 holder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_media);
        super.onCreate(savedInstanceState);
        inIt();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        page = 0;
//        inIt();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 1:
                status = data.getStringExtra("status");
                district1 = data.getStringExtra("district1");
                if (!isNull(data.getStringExtra(district2)))
                    district2 = data.getStringExtra("district2");
                district3 = data.getStringExtra("district3");
                limitcontent = data.getStringExtra("limitcontent");
                up_date1 = data.getStringExtra("up_date1");
                up_date2 = data.getStringExtra("up_date2");
                down_date1 = data.getStringExtra("down_date1");
                down_date2 = data.getStringExtra("down_date2");
                page = 0;
                break;
            case 12:
            case 9:
                page = 0;
                inIt();
                break;
        }
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
//        if (isNull(district2))
//            district2 = XtomSharedPreferencesUtil.get(mContext,"cityId");
//        getNetWorker().mediaList(token,"1","",status,district1,district2,district3,limitcontent,up_date1,
//                up_date2,down_date1,down_date2,String.valueOf(page));
        getNetWorker().mediaList(token, "1", "", "", status, "", XtomSharedPreferencesUtil.get(mContext, "cityId"), "", "0", "", "", type, "", "", "", "", "", "", "", "", "", "", "", "", "", "", String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                showProgressDialog("获取媒体列表");
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
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                freshData();
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
            adapter = new RQMedia2Adapter(MediaActivity.this, medias, null);
            adapter.setEmptyString("暂无媒体信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无媒体信息");
            adapter.setMedias(medias);
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
                showTextDialog("获取媒体列表失败，请稍后重试");
                break;
            case MEDIA_EDIT:
                showTextDialog("操作失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        search_input = (TextView) findViewById(R.id.search_input);
        next_button = (ImageButton) findViewById(R.id.next_button);
        sx_button = (Button) findViewById(R.id.sx_button);
        price_button = (Button) findViewById(R.id.price_button);
        listview = (XtomListView) findViewById(R.id.listview);
        loction_button = (Button) findViewById(R.id.loction_button);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        yd_button = (Button) findViewById(R.id.yd_button);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        //判断用户权限
        //显示相应的按键
        String userP = JhctmApplication.getInstance().getUser().getFeeaccount();
        //CEO
        if ("2".equals(userP)) {
            next_button.setVisibility(View.INVISIBLE);
        }
        //普通用户 隐藏选择时间，和点位预定点位管理
        else if ("1".equals(userP)) {
            radiogroup.setVisibility(View.GONE);
            yd_button.setVisibility(View.GONE);
            sx_button.setVisibility(View.GONE);
            next_button.setVisibility(View.INVISIBLE);
        }
        //公司媒介 隐藏点位预定
        else if ("3".equals(userP)) {
            yd_button.setVisibility(View.GONE);
        }
        //其他
        else {
            radiogroup.setVisibility(View.GONE);
            next_button.setVisibility(View.INVISIBLE);
        }
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索
        search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RQSearchActivity.class);
//                intent.putExtra("keytype", "1");
                startActivity(intent);
            }
        });
        //发布
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPop();
                Intent intent = new Intent(MediaActivity.this, LoactionReserveActivity.class);
                intent.putExtra("keytype", "1");
                startActivityForResult(intent, 12);
            }
        });
        //点位管理
        sx_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaActivity.this, PTManageActivity.class);

                startActivity(intent);
            }
        });
        //价格
        price_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaActivity.this, MediaPriceActivity.class);
                startActivity(intent);
            }
        });
        //地址
        loction_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaActivity.this, MediaOnlyMapActivity.class);
                intent.putExtra("keytype", "2");
                //    intent.putExtra("media",medias);
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
        //判断个人状态
        if (JhctmApplication.getInstance().getUser().getFeeaccount().equals("3"))
            next_button.setVisibility(View.VISIBLE);
        else
            next_button.setVisibility(View.INVISIBLE);
        //选择媒体类型
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_1:
                        type = "0";
                        status = "0";
                        page = 0;
                        inIt();
                        break;
                    case R.id.radio_2:
                        type = "1";
                        status = "1";
                        page = 0;
                        inIt();
                        break;
                    case R.id.radio_3:
                        type = "2";
                        status = "1";
                        page = 0;
                        inIt();
                        break;
                    case R.id.radio_4:
                        type = "0";
                        status = "2";
                        page = 0;
                        inIt();
                        break;
                }
            }
        });
        //点位预定
        yd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoactionReserveActivity.class);
                startActivity(intent);
            }
        });
    }

    private class ViewHolder {
        TextView issue_reput;
        TextView my_reput;
    }

    //展示图片
    private void showPop() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.meida_issue_view, null);
        holder = new ViewHolder();
        holder.issue_reput = (TextView) view.findViewById(R.id.issue_reput);
        holder.my_reput = (TextView) view.findViewById(R.id.my_reput);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        //媒体发布
        holder.issue_reput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaActivity.this, MediaIssueActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        //我发布的
        holder.my_reput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(MediaActivity.this, MyIssueActivity.class);
                startActivity(intent);
            }
        });

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAsDropDown(findViewById(R.id.next_button));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
    }

    //窗口
    private class ViewHolder1 {
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
        holder1 = new ViewHolder1();
        holder1.group_1 = (RadioGroup) view.findViewById(R.id.group_1);
        holder1.compile_text = (TextView) view.findViewById(R.id.compile_text);
        holder1.radiobutton_1 = (RadioButton) view.findViewById(R.id.radiobutton_1);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        if ("2".equals(media.getStatus()))
            holder1.radiobutton_1.setChecked(true);
        else
            holder1.radiobutton_1.setChecked(false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        holder1.group_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        holder1.compile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RqChangeMediaActivity.class);
                intent.putExtra("mediaId", media.getId());
                startActivityForResult(intent,9);
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

}
