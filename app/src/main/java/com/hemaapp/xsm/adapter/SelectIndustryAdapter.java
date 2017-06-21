package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.Industry;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/7.
 * 行业筛选的adapter
 */
public class SelectIndustryAdapter extends HemaAdapter {
    private ArrayList<Industry> industries;
    private Context mContext;

    public SelectIndustryAdapter(Context mContext, ArrayList<Industry> industries) {
        super(mContext);
        this.mContext = mContext;
        this.industries = industries;
    }

    public void setIndustries(ArrayList<Industry> industries) {
        this.industries = industries;
    }

    @Override
    public boolean isEmpty() {
        return industries == null || industries.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : industries.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_select_industry, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    //选择
    private class ViewHolder {
        TextView check_text;
    }

    private void findView(ViewHolder holder, View view) {
        holder.check_text = (TextView) view.findViewById(R.id.check_text);
    }

    private void setData(ViewHolder holder, int position) {
        final Industry industry = industries.get(position);
        holder.check_text.setText(industry.getName());
        if (industry.isCheck())
            holder.check_text.setBackgroundResource(R.drawable.bg_check_indu_on);
        else
            holder.check_text.setBackgroundResource(R.drawable.bg_check_indu_off);
        //选择
        holder.check_text.setTag(R.id.TAG_VIEWHOLDER, industry);
        holder.check_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Industry industry1 = (Industry) v.getTag(R.id.TAG_VIEWHOLDER);
                if (industry1.isCheck()) {
                } else {
                    for (int i = 0; i < industries.size(); i++) {
                        if (industry1 == industries.get(i)) {
                            industries.get(i).setCheck(true);
                        } else
                            industries.get(i).setCheck(false);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }
}
