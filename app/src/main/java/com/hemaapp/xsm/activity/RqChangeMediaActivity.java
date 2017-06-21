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
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.view.BottomPopuWindowTimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 2017/5/31.
 * //二期编辑媒体信息
 */
public class RqChangeMediaActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private EditText input_city;//输入用户名称
    private TextView select_start_date;//上刊日期
    private TextView select_over_date;//下刊日期
    private LinearLayout over_layout;//
    private LinearLayout start_layout;//
    private TextView login_text;
    private String mediaId;
    private int timeType = 1;
    BottomPopuWindowTimePicker selectTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rq_change_media);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("修改媒体信息...");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog("修改媒体信息成功");
        back_button.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("修改媒体信息失败，请稍后重试");
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        input_city = (EditText) findViewById(R.id.input_city);
        select_start_date = (TextView) findViewById(R.id.select_start_date);
        select_over_date = (TextView) findViewById(R.id.select_over_date);
        over_layout = (LinearLayout) findViewById(R.id.over_layout);
        start_layout = (LinearLayout) findViewById(R.id.start_layout);
        login_text = (TextView) findViewById(R.id.login_text);
    }

    @Override
    protected void getExras() {
        mediaId = mIntent.getStringExtra("mediaId");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("编辑");
        next_button.setVisibility(View.INVISIBLE);
        //上刊日期
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
        //下刊日期
        over_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                timeType = 0;
                showSelectTimeBottomPopu();
            }
        });
        //发布
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                String companyName = input_city.getText().toString();
                String up_date = select_start_date.getText().toString();
                String down_date = select_over_date.getText().toString();
                Date beginTime = new Date();
                Date endTime = new Date();
                if (isNull(companyName)) {
                    showTextDialog("请输入客户名称");
                    return;
                }
                if (isNull(up_date) || isNull(down_date)) {
                    showTextDialog("请选择时间");
                    return;
                }
                SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    beginTime = CurrentTime.parse(up_date);
                    endTime = CurrentTime.parse(down_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (((endTime.getTime() - beginTime.getTime()) / (24 * 60 * 60 * 1000)) >= 0) {
                } else {
                    Toast.makeText(mContext, "下刊时间不能小于上刊时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                getNetWorker().mediaSave(token, mediaId, up_date, down_date, companyName, "", "");
            }
        });
    }

    //选择时间
    public void showSelectTimeBottomPopu() {
        Calendar a = Calendar.getInstance();
        //  System.out.println(a.get(Calendar.YEAR));//得到年
        int data = a.get(Calendar.YEAR);
        selectTime = new BottomPopuWindowTimePicker(mContext, popuWidnwTimePickerOnClick, data, 10);
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
