package com.hemaapp.xsm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.hemaapp.xsm.model.CitySan;
import com.hemaapp.xsm.model.Report;
import com.hemaapp.xsm.view.AreaDialog;
import com.hemaapp.xsm.view.BottomPopuWindowTimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lenovo on 2017/3/8.
 * 我要报备
 */
public class WantReportActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private EditText input_company_name;
    private EditText input_user_name;
    private EditText input_user_position;
    private LinearLayout layout_city;//选择投放区域
    private TextView input_city;
    private LinearLayout industry_layout;//选择行业
    private TextView select_industry;
    private LinearLayout number_layout;//选择数量
    private EditText input_number;
    private LinearLayout start_layout;//开始时间
    private TextView select_start_date;
    private LinearLayout over_layout;//结束时间
    private TextView select_over_date;
    private TextView login_text;//发布
    private AreaDialog areaDialog;
    private String province;//省
    private String city;//市
    private String area;//区
    private String district1;//省id
    private String district2;//市id
    private String district3;//区id
    BottomPopuWindowTimePicker selectTime;
    private int timeType = 1;
    private String industry;
    private String industry_text;
    private String reportId;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_want_report);
        super.onCreate(savedInstanceState);
//        if (JhctmApplication.getInstance().getCityInfo() == null)
//            getNetWorker().districtALLList();
        if (!"0".equals(reportId)) {
            String token = JhctmApplication.getInstance().getUser().getToken();
            getNetWorker().reportGet(token, reportId);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                industry = data.getStringExtra("industry");
                industry_text = data.getStringExtra("industry_text");
                select_industry.setText(industry_text);
                break;
            case 2:
                String cityName = data.getStringExtra("cityName");
                String cityId = data.getStringExtra("cityId");
                if (!isNull(cityName)) {
                    district2 = cityId.substring(0, cityId.length() - 1);
                    input_city.setText(cityName.substring(0, cityName.length() - 1));
                }
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                showProgressDialog("获取地区列表");
                break;
            case REPORT_SAVE:
                showProgressDialog("保存全国单信息");
                break;
            case REPORT_GET:
                showProgressDialog("获取信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
            case REPORT_SAVE:
            case REPORT_GET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                HemaArrayResult<CitySan> result1 = (HemaArrayResult<CitySan>) hemaBaseResult;
//                CitySan citySan = result1.getObjects().get(0);
//                ArrayList<CitySan> citySanArrayList = ;
//                CitySan citySan = new CitySan();
                //     ArrayList<CityChildren> cityChildrens= result1.getObjects();
                CitySan citySan = result1.getObjects().get(0);
                getApplicationContext().setCityInfo(citySan);
                break;
            case REPORT_SAVE:
                if ("0".equals(reportId))
                    showTextDialog("报备成功！");
                else
                    showTextDialog("修改报备成功！");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case REPORT_GET:
                HemaArrayResult<Report> result = (HemaArrayResult<Report>) hemaBaseResult;
                report = result.getObjects().get(0);
                setData();
                break;

        }
    }

    /**
     * 填充数据
     */
    private void setData() {
        //客户名称
        input_company_name.setText(report.getName());
        //联系人
        input_user_name.setText(report.getLinkname());
        //联系人职务
        input_user_position.setText(report.getLinkpost());
        //投放区域
        input_city.setText(report.getArea());
        //所属行业
        select_industry.setText(report.getIndustry_text());
        //投放数量
        input_number.setText(report.getPutnum());
        //\\开始时间
        select_start_date.setText(report.getStartdate());
        //结束时间
        select_over_date.setText(report.getEnddate());
        //区域id
        district2 = report.getDistrict();
        //领域id
        industry = report.getIndustry();
        //领域文字
        industry_text = report.getIndustry_text();
        //区名称
        city = report.getArea();

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
            case REPORT_GET:
            case REPORT_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                showTextDialog("获取地区列表失败，请稍后重试");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case REPORT_SAVE:
                showTextDialog("保存全国单信息失败，请稍后重试");
                break;
            case REPORT_GET:
                showTextDialog("获取信息失败，请稍后重试！");
                break;

        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        input_company_name = (EditText) findViewById(R.id.input_company_name);
        input_user_name = (EditText) findViewById(R.id.input_user_name);
        input_user_position = (EditText) findViewById(R.id.input_user_position);
        layout_city = (LinearLayout) findViewById(R.id.layout_city);
        input_city = (TextView) findViewById(R.id.input_city);
        industry_layout = (LinearLayout) findViewById(R.id.industry_layout);
        select_industry = (TextView) findViewById(R.id.select_industry);
        number_layout = (LinearLayout) findViewById(R.id.number_layout);
        input_number = (EditText) findViewById(R.id.input_number);
        start_layout = (LinearLayout) findViewById(R.id.start_layout);
        select_start_date = (TextView) findViewById(R.id.select_start_date);
        over_layout = (LinearLayout) findViewById(R.id.over_layout);
        select_over_date = (TextView) findViewById(R.id.select_over_date);
        login_text = (TextView) findViewById(R.id.login_text);
    }

    @Override
    protected void getExras() {
        reportId = mIntent.getStringExtra("reportId");
        if (isNull(reportId))
            reportId = "0";
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("我要报备");
        next_button.setVisibility(View.INVISIBLE);
        //发布
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = input_company_name.getText().toString().trim();
                String userName = input_user_name.getText().toString().trim();
                String userPosition = input_user_position.getText().toString().trim();
                String cityName = input_city.getText().toString().trim();
                String industry_text = select_industry.getText().toString().trim();
                String number = input_number.getText().toString().trim();
                String startTime = select_start_date.getText().toString().trim();
                String overTime = select_over_date.getText().toString().trim();
                if (isNull(companyName)) {
                    showTextDialog("请输入客户名称");
                    return;
                }
                if (isNull(userName)) {
                    showTextDialog("请输入联系人姓名");
                    return;
                }
                if (isNull(userPosition)) {
                    showTextDialog("请输入联系人职务");
                    return;
                }
                if (isNull(cityName)) {
                    showTextDialog("请选择投放区域");
                    return;
                }
                if (isNull(industry_text)) {
                    showTextDialog("请选择行业");
                    return;
                }
                if (isNull(number)) {
                    showTextDialog("请输入投放数量");
                    return;
                }
                if (isNull(startTime)) {
                    showTextDialog("请选择开始日期");
                    return;
                }
                if (isNull(overTime)) {
                    showTextDialog("请选择结束日期");
                    return;
                }
                SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy/MM/dd");
                Date beginTime = new Date();
                Date endTime = new Date();
                try {
                    beginTime = CurrentTime.parse(startTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    endTime = CurrentTime.parse(overTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (((endTime.getTime() - beginTime.getTime()) / (24 * 60 * 60 * 1000)) >= 0) {

                } else {
                    Toast.makeText(mContext, "下刊时间不能小于上刊时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                //接口发布全国单
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().reportSave(token, companyName, reportId, district2, industry, industry_text, number,
                        cityName, userName, userPosition, startTime, overTime, "1");
            }
        });
        //选择行业
        industry_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WantReportActivity.this, MyReportIndustryActivity.class);
                intent.putExtra("industryId", industry);
                startActivityForResult(intent, 1);
            }
        });
        //选择区域
        layout_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   showCity();
                Intent intent = new Intent(WantReportActivity.this, CityReportActivity.class);
                intent.putExtra("keytype", "2");
                startActivityForResult(intent, 2);
            }
        });
        //开始时间
        start_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                timeType = 1;
                showSelectTimeBottomPopu();
            }
        });
        //结束时间
        over_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                timeType = 2;
                showSelectTimeBottomPopu();
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
            input_city.setText(city);
            areaDialog.cancel();
        }
    }

    //选择时间
    public void showSelectTimeBottomPopu() {
        selectTime = new BottomPopuWindowTimePicker(mContext, popuWidnwTimePickerOnClick, 2017, 10);
        //显示窗口
        selectTime.showAtLocation(select_start_date, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private BottomPopuWindowTimePicker.OnBackClickForString popuWidnwTimePickerOnClick = new BottomPopuWindowTimePicker.OnBackClickForString() {
        @Override
        public void onBackResult(String string) {
            if (timeType == 1)
                select_start_date.setText(string);
            else
                select_over_date.setText(string);
        }
    };

}
