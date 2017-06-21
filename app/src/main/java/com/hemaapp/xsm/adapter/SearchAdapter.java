package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.SearchActivity;
import com.hemaapp.xsm.activity.SearchResultActivity;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/9/20.
 * 搜索的adapter
 */
public class SearchAdapter extends HemaAdapter {
    private ArrayList<String> client;
    private SearchActivity activity;
    private String keytype;
    public SearchAdapter(Context mContext, ArrayList<String> client
            , SearchActivity activity,String keytype) {
        super(mContext);
        this.client = client;
        this.activity = activity;
        this.keytype = keytype;
    }

    @Override
    public boolean isEmpty() {
        return client == null || client.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : client.size();
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
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (isEmpty()) {
//            View view = LayoutInflater.from(mContext).inflate(
//                    R.layout.listitem_empty, null);
//
//            int width = parent.getWidth();
//            int height = parent.getHeight();
//            AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, height);
//            view.setLayoutParams(params);
//            return view;
//        }
        if (convertView==null)
        {
            convertView=LayoutInflater.from(activity).inflate(R.layout.item_search_word,null);
            ViewHolder holder = new ViewHolder();
            findView(holder,convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,position);
        return convertView;
    }

    private class ViewHolder {
        TextView search_data;
        LinearLayout search_layout;
        TextView delete_data;
    }

    private void findView(ViewHolder holder, View view) {
        holder.search_data = (TextView) view.findViewById(R.id.search_data);
        holder.search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
        holder.delete_data = (TextView) view.findViewById(R.id.delete_data);
    }

    private void setData(ViewHolder holder, int position) {
        String word = client.get(position);
        //显示隐藏删除按钮
        if (position==client.size()-1)
        {
            holder.delete_data.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.delete_data.setVisibility(View.GONE);
        }
        holder.search_data.setText(word);
        /**
         * 跳转
         */
        holder.search_layout.setTag(R.id.TAG_VIEWHOLDER,word);
        holder.search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = (String) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent(mContext,SearchResultActivity.class);
                intent.putExtra("keyword",word);
                intent.putExtra("keytype",keytype);
                mContext.startActivity(intent);
            }
        });
        /**
         * 删除
         */
        holder.delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Clear_HistoryList();
            }
        });
    }
}
