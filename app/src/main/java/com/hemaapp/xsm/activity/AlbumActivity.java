package com.hemaapp.xsm.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhUtil;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.AlbumAdapter;
import com.hemaapp.xsm.config.XsmConfig;

import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

/**
 * Created by WangYuxia on 2017/2/14.
 */

public class AlbumActivity extends JhActivity {
    private ImageButton titleLeft;
    private TextView titleTxt;
    private Button titleRight;
    private GridView grid;
    private LinearLayout mLL;
    private TextView finishBtn;
    private TextView numTxt;

    private int limitCount;
    private ArrayList<String> mImgs = new ArrayList<>();//
    private AlbumAdapter adapter;
    private ArrayList<String> hsvImgs = new ArrayList<>();//
    private ArrayList<String> beforeImgs = new ArrayList<>();//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_album);
        super.onCreate(savedInstanceState);
        getImgs();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            cancelProgressDialog();
            setAdapter();
        }
    };

    private void setAdapter() {
        if (mImgs.size() == 0) {
            Toast.makeText(getApplicationContext(), "抱歉，没有找到一张图片",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        adapter = new AlbumAdapter(mContext, mImgs, limitCount);
        grid.setAdapter(adapter);
        grid.setSelection(adapter.getCount() - 1);
    }

    private void getImgs() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog("正在加载...");

        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = AlbumActivity.this
                        .getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED);
                Log.e("TAG", mCursor.getCount() + "");

                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.e("TAG", path);

                    if (mImgs.contains(path))
                        continue;
                    else
                        mImgs.add(path);
                }

                mCursor.close();
                mHandler.sendEmptyMessage(0x110);
            }
        }).start();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
    }

    @Override
    protected void findView() {
        titleLeft = (ImageButton) findViewById(R.id.back_button);
        titleTxt = (TextView) findViewById(R.id.title_text);
        titleRight = (Button) findViewById(R.id.next_button);
        grid = (GridView) findViewById(R.id.album_grid);
        mLL = (LinearLayout) findViewById(R.id.album_content);
        finishBtn = (TextView) findViewById(R.id.album_finish);
        numTxt = (TextView) findViewById(R.id.album_num);
    }

    @Override
    protected void getExras() {
        limitCount = mIntent.getIntExtra("limitCount", 0);
    }

    @Override
    protected void setListener() {
        titleLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTxt.setText("相册");
        titleRight.setText("取消");
        titleRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        finishBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.putExtra("images", hsvImgs);
                setResult(RESULT_OK, it);
                finish();
            }
        });
    }

    public void changeHsv(String path, boolean isAdd) {
        if (isAdd) {
            beforeImgs.add(path);
            new CompressPicTask().execute(path);
        } else {
            int index = beforeImgs.indexOf(path);
            beforeImgs.remove(path);
            hsvImgs.remove(index);
            mLL.removeViewAt(index);
            if (hsvImgs.size() == 0) {
                numTxt.setVisibility(View.INVISIBLE);
            } else {
                numTxt.setText("" + hsvImgs.size());
            }
        }
    }

    private void addHsv(String path) {
        LinearLayout ll = new LinearLayout(mContext);
        ImageView img = new ImageView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mLL.getHeight(), ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(JhUtil.dip2px(mContext, 2), 0, JhUtil.dip2px(mContext, 2), 0);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageWorker.loadImage(new XtomImageTask(img, path, mContext));
        ll.addView(img, lp);
        mLL.addView(ll);
    }

    private class CompressPicTask extends AsyncTask<String, Void, Integer> {
        String compressPath;

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String path = params[0];
                String savedir = XtomFileUtil.getTempFileDir(mContext);
                if(isNull(path) || isNull(savedir)){
                    return 1;
                }else{
                    compressPath = XtomImageUtil.compressPictureWithSaveDir(path,
                            XsmConfig.IMAGE_WIDTH, XsmConfig.IMAGE_WIDTH,
                            XsmConfig.IMAGE_QUALITY, savedir, mContext);
                    return 0;
                }
            } catch (IOException e) {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Integer result) {
            cancelProgressDialog();
            switch (result) {
                case 0:
                    hsvImgs.add(compressPath);
                    addHsv(compressPath);
                    numTxt.setVisibility(View.VISIBLE);
                    numTxt.setText("" + hsvImgs.size());
                    break;
                case 1:
                    showTextDialog("图片压缩失败");
                    break;
            }
        }
    }
}
