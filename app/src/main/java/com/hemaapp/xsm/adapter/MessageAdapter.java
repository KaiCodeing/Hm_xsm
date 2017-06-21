package com.hemaapp.xsm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.Notice;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/6.
 * 消息的adapter
 */
public class MessageAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<Notice> notices;

    public MessageAdapter(Context mContext, ArrayList<Notice> notices) {
        super(mContext);
        this.mContext = mContext;
        this.notices = notices;
    }

    public void setNotices(ArrayList<Notice> notices) {
        this.notices = notices;
    }

    @Override
    public boolean isEmpty() {
        return notices == null || notices.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : notices.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_message_item, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        ImageView user_img;
        TextView user_name;
        TextView message_content;
        View view_show;
        TextView time_text;
    }

    private void findView(ViewHolder holder, View view) {
        holder.user_img = (ImageView) view.findViewById(R.id.user_img);
        holder.user_name = (TextView) view.findViewById(R.id.user_name);
        holder.message_content = (TextView) view.findViewById(R.id.message_content);
        holder.view_show = view.findViewById(R.id.view_show);
        holder.time_text = (TextView) view.findViewById(R.id.time_text);
    }

    private void setData(ViewHolder holder, int position) {
        holder.message_content.setSingleLine();
        Notice notice = notices.get(position);
        String path1 = notice.getAvatar();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.system_message_img)
                .showImageForEmptyUri(R.mipmap.system_message_img)
                .showImageOnFail(R.mipmap.system_message_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path1, holder.user_img, options);
        if ("2".equals(notice.getKeytype()))
        {
            holder.user_img.setImageResource(R.mipmap.media_low_data_img);
            holder.user_name.setText("媒体档期到期提醒");
        }
        else {
            holder.user_name.setText("系统消息");
        }

        holder.message_content.setText(notice.getContent());
        holder.time_text.setText(notice.getRegdate());
        //判断是否已读
        if ("2".equals(notice.getLooktype()))
            holder.view_show.setVisibility(View.INVISIBLE);
        else
            holder.view_show.setVisibility(View.VISIBLE);
    }
}
