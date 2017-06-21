package com.hemaapp.xsm.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.MediaPriceAdapter;
import com.hemaapp.xsm.model.Price;
import com.hemaapp.xsm.model.PriceList;

import java.util.ArrayList;

import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/3/9
 * . 媒体价格
 * MediaPriceAdapter
 */
public class MediaPriceActivity extends JhActivity {
    private MediaPriceAdapter adapter;
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private LinearLayout layout_show;
    private TextView lianmen_title;
    private TextView chengjiao_title;
    private TextView kanli_title;
    private TextView city_name;
    private TextView lianmen_text;
    private TextView chengjiao_text;
    private TextView kanji_text;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private ExpandableListView expanded_menu;
    private ProgressBar progressbar;
    private ViewHodler hodler;
    private ArrayList<PriceList> priceLists = new ArrayList<>();
    private Price price = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_media_price);
        super.onCreate(savedInstanceState);
        inIt();
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().priceList(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRICE_LIST:
                showProgressDialog("获取价格列表");
                break;
            case PRICE_SAVE:
                showProgressDialog("修改价格保存");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRICE_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case PRICE_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRICE_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                HemaArrayResult<PriceList> result = (HemaArrayResult<PriceList>) hemaBaseResult;
                priceLists = result.getObjects();
                setData();
                break;
            case PRICE_SAVE:
                showTextDialog("修改价格成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inIt();
                    }
                }, 1000);

                break;
        }
    }

    //填充数据
    private void setData() {

        for (int i = 0; i < priceLists.size(); i++) {
            for (int j = 0; j < priceLists.get(i).getPrice().size(); j++) {
                if ("1".equals(priceLists.get(i).getPrice().get(j).getEdit())) {
                    price = priceLists.get(i).getPrice().get(j);
                }
            }
        }
        log_i("+++++++++++" + priceLists.size());
        log_i("_________");
        //填充数据
        if (price == null) {
            layout_show.setVisibility(View.GONE);
        } else {
            city_name.setText(price.getCity());
            lianmen_text.setText(price.getUnionprice());
            chengjiao_text.setText(price.getDealprice1() + "-" + price.getDealprice2());
            kanji_text.setText(price.getDemoprcie());
        }
        freshData();
    }

    private void freshData() {
        expanded_menu.setGroupIndicator(null);
        if (adapter == null) {
            adapter = new MediaPriceAdapter(mContext, priceLists);
            expanded_menu.setAdapter(adapter);
        } else {
            adapter.setPriceLists(priceLists);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRICE_LIST:
            case PRICE_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRICE_LIST:
                showTextDialog("获取媒体价格失败，请稍后重试");
                break;
            case PRICE_SAVE:
                showTextDialog("修改价格失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        layout_show = (LinearLayout) findViewById(R.id.layout_show);
        lianmen_title = (TextView) findViewById(R.id.lianmen_title);
        chengjiao_title = (TextView) findViewById(R.id.chengjiao_title);
        kanli_title = (TextView) findViewById(R.id.kanli_title);
        city_name = (TextView) findViewById(R.id.city_name);
        lianmen_text = (TextView) findViewById(R.id.lianmen_text);
        chengjiao_text = (TextView) findViewById(R.id.chengjiao_text);
        kanji_text = (TextView) findViewById(R.id.kanji_text);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        expanded_menu = (ExpandableListView) findViewById(R.id.expanded_menu);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finish();
            }
        });
        title_text.setText("媒体价格");
        next_button.setVisibility(View.INVISIBLE);
        //判断个人角色
        String feeaccount = JhctmApplication.getInstance().getUser().getFeeaccount();
        //普通用户
        if ("1".equals(feeaccount)) {
            lianmen_title.setVisibility(View.GONE);
            lianmen_text.setVisibility(View.GONE);
            chengjiao_title.setVisibility(View.GONE);
            chengjiao_text.setVisibility(View.GONE);
            layout_show.setVisibility(View.GONE);
            next_button.setVisibility(View.INVISIBLE);
        } else if ("2".equals(feeaccount)) {
            lianmen_title.setVisibility(View.VISIBLE);
            lianmen_text.setVisibility(View.VISIBLE);
            chengjiao_title.setVisibility(View.VISIBLE);
            chengjiao_text.setVisibility(View.VISIBLE);
            next_button.setVisibility(View.INVISIBLE);
        } else if ("3".equals(feeaccount)) {
            lianmen_title.setVisibility(View.GONE);
            lianmen_text.setVisibility(View.GONE);
            chengjiao_title.setVisibility(View.VISIBLE);
            chengjiao_text.setVisibility(View.VISIBLE);
            next_button.setText("编辑成交价");
            next_button.setVisibility(View.VISIBLE);
        } else {
            lianmen_title.setVisibility(View.GONE);
            lianmen_text.setVisibility(View.GONE);
            chengjiao_title.setVisibility(View.VISIBLE);
            chengjiao_text.setVisibility(View.VISIBLE);
            next_button.setVisibility(View.INVISIBLE);
        }
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {

            }
        });
        refreshLoadmoreLayout.setLoadmoreable(false);
        //编辑成交价
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showView();
            }
        });
    }

    //成交价
    private class ViewHodler {
        EditText input_min;//最小
        EditText input_max;//最大
        TextView login_text;//确定
    }

    private void showView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_price_section_view, null);
        hodler = new ViewHodler();
        hodler.input_min = (EditText) view.findViewById(R.id.input_min);
        hodler.input_max = (EditText) view.findViewById(R.id.input_max);
        hodler.login_text = (TextView) view.findViewById(R.id.login_text);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //提交
        hodler.login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dealprice1 = hodler.input_min.getText().toString().trim();
                String dealprice2 = hodler.input_max.getText().toString().trim();
                if (isNull(dealprice1)) {
                    showTextDialog("请填写最低价");
                    return;
                }
                if (isNull(dealprice2)) {
                    showTextDialog("请填写最高价");
                    return;
                }
                int min = Integer.valueOf(dealprice1);
                int max = Integer.valueOf(dealprice2);
                if (min > max) {
                    showTextDialog("最低价不能高于最高价");
                    return;
                }
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().priceSave(token, price.getId(), dealprice1, dealprice2);
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
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

}
