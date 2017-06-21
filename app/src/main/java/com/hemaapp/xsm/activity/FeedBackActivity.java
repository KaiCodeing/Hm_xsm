package com.hemaapp.xsm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;

/**
 * Created by lenovo on 2017/3/2.
 * 意见反馈
 */
public class FeedBackActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private EditText input_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feed_back);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("提交反馈意见中");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog("意见反馈成功");
        next_button.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("意见反馈失败，请稍后重试");
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        input_content = (EditText) findViewById(R.id.input_content);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        title_text.setText("意见反馈");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next_button.setText("提交");
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = input_content.getText().toString().trim();
                if (isNull(content))
                {
                    showTextDialog("请填写反馈意见");
                    return;
                }
                String token = JhctmApplication.getInstance().getUser().getToken();
                getNetWorker().adviseAdd(token,content);
            }
        });
    }
}
