package com.hemaapp.xsm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.ReplyImageAdapter;
import com.hemaapp.xsm.config.XsmConfig;
import com.hemaapp.xsm.model.CitySan;
import com.hemaapp.xsm.model.FileUploadResult;
import com.hemaapp.xsm.model.Media;
import com.hemaapp.xsm.model.MediaGet;
import com.hemaapp.xsm.view.AreaDialog;
import com.hemaapp.xsm.view.BottomPopuWindowTimePicker;
import com.hemaapp.xsm.view.JhctmGridView;
import com.hemaapp.xsm.view.JhctmImageWay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

/**
 * Created by lenovo on 2017/3/8.
 * 媒体发布
 */
public class MediaIssueActivity extends JhActivity {
    private ReplyImageAdapter adapter;
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private JhctmGridView gridview;//远景
    private JhctmGridView gridview2;//近景
    private TextView login_text;
    private EditText input_company_name;
    private LinearLayout layout_city;
    private TextView input_city;
    private LinearLayout number_layout;
    private TextView input_number;
    private EditText input_media_gg;
    private LinearLayout start_layout;
    private TextView select_start_date;
    private LinearLayout over_layout;
    private TextView select_over_date;
    private RadioGroup group1;
    private RadioGroup group2;
    private RadioGroup group3;
    private EditText content_text;
    private AreaDialog areaDialog;
    private String mediaId;
    private String province;//省
    private String city;//市
    private String area;//区
    private String district1;//省id
    private String district2;//市id
    private String district3;//区id
    private JhctmImageWay imageWay;
    private JhctmImageWay imageWay2;
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> images2 = new ArrayList<>();
    private int orderby = 0;
    private int getOrderby = 0;
    BottomPopuWindowTimePicker selectTime;
    private int timeType = 1;
    private ReplyImageAdapter adapter2;
    private String imageFar = "";
    private String imageNearly = "";
    private boolean typeImage = true;
    private String lng;
    private String lat;
    private MediaGet mediaGet;
    private CheckBox astrict_type_1;//不限
    private CheckBox astrict_type_2;//房地产
    private CheckBox astrict_type_3;//医疗
    private boolean type1 = false;
    private boolean type2 = false;
    private boolean type3 = false;
    private Media media;
    private boolean imageuri = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_media_issue);
        super.onCreate(savedInstanceState);
        //获取省市区
        if (JhctmApplication.getInstance().getCityInfo() == null)
            getNetWorker().districtALLList();
        imageWay = new JhctmImageWay(mContext, 1, 2) {
            @Override
            public void album() {
                Intent it = new Intent(mContext, AlbumActivity.class);
                it.putExtra("limitCount", 4 - images.size());// 图片选择张数限制
                startActivityForResult(it, albumRequestCode);
            }
        };
        adapter = new ReplyImageAdapter(mContext, images, "1");
        gridview.setAdapter(adapter);

        imageWay2 = new JhctmImageWay(mContext, 3, 4) {
            @Override
            public void album() {
                Intent it = new Intent(mContext, AlbumActivity.class);
                it.putExtra("limitCount", 4 - images2.size());// 图片选择张数限制
                startActivityForResult(it, albumRequestCode);
            }
        };
        adapter2 = new ReplyImageAdapter(mContext, images2, "2");
        gridview2.setAdapter(adapter2);
        //获取媒体详情
        if (!"0".equals(mediaId)) {
            String token = JhctmApplication.getInstance().getUser().getToken();
            getNetWorker().mediaGet(token, mediaId);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1: //相册
                album(data);
                break;
            case 2:
                camera();
                break;
            case 3: //相册
                album2(data);
                break;
            case 4:
                camera2();
                break;
            case 5:
                lat = data.getStringExtra("lat");
                lng = data.getStringExtra("lng");
                input_number.setText(data.getStringExtra("address"));
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showImageWay() {
        imageWay.show();
    }

    public void showImageWay2() {
        imageWay2.show();
    }

    @Override
    protected void onDestroy() {
        deleteCompressPics();
        super.onDestroy();
    }

    // 删除临时图片文件
    private void deleteCompressPics() {
        if (images.size() > 0)
            for (String string : images) {
                File file = new File(string);
                file.delete();
            }
        if (images2.size() > 0)
            for (String string : images2) {
                File file = new File(string);
                file.delete();
            }
    }

    private void camera() {
        String imagepath = imageWay.getCameraImage();

        new CompressPicTask().execute(imagepath);
    }

    private void camera2() {
        String imagepath = imageWay2.getCameraImage();

        new CompressPicTask2().execute(imagepath);
    }

    // 自定义相册选择时处理方法
    private void album(Intent data) {
        if (data == null)
            return;
        ArrayList<String> imgList = data.getStringArrayListExtra("images");
        if (imgList == null)
            return;
        for (String img : imgList) {
            new CompressPicTask().execute(img);
        }
    }

    // 自定义相册选择时处理方法
    private void album2(Intent data) {
        if (data == null)
            return;
        ArrayList<String> imgList = data.getStringArrayListExtra("images");
        if (imgList == null)
            return;
        for (String img : imgList) {
            new CompressPicTask2().execute(img);
        }
    }

    /**
     * 压缩图片
     */
    private class CompressPicTask extends AsyncTask<String, Void, Integer> {
        String compressPath;

        @Override
        protected Integer doInBackground(String... params) {
            try {

                String path = params[0];
                if (isNull(path))
                {
                    showTextDialog("无效的照片路径");
                    return 1;
                }
                String savedir = XtomFileUtil.getTempFileDir(mContext);
                compressPath = XtomImageUtil.compressPictureWithSaveDir(path,
                        XsmConfig.IMAGE_WIDTH, XsmConfig.IMAGE_WIDTH,
                        XsmConfig.IMAGE_QUALITY, savedir, mContext);
                return 0;
            } catch (IOException e) {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在压缩图片");
        }

        @Override
        protected void onPostExecute(Integer result) {
            cancelProgressDialog();
            switch (result) {
                case 0:
                    images.add(compressPath);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    showTextDialog("图片压缩失败");
                    break;

            }
        }
    }

    /**
     * 压缩图片
     */
    private class CompressPicTask2 extends AsyncTask<String, Void, Integer> {
        String compressPath;

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String path = params[0];
                if (isNull(path))
                {
                    showTextDialog("无效的照片路径");
                    return 1;
                }
                String savedir = XtomFileUtil.getTempFileDir(mContext);
                compressPath = XtomImageUtil.compressPictureWithSaveDir(path,
                        XsmConfig.IMAGE_WIDTH, XsmConfig.IMAGE_WIDTH,
                        XsmConfig.IMAGE_QUALITY, savedir, mContext);
                return 0;
            } catch (IOException e) {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在压缩图片");
        }

        @Override
        protected void onPostExecute(Integer result) {
            cancelProgressDialog();
            switch (result) {
                case 0:
                    images2.add(compressPath);
                    adapter2.notifyDataSetChanged();
                    break;
                case 1:
                    showTextDialog("图片压缩失败");
                    break;
            }
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                showProgressDialog("获取地区列表");
                break;
            case MEDIA_SAVE:
                showProgressDialog("保存媒体信息");
                break;
            case FILE_UPLOAD_RETURN_URL:
                showProgressDialog("保存媒体图片");
                break;
            case MEDIA_GET:
                showProgressDialog("获取发布信息");
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
            case MEDIA_SAVE:
            case FILE_UPLOAD_RETURN_URL:
            case MEDIA_GET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                HemaArrayResult<CitySan> result1 = (HemaArrayResult<CitySan>) hemaBaseResult;
//                CitySan citySan = result1.getObjects().get(0);
//                ArrayList<CitySan> citySanArrayList = ;
//                CitySan citySan = new CitySan();
                //     ArrayList<CityChildren> cityChildrens= result1.getObjects();
                CitySan citySan = result1.getObjects().get(0);
                getApplicationContext().setCityInfo(citySan);
                break;
            case MEDIA_SAVE:
//                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
//                mediaId = result.getObjects().get(0);
//                fileUpdate();
                if ("0".equals(mediaId))
                    showTextDialog("发布媒体成功！");
                else {
                    showTextDialog("修改媒体成功！");
                    setResult(RESULT_OK, mIntent);
                    mIntent.putExtra("media", media);
                }
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);

                break;
            case FILE_UPLOAD_RETURN_URL:
                HemaArrayResult<FileUploadResult> result2 = (HemaArrayResult<FileUploadResult>) hemaBaseResult;
                String far = result2.getObjects().get(0).getItem1();
                String nearly = result2.getObjects().get(0).getItem2();

                if (typeImage) {
                    imageFar = imageFar + far + ";" + nearly + "|";
                    if (imageuri)
                        media.setImgurl(nearly);
                } else {
                    imageNearly = imageNearly + far + ";" + nearly + "|";
                }
                //远景
                if (images.size() > 0) {
                    fileUpdate();
                } else if (images2.size() > 0)
                    fileUpdate2();
                else {
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    String title = input_company_name.getText().toString().trim();//点位名称
                    String address = input_number.getText().toString().trim();
                    String spec = input_media_gg.getText().toString().trim();
                    String up_date = select_start_date.getText().toString();
                    String down_date = select_over_date.getText().toString();
                    String cityText = input_city.getText().toString();
                    String mtype = "1";
                    if (group1.getCheckedRadioButtonId() == R.id.media_type_1)
                        mtype = "1";
                    else
                        mtype = "2";
                    String status = "1";
                    if (group2.getCheckedRadioButtonId() == R.id.show_type_1)
                        status = "1";
                    else
                        status = "2";
                    String limitcontent = "";
                    ///判断限制内容
                    if (type1)
                        limitcontent = "1";
                    else {
                        if (type2) {
                            if (type3)
                                limitcontent = "2,3";
                            else
                                limitcontent = "2";
                        } else {
                            if (type3)
                                limitcontent = "3";
                        }
                    }
                    String limitself = "";//其他限制内容
                    limitself = content_text.getText().toString().trim();
                    String ti = imageFar;
                    String t2 = imageNearly;
                    if (media != null) {
                        media.setDown_date(down_date);
                        media.setUp_date(up_date);
                        media.setName(title);
                        media.setStatus(status);
                        media.setMtype(mtype);
                    }
                    getNetWorker().mediaSave(token, mediaId, title, address, district1, district2, district3,
                            province, city, area, spec, up_date, down_date, mtype, status, limitcontent, limitself, lng, lat, imageFar.substring(0, imageFar.length() - 1), imageNearly.substring(0, imageNearly.length() - 1));
                    //
                }
                break;
            case MEDIA_GET:
                HemaArrayResult<MediaGet> result = (HemaArrayResult<MediaGet>) hemaBaseResult;
                setData(result.getObjects().get(0));
                break;
        }
    }

    //填充数据
    private void setData(MediaGet media) {
        mediaGet = media;
        input_company_name.setText(media.getName());
        input_city.setText(media.getProvince() + " " + media.getCity() + " " + media.getArea());
        input_number.setText(media.getAddress());
        input_media_gg.setText(media.getSpec());
        select_start_date.setText(media.getUp_date());
        select_over_date.setText(media.getDown_date());
        //省市区
        district1 = media.getDistrict1();
        district2 = media.getDistrict2();
        district3 = media.getDistrict3();
        province = media.getProvince();
        city = media.getCity();
        area = media.getArea();
        lat = media.getLat();
        lng = media.getLng();
        //判断媒体形式
        if (media.getMtype().equals("1"))
            group1.check(R.id.media_type_1);
        else
            group1.check(R.id.media_type_2);
        //判断展示状态
        if (media.getStatus().equals("1"))
            group2.check(R.id.show_type_1);
        else
            group2.check(R.id.show_type_2);
        //判断限制内容
        if (media.getLimitcontent().equals("1"))
            astrict_type_1.setChecked(true);
        else if (media.getLimitcontent().equals("2"))
            astrict_type_2.setChecked(true);
        else if (media.getLimitcontent().equals("3"))
            astrict_type_3.setChecked(true);
        else {
            astrict_type_2.setChecked(true);
            astrict_type_3.setChecked(true);
        }
        if (!isNull(media.getLimitself()))
            content_text.setText(media.getLimitself());
        //远景照片
        for (int i = 0; i < media.getImgitem().size(); i++) {
            images.add(media.getImgitem().get(i).getImgurlbig());
        }
        //近景照片
        for (int i = 0; i < media.getImgcloseitem().size(); i++) {
            images2.add(media.getImgcloseitem().get(i).getImgurlbig());
        }
        adapter.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

    //删除第一个
    public void dele1(int position) {
        mediaGet.getImgitem().remove(position);
    }

    //删除第二个
    public void dele2(int position) {
        mediaGet.getImgcloseitem().remove(position);
    }

    private void fileUpdate() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        //  getNetWorker().fileUpload(token, "2", mediaId, String.valueOf(orderby),"", "无", images.get(0));

        getNetWorker().fileUploadReturnUrl(token, "1", images.get(0));
        typeImage = true;
        images.remove(0);
        orderby++;
    }

    private void fileUpdate2() {
        String token = JhctmApplication.getInstance().getUser().getToken();
        // getNetWorker().fileUpload(token, "3", mediaId, String.valueOf(getOrderby),"", "无", images2.get(0));
        getNetWorker().fileUploadReturnUrl(token, "1", images2.get(0));
        typeImage = false;
        images2.remove(0);
        getOrderby++;
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
            case MEDIA_SAVE:
            case FILE_UPLOAD_RETURN_URL:
            case MEDIA_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                showTextDialog("获取地区失败，请稍后重试");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case MEDIA_SAVE:
                showTextDialog("媒体发布失败，请稍后重试");
                break;
            case FILE_UPLOAD_RETURN_URL:
                showTextDialog("媒体图片发布失败，请稍后重试");
                break;
            case MEDIA_GET:
                showTextDialog("获取媒体信息失败，请稍后重试");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        gridview = (JhctmGridView) findViewById(R.id.gridview);
        gridview2 = (JhctmGridView) findViewById(R.id.gridview2);
        login_text = (TextView) findViewById(R.id.login_text);
        input_company_name = (EditText) findViewById(R.id.input_company_name);
        layout_city = (LinearLayout) findViewById(R.id.layout_city);
        input_city = (TextView) findViewById(R.id.input_city);
        number_layout = (LinearLayout) findViewById(R.id.number_layout);
        input_number = (TextView) findViewById(R.id.input_number);
        input_media_gg = (EditText) findViewById(R.id.input_media_gg);
        start_layout = (LinearLayout) findViewById(R.id.start_layout);
        select_start_date = (TextView) findViewById(R.id.select_start_date);
        over_layout = (LinearLayout) findViewById(R.id.over_layout);
        select_over_date = (TextView) findViewById(R.id.select_over_date);
        group1 = (RadioGroup) findViewById(R.id.group1);
        group2 = (RadioGroup) findViewById(R.id.group2);
        group3 = (RadioGroup) findViewById(R.id.group3);
        content_text = (EditText) findViewById(R.id.content_text);
        astrict_type_1 = (CheckBox) findViewById(R.id.astrict_type_1);
        astrict_type_2 = (CheckBox) findViewById(R.id.astrict_type_2);
        astrict_type_3 = (CheckBox) findViewById(R.id.astrict_type_3);
    }

    @Override
    protected void getExras() {
        mediaId = mIntent.getStringExtra("mediaId");
        if (isNull(mediaId))
            mediaId = "0";
        media = (Media) mIntent.getSerializableExtra("meidaI");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("媒体发布");
        next_button.setVisibility(View.INVISIBLE);
        //发布
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = JhctmApplication.getInstance().getUser().getToken();
                String title = input_company_name.getText().toString().trim();//点位名称
                String address = input_number.getText().toString().trim();
                String spec = input_media_gg.getText().toString().trim();
                String up_date = select_start_date.getText().toString();
                String down_date = select_over_date.getText().toString();
                String cityText = input_city.getText().toString();
                String mtype = "1";
                if (group1.getCheckedRadioButtonId() == R.id.media_type_1)
                    mtype = "1";
                else
                    mtype = "2";
                String status = "1";
                if (group2.getCheckedRadioButtonId() == R.id.show_type_1)
                    status = "1";
                else
                    status = "2";
                String limitcontent = "";
//                if (group3.getCheckedRadioButtonId() == R.id.astrict_type_1)
//                    limitcontent = "1";
//                else if (group3.getCheckedRadioButtonId() == R.id.astrict_type_2)
//                    limitcontent = "2";
//                else
//                    limitcontent = "3";
                if (type1)
                    limitcontent = "1";
                else {
                    if (type2) {
                        if (type3)
                            limitcontent = "2,3";
                        else
                            limitcontent = "2";
                    } else {
                        if (type3)
                            limitcontent = "3";
                    }
                }
                String limitself = "";//其他限制内容
                limitself = content_text.getText().toString().trim();
                if (isNull(title)) {
                    showTextDialog("请填写点位名称");
                    return;
                }
                if (isNull(address)) {
                    showTextDialog("请填写精确位置");
                    return;
                }
                if (isNull(cityText)) {
                    showTextDialog("请填写所在区域");
                    return;
                }
                if (isNull(spec)) {
                    showTextDialog("请填写规格");
                    return;
                }
                if (isNull(limitcontent)) {
                    showTextDialog("请选择限制内容");
                    return;
                }
                if (isNull(up_date)) {
                    showTextDialog("请填写上刊日期");
                    return;
                }
                if (isNull(down_date)) {
                    showTextDialog("请填写下刊日期");
                    return;
                }
                if (images == null || images.size() == 0) {
                    showTextDialog("请选择远景照片");
                    return;
                }
                if (images2 == null || images2.size() == 0) {
                    showTextDialog("请选择近景照片");
                    return;
                }
                //lng=117.125137 lat=36.650673
                if (mediaGet != null) {
                    for (int i = 0; i < mediaGet.getImgitem().size(); i++) {
                        imageFar = imageFar + mediaGet.getImgitem().get(i).getImgurlbig() + ";" + mediaGet.getImgitem().get(i).getImgurl() + "|";
                    }
                    for (int i = 0; i < mediaGet.getImgcloseitem().size(); i++) {
                        imageNearly = imageNearly + mediaGet.getImgcloseitem().get(i).getImgurlbig() + ";" + mediaGet.getImgcloseitem().get(i).getImgurl() + "|";
                    }
                }
                for (int i = 0; i < 4; i++) {
                    if (images2.get(0).contains("http://"))
                        images2.remove(0);
                    if (images2.size() == 0)
                        break;
                }
                for (int i = 0; i < 4; i++) {
                    if (images.get(0).contains("http://"))
                        images.remove(0);
                    if (images.size() == 0)
                        break;
                }
                if (images.size() > 0) {
                    imageuri = true;
                    fileUpdate();
                } else if (images2.size() > 0)
                    fileUpdate2();
                else {
                    if (media != null) {
                        media.setDown_date(down_date);
                        media.setUp_date(up_date);
                        media.setName(title);
                        media.setStatus(status);
                        media.setMtype(mtype);
                    }
                    getNetWorker().mediaSave(token, mediaId, title, address, district1, district2, district3,
                            province, city, area, spec, up_date, down_date, mtype, status, limitcontent, limitself, lng, lat, imageFar.substring(0, imageFar.length() - 1), imageNearly.substring(0, imageNearly.length() - 1));
                    //
                }

            }
        });
        //选择地区
        layout_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCity();
            }
        });
        //选择开始时间
        select_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                timeType = 1;
                showSelectTimeBottomPopu();
            }
        });
        //结束时间
        select_over_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                timeType = 0;
                showSelectTimeBottomPopu();
            }
        });
        //选择点
        number_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaIssueActivity.this, MediaOnlyMapActivity.class);
                startActivityForResult(intent, 5);
            }
        });
        //不限
        astrict_type_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type1 = true;
                    astrict_type_2.setChecked(false);
                    astrict_type_3.setChecked(false);
                } else
                    type1 = false;
            }
        });
        //房地产
        astrict_type_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    astrict_type_1.setChecked(false);
                    type2 = true;
                } else
                    type2 = false;
            }
        });
        //医疗
        astrict_type_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    astrict_type_1.setChecked(false);
                    type3 = true;
                } else
                    type3 = false;
            }
        });
    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext);
