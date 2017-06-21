package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.CompanyInformationActivity;
import com.hemaapp.xsm.activity.MyCollectActivity;
import com.hemaapp.xsm.model.Union;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/22.
 * 联盟城市
 */
public class UnionAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<Union> unions;
    private String key;
    public UnionAdapter(Context mContext, ArrayList<Union> unions,String key) {
        super(mContext);
        this.mContext = mContext;
        this.unions = unions;
        this.key = key;
    }

    public void setUnions(ArrayList<Union> unions) {
        this.unions = unions;
    }

    @Override
    public boolean isEmpty() {
        return unions == null || unions.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : unions.size();
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
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_company_item, null);

            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);

        return convertView;
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

    private void setData(ViewHolder holder, int position) {
        Union union = unions.get(position);
        holder.company_name.setText(union.getName());
        holder.company_address.setText("所在城市:" + union.getAddress());
        holder.company_tel.setText(union.getLinknum());
        //公司类型
        if ("1".equals(union.getType()))
            holder.company_type.setText("联盟公司");
        else
            holder.company_type.setText("即将加入(可接全国单)");
        //拨打电话
        holder.company_tel_call.setTag(R.id.TAG_VIEWHOLDER, union);
        holder.company_tel_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Union item1 = (Union) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item1.getLinknum()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.company_layout.setTag(R.id.TAG_VIEWHOLDER,union);
        holder.company_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Union union1 = (Union) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent(mContext, CompanyInformationActivity.class);
                intent.putExtra("unionId",union1.getId());
                if (!isNull(key)) {
                    MyCollectActivity activity = (MyCollectActivity) mContext;
                    activity.startActivityForResult(intent, 1);
                } else {
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
