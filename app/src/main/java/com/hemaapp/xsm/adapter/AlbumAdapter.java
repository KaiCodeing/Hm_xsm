package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.album.ImageLoader;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.AlbumActivity;

import java.util.ArrayList;

import xtom.frame.util.XtomWindowSize;

/**
 * Created by WangYuxia on 2017/2/14.
 */

public class AlbumAdapter extends HemaAdapter {
    private AlbumActivity mContext;
    private ArrayList<String> imgs;
    private int limit;
    private int count = 0;
    public String imgPath;

    private int mScreenWidth;
    private int item_width;
    private ArrayList<Boolean> isResult = new ArrayList<Boolean>();

    public AlbumAdapter(Context mContext, ArrayList<String> imgs, int limit) {
        super(mContext);
        this.mContext = (AlbumActivity) mContext;
        this.imgs = imgs;
        this.limit = limit;

        if(imgs != null && imgs.size() > 0){
            for(int i = 0; i<imgs.size(); i++){
                isResult.add(i, false);
            }
        }

        mScreenWidth = XtomWindowSize.getWidth(mContext);
        item_width = (int) ((mScreenWidth / 4.0 + 0.5f));
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_album, null);
            holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        String path = imgs.get(position);
        setData(holder, path, position);
        return convertView;
    }

    private void setData(final ViewHolder h, String path, int position) {
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(path, h.img);
        if(isResult.get(position)){
            h.box.setImageResource(R.drawable.box_album_y);
        }else {
            h.box.setImageResource(R.drawable.box_album_n);;
        }
        h.img.setTag(R.id.TAG, path);
        h.img.setTag(R.id.button, position);
        h.img.setOnClickListener(new View.OnClickListener() {
            boolean isshow = false;

            @Override
            public void onClick(View v) {
                imgPath = (String) v.getTag(R.id.TAG);
                int position = (Integer) v.getTag(R.id.button);
                if (isshow) {
                    h.box.setImageResource(R.drawable.box_album_n);;
                    count--;
                    isshow = !isshow;
                    isResult.set(position, false);
                    mContext.changeHsv(imgPath, false);
                } else {
                    if (count == limit) {
                        mContext.showTextDialog("抱歉，最多能选择" + limit + "张图片");
                    } else {
                        h.box.setImageResource(R.drawable.box_album_y);
                        count++;
                        isResult.set(position, true);
                        isshow = !isshow;
                        mContext.changeHsv(imgPath, true);
                    }
                }
            }
        });
    }

    private void findView(ViewHolder h, View v) {
        h.img = (ImageView) v.findViewById(R.id.album_img);
        h.box = (ImageView) v.findViewById(R.id.album_box);
        h.img.getLayoutParams().height = item_width;
    }

    private static class ViewHolder {
        ImageView img;
        ImageView box;
    }
}

