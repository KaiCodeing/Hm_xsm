package com.hemaapp.xsm.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhFragment;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.MediaOnlyMapActivity;
import com.hemaapp.xsm.activity.RqLoactionResultActivity;
import com.hemaapp.xsm.activity.RqSearchResultActivity;
import com.hemaapp.xsm.model.CitySan;
import com.hemaapp.xsm.model.Media;
import com.hemaapp.xsm.view.AreaDialog;
import com.hemaapp.xsm.view.BottomPopuWindow2TimePicker;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/5/23.
 * 搜索右筛选
 */
public class SearchLiftFragment extends JhFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
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
    private String district1 = "";//省id
    private String district2 = "";//市id
    private String district3 = "";//区id
    BottomPopuWindow2TimePicker selectTime;
    private int timeType = 1;
    private String up_date1 = "";
    private String up_date2 = "";
    private String down_date1 = "";
    private String down_date2 = "";
    //状态
    private String status = "0";
    //限制内容
    private String limitcontent = "0";
    private RadioGroup group3;
    private RadioGroup group4;
    private EditText input_1_1;
    private EditText input_1_2;
    private EditText input_2_1;
    private EditText input_2_2;
    private EditText input_3_1;
    private EditText input_3_2;
    private TextView go_tel;
    private TextView go_loaction;
    private RadioButton button_4_1;
    private RadioButton button_4_2;
    private RadioButton button_4_3;
    private RadioButton button_4_4;
    private RadioButton button_4_5;
    private String typeLP = "0";//楼盘类型
    private RadioGroup group5;
    //调整点位标志
    private String loactionType = "";
    private RadioButton button_1;
    private RadioButton button_2;
    private RadioButton button_3;
    private RadioButton button_2_1;
    private RadioButton button_2_2;
    private RadioButton button_2_3;
    private RadioButton button_3_1;
    private RadioButton button_3_2;
    private RadioButton button_3_3;
    private ShowView showView;
    private String lat;
    private String lng;
    private String distance;
    private LinearLayout loaction_layout;//是否可调整点位
    private String keytype;
    private LinearLayout zs_layout;
    private LinearLayout user_hy_layout;
    private String cus_industry = "0";
    private RadioGroup group6;
    private LinearLayout xz_layout;
    private String typekey;
    private TextView text_lp;
    private RadioGroup group7;
    private RadioButton button_7_1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_lift_search);
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
                JhctmApplication.getInstance().setCityInfo(citySan);
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
                go_loaction.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void findView() {
        group1 = (RadioGroup) findViewById(R.id.group1);
        group2 = (RadioGroup) findViewById(R.id.group2);
        layout_city = (LinearLayout) findViewById(R.id.layout_city);
        input_city = (TextView) findViewById(R.id.input_city);
        number_layout = (LinearLayout) findViewById(R.id.number_layout);
        input_number = (TextView) findViewById(R.id.input_number);
        over_layout = (LinearLayout) findViewById(R.id.over_layout);
        over_time = (TextView) findViewById(R.id.over_time);
        empty_condition = (TextView) findViewById(R.id.empty_condition);
        group3 = (RadioGroup) findViewById(R.id.group3);
        group4 = (RadioGroup) findViewById(R.id.group4);
        input_1_1 = (EditText) findViewById(R.id.input_1_1);
        input_1_2 = (EditText) findViewById(R.id.input_1_2);
        input_2_1 = (EditText) findViewById(R.id.input_2_1);
        input_2_2 = (EditText) findViewById(R.id.input_2_2);
        input_3_1 = (EditText) findViewById(R.id.input_3_1);
        input_3_2 = (EditText) findViewById(R.id.input_3_2);
        go_tel = (TextView) findViewById(R.id.go_tel);
        go_loaction = (TextView) findViewById(R.id.go_loaction);
        button_4_1 = (RadioButton) findViewById(R.id.button_4_1);
        button_4_2 = (RadioButton) findViewById(R.id.button_4_2);
        button_4_3 = (RadioButton) findViewById(R.id.button_4_3);
        button_4_4 = (RadioButton) findViewById(R.id.button_4_4);
        button_4_5 = (RadioButton) findViewById(R.id.button_4_5);
        group5 = (RadioGroup) findViewById(R.id.group5);
        button_1 = (RadioButton) findViewById(R.id.button_1);
        button_2 = (RadioButton) findViewById(R.id.button_2);
        button_3 = (RadioButton) findViewById(R.id.button_3);
        button_2_1 = (RadioButton) findViewById(R.id.button_2_1);
        button_2_2 = (RadioButton) findViewById(R.id.button_2_2);
        button_2_3 = (RadioButton) findViewById(R.id.button_2_3);
        button_3_1 = (RadioButton) findViewById(R.id.button_3_1);
        button_3_2 = (RadioButton) findViewById(R.id.button_3_2);
        button_3_3 = (RadioButton) findViewById(R.id.button_3_3);
        loaction_layout = (LinearLayout) findViewById(R.id.loaction_layout);
        zs_layout = (LinearLayout) findViewById(R.id.zs_layout);
        user_hy_layout = (LinearLayout) findViewById(R.id.user_hy_layout);
        group6 = (RadioGroup) findViewById(R.id.group6);
        xz_layout = (LinearLayout) findViewById(R.id.xz_layout);
        text_lp = (TextView) findViewById(R.id.text_lp);
        group7 = (RadioGroup) findViewById(R.id.group7);
        button_7_1 = (RadioButton) findViewById(R.id.button_7_1);
    }

    @Override
    protected void setListener() {
        keytype = getActivity().getIntent().getStringExtra("keytype");
        String text = getActivity().getIntent().getStringExtra("text");
        if (!isNull(text))
            text_lp.setText("按投放楼盘类型");
        button_4_2.setOnCheckedChangeListener(this);
        button_4_1.setOnCheckedChangeListener(this);
        button_4_3.setOnCheckedChangeListener(this);
        button_4_5.setOnCheckedChangeListener(this);
        button_4_4.setOnCheckedChangeListener(this);
        button_1.setOnCheckedChangeListener(this);
        button_2.setOnCheckedChangeListener(this);
        button_3.setOnCheckedChangeListener(this);
        button_2_1.setOnCheckedChangeListener(this);
        button_2_2.setOnCheckedChangeListener(this);
        button_2_3.setOnCheckedChangeListener(this);
        button_3_1.setOnCheckedChangeListener(this);
        button_3_2.setOnCheckedChangeListener(this);
        button_3_3.setOnCheckedChangeListener(this);
        button_7_1.setOnCheckedChangeListener(this);
        go_tel.setOnClickListener(this);
        go_loaction.setOnClickListener(this);
        layout_city.setOnClickListener(this);
        number_layout.setOnClickListener(this);
        over_layout.setOnClickListener(this);
        //判断用户权限显示控件
        String userType = JhctmApplication.getInstance().getUser().getFeeaccount();
        if ("1".equals(userType)) {
            //普通用户
            loaction_layout.setVisibility(View.GONE);
        }
        if (!isNull(keytype)) {
            if ("1".equals(keytype)) {
                zs_layout.setVisibility(View.GONE);
                loaction_layout.setVisibility(View.GONE);
                number_layout.setVisibility(View.GONE);
                over_layout.setVisibility(View.GONE);
            } else {
                zs_layout.setVisibility(View.GONE);
                number_layout.setVisibility(View.GONE);
                over_layout.setVisibility(View.GONE);
                user_hy_layout.setVisibility(View.VISIBLE);
                xz_layout.setVisibility(View.GONE);
            }
        }

    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(getActivity(), "1");
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
            //清空地图信息
            lng = "";
            lat = "";
            distance = "";
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.button_4_1:
                group5.clearCheck();
                typeLP = "0";
                break;
            case R.id.button_4_2:
                group5.clearCheck();
                typeLP = "2";
                break;
            case R.id.button_4_3:
                group5.clearCheck();
                typeLP = "3";
                break;
            case R.id.button_4_4:
                group4.clearCheck();
                typeLP = "4";
                break;
            case R.id.button_4_5:
                group4.clearCheck();
                typeLP = "5";
                break;
            //状态全部
            case R.id.button_1:
                status = "0";
                break;
            //状态上刊
            case R.id.button_2:
                status = "1";
                break;
            //状态空档期
            case R.id.button_3:
                status = "2";
                break;
            //限制内容
            case R.id.button_2_1:
                group7.clearCheck();
                limitcontent = "1";
                break;
            //当地产
            case R.id.button_2_2:
                group7.clearCheck();
                limitcontent = "2";
                break;
            case R.id.button_2_3:
                group2.clearCheck();
                limitcontent = "3";
                break;
            case R.id.button_7_1:
                group7.clearCheck();
                limitcontent = "0";
                break;
            //是否可调整点位
            case R.id.button_3_1:
                loactionType = "0";
                break;
            case R.id.button_3_2:
                loactionType = "1";
                break;
            case R.id.button_3_3:
                loactionType = "2";
                break;
            //客户类型
            case R.id.button_6_1:
                cus_industry = "1";
                break;
            case R.id.button_6_2:
                cus_industry = "2";
                break;
            case R.id.button_6_3:
                cus_industry = "0";
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //确定
            case R.id.go_loaction:
                //判断各种状态
                //楼盘均价
                String priceMin = input_1_1.getText().toString();
                String priceMax = input_1_2.getText().toString();
                if (!isNull(priceMax) && !isNull(priceMin)) {
                    if (Integer.valueOf(priceMin) > Integer.valueOf(priceMax)) {
                        showTextDialog("楼盘均价不合理");
                        return;
                    }
                }
                //按覆盖人数
                String peopleMin = input_2_1.getText().toString();
                String peopleMax = input_2_2.getText().toString();
                if (!isNull(peopleMin) && !isNull(peopleMax)) {
                    if (Integer.valueOf(peopleMin) > Integer.valueOf(peopleMax)) {
                        showTextDialog("覆盖人数不合理");
                        return;
                    }
                }
                //车位数量
                String carMin = input_3_1.getText().toString();
                String carMax = input_3_2.getText().toString();
                if (!isNull(carMin) && !isNull(carMax)) {
                    if (Integer.valueOf(carMin) > Integer.valueOf(carMax)) {
                        showTextDialog("车位数量不合理");
                        return;
                    }
                }
                //判断各种字段数量  状态
                if (group1.getCheckedRadioButtonId() == R.id.button_1) {
                    status = "0";
                } else if (group1.getCheckedRadioButtonId() == R.id.button_2) {
                    status = "1";
                } else {
                    status = "2";
                }
                //限制内容
                if (group2.getCheckedRadioButtonId() == R.id.button_2_1)
                    limitcontent = "1";
                else if (group2.getCheckedRadioButtonId() == R.id.button_2_2)
                    limitcontent = "2";
                else if (group2.getCheckedRadioButtonId() == R.id.button_7_1)
                    limitcontent = "0";
                else
                    limitcontent = "3";
                //是否可调整点位
                if (group3.getCheckedRadioButtonId() == R.id.button_3_1)
                    loactionType = "";
                else if (group3.getCheckedRadioButtonId() == R.id.button_3_2)
                    loactionType = "1";
                else
                    loactionType = "2";
                //楼盘类型
                if (group4.getCheckedRadioButtonId() == R.id.button_4_1)
                    typeLP = "0";
                else if (group4.getCheckedRadioButtonId() == R.id.button_4_2)
                    typeLP = "2";
                else if (group4.getCheckedRadioButtonId() == R.id.button_4_3)
                    typeLP = "3";
                else if (group5.getCheckedRadioButtonId() == R.id.button_4_4)
                    typeLP = "4";
                else
                    typeLP = "5";
                //客户行业类型
                if (group6.getCheckedRadioButtonId() == R.id.button_6_1)
                    cus_industry = "1";
                else if (group6.getCheckedRadioButtonId() == R.id.button_6_2)
                    cus_industry = "2";
                else
                    cus_industry = "0";
                //没有搜索
                if ("1".equals(keytype)) {
                    loactionType = "";
                    cus_industry = "";
                } else if ("2".equals(keytype)) {
                    limitcontent = "1";
                }
                //传递参数
                Intent intent;
                if (isNull(keytype))
                    intent = new Intent(getActivity(), RqSearchResultActivity.class);
                else {
                    intent = new Intent(getActivity(), RqLoactionResultActivity.class);
                }
                intent.putExtra("keytype", keytype);
                intent.putExtra("status", status);
                intent.putExtra("district1", district1);
                intent.putExtra("district2", district2);
                intent.putExtra("district3", district3);
                intent.putExtra("limitcontent", limitcontent);
                intent.putExtra("change_flag", loactionType);
                intent.putExtra("up_date1", up_date1);
                intent.putExtra("up_date2", up_date2);
                intent.putExtra("down_date1", down_date1);
                intent.putExtra("down_date2", down_date2);
                intent.putExtra("house_type", typeLP);
                intent.putExtra("time_out", "0");
                intent.putExtra("house_pirce_min", priceMin);
                intent.putExtra("house_pirce_max", priceMax);
                intent.putExtra("people_num_min", peopleMin);
                intent.putExtra("people_num_max", peopleMax);
                intent.putExtra("park_num_min", carMin);
                intent.putExtra("park_num_max", carMax);
                intent.putExtra("lng", lng);
                intent.putExtra("lat", lat);
                intent.putExtra("range_val", distance);
                intent.putExtra("cus_industry", cus_industry);
                intent.putExtra("typekey", getActivity().getIntent().getStringExtra("typekey"));
                startActivityForResult(intent, 1);
                break;
            //清空
            case R.id.go_tel:
                group1.check(R.id.button_1);
                group2.check(R.id.button_7_1);
                group3.check(R.id.button_3_1);
                group4.check(R.id.button_4_1);
                group6.check(R.id.button_6_3);
                input_1_1.setText("");
                input_1_2.setText("");
                input_2_1.setText("");
                input_2_2.setText("");
                input_3_1.setText("");
                input_3_2.setText("");
                input_city.setText("");
                district1 = "";
                district2 = "";
                district3 = "";
                input_number.setText("");
                over_time.setText("");
                up_date1 = "";
                up_date2 = "";
                down_date1 = "";
                down_date2 = "";
                lat = "";
                lng = "";
                distance = "";
                break;
            //点位选择
            case R.id.layout_city:
                showQK();
                break;
            //上刊时间
            case R.id.number_layout:
                timeType = 1;
                showSelectTimeBottomPopu();
                break;
            //下刊时间
            case R.id.over_layout:
                timeType = 2;
                showSelectTimeBottomPopu();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 6:
                lat = data.getStringExtra("lat");
                lng = data.getStringExtra("lng");
                distance = data.getStringExtra("distance");
                input_city.setText(data.getStringExtra("address"));
                district1 = "";
                district2 = "";
                district3 = "";
                break;
            case 1:
                ArrayList<Media> medias = (ArrayList<Media>) data.getSerializableExtra("medias");
                Intent intent = new Intent();
                intent.putExtra("medias", medias);
                getActivity().setResult(getActivity().RESULT_OK, intent);
                getActivity().finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //选择时间
    public void showSelectTimeBottomPopu() {
        selectTime = new BottomPopuWindow2TimePicker(getContext(), popuWidnwTimePickerOnClick, 2017, 10);
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

    //清空
    private class ShowView {
        TextView camera_text;
        TextView album_text;
        TextView textView1_camera;
    }

    private void showQK() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popwindo_camera, null);
        showView = new ShowView();
        showView.camera_text = (TextView) view.findViewById(R.id.camera_text);
        showView.album_text = (TextView) view.findViewById(R.id.album_text);
        showView.textView1_camera = (TextView) view.findViewById(R.id.textView1_camera);
        showView.textView1_camera.setBackgroundResource(R.color.white);
        showView.textView1_camera.setTextColor(getResources().getColor(R.color.backgroud_title));
        showView.camera_text.setText("选择区域");
        showView.album_text.setText("地图选点");
        showView.textView1_camera.setText("取消");
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //选择区域
        showView.camera_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showCity();
            }
        });
        //地图选点
        showView.album_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MediaOnlyMapActivity.class);
                intent.putExtra("kkkk", "111");
                SearchLiftFragment.this.startActivityForResult(intent, 6);
                popupWindow.dismiss();
            }
        });
        //取消
        showView.textView1_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
