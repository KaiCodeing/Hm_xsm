package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;

/**
 * Created by lenovo on 2017/3/3.
 * 联盟公司
 */
public class UnionCompanyActivity extends HemaAdapter {
    private Context mContext;
    public UnionCompanyActivity(Context mContext) {
        super(mContext);
    }

    @Override
    public int getCount() {
        return 0;
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
            return  getEmptyView(viewGroup);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_company_item, null);
            ViewHolder holder = new ViewHolder();
            findView(holder,view);
            view.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,i);
        return view;
    }
    private class ViewHolder{
        LinearLayout company_layout;
        TextView company_name;
        TextView company_address;
        TextView company_tel;
        TextView company_type;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.company_layout = (LinearLayout) view.findViewById(R.id.company_layout);
        holder.company_name = (TextView) view.findViewById(R.id.company_name);
        holder.company_address = (TextView) view.findViewById(R.id.company_address);
        holder.company_tel = (TextView) view.findViewById(R.id.company_tel);
        holder.company_type = (TextView) view.findViewById(R.id.company_type);
    //    holder.company_tel_call = (TextView) view.findViewById(R.id.company_tel_call);
    }
    private void setData(ViewHolder holder,int position)
    {

    }
}
