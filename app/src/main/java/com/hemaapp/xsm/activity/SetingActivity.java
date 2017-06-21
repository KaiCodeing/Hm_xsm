package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhUpGrade;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.User;

import java.math.BigDecimal;

import xtom.frame.XtomActivityManager;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.media.XtomVoicePlayer;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/3/2.
 * 设置的activity
 */
public class SetingActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private ImageView check_tuisong;
    private TextView cache;
    private TextView versions;
    private LinearLayout layout_password;//密码管理
    private TextView login_text;//退出登录
    private LinearLayout clear_layout;
    private LinearLayout updata_layout;
    private User user;
    private DeleteView deleteView;//清空
    private JhUpGrade upGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SYS_SET:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SYS_SET:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SYS_SET:
                String keyt = hemaNetTask.getParams().get("value");
                if ("0".equals(keyt)) {
                    JhctmApplication.getInstance().getUser().setExpiredflag("0");
                    check_tuisong.setImageResource(R.mipmap.tuisong_off_img);
                } else {
                    JhctmApplication.getInstance().getUser().setExpiredflag("1");
                    check_tuisong.setImageResource(R.mipmap.tuisong_on_img);
                }
                break;
            case CLIENT_LOGINOUT:
                JhctmApplication.getInstance().setUser(null);
                XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
                XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
                //XtomSharedPreferencesUtil.save(getActivity(), "city_name", "");
                XtomActivityManager.finishAll();
                Intent it = new Intent(mContext, LoginActivity.class);
                startActivity(it);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SYS_SET:
            case CLIENT_LOGINOUT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SYS_SET:
                showTextDialog("设置消息失败，请稍后重试");
                break;
            case CLIENT_LOGINOUT:
                showTextDialog("退出登录失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        check_tuisong = (ImageView) findViewById(R.id.check_tuisong);
        cache = (TextView) findViewById(R.id.cache);
        versions = (TextView) findViewById(R.id.versions);
        layout_password = (LinearLayout) findViewById(R.id.layout_password);
        login_text = (TextView) findViewById(R.id.login_text);
        clear_layout = (LinearLayout) findViewById(R.id.clear_layout);
        updata_layout = (LinearLayout) findViewById(R.id.updata_layout);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        String notice = JhctmApplication.getInstance().getUser().getExpiredflag();
        if ("1".equals(notice)) {
            check_tuisong.setImageResource(R.mipmap.tuisong_on_img);
        } else {
            check_tuisong.setImageResource(R.mipmap.tuisong_off_img);
        }
        //接受推送
        check_tuisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notice = JhctmApplication.getInstance().getUser().getExpiredflag();
                String token = JhctmApplication.getInstance().getUser().getToken();
                if ("1".equals(notice)) {
                    getNetWorker().sysSet(token, "0");
                } else {
                    getNetWorker().sysSet(token, "1");
                }
            }
        });
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("设置");
        next_button.setVisibility(View.INVISIBLE);
        cache.setText(bytes2kb(XtomImageCache.getInstance(mContext).getCacheSize()));
        //清除缓存
        clear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearTask().execute();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String version_text = HemaUtil.getAppVersionForSever(mContext);
        //版本号
        versions.setText(version_text);
        updata_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastv = JhctmApplication.getInstance().getSysInitInfo().getAndroid_last_version();
                if (lastv.equals(version_text)) {
                    showTextDialog("已经是最新版本");
                } else {
                    upGrade = new JhUpGrade(mContext);
                    if (upGrade != null) {
                        upGrade.check();
                    }
                }
            }
        });

        //关于软件
        layout_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetingActivity.this, WebViewActivity.class);
                intent.putExtra("keytype", "3");
                startActivity(intent);
            }
        });
        //退出登录
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelete();
            }
        });
    }

    private class ClearTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // 删除图片缓存
            XtomImageCache.getInstance(mContext).deleteCache();
            // 删除语音缓存
            XtomVoicePlayer player = XtomVoicePlayer.getInstance(mContext);
            player.deleteCache();
            player.release();
            return null;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在清除缓存");
        }

        @Override
        protected void onPostExecute(Void result) {
            cancelProgressDialog();
            showTextDialog("清除缓存成功");
            cache.setText(bytes2kb(XtomImageCache.getInstance(mContext).getCacheSize()));
        }
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "M");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "K");
    }

    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    private void showDelete() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_right_left, null);
        deleteView = new DeleteView();
        deleteView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        deleteView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        deleteView.text = (TextView) view.findViewById(R.id.text);
        deleteView.iphone_number.setVisibility(View.GONE);
        deleteView.text.setText("确定要退出当前登录吗");
        deleteView.text.setTextColor(getResources().getColor(R.color.black));
//        if (!isNull(id)) {
//            deleteView.text = (TextView) view.findViewById(R.id.text);
//            deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
//            deleteView.text.setText("确定要删除该条消息吗？");
//            deleteView.iphone_number.setText("一旦删除将不能找回");
//
//        }
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        deleteView.close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        deleteView.yas_pop.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      popupWindow.dismiss();
                                                      String token = JhctmApplication.getInstance().getUser().getToken();
                                                      getNetWorker().clientLoginout(token);
                                                  }


                                              }
        );
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
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

}
