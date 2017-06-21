package com.hemaapp.xsm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.PTAdapter;
import com.hemaapp.xsm.adapter.ReplyImageAdapter;
import com.hemaapp.xsm.config.XsmConfig;
import com.hemaapp.xsm.model.FileUploadResult;
import com.hemaapp.xsm.model.Media;
import com.hemaapp.xsm.view.BottomPopuWindowTimePicker;
import com.hemaapp.xsm.view.JhctmGridView;
import com.hemaapp.xsm.view.JhctmImageWay;
import com.hemaapp.xsm.view.MyListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

/**
 * Created by lenovo on 2017/6/7.
 * 点位预定
 */
public class LoactionReserveActivity extends JhActivity {
    private TextView title_text;
    private ImageButton back_button;
    private Button next_button;
    private LinearLayout start_layout;//上刊时间
    private LinearLayout over_layout;//下刊时间
    private LinearLayout layout_city;//点位名称
    private EditText input_company_name;//客户名称
    private TextView select_start_date;
    private TextView select_over_date;
    private TextView input_city;
    private MyListView listview;
    private int timeType = 1;
    BottomPopuWindowTimePicker selectTime;
    private TextView login_text;
    private String loactionId;
    private PTAdapter adapter;
    private String keytype;
    private LinearLayout zp_layout;
    private ImageView check_tuisong;
    private LinearLayout dw_layout;//调整点位
    private JhctmGridView gridview;
    private JhctmGridView gridview2;
    private String change_flag = "1";
    private JhctmImageWay imageWay;
    private JhctmImageWay imageWay2;
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> images2 = new ArrayList<>();
    private ReplyImageAdapter adapter2;
    private String imageFar = "";
    private String imageNearly = "";
    private boolean typeImage = true;
    private boolean imageuri = false;
    private ReplyImageAdapter imageadapter;
    private int orderby = 0;
    private int getOrderby = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reserve_loaction);
        super.onCreate(savedInstanceState);
        if (!isNull(keytype)) {
            imageWay = new JhctmImageWay(mContext, 1, 2) {
                @Override
                public void album() {
                    Intent it = new Intent(mContext, AlbumActivity.class);
                    it.putExtra("limitCount", 4 - images.size());// 图片选择张数限制
                    startActivityForResult(it, albumRequestCode);
                }
            };
            imageadapter = new ReplyImageAdapter(mContext, images, "1");
            gridview.setAdapter(imageadapter);

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
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case POINT_PRE:
                showProgressDialog("提交点位预定信息");
                break;
            case FILE_UPLOAD_RETURN_URL:
                showProgressDialog("提交照片...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case POINT_PRE:
                showTextDialog("提交成功");
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
                } else {
                    imageNearly = imageNearly + far + ";" + nearly + "|";
                }
                //远景
                if (images.size() > 0) {
                    fileUpdate();
                } else if (images2.size() > 0)
                    fileUpdate2();
                else {
                    String up_data = select_start_date.getText().toString();
                    String down_data = select_over_date.getText().toString();
                    String userName = input_company_name.getText().toString();
                    String token = JhctmApplication.getInstance().getUser().getToken();
                    if (!isNull(imageNearly))
                    getNetWorker().mediaSave(token, "1", change_flag, loactionId, up_data, down_data, userName, imageFar.substring(0, imageFar.length() - 1), imageNearly.substring(0, imageNearly.length() - 1));
                    else
                        getNetWorker().mediaSave(token, "1", change_flag, loactionId, up_data, down_data, userName, imageFar.substring(0, imageFar.length() - 1), "");
                }
                break;
            case MEDIA_SAVE:
                showTextDialog("提交成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                break;

        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case POINT_PRE:
                showTextDialog("提价失败，请稍后重试");
                break;
            case FILE_UPLOAD_RETURN_URL:
                showTextDialog("提交照片失败，请稍后重试");
                break;
            case MEDIA_SAVE:
                showTextDialog("提价失败，请稍后重试");
                break;
        }

    }

    @Override
    protected void findView() {
        title_text = (TextView) findViewById(R.id.title_text);
        back_button = (ImageButton) findViewById(R.id.back_button);
        next_button = (Button) findViewById(R.id.next_button);
        start_layout = (LinearLayout) findViewById(R.id.start_layout);
        over_layout = (LinearLayout) findViewById(R.id.over_layout);
        layout_city = (LinearLayout) findViewById(R.id.layout_city);
        input_company_name = (EditText) findViewById(R.id.input_company_name);
        select_start_date = (TextView) findViewById(R.id.select_start_date);
        select_over_date = (TextView) findViewById(R.id.select_over_date);
        input_city = (TextView) findViewById(R.id.input_city);
        listview = (MyListView) findViewById(R.id.listview);
        login_text = (TextView) findViewById(R.id.login_text);
        zp_layout = (LinearLayout) findViewById(R.id.zp_layout);
        check_tuisong = (ImageView) findViewById(R.id.check_tuisong);
        dw_layout = (LinearLayout) findViewById(R.id.dw_layout);
        gridview = (JhctmGridView) findViewById(R.id.gridview);
        gridview2 = (JhctmGridView) findViewById(R.id.gridview2);
    }

    @Override
    protected void getExras() {
        keytype = getIntent().getStringExtra("keytype");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 0:
                ArrayList<Media> medias = (ArrayList<Media>) data.getSerializableExtra("medias");
                //遍历集合获取id
                StringBuffer bufferid = new StringBuffer();
                ArrayList<Media> medias1 = new ArrayList<>();
                int i = 0;
                for (Media media : medias
                        ) {
                    if (media.isCheck()) {
                        bufferid.append(media.getId() + ",");
                        i++;
                        medias1.add(media);
                    }
                }
                if (i != 0)
                    loactionId = bufferid.substring(0, bufferid.length() - 1);
                freshData(medias1);
                input_city.setText("已选择"+String.valueOf(i)+"个点位");
                break;
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
        }
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
                    imageadapter.notifyDataSetChanged();
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
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (isNull(keytype)) {
            title_text.setText("点位预定");
            next_button.setText("预定列表");
            login_text.setText("提交");
        } else {
            title_text.setText("媒体发布");
            next_button.setVisibility(View.INVISIBLE);
            zp_layout.setVisibility(View.VISIBLE);
            dw_layout.setVisibility(View.VISIBLE);
        }
        //是否可调整点位
        check_tuisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change_flag.equals("1")) {
                    change_flag = "2";
                    check_tuisong.setImageResource(R.mipmap.tuisong_on_img);
                } else {
                    change_flag = "1";
                    check_tuisong.setImageResource(R.mipmap.tuisong_off_img);
                }
            }
        });
        //预定列表
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReserveListActivity.class);
                startActivity(intent);
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
        //选择点位
        layout_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoactionReserveActivity.this, LoactionSelectActivity.class);
                if (isNull(keytype))
                    intent.putExtra("keytype", "5");
                else
                    intent.putExtra("keytype", "5");
                startActivityForResult(intent, 0);
            }
        });
        //提交
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String up_data = select_start_date.getText().toString();
                String down_data = select_over_date.getText().toString();
                String userName = input_company_name.getText().toString();
                if (isNull(up_data)) {
                    showTextDialog("请选择上刊日期");
                    return;
                }
                if (isNull(down_data)) {
                    showTextDialog("请选择下刊日期");
                    return;
                }
                if (isNull(userName)) {
                    showTextDialog("请填写客户名称");
                    return;
                }
                if (isNull(loactionId)) {
                    showTextDialog("请选择点位");
                    return;
                }

                String token = JhctmApplication.getInstance().getUser().getToken();
                if (isNull(keytype))
                    getNetWorker().pointPre(token, loactionId, up_data, down_data, userName);
                else {
                    if (images == null || images.size() == 0) {
                        showTextDialog("请选择远景照片");
                        return;
                    }
//                    if (images2 == null || images2.size() == 0) {
//                        showTextDialog("请选择近景照片");
//                        return;
//                    }
                    if (images.size() > 0) {
                        imageuri = true;
                        fileUpdate();
                    } else if (images2.size() > 0)
                        fileUpdate2();
                }
            }
        });
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
