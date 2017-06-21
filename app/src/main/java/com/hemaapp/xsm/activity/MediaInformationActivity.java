package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.showlargepic.ShowLargePicActivity;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhUtil;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.GoodAdAdapter;
import com.hemaapp.xsm.model.MediaGet;
import com.hemaapp.xsm.view.JhViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lenovo on 2017/3/9.
 * 媒体详情
 */
public class MediaInformationActivity extends JhActivity {
    private ImageButton back_button;
    private ImageButton add_button;//收藏
    private ImageButton next_button;//分享
    private JhViewPager adviewpager;
    private FrameLayout vp_top;
    private RadioGroup radiogroup;
    private TextView company_name;//公司名称
    private TextView media_keytype;//媒体形式
    private TextView media_show;//展示状态
    private TextView media_start;//发布日期
    private TextView media_standard;//媒体规格
    private TextView media_astrict;//限制内容
    private TextView media_city;//所在区域
    private TextView media_address;//精确位置
    private TextView issue_user;//发布人
    private TextView issue_company;//联盟公司
    private TextView issue_tel;//联系电话
    private TextView issue_time;//发布时间
    private TextView go_tel;//一键拨打
    private TextView go_loaction;//查看地图
    private LinearLayout add_view_photo;//加载图
    private String mediaId;
    private MediaGet mediaGet;
    private GoodAdAdapter adAdapter;
    private boolean frist = false;
    private ImageView company_img;
    private LinearLayout yy_layout;
    private TextView industry_name;
    private LinearLayout kd_layout;
    private LinearLayout layout_zp;
    private TextView type_gate_loaction;
    private TextView door_loaction;
    private TextView media_xz;
    private TextView media_city_houw;
    private TextView media_address_loaction;
    private ImageView meida_iamge;
    private TextView media_data_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_media_information);
        super.onCreate(savedInstanceState);
        inIt();
    }

    /**
     * 初始化
     */
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().mediaGet(token, mediaId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {

            case MEDIA_GET:
                showProgressDialog("获取媒体信息");
                break;
            case COLLECT_SAVE:

                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_GET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {

            case MEDIA_GET:
                HemaArrayResult<MediaGet> result = (HemaArrayResult<MediaGet>) hemaBaseResult;
                mediaGet = result.getObjects().get(0);
                setData();
                break;
            case COLLECT_SAVE:
                String keytype = hemaNetTask.getParams().get("flag");
                if ("1".equals(keytype)) {
                    add_button.setImageResource(R.mipmap.coolect_on_img);
                    mediaGet.setIscollect("1");
                } else {
                    add_button.setImageResource(R.mipmap.collect_case_img);
                    mediaGet.setIscollect("0");
                }
                frist = true;
                break;
        }
    }

    /**
     * 填充数据
     */
    private void setData() {
        //广告列表
        double width = JhUtil.getScreenWidth(this);
        double height = width / 4 * 3;
        ViewGroup.LayoutParams params1 = adviewpager.getLayoutParams();
        params1.width = (int) width;
        params1.height = (int) height;
        adviewpager.setLayoutParams(params1);
        adAdapter = new GoodAdAdapter(mContext, radiogroup, vp_top, mediaGet.getImgitem());
        adviewpager.setAdapter(adAdapter);
        if (mediaGet.getImgitem() != null)
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
        //空档期
        if ("2".equals(mediaGet.getStatus())) {
            radiogroup.setVisibility(View.INVISIBLE);
            adviewpager.setVisibility(View.INVISIBLE);
            yy_layout.setVisibility(View.INVISIBLE);
            kd_layout.setVisibility(View.GONE);
            layout_zp.setVisibility(View.GONE);
            go_tel.setVisibility(View.GONE);
            company_img.setVisibility(View.VISIBLE);
            yy_layout.setVisibility(View.VISIBLE);
            go_loaction.setPadding(0, JhUtil.dip2px(mContext,20),JhUtil.dip2px(mContext,160),JhUtil.dip2px(mContext,20));
            meida_iamge.setVisibility(View.GONE);
            add_view_photo.setVisibility(View.GONE);
            String path = mediaGet.getImgurlbig_d();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ad_defalut_bg)
                    .showImageForEmptyUri(R.mipmap.ad_defalut_bg)
                    .showImageOnFail(R.mipmap.ad_defalut_bg).cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoader.getInstance().displayImage(path, company_img, options);
            industry_name.setText("招商电话:" + mediaGet.getRecuitnum());
        } else {
            company_img.setVisibility(View.INVISIBLE);
            yy_layout.setVisibility(View.INVISIBLE);
        }
        company_name.setText(mediaGet.getCustom_name());
        if (!"1".equals(JhctmApplication.getInstance().getUser().getFeeaccount())) {
            //判断是否可调整点位
            if ("2".equals(mediaGet.getChange_flag()))
                type_gate_loaction.setVisibility(View.VISIBLE);
            else
                type_gate_loaction.setVisibility(View.GONE);
        } else
            type_gate_loaction.setVisibility(View.GONE);
        //入口
        door_loaction.setText(mediaGet.getName()+"---"+mediaGet.getIngress());
        //
        if ("1".equals(mediaGet.getMtype())) {
            media_keytype.setText("媒体形式: 道闸广告");
        } else {
            media_keytype.setText("媒体形式: 门禁广告");
        }
        //楼盘类型
//        if ("1".equals(mediaGet.getStatus()))
//            media_show.setText("展示状态: 上刊");
//        else
//            media_show.setText("展示状态: 空档期");
//        media_show.setText(mediaGet.getHouse_type_text());
//        //楼盘均价
//        media_start.setText(mediaGet.getHouse_price()+"元/㎡");
//        //覆盖人数
//        media_standard.setText(mediaGet.getPeople_num()+"人");
//        //车位数量
//        media_astrict.setText(mediaGet.getPark_num());
//        //展示状态
//        if ("1".equals(mediaGet.getStatus()))
//            media_show.setText("展示状态: 上刊");
//        else
//            media_show.setText("展示状态: 空档期");
//        //发布日期
//        media_start.setText("发布日期: " + mediaGet.getUp_date() + "--" + mediaGet.getDown_date());
//        //规格
//        media_standard.setText("媒体规格: " + mediaGet.getSpec());
//        //限制内容
//        String ge = mediaGet.getLimitself();
//        if (isNull(ge))
//            ge = "";
//        if ("1".equals(mediaGet.getLimitcontent()))
//            media_astrict.setText("限制内容: 不限" + ge);
//        else if ("2".equals(mediaGet.getLimitcontent()))
//            media_astrict.setText("限制内容: 房地产" + ge);
//        else if ("3".equals(mediaGet.getLimitcontent()))
//            media_astrict.setText("限制内容: 医疗" + ge);
//        else
//            media_astrict.setText("限制内容: 房地产,医疗" + ge);
//        //所在区域
//        media_city.setText("所在区域: " + mediaGet.getProvince() + " " + mediaGet.getCity() + " " + mediaGet.getArea());
//        //详细地址
//        media_address.setText("精确位置: " + mediaGet.getAddress());
        //发布人
        issue_user.setText("发布人: " + mediaGet.getClientname());
        //联盟公司
        issue_company.setText("联盟公司: " + mediaGet.getUnion());
        //联系电话
        issue_tel.setText("联系电话 :" + mediaGet.getClientmobile());
        //issue_time发布时间
        issue_time.setText("发布时间: " + mediaGet.getRegdate());
        //楼盘类型
        media_show.setText("楼盘类型:" + mediaGet.getHouse_type_text());
        //楼盘均价
        media_start.setText("楼盘均价:" + mediaGet.getHouse_price() + "元/㎡");
        //覆盖人数
        media_standard.setText("覆盖人数:" + mediaGet.getPeople_num() + "人");
        //车位数量
        media_astrict.setText("车位数量:" + mediaGet.getPark_num());
        //展示状态
        if ("1".equals(mediaGet.getStatus()))
            media_city.setText("展示状态: 上刊");
        else
            media_city.setText("展示状态: 空档期");
        //发布日期
        media_data_time.setText("发布日期:"+mediaGet.getUp_date() + "--" + mediaGet.getDown_date());
        //媒体规格
        media_address.setText("媒体规格:" + mediaGet.getSpec());
        //限制内容
        media_xz.setText("限制内容:" + mediaGet.getLimitself());
        String ge = mediaGet.getLimitself();
        if (isNull(ge))
            ge = "";
        if ("1".equals(mediaGet.getLimitcontent()))
            media_xz.setText("限制内容: 不限" + ge);
        else if ("2".equals(mediaGet.getLimitcontent()))
            media_xz.setText("限制内容: 房地产" + ge);
        else if ("3".equals(mediaGet.getLimitcontent()))
            media_xz.setText("限制内容: 医疗" + ge);
        else
            media_xz.setText("限制内容: 房地产,医疗" + ge);
        //所在区域
        media_city_houw.setText("所在区域:" + mediaGet.getProvince() + " " + mediaGet.getCity() + " " + mediaGet.getArea());
        //详细地址
        media_address_loaction.setText(" " + Html.fromHtml("<u>" + mediaGet.getAddress() + "</u>"));
        //加载图片
        add_view_photo.removeAllViews();
        if (mediaGet.getImgcloseitem() != null && mediaGet.getImgcloseitem().size() != 0)
            for (int i = 0; i < mediaGet.getImgcloseitem().size(); i++) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.view_add_request, null);
                ImageView im = (ImageView) view.findViewById(R.id.add_img);
                String path = mediaGet.getImgcloseitem().get(i).getImgurlbig();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.ad_defalut_bg)
                        .showImageForEmptyUri(R.mipmap.ad_defalut_bg)
                        .showImageOnFail(R.mipmap.ad_defalut_bg).cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoader.getInstance().displayImage(path, im, options);
                im.setTag(R.id.TAG, i);
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag(R.id.TAG);
                        Intent it = new Intent(mContext, ShowLargePicActivity.class);
                        it.putExtra("position", position);
                        it.putExtra("images", mediaGet.getImgcloseitem());
                        it.putExtra("titleAndContentVisible", false);
                        mContext.startActivity(it);
                    }
                });
                add_view_photo.addView(view);
            }
        else{
            layout_zp.setVisibility(View.GONE);
        }
        //收藏
        if ("1".equals(mediaGet.getIscollect())) {
            add_button.setImageResource(R.mipmap.coolect_on_img);
        } else
            add_button.setImageResource(R.mipmap.collect_case_img);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {

            case MEDIA_GET:
            case COLLECT_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {

            case MEDIA_GET:
                showTextDialog("获取媒体详情失败，请稍后重试");
                break;
            case COLLECT_SAVE:
                showTextDialog("收藏操作失败，请稍后重试");
                break;


        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        add_button = (ImageButton) findViewById(R.id.add_button);
        next_button = (ImageButton) findViewById(R.id.next_button);
        adviewpager = (JhViewPager) findViewById(R.id.adviewpager);
        vp_top = (FrameLayout) findViewById(R.id.vp_top);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        company_name = (TextView) findViewById(R.id.company_name);
        media_keytype = (TextView) findViewById(R.id.media_keytype);
        media_show = (TextView) findViewById(R.id.media_show);
        media_start = (TextView) findViewById(R.id.media_start);
        media_standard = (TextView) findViewById(R.id.media_standard);
        media_astrict = (TextView) findViewById(R.id.media_astrict);
        media_city = (TextView) findViewById(R.id.media_city);
        media_address = (TextView) findViewById(R.id.media_address);
        issue_user = (TextView) findViewById(R.id.issue_user);
        issue_company = (TextView) findViewById(R.id.issue_company);
        issue_tel = (TextView) findViewById(R.id.issue_tel);
        issue_time = (TextView) findViewById(R.id.issue_time);
        go_tel = (TextView) findViewById(R.id.go_tel);
        go_loaction = (TextView) findViewById(R.id.go_loaction);
        add_view_photo = (LinearLayout) findViewById(R.id.add_view_photo);
        company_img = (ImageView) findViewById(R.id.company_img);
        yy_layout = (LinearLayout) findViewById(R.id.yy_layout);
        industry_name = (TextView) findViewById(R.id.industry_name);
        kd_layout = (LinearLayout) findViewById(R.id.kd_layout);
        layout_zp = (LinearLayout) findViewById(R.id.layout_zp);
        type_gate_loaction = (TextView) findViewById(R.id.type_gate_loaction);
        door_loaction = (TextView) findViewById(R.id.door_loaction);
        media_xz = (TextView) findViewById(R.id.media_xz);
        media_city_houw = (TextView) findViewById(R.id.media_city_houw);
        media_address_loaction = (TextView) findViewById(R.id.media_address_loaction);
        meida_iamge = (ImageView) findViewById(R.id.meida_iamge);
        media_data_time = (TextView) findViewById(R.id.media_data_time);
    }

    @Override
    protected void getExras() {
        mediaId = mIntent.getStringExtra("mediaId");
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
        //收藏
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                if ("1".equals(mediaGet.getIscollect()))
                    getNetWorker().collectSave(token, "2", "1", mediaId);
                else
                    getNetWorker().collectSave(token, "1", "1", mediaId);
            }
        });
        next_button.setVisibility(View.GONE);
        //分享
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //拨打
        go_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mediaGet.getClientmobile());
                intent.setData(data);
                startActivity(intent);
            }
        });
        //查看地图
        go_loaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaInformationActivity.this, MediaOnlyMapActivity.class);
                intent.putExtra("keytype", "1");
                intent.putExtra("lng", mediaGet.getLng());
                intent.putExtra("lat", mediaGet.getLat());
                intent.putExtra("cityL", mediaGet.getName());
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
