package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.R;

/**
 * Created by lenovo on 2017/2/27.
 * 注册，找回密码
 */
public class FindPwdActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private EditText username_text_add;
    private EditText yanzheng_text;
    private Button send_button;
    private LinearLayout agin_layout;
    private TextView second;
    private String keytype;//null  注册  非空 找回密码
    private TimeThread timeThread;
    private TextView phone_show;
    private LinearLayout layout_add_user;
    private String username;
    private TextView login_text;
    private CheckBox check_zu;
    private TextView xieyi_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_findpwd);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showProgressDialog("正在验证手机号");
                break;
            case CODE_GET:
                showProgressDialog("正在发送验证码");
                break;
            case CODE_VERIFY:
                showProgressDialog("正在验证验证码");
                break;
            case PASSWORD_RESET:
                showProgressDialog("正在修改密码");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
            case CODE_GET:
            case CODE_VERIFY:
            case PASSWORD_RESET:
                cancelProgressDialog();
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                if (isNull(keytype)) {
                    showTextDialog("手机号已被注册！");
                } else {
                    String username = hemaNetTask.getParams().get("username");
                    getNetWorker().codeGet(username,"2");
                }
                break;
            case CODE_GET:
                this.username = hemaNetTask.getParams().get("username");
                phone_show.setText("验证码已发送到 "
                        + HemaUtil.hide(hemaNetTask.getParams().get("username"), "1"));
                phone_show.setVisibility(View.VISIBLE);

                timeThread = new TimeThread(new TimeHandler(this));
                timeThread.start();
                break;
            case CODE_VERIFY:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String temp_token = result.getObjects().get(0);
                log_i("接口返回的token++" + temp_token);
                String userName = username_text_add.getText().toString();
                if (isNull(this.username)) {
                    showTextDialog("请填写手机号");
                    return;
                }
                if (this.username.equals(userName)) {
                } else {
                    showTextDialog("两次输入手机号码不一致，\n请确认");
                    return;
                }
                Intent intent = new Intent(FindPwdActivity.this,SetPasswordActivity.class);
                intent.putExtra("temp_token",temp_token);
                intent.putExtra("code",yanzheng_text.getText().toString());
                intent.putExtra("username",userName);
                intent.putExtra("keytype",keytype);
                startActivity(intent);
                break;

            default:

                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                if (isNull(keytype))
                {
                    String username = hemaNetTask.getParams().get("username");
                    getNetWorker().codeGet(username,"1");
                }
                else
                showTextDialog("手机号未注册！");
                break;
            case CODE_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case CODE_VERIFY:
                if (hemaBaseResult.getError_code() == 103) {
                    showTextDialog("输入的验证码不正确！");
                } else {
                    showTextDialog(hemaBaseResult.getMsg());
                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showTextDialog("验证手机号失败");
                break;
            case CODE_GET:
                showTextDialog("发送验证码失败");
                break;
            case CODE_VERIFY:
                showTextDialog("校验验证码失败");
                break;
            case PASSWORD_RESET:
                showTextDialog("修改密码失败，请稍后重试");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        username_text_add = (EditText) findViewById(R.id.username_text_add);
        yanzheng_text = (EditText) findViewById(R.id.yanzheng_text);
        send_button = (Button) findViewById(R.id.send_button);
        agin_layout = (LinearLayout) findViewById(R.id.agin_layout);
        second = (TextView) findViewById(R.id.second);
        phone_show = (TextView) findViewById(R.id.phone_show);
        layout_add_user = (LinearLayout) findViewById(R.id.layout_add_user);
        login_text = (TextView) findViewById(R.id.login_text);
        check_zu = (CheckBox) findViewById(R.id.check_zu);
        xieyi_text = (TextView) findViewById(R.id.xieyi_text);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        /**
         * 回退
         */
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        if (isNull(keytype)) {
            title_text.setText("注册");

        } else {
            title_text.setText("忘记密码");
            phone_show.setVisibility(View.GONE);
            layout_add_user.setVisibility(View.GONE);
        }
        next_button.setVisibility(View.INVISIBLE);
        /**
         * 发送验证码
         */
        send_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = username_text_add.getText().toString();
                if (isNull(username)) {
                    showTextDialog("请输入手机号");
                    return;
                }
                // String mobile = "^[1][3-8]+\\d{9}";
                String mobile = "\\d{11}";// 只判断11位
                if (!username.matches(mobile)) {
                    showTextDialog("您输入的手机号不正确");
                    return;
                }
                getNetWorker().clientVerify(username);
                // if ("1".equals(type)) {
                // getNetWorker().codeGet(username);
                // }
                // else if ("2".equals(type)) {
                //
                // }

            }
        });
        //y验证验证码
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNull(username_text_add.getText().toString())) {
                    showTextDialog("请先验证手机号");
                    return;
                }
                if (isNull(yanzheng_text.getText().toString())) {
                    showTextDialog("输入的验证码不能为空!");
                    return;
                }
                if (!check_zu.isChecked()) {
                    showTextDialog("请同意注册说明");
                    return;
                }
                String codeString = yanzheng_text.getText().toString();
                getNetWorker().codeVerify(username_text_add.getText().toString(),
                        codeString);
            }
        });
        //注册协议
        xieyi_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPwdActivity.this,WebViewActivity.class);
                intent.putExtra("keytype","1");
                startActivity(intent);
            }
        });
    }
    private class TimeThread extends Thread {
        private int curr;

        private TimeHandler timeHandler;

        public TimeThread(TimeHandler timeHandler) {
            this.timeHandler = timeHandler;
        }

        void cancel() {
            curr = 0;
        }

        @Override
        public void run() {
            curr = 60;
            while (curr > 0) {
                timeHandler.sendEmptyMessage(curr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
                curr--;
            }
            timeHandler.sendEmptyMessage(-1);
        }
    }

    private static class TimeHandler extends Handler {
        FindPwdActivity activity;

        public TimeHandler(FindPwdActivity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:

                    activity.send_button.setText("重新发送");
                    activity.send_button.setVisibility(View.VISIBLE);
                    activity.agin_layout.setVisibility(View.INVISIBLE);
                    break;
                default:
                    activity.send_button.setVisibility(View.GONE);
                    activity.agin_layout.setVisibility(View.VISIBLE);
                    activity.second.setText("" + msg.what);
                    break;
            }
        }
    }

}
