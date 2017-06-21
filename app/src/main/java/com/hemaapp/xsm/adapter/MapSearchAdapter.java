package com.hemaapp.xsm.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.activity.SearchActivity_Map;
import com.hemaapp.xsm.model.MyPoi;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.XtomAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Torres on 2016/12/26.
 * 地图搜索的adapter
 */

public class MapSearchAdapter extends XtomAdapter {
    private SearchActivity_Map mActivity;
    private PoiItem pi;
    private ArrayList<MyPoi> ps = new ArrayList<MyPoi>();

    public MapSearchAdapter(SearchActivity_Map mActivity) {
        super();
        this.mActivity = mActivity;
    }

    public void setItems(boolean isLoc, List<PoiItem> items) {
        ps.clear();
        if (items != null) {
            for (PoiItem p : items)
                ps.add(new MyPoi(p.getTitle(), p.getSnippet(), p.getLatLonPoint(), false));
        }
    }

    public void setGou(int position) {
        for (MyPoi p : ps)
            p.setCheck(false);
        ps.get(position).setCheck(true);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ps.size();
    }

    @Override
    public Object getItem(int position) {
        return ps.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PoiHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.listitem_mappoi, null);
            holder = new PoiHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else
            holder = (  PoiHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    private void setData(  PoiHolder h, int position) {
        final MyPoi mp = ps.get(position);
//        if(position!=0){
//            h.tvCommend.setVisibility(View.GONE);
//        }
//        else
//            h.tvCommend.setVisibility(View.VISIBLE);
        h.name.setText(mp.getName());
        if (isNull(mp.getAddress()))
            h.address.setVisibility(View.GONE);
        else {
            h.address.setVisibility(View.VISIBLE);
            h.address.setText(mp.getAddress());
        }

        h.vAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent data = new Intent();
                data.putExtra("cityL", mp.getAddress());
                data.putExtra("lng",mp.getLl().getLongitude());
                data.putExtra("lat",mp.getLl().getLatitude());
                mActivity.setResult(RESULT_OK, data);
                mActivity.finish();
            }
        });
//        if (mp.isCheck())
            h.gou.setVisibility(View.VISIBLE);
//        else
//            h.gou.setVisibility(View.GONE);
    }

    private void findView(  PoiHolder h, View v) {
        h.loc = (ImageView) v.findViewById(R.id.poi_img);
        h.name = (TextView) v.findViewById(R.id.poi_name);
        h.address = (TextView) v.findViewById(R.id.poi_address);
        h.gou = (ImageView) v.findViewById(R.id.poi_gou);
        h.vAll=v.findViewById(R.id.vAll);
   //     h.tvCommend= (TextView) v.findViewById(R.id.tvCommend);
    }

    private class PoiHolder {
        ImageView loc;
        TextView name;
        TextView address;
        ImageView gou;
        View vAll;
      //  TextView tvCommend;
    }

}
