package com.hemaapp.xsm.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.PTAdapter;
import com.hemaapp.xsm.model.Media;
import com.hemaapp.xsm.model.PreGet;
import com.hemaapp.xsm.view.MyListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2017/6/7.
 * 预定详情
 */
public class ReserveInformationActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private TextView reserve_name;
    private ImageView reserve_delete;
    private TextView reserve_type;
    private TextView reserve_time;
    private TextView reserve_loaction;
    private TextView reserve_issue_data;
    private MyListView listview;
    private String reserveId;
    private PTAdapter adapter;
    private DeleteView deleteView;//清空

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reserve_information);
        super.onCreate(savedInstanceState);
        inIt();
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().preGet(token, reserveId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRE_GET:
                showProgressDialog("获取预定详情");
                break;
            case PRE_DELETE:

                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRE_GET:
                cancelProgressDialog();
                break;
            case PRE_DELETE:

                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(final HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRE_GET:
                HemaArrayResult<PreGet> result = (HemaArrayResult<PreGet>) hemaBaseResult;
                PreGet preGet = result.getObjects().get(0);
                setData(preGet);
                break;
            case PRE_DELETE:
                showTextDialog("删除预定成功！");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK, mIntent);
                        mIntent.putExtra("id", hemaNetTask.getParams().get("id"));
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    private void setData(final PreGet preGet1) {
        reserve_name.setText(preGet1.getCustom_name());
        //判断状态
        if ("1".equals(preGet1.getStatus()))
            reserve_type.setText("待成交");
        else
            reserve_type.setText("已成交");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String regdate = XtomTimeUtil.TransTime(preGet1.getRegdate(), "MM-dd HH:mm");
        reserve_time.setText(regdate);
        reserve_loaction.setText("预定点位:" + preGet1.getName_str());
        reserve_issue_data.setText("预定发布日期:" + preGet1.getUp_date() + "--" + preGet1.getDown_date());
        //删除
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelete(preGet1.getId());
            }
        });
        freshData(preGet1.getPoint_r());
    }
    //删除操作

    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    public void showDelete(final String id) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_right_left, null);
        deleteView = new DeleteView();
        deleteView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        deleteView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        deleteView.text = (TextView) view.findViewById(R.id.text);
        deleteView.iphone_number.setVisibility(View.GONE);
        deleteView.text.setText("确定要删除该预定吗?");
        deleteView.text.setTextColor(getResources().getColor(R.color.black));

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
                                                      getNetWorker().preDelete(token, id);
                                                  }


                                              }
        );
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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

    private void freshData(ArrayList<Media> medias) {
        if (adapter == null) {
            adapter = new PTAdapter(mContext, medias, null);
            adapter.setEmptyString("暂无媒体信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无媒体信息");
            adapter.setMedias(medias);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRE_GET:
            case PRE_DELETE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PRE_GET:
                showTextDialog("获取预定详情失败，请稍后重试");
                break;
            case PRE_DELETE:
                showTextDialog("删除失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        reserve_name = (TextView) findViewById(R.id.reserve_name);
        reserve_delete = (ImageView) findViewById(R.id.reserve_delete);
        reserve_type = (TextView) findViewById(R.id.reserve_type);
        reserve_time = (TextView) findViewById(R.id.reserve_time);
        reserve_loaction = (TextView) findViewById(R.id.reserve_loaction);
        reserve_issue_data = (TextView) findViewById(R.id.reserve_issue_data);
        listview = (MyListView) findViewById(R.id.listview);
    }

    @Override
    protected void getExras() {
        reserveId = mIntent.getStringExtra("reserveId");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("预定详情");
        next_button.setImageResource(R.mipmap.reserve_infor_delect_img);
    }
}
