package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.Industry;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/8.
 * 我要报备中的选择行业
 */
public class MyReportIndustryAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<Industry> industries;
    public MyReportIndustryAdapter(Context mContext,ArrayList<Industry> industries) {
        super(mContext);
        this.mContext = mContext;
        this.industries = industries;
    }

    @Override
    public boolean isEmpty() {
        return industries==null || industries.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:industries.size();
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
        if (isEmpty())
            return  getEmptyView(parent);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_report_industry, null);
            ViewHolder holder = new ViewHolder();
            findView(holder,convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,position);
        return convertView;
    }
    private class ViewHolder{
        LinearLayout layout_industry;
        TextView industry_name;
        ImageView check;
        View line;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.layout_industry = (LinearLayout) view.findViewById(R.id.layout_industry);
        holder.industry_name = (TextView) view.findViewById(R.id.industry_name);
        holder.check = (ImageView) view.findViewById(R.id.check);
        holder.line = view.findViewById(R.id.line);
    }
    private void setData(ViewHolder holder,int position)
    {
        Industry industry = industries.get(position);
        holder.industry_name.setText(industry.getName());
        if (industry.isCheck())
        {
            holder.check.setVisibility(View.VISIBLE);
        }
        else
            holder.check.setVisibility(View.GONE);
    }
}
