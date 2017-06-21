package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.PTAdapter;
import com.hemaapp.xsm.model.Media;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2017/6/8.
 * 点位预定选择
 */
public class LoactionSelectActivity extends JhActivity {
    private ImageButton back_button;
    private TextView search_input;
    private Button next_button;
    private RadioGroup radiogroup;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private Integer page = 0;
    private String status = "0";
    private String change_flag = "";
    private String time_out = "";
    private ArrayList<Media> medias = new ArrayList<>();
    private PTAdapter adapter;
    private String keytype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loaction_select);
        super.onCreate(savedInstanceState);
        inIt();
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().mediaList(token, keytype,"","",status,"","","","",change_flag,"",time_out,"",
                "","","","","","","","","","","","","",String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("获取点位列表");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
        refreshLoadmoreLayout.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
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
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new PTAdapter(mContext, medias, "2");
            adapter.setEmptyString("暂无点位信息");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无点位信息");
            adapter.setMedias(medias);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        String page = hemaNetTask.getParams().get("page");
        if ("0".equals(page)) {
            refreshLoadmoreLayout.refreshFailed();
        } else {
            refreshLoadmoreLayout.loadmoreFailed();
        }
        showTextDialog("获取点位列表失败，请稍后重试");
    }

    @Override
    protected void findView() {
        search_input = (TextView) findViewById(R.id.search_input);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //确定 选择
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
//                StringBuffer buffer = new StringBuffer();
                for (Media media : medias)
                    if (media.isCheck()) {
                        i++;
//                        buffer.append(media.getId()+",");
                    }
                if (i == 0) {
                    showTextDialog("请选择点位");
                    return;
                }
                setResult(RESULT_OK, mIntent);
                mIntent.putExtra("medias", medias);
                finish();
            }
        });
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_1:
                        status = "0";
                        change_flag = "";
                        time_out = "";
                        page = 0;
                        inIt();
                        break;
                    case R.id.radio_2:
                        status = "1";
                        change_flag = "2";
                        time_out = "";
                        page = 0;
                        inIt();
                        break;
                    case R.id.radio_3:
                        status = "1";
                        change_flag = "";
                        time_out = "2";
                        page = 0;
                        inIt();
                        break;
                    case R.id.radio_4:
                        status = "2";
                        change_flag = "";
                        time_out = "";
                        page = 0;
                        inIt();
                        break;
                }
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
        //搜搜
        search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoactionSelectActivity.this,RQSearchActivity.class);
                intent.putExtra("keytype","2");
                intent.putExtra("typekey",  keytype);
                intent.putExtra("text","text");
                startActivityForResult(intent,1);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 1:
                ArrayList<Media> medias = (ArrayList<Media>) data.getSerializableExtra("medias");
                //遍历集合获取id
                StringBuffer bufferid = new StringBuffer();
//                ArrayList<Media> medias1 = new ArrayList<>();
                int i = 0;
                for (Media media : medias
                        ) {
                    if (media.isCheck()) {
                        bufferid.append(media.getId() + ",");
                        i++;
//                        medias1.add(media);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("medias", medias);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

}
