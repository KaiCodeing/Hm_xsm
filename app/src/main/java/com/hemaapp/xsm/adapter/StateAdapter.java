package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.MyCollectActivity;
import com.hemaapp.xsm.activity.MyReportActivity;
import com.hemaapp.xsm.activity.StateInformationActivity;
import com.hemaapp.xsm.model.Report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lenovo on 2017/3/3.
 * 全国单
 */
public class StateAdapter extends HemaAdapter {
    private ArrayList<Report> reports;
    private String keytype;
    private String getKeytype;

    public StateAdapter(Context mContext, ArrayList<Report> reports, String keytype) {
        super(mContext);
        this.mContext = mContext;
        this.reports = reports;
        this.keytype = keytype;
    }

    public StateAdapter(Context mContext, ArrayList<Report> reports, String keytype, String key) {
        super(mContext);
        this.mContext = mContext;
        this.reports = reports;
        this.keytype = keytype;
        this.getKeytype = key;
    }

    public void setReports(ArrayList<Report> reports) {
        this.reports = reports;
    }

    @Override
    public boolean isEmpty() {
        return reports == null || reports.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : reports.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (isEmpty())
            return getEmptyView(viewGroup);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_state_item, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, view);
            view.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, i);
        return view;
    }

    private class ViewHolder {
        LinearLayout state_layout;
        TextView company_name;
        TextView issue_type;
        TextView name_time;
        TextView address_text;
        TextView position_text;
        TextView cause_text;
        ImageView genjin_view;
    }

    private void findView(ViewHolder holder, View view) {
        holder.state_layout = (LinearLayout) view.findViewById(R.id.state_layout);
        holder.company_name = (TextView) view.findViewById(R.id.company_name);
        holder.issue_type = (TextView) view.findViewById(R.id.issue_type);
        holder.name_time = (TextView) view.findViewById(R.id.name_time);
        holder.address_text = (TextView) view.findViewById(R.id.address_text);
        holder.position_text = (TextView) view.findViewById(R.id.position_text);
        holder.cause_text = (TextView) view.findViewById(R.id.cause_text);
        holder.genjin_view = (ImageView) view.findViewById(R.id.genjin_view);
    }

    private void setData(ViewHolder holder, int position) {
        Report report = reports.get(position);
        holder.company_name.setText(report.getName());
        //发布状态
        if ("1".equals(report.getStatus())) {
            holder.issue_type.setBackgroundResource(R.drawable.sh_text_doom);
            holder.issue_type.setText("已报备");
        } else if ("2".equals(report.getStatus())) {
            holder.issue_type.setBackgroundResource(R.drawable.sh_text_doom);
            holder.issue_type.setText("已发布");
        } else {
            holder.issue_type.setBackgroundResource(R.drawable.sh_text_gate);
            holder.issue_type.setText("已结束");
        }
        //公司名称 时间
        holder.name_time.setText(report.getCompony() + " " + report.getRegdate());
        //投放区域
        holder.address_text.setText("投放区域:" + report.getArea());
        //所属行业
        holder.position_text.setText("所属行业:" + report.getIndustry_text());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date beginTime = new Date();
        Date endTime = new Date();
        try {
            beginTime = sdf.parse(report.getStartdate());
            endTime = sdf.parse(report.getEnddate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String datestr = sdf.format(beginTime);
        String datestr2 = sdf.format(endTime);
        //预计投放时间
        if ("2".equals(report.getStatus()))
            holder.cause_text.setText("投放时间: " + datestr + "--" + datestr2);
        else
            holder.cause_text.setText("预计投放时间: " + datestr + "--" + datestr2);
        //详情
        holder.state_layout.setTag(R.id.TAG_VIEWHOLDER, report);
        holder.state_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report report1 = (Report) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent(mContext, StateInformationActivity.class);
                intent.putExtra("reportId", report1.getId());
                if (!isNull(getKeytype)) {
                    MyCollectActivity activity = (MyCollectActivity) mContext;
                    activity.startActivityForResult(intent, 1);
                } else {
                    mContext.startActivity(intent);
                }
            }
        });
        //判断是否是个人
        if (isNull(keytype))
            holder.genjin_view.setVisibility(View.GONE);
        else
            holder.genjin_view.setVisibility(View.VISIBLE);
        //
        holder.genjin_view.setTag(R.id.TAG_VIEWHOLDER, report);
        holder.genjin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report report1 = (Report) v.getTag(R.id.TAG_VIEWHOLDER);
                MyReportActivity activity = (MyReportActivity) mContext;
                activity.showPop(report1);
            }
        });
    }
}
