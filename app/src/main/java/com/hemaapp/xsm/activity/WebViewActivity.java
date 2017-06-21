package com.hemaapp.xsm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.R;

/**
 * Created by lenovo on 2017/3/13.
 * 网页
 */
public class WebViewActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private HemaWebView webview_aboutwe;
    private String keytype;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webveiw);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        webview_aboutwe = (HemaWebView) findViewById(R.id.webview_aboutwe);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
        url = mIntent.getStringExtra("url");
        log_i("+++++"+url);
    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        String sys_web_service = getApplicationContext().getSysInitInfo()
                .getSys_web_service();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if ("1".equals(keytype)) {
            String path = sys_web_service + "webview/parm/protocal";
            webview_aboutwe.loadUrl(path);
            title_text.setText("注册协议");
            return;
        } else if ("2".equals(keytype)) {
//            webview_aboutwe.loadUrl(url);
            title_text.setText("预览");
            return;
        }
        else if("3".equals(keytype))
        {
            String path = sys_web_service + "webview/parm/aboutus";
            webview_aboutwe.loadUrl(path);
            title_text.setText("关于软件");
        }else if("4".equals(keytype))
        {
            String path = sys_web_service + "webview/parm/news_"+url;
            webview_aboutwe.loadUrl(path);
            title_text.setText("要闻详情");
        }
    }
}