//            areaDialog.closeSan();
            areaDialog.setButtonListener(new onbutton());
            return;
        }
        areaDialog.show();
    }

    private class onbutton implements com.hemaapp.xsm.view.AreaDialog.OnButtonListener {

        @Override
        public void onLeftButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub

            areaDialog.cancel();
        }


        @Override
        public void onRightButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub
            input_city.setText(areaDialog.getCityName());
//            homecity = home_text.getText().toString();
//            home_text.setTag(areaDialog.getId());
            String[] cityid = areaDialog.getId().split(",");
            String[] cityName = areaDialog.getCityName().split(" ");
            district1 = cityid[0];
            district2 = cityid[1];
            district3 = cityid[2];
            province = cityName[0];
            city = cityName[1];
            area = cityName[2];
            log_i("++" + province + " ---" + city + "==" + area);
            areaDialog.cancel();
        }
    }

    //选择时间
    public void showSelectTimeBottomPopu() {
        selectTime = new BottomPopuWindowTimePicker(mContext, popuWidnwTimePickerOnClick, 2017, 10);
        //显示窗口
        selectTime.showAtLocation(select_start_date, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private BottomPopuWindowTimePicker.OnBackClickForString popuWidnwTimePickerOnClick = new BottomPopuWindowTimePicker.OnBackClickForString() {
        @Override
        public void onBackResult(String string) {
            if (timeType == 1)
                select_start_date.setText(string);
            else
                select_over_date.setText(string);
        }
    };

}
