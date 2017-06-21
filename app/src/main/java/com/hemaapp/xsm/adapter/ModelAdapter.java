package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.Argument;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/6.
 * 模板的adapter
 */
public class ModelAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<Argument> arguments;
    public ModelAdapter(Context mContext,ArrayList<Argument> arguments) {
        super(mContext);
        this.mContext = mContext;
        this.arguments = arguments;
    }

    public void setArguments(ArrayList<Argument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public boolean isEmpty() {
        return arguments==null || arguments.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:arguments.size();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_model_item, null);
            ViewHolder holder = new ViewHolder();
            findView(holder,view);
            view.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,i);
        return view;
    }
    private class ViewHolder{
        LinearLayout model_layout;
        TextView model_name;
        TextView model_time;
        ImageView show_word;
        ImageView download_word;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.model_layout = (LinearLayout) view.findViewById(R.id.model_layout);
        holder.model_name = (TextView) view.findViewById(R.id.model_name);
        holder.model_time = (TextView) view.findViewById(R.id.model_time);
        holder.show_word = (ImageView) view.findViewById(R.id.show_word);
        holder.download_word = (ImageView) view.findViewById(R.id.download_word);
    }
    private void setData(ViewHolder holder,int position)
    {
        final Argument argument = arguments.get(position);
        holder.model_name.setText(argument.getName());
        holder.model_time.setText(argument.getRegdate());
        //预览
        holder.show_word.setTag(R.id.TAG_VIEWHOLDER,argument);
        holder.show_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Argument argument1 = (Argument) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(argument1.getModelurl());
                intent.setData(content_url);
                mContext.startActivity(intent);
            }
        });
        //下载
        holder.download_word.setTag(R.id.TAG_VIEWHOLDER,argument);
        holder.download_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Argument argument1 = (Argument) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(argument1.getModelurl());
                intent.setData(content_url);
                mContext.startActivity(intent);
            }
        });
    }
}
