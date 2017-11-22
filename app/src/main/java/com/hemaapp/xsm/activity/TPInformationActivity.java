package com.hemaapp.xsm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhUtil;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.GoodAdAdapter;
import com.hemaapp.xsm.model.MediaGet;
import com.hemaapp.xsm.view.JhViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lenovo on 2017/6/2.
 * 点位详情
 */
public class TPInformationActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
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
    private String loactionId;
    private MediaGet mediaGet;
    private GoodAdAdapter adAdapter;
    private ImageView company_img;
    private LinearLayout yy_layout;
    private TextView industry_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tp_information);
        super.onCreate(savedInstanceState);
        inIt();
    }

    //初始化
    private void inIt() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        getNetWorker().mediaGet(token, loactionId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("获取点位信息");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        HemaArrayResult<MediaGet> result = (HemaArrayResult<MediaGet>) hemaBaseResult;
        mediaGet = result.getObjects().get(0);
        setData();
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
        //  if (mediaGet.getImgitem() == null || mediaGet.getImgitem().size() == 0) {
        radiogroup.setVisibility(View.INVISIBLE);
        adviewpager.setVisibility(View.INVISIBLE);
        yy_layout.setVisibility(View.INVISIBLE);
        String path = mediaGet.getImgurlbig_d();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ad_defalut_bg)
                .showImageForEmptyUri(R.mipmap.ad_defalut_bg)
                .showImageOnFail(R.mipmap.ad_defalut_bg).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, company_img, options);
        industry_name.setText("招商电话:" + mediaGet.getLinknum());
//        } else {
//            company_img.setVisibility(View.INVISIBLE);
//            yy_layout.setVisibility(View.INVISIBLE);
//        }
        // company_name.setText(mediaGet.getName() + "-----" + mediaGet.getIngress());
        company_name.setText(mediaGet.getName());
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
        //媒体规格
        media_address.setText("媒体规格:" + mediaGet.getSpec());
        //限制内容
        issue_user.setText("限制内容:" + mediaGet.getLimitself());
        String ge = mediaGet.getLimitself();
        if (isNull(ge))
            ge = "";
        if ("1".equals(mediaGet.getLimitcontent()))
            issue_user.setText("限制内容: 不限" + ge);
        else if ("2".equals(mediaGet.getLimitcontent()))
            issue_user.setText("限制内容: 房地产" + ge);
        else if ("3".equals(mediaGet.getLimitcontent()))
            issue_user.setText("限制内容: 医疗" + ge);
        else
            issue_user.setText("限制内容: 房地产,医疗" + ge);
        //所在区域
        issue_company.setText("所在区域:" + mediaGet.getProvince() + " " + mediaGet.getCity() + " " + mediaGet.getArea());
        //详细地址
        issue_tel.setText(" " + Html.fromHtml("<u>" + mediaGet.getAddress() + "</u>"));

        //加载图片
//        add_view_photo.removeAllViews();
//        if (mediaGet.getImgcloseitem() != null && mediaGet.getImgcloseitem().size() != 0)
//            for (int i = 0; i < mediaGet.getImgcloseitem().size(); i++) {
//                View view = LayoutInflater.from(mContext).inflate(R.layout.view_add_request, null);
//                ImageView im = (ImageView) view.findViewById(R.id.add_img);
//                String path = mediaGet.getImgcloseitem().get(i).getImgurlbig();
//                DisplayImageOptions options = new DisplayImageOptions.Builder()
//                        .showImageOnLoading(R.mipmap.ad_defalut_bg)
//                        .showImageForEmptyUri(R.mipmap.ad_defalut_bg)
//                        .showImageOnFail(R.mipmap.ad_defalut_bg).cacheInMemory(true)
//                        .bitmapConfig(Bitmap.Config.RGB_565).build();
//                ImageLoader.getInstance().displayImage(path, im, options);
//                im.setTag(R.id.TAG, i);
//                im.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int position = (int) v.getTag(R.id.TAG);
//                        Intent it = new Intent(mContext, ShowLargePicActivity.class);
//                        it.putExtra("position", position);
//                        it.putExtra("images", mediaGet.getImgcloseitem());
//                        it.putExtra("titleAndContentVisible", false);
//                        mContext.startActivity(it);
//                    }
//                });
//                add_view_photo.addView(view);
//            }
        //地图
        go_loaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TPInformationActivity.this, MediaOnlyMapActivity.class);
                intent.putExtra("keytype", "1");
                intent.putExtra("lng", mediaGet.getLng());
                intent.putExtra("lat", mediaGet.getLat());
                intent.putExtra("cityL", mediaGet.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("获取点位信息失败，请稍后重试");
    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
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
    }

    @Override
    protected void getExras() {
        loactionId = mIntent.getStringExtra("loactionId");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("点位详情");
        next_button.setVisibility(View.INVISIBLE);

    }
}
