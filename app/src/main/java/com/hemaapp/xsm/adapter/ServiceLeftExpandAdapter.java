package com.hemaapp.xsm.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.xsm.R;
import com.hemaapp.xsm.fragment.ServiceLeftFragment;
import com.hemaapp.xsm.model.Service;
import com.hemaapp.xsm.model.ServiceItem;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/3.
 * 客服的媒介电话
 */
public class ServiceLeftExpandAdapter extends BaseExpandableListAdapter {
    private ServiceLeftFragment fragment;
    private ArrayList<Service> services;

    public ServiceLeftExpandAdapter(ServiceLeftFragment fragment, ArrayList<Service> services) {
        this.fragment = fragment;
        this.services = services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    @Override
    public int getGroupCount() {
        return (services == null || services.size() == 0) ? 0 : services.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return (services.get(i).getPrice() == null || services.get(i).getPrice().size() == 0) ? 0 : services.get(i).getPrice().size();
    }

    @Override
    public Object getGroup(int i) {
        return services.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return services.get(i).getPrice().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.adapter_grpup_item, null);
            GroupView holder = new GroupView();
            findGroup(holder, view);
            view.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        GroupView holder = (GroupView) view.getTag(R.id.TAG_VIEWHOLDER);
        setGroup(holder, i,b);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.adapter_company_item, null);

            ViewHolder holder = new ViewHolder();
            findView(holder, view);
            view.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, i,i1);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    //父控件
    private class GroupView {
        LinearLayout city_layout;
        TextView city_name;
        ImageView show_bz;
    }

    private void findGroup(GroupView groupView, View view) {
        groupView.city_layout = (LinearLayout) view.findViewById(R.id.city_layout);
        groupView.city_name = (TextView) view.findViewById(R.id.city_name);
        groupView.show_bz = (ImageView) view.findViewById(R.id.show_bz);
    }

    private void setGroup(GroupView group, int position, boolean check) {
        Service service = services.get(position);
        group.city_name.setText(service.getProvince());
        if (check)
            group.show_bz.setImageResource(R.mipmap.service_shang_jian_img);
        else
            group.show_bz.setImageResource(R.mipmap.service_xia_jian_img);
    }

    //子控件
    private class ViewHolder {
        LinearLayout company_layout;
        TextView company_name;
        TextView company_address;
        TextView company_tel;
        TextView company_type;
        TextView company_tel_call;
    }

    private void findView(ViewHolder holder, View view) {
        holder.company_layout = (LinearLayout) view.findViewById(R.id.company_layout);
        holder.company_name = (TextView) view.findViewById(R.id.company_name);
        holder.company_address = (TextView) view.findViewById(R.id.company_address);
        holder.company_tel = (TextView) view.findViewById(R.id.company_tel);
        holder.company_type = (TextView) view.findViewById(R.id.company_type);
        holder.company_tel_call = (TextView) view.findViewById(R.id.company_tel_call);
    }

    private void setData(ViewHolder holder, int position,int childPosition) {
        ServiceItem item = services.get(position).getPrice().get(childPosition);
        holder.company_name.setText(item.getName());
        holder.company_address.setText("所在城市:"+item.getCity());
        holder.company_tel.setText(item.getLinknum());
        //公司类型
        if ("1".equals(item.getType()))
        holder.company_type.setText("联盟公司");
        else
            holder.company_type.setText("即将加入(可接全国单)");
        //拨打电话
        holder.company_tel_call.setTag(R.id.TAG_VIEWHOLDER,item);
        holder.company_tel_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceItem item1 = (ServiceItem) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+item1.getLinknum()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fragment.getActivity().startActivity(intent);
            }
        });
    }
}
