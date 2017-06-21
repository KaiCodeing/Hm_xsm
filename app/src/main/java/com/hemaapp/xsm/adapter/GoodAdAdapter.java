package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hemaapp.hm_FrameWork.model.Image;
import com.hemaapp.hm_FrameWork.showlargepic.ShowLargePicActivity;
import com.hemaapp.xsm.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;

/**
 * Created by lenovo on 2017/1/11.
 */
public class GoodAdAdapter extends PagerAdapter {
    private Context mContext;
    private RadioGroup mIndicator;
    private View view;
    private int size;
    private XtomImageWorker imgWorker;
    private ArrayList<Image> indexads ;

    public GoodAdAdapter(Context mContext, RadioGroup mIndicator,
                         View view, ArrayList<Image> indexads) {
        this.mContext = mContext;
        this.mIndicator = mIndicator;
        this.view = view;
        this.indexads = indexads;
        imgWorker = new XtomImageWorker(mContext);
        init();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return indexads==null||indexads.size()==0?1:indexads.size();
    }

    private void init() {
        float density = mContext.getResources().getDisplayMetrics().density;
        size = (int) (density * 8);
        mIndicator = (RadioGroup) view.findViewById(R.id.radiogroup);
        mIndicator.removeAllViews();
        if (getCount() > 0)
            for (int i = 0; i < getCount(); i++) {
                RadioButton button = new RadioButton(mContext);
                button.setId(i);
                button.setClickable(false);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        size, size);
                params.leftMargin = (int) (3 * density);
                params.rightMargin = (int) (3 * density);
                if (i == 0)
                    button.setChecked(true);
                button.setButtonDrawable(android.R.color.transparent);
                button.setBackgroundResource(R.drawable.indicator_show);
                mIndicator.addView(button, params);
            }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void notifyDataSetChanged() {
        init();
        super.notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        // super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView mView;
        if (container.getChildAt(position) == null) {

            mView = new ImageView(mContext);
            mView.setScaleType(ImageView.ScaleType.CENTER_CROP);
          //  mView.setAdjustViewBounds(true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
//            double width= JhUtil.getScreenWidth(mContext);
//            double height=width/1*1;
//            params.width = (int) width;
//            params.height=(int) height;
               mView.setLayoutParams(params);
            container.addView(mView, position);
            if (indexads==null || indexads.size()==0)
            {
                mView.setImageResource(R.mipmap.ad_defalut_bg);
                return mView;
            }
            Image ad = indexads.get(position);
            mView.setImageResource(R.mipmap.ad_defalut_bg);
            try {
                XtomImageTask task = new XtomImageTask(mView, new URL(
                        ad.getImgurlbig()), mContext);
                imgWorker.loadImage(task);
            } catch (MalformedURLException e) {
                //	mView.setImageBitmap(null);
                mView.setImageResource(R.mipmap.ad_defalut_bg);
            }
            mView.setTag(R.id.TAG, position);
            mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int p = (int) v.getTag(R.id.TAG);
                    Intent it = new Intent(mContext, ShowLargePicActivity.class);
                    it.putExtra("position", p);
                    it.putExtra("images", indexads);
                    it.putExtra("titleAndContentVisible", false);
                    mContext.startActivity(it);
                }
            });
        } else
            mView = (ImageView) container.getChildAt(position);
        return mView;
    }

    public ViewGroup getIndicator() {
        return mIndicator;
    }

}
