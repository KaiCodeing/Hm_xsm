package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.R;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;

/**
 * Created by lenovo on 2017/3/1.
 * 重设密码  keytype 为空
 * 设置密码 keytype 非空
 */
public class SetPasswordActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private EditText new_passsword_text;
    private EditText password_text;
    private TextView login_text;
    private String keytype;
    private String username;
    private String temp_token;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_password);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case PASSWORD_RESET:
                showProgressDialog("设置新密码...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case PASSWORD_RESET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case PASSWORD_RESET:
                showTextDialog("设置新密码成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        XtomActivityManager.finishAll();
                        Intent intent = new Intent(SetPasswordActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }

                },1000);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case PASSWORD_RESET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case PASSWORD_RESET:
            showTextDialog("设置新密码失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        new_passsword_text = (EditText) findViewById(R.id.new_passsword_text);
        password_text = (EditText) findViewById(R.id.password_text);
        login_text = (TextView) findViewById(R.id.login_text);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
        temp_token = mIntent.getStringExtra("temp_token");
        username = mIntent.getStringExtra("username");
        code = mIntent.getStringExtra("code");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next_button.setVisibility(View.INVISIBLE);
        if (isNull(keytype))
            title_text.setText("设置密码");
        else
            title_text.setText("重设密码");
        //确定
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordS = new_passsword_text.getText().toString()
                        .trim();
                String passwordS2 = password_text.getText().toString().trim();
                if (isNull(passwordS) || passwordS.equals("")) {
                    showTextDialog("请输入密码");
                    return;
                }
                if (isNull(passwordS2) || passwordS2.equals("")) {
                    showTextDialog("请输入确认密码");
                    return;
                }
                if (!passwordS.equals(passwordS2)) {
                    showTextDialog("两次密码输入不一致");

                    return;
                }
                if (passwordS.length() < 6 ) {
                    showTextDialog("请设置大于6位密码");
                    return;
                }
                //注册
                if (isNull(keytype))
                {
                    Intent intent = new Intent(SetPasswordActivity.this,
                            UserInformationActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("tempToken", temp_token);
                    intent.putExtra("password", passwordS);
                    startActivity(intent);
                }
                else
                getNetWorker().passwordReset(temp_token,username, "1", Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(passwordS)));
            }
        });
    }
}
