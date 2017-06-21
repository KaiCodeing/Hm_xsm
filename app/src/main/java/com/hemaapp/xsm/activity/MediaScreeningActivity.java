package com.hemaapp.xsm.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.hemaapp.xsm.view.AreaDialog;
import com.hemaapp.xsm.view.BottomPopuWindow2TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lenovo on 2017/3/9.
 * 媒体筛选
 */
public class MediaScreeningActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private RadioGroup group1;
    private RadioGroup group2;
    private LinearLayout layout_city;//区域筛选
    private TextView input_city;
    private LinearLayout number_layout;//选择上刊日期
    private TextView input_number;
    private LinearLayout over_layout;//下刊日期
    private TextView over_time;
    private TextView empty_condition;//清空
    private AreaDialog areaDialog;
    private String province;//省
    private String city;//市
    private String area;//区
    private String district1="";//省id
    private String district2="";//市id
    private String district3="";//区id
    BottomPopuWindow2TimePicker selectTime;
    private int timeType = 1;
    private String up_date1="";
    private String up_date2="";
    private String down_date1="";
    private String down_date2="";
    //状态
    private String status="0";
    //限制内容
    private String limitcontent="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_screening_media);
        super.onCreate(savedInstanceState);
        if (JhctmApplication.getInstance().getCityInfo() == null)
            getNetWorker().districtALLList();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                showProgressDialog("获取地区列表");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
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
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:

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

        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        group1 = (RadioGroup) findViewById(R.id.group1);
        group2 = (RadioGroup) findViewById(R.id.group2);
        layout_city = (LinearLayout) findViewById(R.id.layout_city);
        input_city = (TextView) findViewById(R.id.input_city);
        number_layout = (LinearLayout) findViewById(R.id.number_layout);
        input_number = (TextView) findViewById(R.id.input_number);
        over_layout = (LinearLayout) findViewById(R.id.over_layout);
        over_time = (TextView) findViewById(R.id.over_time);
        empty_condition = (TextView) findViewById(R.id.empty_condition);
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
        title_text.setText("媒体筛选");
        next_button.setText("确定");

        layout_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCity();
            }
        });
        //选择上刊日期
        number_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeType = 1;
                showSelectTimeBottomPopu();
            }
        });
        //选择下刊日期
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
                group1.check(R.id.button_1);
                status="0";
                group2.check(R.id.button_2_1);
                limitcontent="0";
                input_city.setText("");
                district1 ="";
                district2 = "";
                district3 = "";
                province = "";
                city = "";
                area = "";
                input_number.setText("");
                up_date1="";
                up_date2="";
                over_time.setText("");
                down_date1="";
                down_date2="";
            }
        });
        //确定
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (group1.getCheckedRadioButtonId()==R.id.button_1)
                    status="0";
                else if(group1.getCheckedRadioButtonId()==R.id.button_2)
                    status="1";
                else
                    status="2";
                if (group2.getCheckedRadioButtonId()==R.id.button_2_1)
                    limitcontent ="0";
                else if(group2.getCheckedRadioButtonId()==R.id.button_2_2)
                    limitcontent ="1";
                else
                    limitcontent="2";
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
                    if(isNull(up_date1))
                    {}
                    else {
                        Toast.makeText(mContext, "下刊时间不能小于上刊时间", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                mIntent.putExtra("status",status);
                mIntent.putExtra("limitcontent",limitcontent);

                mIntent.putExtra("up_date1",up_date1);
                mIntent.putExtra("up_date2",up_date2);
                mIntent.putExtra("down_date1",down_date1);
                mIntent.putExtra("down_date2",down_date2);
                mIntent.putExtra("district1",district1);
                mIntent.putExtra("district2",district2);
                mIntent.putExtra("district3",district3);
                setResult(RESULT_OK, mIntent);
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                finish();
            }
        });
    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext,"1");
            areaDialog.setType();
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
            input_city.setText(areaDialog.getCityName());
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
            areaDialog.cancel();
        }
    }

    //选择时间
    public void showSelectTimeBottomPopu() {
        selectTime = new BottomPopuWindow2TimePicker(mContext, popuWidnwTimePickerOnClick, 2017,10);
        //显示窗口
        selectTime.showAtLocation(number_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private BottomPopuWindow2TimePicker.OnBackClickForString popuWidnwTimePickerOnClick = new BottomPopuWindow2TimePicker.OnBackClickForString() {
        @Override
        public void onBackResult(String start, String over) {
            if (timeType == 1) {
                input_number.setText(start + "--" + over);
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
