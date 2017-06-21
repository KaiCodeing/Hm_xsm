package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.CaseInformationActivity;
import com.hemaapp.xsm.activity.MyCollectActivity;
import com.hemaapp.xsm.model.DemoList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/3.
 * 案例adapter
 */
public class CaseAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<DemoList> demoLists;
    private String key;
    public CaseAdapter(Context mContext, ArrayList<DemoList> demoLists) {
        super(mContext);
        this.mContext = mContext;
        this.demoLists = demoLists;
    }
    public CaseAdapter(Context mContext, ArrayList<DemoList> demoLists,String key) {
        super(mContext);
        this.mContext = mContext;
        this.demoLists = demoLists;
        this.key = key;
    }

    public void setDemoLists(ArrayList<DemoList> demoLists) {
        this.demoLists = demoLists;
    }

    @Override
    public boolean isEmpty() {
        return demoLists==null || demoLists.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:demoLists.size();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_case_item, null);
            ViewHolder holder = new ViewHolder();
            findView(holder,view);
            view.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,i);
        return view;
    }
    private class ViewHolder{
        LinearLayout item_case_layout;
        TextView company_name;
        TextView time_case;
        ImageView company_img;
        TextView industry_name;
        TextView case_name;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.item_case_layout = (LinearLayout) view.findViewById(R.id.item_case_layout);
        holder.company_name = (TextView) view.findViewById(R.id.company_name);
        holder.time_case = (TextView) view.findViewById(R.id.time_case);
        holder.company_img = (ImageView) view.findViewById(R.id.company_img);
        holder.industry_name = (TextView) view.findViewById(R.id.industry_name);
        holder.case_name = (TextView) view.findViewById(R.id.case_name);
    }
    private void setData(ViewHolder holder,int position)
    {
        DemoList demoList = demoLists.get(position);
        holder.company_name.setText(demoList.getName());
        holder.time_case.setText(demoList.getRegdate());
        holder.industry_name.setText("所属行业:"+demoList.getIndustry_text());
        holder.case_name.setText("案例小区:"+demoList.getVillige());
        String path = demoList.getImgurlbig();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ad_defalut_bg)
                .showImageForEmptyUri(R.mipmap.ad_defalut_bg)
                .showImageOnFail(R.mipmap.ad_defalut_bg).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.company_img, options);
        //跳转详情
        holder.item_case_layout.setTag(R.id.TAG_VIEWHOLDER,demoList);
        holder.item_case_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoList demoList1 = (DemoList) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent(mContext, CaseInformationActivity.class);
                intent.putExtra("demoid",demoList1.getId());
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
