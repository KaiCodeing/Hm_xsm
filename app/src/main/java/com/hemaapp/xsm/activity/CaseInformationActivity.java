package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhUtil;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.GoodAdAdapter;
import com.hemaapp.xsm.model.DemoGet;
import com.hemaapp.xsm.view.JhViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lenovo on 2017/3/7.
 * 案例详情
 */
public class CaseInformationActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private ImageButton next_button;
    private JhViewPager adviewpager;
    private RadioGroup radiogroup;
    private TextView company_case_title;
    private TextView time_case;
    private TextView industry_name;
    private TextView city_name;
    private TextView address_name;
    private ImageView show_loaction;
    private HemaWebView webview;
    private VideoView video;
    private ImageView video_image;
    private ImageView video_open;
    private String demoid;
    private DemoGet demoGet;
    private FrameLayout vp_top;
    private GoodAdAdapter adAdapter;
    private FrameLayout fvideo;
    private LinearLayout video_layout;
    private boolean frist = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_case_information);
        super.onCreate(savedInstanceState);
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt()
    {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().demoGet(token,demoid);
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case DEMO_GET:
                showProgressDialog("获取案例信息");
                break;
            case COLLECT_SAVE:
                showProgressDialog("保存收藏操作");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case DEMO_GET:
            case COLLECT_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case DEMO_GET:
                HemaArrayResult<DemoGet> result = (HemaArrayResult<DemoGet>) hemaBaseResult;
                demoGet = result.getObjects().get(0);
                setData();
                break;
            case COLLECT_SAVE:
                String keytype = hemaNetTask.getParams().get("flag");
                if ("1".equals(keytype))
                {
                    showTextDialog("收藏成功");
                    demoGet.setIscollect("1");
                    next_button.setImageResource(R.mipmap.coolect_on_img);
                }
                else
                {
                    showTextDialog("取消收藏成功");
                    demoGet.setIscollect("0");
                    next_button.setImageResource(R.mipmap.collect_case_img);
                }
                frist = true;
                break;
        }
    }

    /**
     * 填写数据
     */
    private void setData()
    {
        double width = JhUtil.getScreenWidth(this);
        double height = width / 1 * 1;
        ViewGroup.LayoutParams params1 = adviewpager.getLayoutParams();
        params1.width = (int) width;
        params1.height = (int) height;
        adviewpager.setLayoutParams(params1);
        adAdapter = new GoodAdAdapter(mContext, radiogroup, vp_top, demoGet.getImgitem());
        adviewpager.setAdapter(adAdapter);
        if (demoGet.getImgitem() != null)
            adviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
                @Override
                public void onPageSelected(int position) {
                    if (adAdapter != null) {
                        ViewGroup indicator = adAdapter.getIndicator();
                        if (indicator != null) {
                            RadioButton rbt = (RadioButton) indicator.getChildAt(position);
                            if (rbt != null)
                                rbt.setChecked(true);
                        }
                    }
                }
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        company_case_title.setText(demoGet.getName());
        time_case.setText(demoGet.getRegdate());
        industry_name.setText("所属行业: "+demoGet.getIndustry_text());
        city_name.setText("案例小区:"+demoGet.getVillige());
        address_name.setText("案例位置: "+demoGet.getAddress());
        //收藏
        //判断是否收藏
        if ("0".equals(demoGet.getIscollect()))
        {
            next_button.setImageResource(R.mipmap.collect_case_img);
        }
        else
            next_button.setImageResource(R.mipmap.coolect_on_img);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                if ("0".equals(demoGet.getIscollect()))
                {
                    getNetWorker().collectSave(token,"1","3",demoid);
                }
                else
                    getNetWorker().collectSave(token,"2","3",demoid);
            }
        });
        String sys_web_service = getApplicationContext().getSysInitInfo()
                .getSys_web_service();
        String path = sys_web_service + "webview/parm/demo_"+demoid;
        webview.loadUrl(path);
        log_i("++++++++++++"+path);
        //大图
        String path1 = demoGet.getMvimgurlbig();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ad_defalut_bg)
                .showImageForEmptyUri(R.mipmap.ad_defalut_bg)
                .showImageOnFail(R.mipmap.ad_defalut_bg).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path1, video_image, options);
        //视频播放
        video_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.start();
                video_open.setVisibility(View.GONE);
                video_image.setVisibility(View.GONE);
            }
        });
        if(isNull(demoGet.getMvurl())){
            video_layout.setVisibility(View.GONE);
            fvideo.setVisibility(View.GONE);
        }else {
            //videohui.setTag(R.id.TAG,infor.getVideourl());//绑定imageview
            //mVideoThumbLoader.showThumbByAsynctack(infor.getVideourl(), videohui);
            Uri uri = Uri.parse( demoGet.getMvurl() );
            //设置视频控制器
                video.setMediaController(new MediaController(mContext));
            //播放完成回调
            video.setOnCompletionListener( new MyPlayerOnCompletionListener());
            //设置视频路径
            video.setVideoURI(uri);
            //videohui.setImageBitmap(createVideoThumbnail(infor.getVideourl(),300,200));
        }
    }
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            video_open.setVisibility(View.VISIBLE);
        }
    }
        @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case DEMO_GET:
            case COLLECT_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case DEMO_GET:
                showTextDialog("获取案例详情失败，请稍后重试");
                break;
            case COLLECT_SAVE:
                showTextDialog("保存操作失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (ImageButton) findViewById(R.id.next_button);
        adviewpager = (JhViewPager) findViewById(R.id.adviewpager);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        company_case_title = (TextView) findViewById(R.id.company_case_title);
        time_case = (TextView) findViewById(R.id.time_case);
        industry_name = (TextView) findViewById(R.id.industry_name);
        city_name = (TextView) findViewById(R.id.city_name);
        address_name = (TextView) findViewById(R.id.address_name);
        show_loaction = (ImageView) findViewById(R.id.show_loaction);
        webview = (HemaWebView) findViewById(R.id.webview);
        video = (VideoView) findViewById(R.id.video);
        video_image = (ImageView) findViewById(R.id.video_image);
        video_open = (ImageView) findViewById(R.id.video_open);
        vp_top = (FrameLayout) findViewById(R.id.vp_top);
        fvideo = (FrameLayout) findViewById(R.id.fvideo);
        video_layout = (LinearLayout) findViewById(R.id.video_layout);
    }

    @Override
    protected void getExras() {
        demoid = mIntent.getStringExtra("demoid");
    }

    @Override
    protected void setListener() {
    back_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (frist)
                setResult(RESULT_OK, mIntent);
            finish();
        }
    });
        title_text.setText("案例展示详情");
        next_button.setImageResource(R.mipmap.collect_case_img);
        //地图
        show_loaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseInformationActivity.this,MediaOnlyMapActivity.class);
                intent.putExtra("keytype","1");
                intent.putExtra("lng",demoGet.getLng());
                intent.putExtra("lat",demoGet.getLat());
                intent.putExtra("cityL",demoGet.getName());
                startActivity(intent);
            }
        });
    }
    @Override
    protected boolean onKeyBack() {
        if (frist)
            setResult(RESULT_OK, mIntent);
        finish();
        return true;
    }
}
