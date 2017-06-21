package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.SelectIndustryAdapter;
import com.hemaapp.xsm.model.CitySan;
import com.hemaapp.xsm.model.Industry;
import com.hemaapp.xsm.view.AreaDialog;
import com.hemaapp.xsm.view.BottomPopuWindow2TimePicker;
import com.hemaapp.xsm.view.JhctmGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lenovo on 2017/3/7.
 * 全国单筛选
 */
public class StateSelectActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private JhctmGridView gridview;
    private JhctmGridView gridview_type;
    private TextView user_position;//城市
    private LinearLayout city_layout;
    private LinearLayout start_layout;
    private TextView start_time;
    private TextView over_time;
    private LinearLayout over_layout;
    private ArrayList<Industry> industries = new ArrayList<>();
    private ArrayList<Industry> stats = new ArrayList<>();
    private SelectIndustryAdapter adapter;
    private SelectIndustryAdapter adapter2;
    private AreaDialog areaDialog;
    private String province = "";//省
    private String city = "";//市
    private String area = "";//区
    private String district1 = "";//省id
    private String district2 = "";//市id
    private String district3 = "";//区id
    private int timeType = 1;
    BottomPopuWindow2TimePicker selectTime;
    private String up_date1 = "";
    private String up_date2 = "";
    private String down_date1 = "";
    private String down_date2 = "";
    private TextView empty_condition;//清空
    private String industryId = "";
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_state_select);
        super.onCreate(savedInstanceState);
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().industryList(token);
        stats.add(new Industry("0", "全部", true));
        stats.add(new Industry("1", "已报备", false));
        stats.add(new Industry("2", "已发布", false));
        stats.add(new Industry("3", "已过期", false));
        if (JhctmApplication.getInstance().getCityInfo() == null)
            getNetWorker().districtALLList();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
                showProgressDialog("获取行业列表");

                break;
            case DISTRICT_LIST:
                showProgressDialog("获取地区列表");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
            case DISTRICT_LIST:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
                HemaArrayResult<Industry> result = (HemaArrayResult<Industry>) hemaBaseResult;
                industries = result.getObjects();
                industries.add(0, new Industry("0", "全部", true));
                if (industries == null || industries.size() == 0) {
                } else {
                    industries.get(0).setCheck(true);
                }
                freshData();
                break;
            case DISTRICT_LIST:
                HemaArrayResult<CitySan> result1 = (HemaArrayResult<CitySan>) hemaBaseResult;
