package com.hemaapp.xsm.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.AdsActivity;


/**
 * Created by lenovo on 2016/9/19.
 */
public class AdsAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private AdsActivity mContext;
    private ViewHolder viewHolder;
    private int size;
    private TextView dianjiTextView;
    public AdsAdapter(AdsActivity mContext,
                      RadioGroup radioGroup, ViewPager viewPager, TextView dianjiTextView) {
        this.radioGroup = radioGroup;
        this.viewPager = viewPager;
        this.mContext = mContext;
        this.dianjiTextView = dianjiTextView;
        this.viewPager.setOnPageChangeListener(this);
        initIndicator();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    private void initIndicator() {
        // 设置提示原点
        float density = mContext.getResources()
                .getDisplayMetrics().density;
        size = (int) (density * 15);
        radioGroup.removeAllViews();
        if (getCount() > 1) {
            for (int i = 0; i < getCount(); i++) {
                RadioButton button = new RadioButton(mContext);
                button.setButtonDrawable(R.drawable.indicator_show);
                button.setChecked(false);
                button.setId(i);
                button.setClickable(false);
                int width = (int) ((i == getCount() - 1) ? size : size * 1.5);
                LayoutParams params = new LayoutParams(width, size);
                button.setLayoutParams(params);
                if (i == 0) {
                    button.setChecked(true);
                }
                radioGroup.addView(button);
            }
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        View mView = null;
//		if (container.getChildAt(position) == null) {
        mView = LayoutInflater.from(mContext).inflate(
                R.layout.item_software_imageparger, null);
        viewHolder = new ViewHolder();
        findview(mView, viewHolder);
        switch (position) {
            case 0:
                viewHolder.imageView.setImageResource(R.mipmap.start1);
                break;
            case 1:
                viewHolder.imageView.setImageResource(R.mipmap.start2);
                break;
            case 2:
                viewHolder.imageView.setImageResource(R.mipmap.start3);
                break;
            default:
                break;
        }
//		}
        container.addView(mView);
        return mView;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
//		super.destroyItem(container, position, object);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
        switch (position) {

            case 1:
            case 2:
                dianjiTextView.setVisibility(View.VISIBLE);
                break;
            case 0:
                dianjiTextView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void findview(View mView, ViewHolder vHolder) {
        vHolder.imageView = (ImageView) mView.findViewById(R.id.food_img);
    }

    public static class ViewHolder {
        ImageView imageView;
    }

}
