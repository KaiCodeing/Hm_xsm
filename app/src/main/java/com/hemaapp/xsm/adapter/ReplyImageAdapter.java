package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.showlargepic.ShowLargePicActivity;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.LoactionReserveActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;

/**
 * Created by WangYuxia on 2017/2/13.
 */

public class ReplyImageAdapter extends HemaAdapter implements View.OnClickListener {
    private static final int TYPE_ADD = 0;
    private static final int TYPE_IMAGE = 1;

    private ArrayList<String> images;
    private String type;
    public ReplyImageAdapter(Context mContext,
                             ArrayList<String> images,String type) {
        super(mContext);
        this.images = images;
        this.type = type;
    }

    @Override
    public int getCount() {
        int count;
        int size = images == null ? 0 : images.size();
        if (size < 4)
            count = size + 1;
        else
            count = 4;
        return count;
    }

    @Override
    public boolean isEmpty() {
        int size = images == null ? 0 : images.size();
        return size == 0;
    }

    @Override
    public int getItemViewType(int position) {
        int size = images == null ? 0 : images.size();
        int count = getCount();
        if (size < 4) {
            if (position == count - 1)
                return TYPE_ADD;
            else
                return TYPE_IMAGE;
        } else {
            return TYPE_IMAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder = null;
        if (convertView == null) {
            switch (type) {
                case TYPE_ADD:
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.griditem_image_add, null);
                    holder = new ViewHolder();
                    findView(convertView, holder);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
                    break;
                case TYPE_IMAGE:
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.griditem_image_content, null);
                    holder = new ViewHolder();
                    findView(convertView, holder);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }

        switch (type) {
            case TYPE_ADD:
                setDataAdd( holder);
                break;
            case TYPE_IMAGE:
                setDataImage(position, holder);
                break;
        }

        return convertView;
    }

    private void setDataAdd( ViewHolder holder) {
        holder.addButton.setOnClickListener(this);
    }

    private void setDataImage(int position, ViewHolder holder) {
        String path = images.get(position);
        holder.deleteButton.setTag(path);
        holder.deleteButton.setOnClickListener(this);

        JhActivity activity = (JhActivity) mContext;
        holder.imageView.setCornerRadius(2);
        if (path.contains("http://"))
        {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.case_defult_img)
                    .showImageForEmptyUri(R.mipmap.case_defult_img)
                    .showImageOnFail(R.mipmap.case_defult_img).cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoader.getInstance().displayImage(path, holder.imageView, options);
        }
        else
        {
            XtomImageTask imageTask = new XtomImageTask(holder.imageView, path,
                    mContext);
            activity.imageWorker.loadImage(imageTask);
        }
        holder.imageView.setTag(R.id.TAG, position);
        holder.deleteButton.setTag(R.id.TAG,position);
        holder.imageView.setOnClickListener(this);
    }

    private void findView(View view, ViewHolder holder) {
        holder.addButton = (ImageView) view.findViewById(R.id.image_add);
        holder.imageView = (RoundedImageView) view.findViewById(R.id.imageview);
        holder.deleteButton = (ImageView) view.findViewById(R.id.delete);
    }

    private static class ViewHolder {
        ImageView addButton;
        RoundedImageView imageView;
        ImageView deleteButton;
    }

    @Override
    public void onClick(View v) {
       LoactionReserveActivity activity = (LoactionReserveActivity) mContext;
        switch (v.getId()) {
            case R.id.image_add:
                if ("1".equals(type))
              activity.showImageWay();
                else
                activity.showImageWay2();
                break;
            case R.id.delete:
                String dPath = (String) v.getTag();
                int position1 = (int) v.getTag(R.id.TAG);
                File file = new File(dPath);
                file.delete();
                images.remove(dPath);
//                if (dPath.contains("http://"))
//                {
//                    if ("1".equals(type))
//                        activity.dele1(position1);
//                    else
//                        activity.dele2(position1);
//                }
                notifyDataSetChanged();
                break;
            case R.id.imageview:
                int position = (int) v.getTag(R.id.TAG);
                Intent sIt = new Intent(mContext, ShowLargePicActivity.class);
                sIt.putExtra("position", position);
                sIt.putExtra("imagelist", images);
                mContext.startActivity(sIt);
                break;
        }
    }
}