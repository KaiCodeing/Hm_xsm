package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.ReserveListActivity;
import com.hemaapp.xsm.model.PreGet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2017/6/7.
 * 点位列表的adapter
 */
public class ReserveListAdapter extends HemaAdapter {
    private ReserveListActivity activity;
    private ArrayList<PreGet> preGets;
    public ReserveListAdapter(Context mContext,ReserveListActivity activity,ArrayList<PreGet> preGets) {
        super(mContext);
        this.activity = activity;
        this.preGets = preGets;
    }

    public void setPreGets(ArrayList<PreGet> preGets) {
        this.preGets = preGets;
    }

    @Override
    public boolean isEmpty() {
        return preGets==null || preGets.size()==0 ;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:preGets.size();
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
            return getEmptyView(parent);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_reserve_list, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
            setData(holder, position);
        return convertView;
    }
    private class ViewHolder{
        TextView reserve_name;
        ImageView reserve_delete;
        TextView reserve_type;
        TextView reserve_time;
        TextView reserve_loaction;
        TextView reserve_issue_data;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.reserve_name = (TextView) view.findViewById(R.id.reserve_name);
        holder.reserve_delete = (ImageView) view.findViewById(R.id.reserve_delete);
        holder.reserve_type = (TextView) view.findViewById(R.id.reserve_type);
        holder.reserve_time = (TextView) view.findViewById(R.id.reserve_time);
        holder.reserve_loaction = (TextView) view.findViewById(R.id.reserve_loaction);
        holder.reserve_issue_data = (TextView) view.findViewById(R.id.reserve_issue_data);
    }
    private void setData(ViewHolder holder,int position){
        PreGet preGet1 = preGets.get(position);
        holder.reserve_name.setText(preGet1.getCustom_name());
        //判断状态
        holder.reserve_type.setText(preGet1.getStatus_text());
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String regdate = XtomTimeUtil.TransTime(preGet1.getRegdate(), "MM-dd HH:mm");
        holder.reserve_time.setText(regdate);
        holder.reserve_loaction.setText("预定点位:"+preGet1.getName_str());
        holder.reserve_issue_data.setText("预定发布日期:"+preGet1.getUp_date()+"--"+preGet1.getDown_date());
        //删除点位
        holder.reserve_delete.setTag(R.id.TAG,preGet1);
        holder.reserve_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreGet preGet = (PreGet) v.getTag(R.id.TAG);
                activity.showDelete(preGet.getId());
            }
        });
    }
}
