package com.hemaapp.xsm.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhUpGrade;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.ConReport;
import com.hemaapp.xsm.model.SysInitInfo;

import java.util.ArrayList;

import xtom.frame.XtomActivityManager;

/**
 * Created by lenovo on 2017/5/17.
 * 版本升级activity
 */
public class UpLoadActivity extends JhActivity implements View.OnClickListener {
    private ImageView colos_img;
    private LinearLayout add_text;
    private TextView download_now;
    private JhUpGrade upGrade;
    private ArrayList<ConReport> conReports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_upload);
        super.onCreate(savedInstanceState);
        upGrade = new JhUpGrade(this);
        getNetWorker().upDateInfor();
        ;
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("获取更新内容");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        HemaArrayResult<ConReport> result = (HemaArrayResult<ConReport>) hemaBaseResult;
        conReports = result.getObjects();
        setData();
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("获取失败");
    }

    /**
     * 填充更新数据
     */
    private void setData() {
        if (conReports != null || conReports.size() != 0) {
            add_text.removeAllViews();
            for (int i = 0; i < conReports.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.upload_new_text,null);
                TextView textView = (TextView) view.findViewById(R.id.newtext);
              //  textView.setText(String.valueOf(i+1)+". "+conReports.get(i).getContent());
                textView.setText(conReports.get(i).getContent());
                add_text.addView(view);
            }
        }
    }

    @Override
    protected void findView() {
        colos_img = (ImageView) findViewById(R.id.colos_img);
        add_text = (LinearLayout) findViewById(R.id.add_text);
        download_now = (TextView) findViewById(R.id.download_now);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        colos_img.setOnClickListener(this);
        download_now.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.colos_img:
                log_i("点击了-----------------");
                XtomActivityManager.finishAll();
                break;
            //添加文字
            case R.id.add_text:
                break;
            //立即下载
            case R.id.download_now:
                log_i("点击了++++++");
                SysInitInfo sysInitInfo = JhctmApplication.getInstance().getSysInitInfo();
                upGrade.upGrade(sysInitInfo);
                finish();
                break;
        }
    }
}
