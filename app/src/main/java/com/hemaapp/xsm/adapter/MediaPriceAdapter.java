package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.Price;
import com.hemaapp.xsm.model.PriceList;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/9.
 * 媒体价格
 */
public class MediaPriceAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<PriceList> priceLists;

    public MediaPriceAdapter(Context mContext, ArrayList<PriceList> priceList) {
        this.mContext = mContext;
        this.priceLists = priceList;
    }

    public void setPriceLists(ArrayList<PriceList> priceLists) {
        this.priceLists = priceLists;
    }

    @Override
    public int getGroupCount() {
        return (priceLists == null || priceLists.size() == 0) ? 0 : priceLists.size();

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (priceLists.get(groupPosition).getPrice() == null || priceLists.get(groupPosition).getPrice().size() == 0) ? 0 : priceLists.get(groupPosition).getPrice().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return priceLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return priceLists.get(groupPosition).getPrice().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_grpup_item, null);
            GroupView holder = new GroupView();
            findGroupView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        GroupView holder = (GroupView) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setGroupView(holder, groupPosition, isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_price_group_item, null);
            ChildView holder = new ChildView();
            findChildView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ChildView holder = (ChildView) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setChildView(holder, groupPosition, childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    //父控件
    private class GroupView {
        LinearLayout city_layout;
        TextView city_name;
        ImageView show_bz;
    }

    private void findGroupView(GroupView groupView, View view) {
        groupView.city_layout = (LinearLayout) view.findViewById(R.id.city_layout);
        groupView.city_name = (TextView) view.findViewById(R.id.city_name);
        groupView.show_bz = (ImageView) view.findViewById(R.id.show_bz);
    }

    private void setGroupView(GroupView groupView, int position, boolean isExpanded) {
        PriceList priceList = priceLists.get(position);
        groupView.city_name.setText(priceList.getName());
        if (isExpanded)
            groupView.show_bz.setImageResource(R.mipmap.service_shang_jian_img);
        else
            groupView.show_bz.setImageResource(R.mipmap.service_xia_jian_img);
    }

    //字控件
    private class ChildView {
        LinearLayout title_layout;
        TextView lianmen_title;
        TextView chengjiao_title;
        TextView kanli_title;
        TextView city_name;
        TextView lianmen_text;
        TextView chengjiao_text;
        TextView kanji_text;
    }

    private void findChildView(ChildView childView, View view) {
        childView.title_layout = (LinearLayout) view.findViewById(R.id.title_layout);
        childView.lianmen_title = (TextView) view.findViewById(R.id.lianmen_title);
        childView.chengjiao_title = (TextView) view.findViewById(R.id.chengjiao_title);
        childView.kanli_title = (TextView) view.findViewById(R.id.kanli_title);
        childView.city_name = (TextView) view.findViewById(R.id.city_name);
        childView.lianmen_text = (TextView) view.findViewById(R.id.lianmen_text);
        childView.chengjiao_text = (TextView) view.findViewById(R.id.chengjiao_text);
        childView.kanji_text = (TextView) view.findViewById(R.id.kanji_text);
    }

    private void setChildView(ChildView childView, int groupPosition, int childPosition) {
        Price price = priceLists.get(groupPosition).getPrice().get(childPosition);
        childView.city_name.setText(price.getCity());
        childView.lianmen_text.setText(price.getUnionprice());
        childView.chengjiao_text.setText(price.getDealprice1() + "-" + price.getDealprice2());
        childView.kanji_text.setText(price.getDemoprcie());
        if (childPosition == 0) {
            childView.title_layout.setVisibility(View.VISIBLE);
        } else
            childView.title_layout.setVisibility(View.GONE);
        //判断用户类型
        String feeaccount = JhctmApplication.getInstance().getUser().getFeeaccount();
        //普通
        if ("1".equals(feeaccount)) {
            childView.lianmen_title.setVisibility(View.GONE);
            childView.lianmen_text.setVisibility(View.GONE);
            childView.chengjiao_title.setVisibility(View.GONE);
            childView.chengjiao_text.setVisibility(View.GONE);
        }
        //CEO
        else if ("2".equals(feeaccount)) {
            childView.lianmen_title.setVisibility(View.VISIBLE);
            childView.lianmen_text.setVisibility(View.VISIBLE);
            childView.chengjiao_title.setVisibility(View.VISIBLE);
            childView.chengjiao_text.setVisibility(View.VISIBLE);
        } else {
            childView.lianmen_title.setVisibility(View.GONE);
            childView.lianmen_text.setVisibility(View.GONE);
            childView.chengjiao_title.setVisibility(View.VISIBLE);
            childView.chengjiao_text.setVisibility(View.VISIBLE);
        }
    }
}