//                CitySan citySan = result1.getObjects().get(0);
//                ArrayList<CitySan> citySanArrayList = ;
//                CitySan citySan = new CitySan();
                //     ArrayList<CityChildren> cityChildrens= result1.getObjects();
                CitySan citySan = result1.getObjects().get(0);
                getApplicationContext().setCityInfo(citySan);
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new SelectIndustryAdapter(mContext, industries);
            gridview.setAdapter(adapter);
        } else {
            adapter.setIndustries(industries);
            adapter.notifyDataSetChanged();
        }
        if (adapter2 == null) {
            adapter2 = new SelectIndustryAdapter(mContext, stats);
            gridview_type.setAdapter(adapter2);
        } else {
            adapter2.setIndustries(stats);
            adapter2.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
            case DISTRICT_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INDUSTRY_LIST:
                showTextDialog("获取行业列表失败，请稍后重试");
                break;
            case DISTRICT_LIST:
                showTextDialog("获取地区列表失败，请稍后重试");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        gridview = (JhctmGridView) findViewById(R.id.gridview);
        gridview_type = (JhctmGridView) findViewById(R.id.gridview_type);
        user_position = (TextView) findViewById(R.id.user_position);
        city_layout = (LinearLayout) findViewById(R.id.city_layout);
        start_layout = (LinearLayout) findViewById(R.id.start_layout);
        start_time = (TextView) findViewById(R.id.start_time);
        over_time = (TextView) findViewById(R.id.over_time);
        over_layout = (LinearLayout) findViewById(R.id.over_layout);
        empty_condition = (TextView) findViewById(R.id.empty_condition);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 1:
                String cityName = data.getStringExtra("name");
                district2 = data.getStringExtra("district2");
                user_position.setText(cityName);
                break;
        }
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("全国单筛选");
        next_button.setText("确定");
        //城市筛选
        city_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showCity();
                Intent intent = new Intent(StateSelectActivity.this,City0Activity.class);
                intent.putExtra("keytype","2");
                startActivityForResult(intent,1);
            }
        });
        //选择时间
        start_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeType = 1;
                showSelectTimeBottomPopu();
            }
        });
        //结束时间
        over_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeType = 2;
                showSelectTimeBottomPopu();
            }
        });
        //清空
        empty_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //行业
                for (int i = 0; i < industries.size(); i++) {
                    if (i == 0)
                        industries.get(i).setCheck(true);
                    else
                        industries.get(i).setCheck(false);
                }
                //状态
                for (int i = 0; i < stats.size(); i++) {
                    if (i == 0)
                        stats.get(i).setCheck(true);
                    else
                        stats.get(i).setCheck(false);
                }
                //
                user_position.setText("");
                city = "";
                district2 = "";
                start_time.setText("");
                up_date1 = "";
                up_date2 = "";
                over_time.setText("");
                down_date1 = "";
                down_date2 = "";
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });
        //确定
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //行业
                for (int i = 0; i < industries.size(); i++) {
                    if (industries.get(i).isCheck())
                        industryId = industries.get(i).getId();
                }
                //状态
                for (int i = 0; i < stats.size(); i++) {
                    if (stats.get(i).isCheck())
                        status = stats.get(i).getId();
                }
                SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy/MM/dd");
                Date beginTime = new Date();
                Date endTime = new Date();
                try {
                    beginTime = CurrentTime.parse(up_date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    endTime = CurrentTime.parse(down_date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (((endTime.getTime() - beginTime.getTime()) / (24 * 60 * 60 * 1000)) >= 0) {

                } else {
                    Toast.makeText(mContext, "下刊时间不能小于上刊时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                mIntent.putExtra("status", status);
                mIntent.putExtra("industryId", industryId);
                mIntent.putExtra("up_date1", up_date1);
                mIntent.putExtra("up_date2", up_date2);
                mIntent.putExtra("down_date1", down_date1);
                mIntent.putExtra("down_date2", down_date2);
                mIntent.putExtra("district2", district2);
                mIntent.putExtra("city", city);
                setResult(RESULT_OK, mIntent);
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                finish();
            }
        });
    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext);
//            areaDialog.closeSan();
            areaDialog.setButtonListener(new onbutton());
            return;
        }
        areaDialog.show();
    }

    private class onbutton implements com.hemaapp.xsm.view.AreaDialog.OnButtonListener {

        @Override
        public void onLeftButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub

            areaDialog.cancel();
        }


        @Override
        public void onRightButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub

//            homecity = home_text.getText().toString();
//            home_text.setTag(areaDialog.getId());
            String[] cityid = areaDialog.getId().split(",");
            String[] cityName = areaDialog.getCityName().split(" ");
            district1 = cityid[0];
            district2 = cityid[1];
            district3 = cityid[2];
            province = cityName[0];
            city = cityName[1];
            area = cityName[2];
            log_i("++" + province + " ---" + city + "==" + area);
            user_position.setText(city);
            areaDialog.cancel();
        }
    }

    //选择时间
    public void showSelectTimeBottomPopu() {
        selectTime = new BottomPopuWindow2TimePicker(mContext, popuWidnwTimePickerOnClick, 2017, 10);
        //显示窗口
        selectTime.showAtLocation(start_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private BottomPopuWindow2TimePicker.OnBackClickForString popuWidnwTimePickerOnClick = new BottomPopuWindow2TimePicker.OnBackClickForString() {
        @Override
        public void onBackResult(String start, String over) {
            if (timeType == 1) {
                start_time.setText(start + "--" + over);
                up_date1 = start;
                up_date2 = over;
            } else {
                over_time.setText(start + "--" + over);
                down_date1 = start;
                down_date2 = over;
            }

        }
    };

}
