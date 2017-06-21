package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.MediaAdapter;
import com.hemaapp.xsm.model.Media;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/3/9.
 * 我的发布
 */
public class MyIssueActivity extends JhActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private MediaAdapter adapter;
    private ViewHolder holder;
    private Integer page = 0;
    private ArrayList<Media> medias = new ArrayList<>();
    private DeleteView deleteView;//清空

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_issue);
        super.onCreate(savedInstanceState);
        inIt();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 0;
        inIt();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            page = 0;
//            inIt();
//            return;
//        }
//        switch (requestCode) {
//            case 1:
//                Media media = (Media) data.getSerializableExtra("media");
//                for (int i = 0; i < medias.size(); i++) {
//                    if (medias.get(i).getId().equals(media.getId()))
//                        medias.get(i).setImgurl(media.getImgurl());
//                    medias.get(i).setStatus(media.getStatus());
//                    medias.get(i).setMtype(media.getMtype());
//                    medias.get(i).setName(media.getName());
//                    medias.get(i).setUp_date(media.getUp_date());
//                    medias.get(i).setDown_date(media.getDown_date());
//                }
//                freshData();
//                break;
//        }
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().mediaList(token, "2", "", "", "", "", "", "", "",
                "", "", "", String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                showProgressDialog("获取媒体列表");
                break;
            case MEDIA_EDIT:
                break;
            case MEDIA_DEL:
                showProgressDialog("删除媒体...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                cancelProgressDialog();
                break;
            case MEDIA_EDIT:
            case MEDIA_DEL:
                cancelProgressDialog();
                break;

        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                HemaPageArrayResult<Media> result = (HemaPageArrayResult<Media>) hemaBaseResult;
                ArrayList<Media> medias = result.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.medias.clear();
                    this.medias.addAll(medias);

                    JhctmApplication application = JhctmApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (medias.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (medias.size() > 0)
                        this.medias.addAll(medias);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                freshData();
                break;
            case MEDIA_EDIT:
            case MEDIA_DEL:
                page = 0;
                inIt();
                break;

        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new MediaAdapter(mContext, medias, "1");
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
            case MEDIA_LIST:
            case MEDIA_EDIT:
                showTextDialog(hemaBaseResult.getMsg());
                break;

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取媒体列表失败，请稍后重试");
                break;
            case MEDIA_EDIT:
                showTextDialog("操作失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finish();
            }
        });
        title_text.setText("我的发布");
        next_button.setImageResource(R.mipmap.issue_blog_img);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyIssueActivity.this, MediaIssueActivity.class);
                startActivityForResult(intent,2);
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                inIt();
            }
        });
    }

    private class ViewHolder {
        RadioGroup group_1;
        TextView compile_text;//编辑
        TextView delect_text;//删除
        RadioButton radiobutton_1;
        RadioButton radiobutton_2;
    }


    public void showPop(final Media media, View imageView) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_show_keytype_view, null);
        holder = new ViewHolder();
        holder.group_1 = (RadioGroup) view.findViewById(R.id.group_1);
        holder.compile_text = (TextView) view.findViewById(R.id.compile_text);
        holder.delect_text = (TextView) view.findViewById(R.id.delect_text);
        holder.radiobutton_1 = (RadioButton) view.findViewById(R.id.radiobutton_1);
        holder.radiobutton_2 = (RadioButton) view.findViewById(R.id.radiobutton_2);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        if (media.getStatus().equals("1"))
            holder.radiobutton_1.setChecked(true);
        else
            holder.radiobutton_2.setChecked(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //选择进度
        holder.group_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                switch (checkedId) {
                    case R.id.radiobutton_1:
                        getNetWorker().mediaEdit(token, media.getId(), "1");
                        popupWindow.dismiss();
                        break;
                    case R.id.radiobutton_2:
                        getNetWorker().mediaEdit(token, media.getId(), "2");
                        popupWindow.dismiss();
                        break;
                }
            }
        });
        //编辑
        holder.compile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(MyIssueActivity.this, MediaIssueActivity.class);
                intent.putExtra("mediaId", media.getId());
                intent.putExtra("meidaI", media);
                startActivityForResult(intent, 1);
            }
        });
        //删除
        holder.delect_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showDelete(media.getId());
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //  popupWindow.showAsDropDown(findViewById(imageView.getId()));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    private void showDelete(final String id) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_right_left, null);
        deleteView = new DeleteView();
        deleteView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        deleteView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        deleteView.text = (TextView) view.findViewById(R.id.text);
        deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        deleteView.text.setText("确定要删除该条媒体吗？");
        deleteView.iphone_number.setText("一旦删除将不能找回");
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
                                                      getNetWorker().mediaDel(token, id);
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

}
