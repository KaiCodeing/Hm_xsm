package com.hemaapp.xsm.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.adapter.Location0Adapter;
import com.hemaapp.xsm.model.DistrictInfor;
import com.hemaapp.xsm.model.PinyinComparator;
import com.hemaapp.xsm.util.CharacterParser;
import com.hemaapp.xsm.view.ClearEditText;
import com.hemaapp.xsm.view.LetterListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;

public class City0Activity extends JhActivity implements AMapLocationListener {
    private ImageButton backBtn;
    private Button titleRight;
    //private TextView title;
    private ClearEditText etSearch;
    private LetterListView letterList;
    private ListView mCityList;
    private TextView overlay;

    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private Handler handler;
    private OverlayThread overlayThread;
    private WindowManager windowManager;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private LoacationReceiver loacationReceiver;

    private String locationCity;
    private ArrayList<DistrictInfor> allDistricts;
    private ArrayList<DistrictInfor> visitDistricts;
    private ArrayList<DistrictInfor> hotDistricts;
    private ArrayList<DistrictInfor> filterDateList;
    private Location0Adapter mLocationAdapter;
    private String keytype;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_city);
        super.onCreate(savedInstanceState);
        handler = new Handler();
        overlayThread = new OverlayThread();
        initOverlay();
        initViews();
        //getLocationCity();
        districtList();
        if (locationPermissionEnable()) {
            startLocation();
        }
    }

    private boolean locationPermissionEnable() {
        if ((ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {//判断是否拥有定位权限
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意授权
                startLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startLocation() {
        if (locationClient == null) {
            locationClient = new AMapLocationClient(mContext);
            locationOption = new AMapLocationClientOption();
            //设置定位监听
            locationClient.setLocationListener(this);
            //设置为高精度定位模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationOption.setOnceLocation(true);
            //设置定位参数
            locationClient.setLocationOption(locationOption);
            locationClient.startLocation();
        }
    }

    private void stopLocation() {
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    protected void onDestroy() {
        windowManager.removeView(overlay);
        stopLocation();
        if (loacationReceiver != null)
            unregisterReceiver(loacationReceiver);
        super.onDestroy();
    }


    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        stopLocation();
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) { //定位成功
            if (aMapLocation != null)
                locationCity = aMapLocation.getCity();
            if (isNull(locationCity)) {
                JhctmApplication application = JhctmApplication.getInstance();
                String action = application.getPackageName() + ".location";
                IntentFilter mFilter = new IntentFilter(action);
                loacationReceiver = new LoacationReceiver();
                registerReceiver(loacationReceiver, mFilter);
            }
        } else {
            startLocation();
        }
    }

    private void initViews() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        // 根据输入框输入值的改变来过滤搜索
        etSearch.addTextChangedListener(new TextChageListener());
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        filterDateList = new ArrayList<DistrictInfor>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = allDistricts;
        } else {
            filterDateList.clear();
            for (DistrictInfor district : allDistricts) {
                String name = district.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString().toLowerCase(
                                Locale.getDefault()))) {
                    filterDateList.add(district);
                }
            }
        }
        boolean isshow = etSearch.isVisibile();
        mLocationAdapter.setShow(!isshow);
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        mLocationAdapter.updateListView(filterDateList);
    }

    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private void districtList() {
        getNetWorker().districtList("1", "2");
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        JhHttpInformation information = (JhHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                showProgressDialog("正在获取城市列表");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        JhHttpInformation information = (JhHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        JhHttpInformation information = (JhHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_LIST:
                @SuppressWarnings("unchecked")
                HemaPageArrayResult<DistrictInfor> dResult = (HemaPageArrayResult<DistrictInfor>) baseResult;
                allDistricts = dResult.getObjects();

                mLocationAdapter.setLastCties(visitDistricts);
                mLocationAdapter.setLocCity(locationCity);

                Collections.sort(allDistricts, pinyinComparator);
                mLocationAdapter.setList(allDistricts);
                setAdapter(allDistricts);
                mLocationAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void setAdapter(List<DistrictInfor> list) {
        if (list != null) {
            mCityList.setAdapter(mLocationAdapter);
            alphaIndexer = mLocationAdapter.getAlphaIndexer();
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void findView() {
        backBtn = (ImageButton) findViewById(R.id.back_button);
        //	titleRight = (Button) findViewById(R.id.title_btn_right);
        etSearch = (ClearEditText) findViewById(R.id.etsearch);
        letterList = (LetterListView) findViewById(R.id.letterListView);
        mCityList = (ListView) findViewById(R.id.city_list);
        mLocationAdapter = new Location0Adapter(this, true);
        //title = (TextView) findViewById(R.id.title_text);
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //title.setText("当前城市");
        //titleRight.setVisibility(View.GONE);
        letterList
                .setOnTouchingLetterChangedListener(new LetterListViewListener());
        mCityList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                DistrictInfor citysel = (DistrictInfor) view.getTag(R.id.TAG);
                if (citysel != null)
                    itemclick(citysel);
            }
        });
        if ("1".equals(keytype))
            backBtn.setVisibility(View.INVISIBLE);
    }

    /**
     * 城市列表点击事件
     */
    public void itemclick(DistrictInfor info) {
        if (info == null) {
            XtomToastUtil.showShortToast(mContext, "暂不支持此城市");
            return;
        }
        if ("2".equals(keytype)) {
            setResult(RESULT_OK, mIntent);
            mIntent.putExtra("name", info.getName());
            mIntent.putExtra("district2", info.getId());
            finish();
        } else {
            mIntent.putExtra("name", info.getName());
            XtomSharedPreferencesUtil.save(mContext, "cityId", info.getId());
            setResult(RESULT_OK, mIntent);
            finish();
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN && "1".equals(keytype)) {
//            return true;
//        } else
//            return false;
//
//
//    }

    private class LetterListViewListener implements
            LetterListView.OnTouchingLetterChangedListener {

        @SuppressLint("NewApi")
        @Override
        public void onTouchingLetterChanged(final String s) {
            if (alphaIndexer != null && alphaIndexer.get(s) != null) {
                final int position = alphaIndexer.get(s);
                log_w("=s=" + s);
                mCityList.setSelection(position + 1);
            } else if ("↑".equals(s)) {
                mCityList.setSelection(0);
            }
            overlay.setText(s);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1500);
        }
    }


    private class LoacationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            JhctmApplication application = JhctmApplication.getInstance();
            String realAction = application.getPackageName() + ".location";
            String action = intent.getAction();
            if (realAction.equals(action)) {
                mLocationAdapter.setLocCity(locationCity);
                if (mCityList.getAdapter() != null)
                    mLocationAdapter.notifyDataSetChanged();
            }
        }
    }

    private class TextChageListener implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
            filterData(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
