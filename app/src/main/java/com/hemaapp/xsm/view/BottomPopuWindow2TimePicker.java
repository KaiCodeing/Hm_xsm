package com.hemaapp.xsm.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.xsm.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 2017/3/17.
 */
public class BottomPopuWindow2TimePicker extends PopupWindow {


    private Context context;
    private Button topCancleButt;
    private Button topConfirmButt;
    private View mMenuView;
    private NumberPicker picker_day, picker_month, picker_year;
    private int startYear;//0代表从当前年份开始
    private int yearCount = 20;
    private String[] day_str;
    private String[] month_str;
    private String[] year_str;
    private int choose_day = 0, choose_month = 0, choose_year = 2016;
    private OnBackClickForString itemsOnClick;
    public String start_time;
    public String over_time;
    private TextView text;

    public BottomPopuWindow2TimePicker(Context context, OnBackClickForString itemsOnClick, int startYear, int yearCount) {
        super(context);
        this.context = context;
        Calendar a = Calendar.getInstance();
        this.itemsOnClick = itemsOnClick;
        this.startYear = startYear;
        this.startYear = a.get(Calendar.YEAR);
        this.yearCount = yearCount;
        findView();
        initPopu();
        setListerner();
        setData();
    }

    //初始化
    private void initPopu() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @SuppressLint("NewApi")
    private void findView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.bottom_popu_window_time_picker, null);
        picker_day = (NumberPicker) mMenuView.findViewById(R.id.number_picker_third);
        picker_month = (NumberPicker) mMenuView.findViewById(R.id.number_picker_second);
        picker_year = (NumberPicker) mMenuView.findViewById(R.id.number_picker_first);
        text = (TextView) mMenuView.findViewById(R.id.text);
        text.setVisibility(View.VISIBLE);
        ((EditText) picker_day.getChildAt(0)).setFocusable(false);
        ((EditText) picker_month.getChildAt(0)).setFocusable(false);
        ((EditText) picker_year.getChildAt(0)).setFocusable(false);
        picker_day.setWrapSelectorWheel(true);
        picker_month.setWrapSelectorWheel(true);
        picker_year.setWrapSelectorWheel(true);
        topCancleButt = (Button) mMenuView.findViewById(R.id.top_cancle_butt);
        topConfirmButt = (Button) mMenuView.findViewById(R.id.top_confirm_butt);
        topConfirmButt.setText("下一步");
    }

    @SuppressLint("NewApi")
    private void setListerner() {
        //设置按钮监听
        topConfirmButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                itemsOnClick.onBackResult(choose_year + "/" + (choose_month < 10 ? "0" + choose_month : choose_month) + "/" + choose_day);
//                dismiss();
                if (text.getText().toString().equals("选择起始日期")) {
                    start_time = choose_year + "/" + (choose_month < 10 ? "0" + choose_month : choose_month) + "/" + choose_day;
                    text.setText("选择终止日期");
                    topConfirmButt.setText("完成");
                } else {
                    over_time = choose_year + "/" + (choose_month < 10 ? "0" + choose_month : choose_month) + "/" + choose_day;
                    SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy/MM/dd");
                    Date beginTime = new Date();
                    Date endTime = new Date();
                    try {
                        beginTime = CurrentTime.parse(start_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        endTime = CurrentTime.parse(over_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (((endTime.getTime() - beginTime.getTime()) / (24 * 60 * 60 * 1000)) >= 0) {
                        itemsOnClick.onBackResult(start_time, over_time);
                        dismiss();
                    } else {
                        Toast.makeText(context, "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        topCancleButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     *
     */
    public abstract interface OnBackClickForString {
        public abstract void onBackResult(String start_time, String over_time);
    }

    //设置数据
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setData() {
        Calendar calendar = Calendar.getInstance();
        int nowmonth = calendar.get(Calendar.MONTH) + 1;
        int nowday = calendar.get(Calendar.DAY_OF_MONTH);
        choose_year = startYear;
        choose_month = nowmonth;
        choose_day = nowday;
        year_str = getYearStringArray();
        picker_year.setDisplayedValues(year_str);
        picker_year.setMinValue(0);
        picker_year.setMaxValue(year_str.length - 1);
        picker_year.setValue(0);
        picker_year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                choose_year = Integer.parseInt(year_str[newVal].substring(0, 4));
                day_str = null;
                day_str = getDayStringArray(choose_month, choose_year);
                if (picker_day.getMaxValue() > day_str.length - 1) {
                    picker_day.setMaxValue(day_str.length - 1);
                }
                picker_day.setDisplayedValues(day_str);
                picker_day.setMinValue(0);
                picker_day.setMaxValue(day_str.length - 1);
                if (choose_day > day_str.length) {
                    picker_day.setValue(day_str.length - 1);
                    choose_day = day_str.length;
                } else {
                    picker_day.setValue(choose_day - 1);
                }
            }
        });

        month_str = new String[]{"01月", "02月", "03月", "04月", "05月", "06月", "07月", "08月", "09月", "10月", "11月", "12月"};
        picker_month.setDisplayedValues(month_str);
        picker_month.setMinValue(0);
        picker_month.setMaxValue(month_str.length - 1);
        picker_month.setValue(nowmonth - 1);
        picker_month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                choose_month = newVal + 1;
                day_str = getDayStringArray(choose_month, choose_year);
                if (picker_day.getMaxValue() > day_str.length - 1) {
                    picker_day.setMaxValue(day_str.length - 1);
                }
                picker_day.setDisplayedValues(day_str);
                picker_day.setMaxValue(day_str.length - 1);
                picker_day.setMinValue(0);

                if (choose_day > day_str.length) {
                    picker_day.setValue(day_str.length - 1);
                    choose_day = day_str.length;
                } else {
                    picker_day.setValue(choose_day - 1);
                }
            }
        });

        day_str = getDayStringArray(nowmonth, 1988);
        picker_day.setDisplayedValues(day_str);
        picker_day.setMinValue(0);
        picker_day.setMaxValue(day_str.length - 1);
        if (nowday > day_str.length) {
            picker_day.setValue(day_str.length - 1);
        } else {
            picker_day.setValue(nowday - 1);
        }
        picker_day.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                choose_day = newVal + 1;
            }
        });
    }

    public String[] getStringArray(int num, String str) {
        String[] new_arry = new String[num];
        for (int i = 0; i < num; i++) {
            if (i < 10) {
                new_arry[i] = "0" + i + " " + str;
            } else {
                new_arry[i] = i + " " + str;
            }
        }
        return new_arry;
    }

    public String[] getDayStringArray(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        int nowMothDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数

        String[] new_arry = new String[nowMothDays];
        for (int i = 0; i < nowMothDays; i++) {
            if (i < 9)
                new_arry[i] = "0" + (i + 1) + "日";
            else
                new_arry[i] = (i + 1) + "日";
        }
        return new_arry;
    }

    public String[] getYearStringArray() {
        String[] new_arry = new String[yearCount];
        if (startYear != 0) {
            int m =yearCount/2;
            for (int i = 0; i < yearCount; i++) {
                if (i < yearCount / 2)
                    new_arry[i] = (startYear + i) + "年";
                else
                {
                    new_arry[i] = (startYear - m) + "年";
                    m--;
                }
            }
        } else {
            for (int i = 0; i < yearCount; i++) {
                new_arry[i] = (choose_year + i) + "年";
            }
        }
        return new_arry;
    }
}
