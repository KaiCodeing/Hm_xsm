package com.hemaapp.xsm.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.MapSearchAdapter;
import com.hemaapp.xsm.db.SearchDBClient;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.media.XtomVoicePlayer;
import xtom.frame.util.XtomToastUtil;

/**
 * poi 搜索
 * Created by Torres on 2016/12/26.
 */

public class SearchActivity_Map extends JhActivity implements PoiSearch.OnPoiSearchListener{

    private ImageView back;
    private EditText edit;
    private TextView right;
    private ListView list;
    private CheckBox icon4;
    private SearchDBClient mClient;
    private ArrayList<String> search_strs = new ArrayList<String>();
    private String keyword;
    private String lastPalyId;
    private String sName;
    private XtomVoicePlayer player;
    private boolean flag;
    private ImageView imgSearchNone;
    private LatLonPoint lp ;//
    private PoiResult poiResult; // poi返回的结果
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private myPoiOverlay poiOverlay;// poi图层
    private String keyWord = "";
    private String city = "";
    private List<PoiItem> poiItems;// poi数据

    private String lat;
    private  String lng;
    private MapSearchAdapter adapter;
    private boolean isLoc;
    private  EditText edSearch;
    private int currentPage = 0;// 当前页面，从0开始计数
    private String distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_wh);
        super.onCreate(savedInstanceState);
        lp=new LatLonPoint(Double.valueOf(lat),Double.valueOf(lng));

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        keyword=edSearch.getText().toString().trim();
        if(isNull(keyword)){
            XtomToastUtil.showShortToast(mContext,"请输入要搜索的关键词");
            return;
        }
        currentPage = 0;
        query = new PoiSearch.Query(keyword, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10000);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            if (!isNull(distance))
                poiSearch.setBound(new PoiSearch.SearchBound(lp, Integer.valueOf(distance)*1000, false));//
           //
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        back = (ImageView) findViewById(R.id.img_back);
        edit = (EditText) findViewById(R.id.EdSearch);
        right = (TextView) findViewById(R.id.tvright);
        list = (ListView) findViewById(R.id.listview);
        imgSearchNone = (ImageView) findViewById(R.id.imgnone);
        edSearch= (EditText) findViewById(R.id.EdSearch);
    }

    @Override
    protected void getExras() {
      //  city=mIntent.getStringExtra("city");
        lat=mIntent.getStringExtra("lat");
        lng=mIntent.getStringExtra("lng");
        city = "济南市";
        distance = mIntent.getStringExtra("distance");
//        lat=String.valueOf(36.650484);
//        lng=String.valueOf(117.125119);
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
              finish();
            }
        });


        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNull(edSearch.getText().toString()))
                doSearchQuery();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onPoiSearched(PoiResult result, int rcode) {

        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                List<PoiItem> ps = result.getPois();
                list.setVisibility(View.VISIBLE);
                setList(ps);
            } else {
            }
        } else {

        }

    }
    private void setList(List<PoiItem> ps) {
        if (adapter == null) {
            adapter = new MapSearchAdapter(this);
            adapter.setItems(isLoc, ps);
            list.setAdapter(adapter);
        } else {
            adapter.setItems(isLoc, ps);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


//
//    public void delete(String searchname) {
//        if (mClient.delete(searchname)) {
//            search_strs.remove(searchname);
//            search_adapter.notifyDataSetChanged();
//            if (search_strs.size() == 0) {
//                imgSearchNone.setVisibility(View.VISIBLE);
//            }
//        }
//    }

    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

        public myPoiOverlay(AMap amap, List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }
    }

}
