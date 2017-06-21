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

import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;

/**
 * Created by lenovo on 2017/3/2.
 * 密码管理
 */
public class ChangePWDActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private EditText old_password;
    private EditText new_password;
    private EditText yes_new_password;
    private TextView login_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_pwd);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("修改密码中");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
    cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog("修改密码成功");
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
        showTextDialog("修改密码失败，请稍后重试");
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        yes_new_password = (EditText) findViewById(R.id.yes_new_password);
        login_text = (TextView) findViewById(R.id.login_text);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        title_text.setText("密码管理");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next_button.setVisibility(View.INVISIBLE);
        //提交
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPsw()) {
                    String oldPwd = old_password.getText().toString().trim();
                    String newPwd = new_password.getText().toString().trim();
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    getNetWorker().passwordSave(token, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(oldPwd)), Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(newPwd)));
                }
            }
        });
    }

    //检测
    private boolean checkPsw() {
        String oldPwd = old_password.getText().toString().trim();
        String newPwd = new_password.getText().toString().trim();
        String newPwd1 = yes_new_password.getText().toString().trim();
        if (isNull(oldPwd)) {
            showTextDialog("请填写原密码");
            return false;
        }
        if (isNull(newPwd)) {
            showTextDialog("请填写新密码");
            return false;
        }
        if (isNull(newPwd1)) {
            showTextDialog("请填写确认密码");
            return false;
        }
        if (!newPwd.equals(newPwd1)) {
            showTextDialog("请保持两次输入密码一致");
            return false;
        }
        if (newPwd.length() < 6 || newPwd.length() > 20) {
            showTextDialog("请填写6-20位密码");
            return false;
        }
     return true;
    }
}
