package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhNetWorker;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.User;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/2/27.
 * 登录
 */
public class LoginActivity extends JhActivity {
    private EditText username_text;
    private EditText password_text;
    private TextView login_text;
    private TextView add_user;
    private TextView forget_password;
    private ImageView bank_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:

                showProgressDialog("正在验证登录信息");
                break;

            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
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
            case CLIENT_LOGIN:
                HemaArrayResult<User> userArrayResult = (HemaArrayResult<User>) hemaBaseResult;
                User user = userArrayResult.getObjects().get(0);

                getApplicationContext().setUser(user);
                String username = hemaNetTask.getParams().get("username");
                String password = hemaNetTask.getParams().get("password");
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", password);
                XtomActivityManager.finishAll();
                String token = user.getToken();
                GotoMain();
                break;
            default:
                break;
        }
    }
    /**
     * @方法名称: GotoMain
     * @功能描述: TODO跳转到主界面
     * @返回值: void
     */
    private void GotoMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                log_i(result.getError_code() + "Error_code");
                if ("102".equals(result.getError_code() + "")) {
                    showTextDialog("账号或密码错误，请重新填写");
                } else if ("106".equals(hemaBaseResult.getError_code() + "")) {
                    showTextDialog("该手机号尚未注册，请先注册");
                } else {
                    showTextDialog(result.getMsg());
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
            case CLIENT_LOGIN:
                showTextDialog("登录失败，请稍后登录");
                break;

            default:
                break;
        }
    }

    @Override
    protected void findView() {
        username_text = (EditText) findViewById(R.id.username_text);
        password_text = (EditText) findViewById(R.id.password_text);
        login_text = (TextView) findViewById(R.id.login_text);
        add_user = (TextView) findViewById(R.id.add_user);
        forget_password = (TextView) findViewById(R.id.forget_password);
        bank_img = (ImageView) findViewById(R.id.bank_img);
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
        bank_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XtomActivityManager.finishAll();
            }
        });
        //登录
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = username_text.getText().toString();
                String password = password_text.getText().toString();
                if (isNull(username) || username.equals("")) {
                    showTextDialog("请填写手机号");
                    return;
                }
                String mobile = "\\d{11}";// 只判断11位
                if (!username.matches(mobile)) {
                    showTextDialog("手机格式不正确，请重新输入");
                    return;
                }
                if (isNull(password) || password.equals("")) {
                    showTextDialog("请填写登录密码");
                    return;
                }
                if (password.trim().length() >= 6 && password.trim().length() <= 12) {

                } else {
                    showTextDialog("密码输入不正确\n请输入6-12位密码");
                    return;
                }
                JhNetWorker netWorker = getNetWorker();
                netWorker.clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password)));
            }
        });
        //注册
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,FindPwdActivity.class);
                startActivity(intent);
            }
        });
        //找回密码
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,FindPwdActivity.class);
                intent.putExtra("keytype","1");
                startActivity(intent);
            }
        });
    }
}
