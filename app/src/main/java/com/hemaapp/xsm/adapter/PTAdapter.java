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
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.TPInformationActivity;
import com.hemaapp.xsm.model.Media;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/6/2.
 * 点位的adpater
 */
public class PTAdapter extends HemaAdapter {
    private ArrayList<Media> medias;
    private Context mContext;
    private String colseType;

    public PTAdapter(Context mContext, ArrayList<Media> medias, String colseType) {
        super(mContext);
        this.medias = medias;
        this.colseType = colseType;
        this.mContext = mContext;
    }

    public void setColseType(String colseType) {
        this.colseType = colseType;
    }

    public void setMedias(ArrayList<Media> medias) {
        this.medias = medias;
    }

    @Override
    public boolean isEmpty() {
        return medias == null || medias.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : medias.size();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_media_item, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, view);
            view.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, i);
        return view;
    }

    private class ViewHolder {
        LinearLayout media_layout;
        ImageView media_img;
        TextView title;
        TextView state;
        TextView time;
        TextView type_door;//门禁广告
        TextView type_gate;//道闸广告
        ImageView show_pop;
        ImageView all_buy_img;
        TextView type_gate_loaction;//可调整点位
    }

    private void findView(ViewHolder holder, View view) {
        holder.media_layout = (LinearLayout) view.findViewById(R.id.media_layout);
        holder.media_img = (ImageView) view.findViewById(R.id.media_img);
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.state = (TextView) view.findViewById(R.id.state);
        holder.time = (TextView) view.findViewById(R.id.time);
        holder.type_door = (TextView) view.findViewById(R.id.type_door);
        holder.type_gate = (TextView) view.findViewById(R.id.type_gate);
        holder.show_pop = (ImageView) view.findViewById(R.id.show_pop);
        holder.all_buy_img = (ImageView) view.findViewById(R.id.all_buy_img);
        holder.type_gate_loaction = (TextView) view.findViewById(R.id.type_gate_loaction);
    }

    private void setData(ViewHolder holder, int position) {
        final Media media = medias.get(position);
        //图像
        //商品图片
        String path = media.getImgurl_d();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.media_de_img)
                .showImageForEmptyUri(R.mipmap.media_de_img)
                .showImageOnFail(R.mipmap.media_de_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.media_img, options);
        //
        holder.title.setText(media.getName() +""+media.getIngress());
        //空档期，上刊
        if (media.getStatus().equals("1")) {
            holder.state.setText("展示状态:上刊");
        } else
            holder.state.setText("展示状态:空档期");
        //媒体规格
        holder.time.setText("媒体规格:" + media.getSpec());
        //道闸 门禁
        if (media.getMtype().equals("1")) {
            holder.type_door.setVisibility(View.GONE);
            holder.type_gate.setVisibility(View.VISIBLE);
        } else {
            holder.type_door.setVisibility(View.VISIBLE);
            holder.type_gate.setVisibility(View.GONE);
        }
//        //判断用户权限是否可以显示调整点位
        //普通用户
        if ("1".equals(JhctmApplication.getInstance().getUser().getFeeaccount())) {
            holder.type_gate_loaction.setVisibility(View.GONE);
        } else {
            if (!"1".equals(media.getChange_flag()))
                holder.type_gate_loaction.setVisibility(View.VISIBLE);
            else
                holder.type_gate_loaction.setVisibility(View.GONE);
        }
//        //判断是否可以编辑//不能
//        if ("3".equals(JhctmApplication.getInstance().getUser().getFeeaccount()))
//            if ("0".equals(media.getIsedit())) {
//                holder.show_pop.setVisibility(View.GONE);
//            } else {
//                holder.show_pop.setVisibility(View.VISIBLE);
//            }
        //判断是否全选
//        if ("1".equals(colseType)) {    //判断是否有编辑的权限、、没有
//            holder.show_pop.setVisibility(View.GONE);
//            if ("0".equals(media.getIsedit())) {
//                //   holder.show_pop.setVisibility(View.GONE);
//                holder.all_buy_img.setVisibility(View.GONE);
//            } else {
//                //    holder.show_pop.setVisibility(View.VISIBLE);
//                holder.all_buy_img.setVisibility(View.VISIBLE);
//                //判断是否选中
//                if (media.isCheck())
//                    holder.all_buy_img.setBackgroundResource(R.mipmap.search_all_off_img);
//                else
//                    holder.all_buy_img.setBackgroundResource(R.mipmap.search_all_on_img);
//                //选择操作
//                holder.all_buy_img.setTag(R.id.TAG, media);
//                holder.all_buy_img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Media media1 = (Media) v.getTag(R.id.TAG);
//                        if (media1.isCheck())
//                            media1.setCheck(false);
//                        else
//                            media1.setCheck(true);
//                        // mContext.changeAll(media1.getId());
//                    }
//                });
//            }
//        } else {
//            //  holder.show_pop.setVisibility(View.VISIBLE);
//            if ("0".equals(media.getIsedit())) {
//                holder.show_pop.setVisibility(View.GONE);
//            } else
//                holder.show_pop.setVisibility(View.VISIBLE);
//            holder.all_buy_img.setVisibility(View.GONE);
//        }
        //编辑的操作
//        holder.show_pop.setTag(R.id.TAG, media);
//        holder.show_pop.setTag(R.id.TAG_VIEWHOLDER, holder.show_pop);
//        holder.show_pop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Media media1 = (Media) v.getTag(R.id.TAG);
//                ImageView imageView = (ImageView) v.getTag(R.id.TAG_VIEWHOLDER);
//                mContext.changeState(media1);
//            }
//        });
        //展示是否选择
        if ("1".equals(colseType))
        {
            holder.all_buy_img.setVisibility(View.GONE);
        }
        else if("2".equals(colseType))
        {
            holder.all_buy_img.setVisibility(View.VISIBLE);
        }
        //是否选择了
        if (media.isCheck())
            holder.all_buy_img.setBackgroundResource(R.mipmap.search_all_off_img);
        else
            holder.all_buy_img.setBackgroundResource(R.mipmap.search_all_on_img);
        //选择操作
        holder.all_buy_img.setTag(R.id.TAG,media);
        holder.all_buy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Media media1 = (Media) v.getTag(R.id.TAG);
                if (media1.isCheck())
                    media1.setCheck(false);
                else
                    media1.setCheck(true);
                notifyDataSetChanged();
            }

            });
        //查看详情
        holder.media_layout.setTag(R.id.TAG_VIEWHOLDER, media);
        holder.media_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Media media1 = (Media) v.getTag(R.id.TAG_VIEWHOLDER);
                Intent intent = new Intent(mContext, TPInformationActivity.class);
                intent.putExtra("loactionId", media1.getId());
                mContext.startActivity(intent);
            }
        });
    }
}
