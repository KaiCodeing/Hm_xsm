package com.hemaapp.xsm.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.xsm.JhActivity;
import com.hemaapp.xsm.JhHttpInformation;
import com.hemaapp.xsm.JhctmApplication;
import com.hemaapp.xsm.R;
import com.hemaapp.xsm.model.Media;
import com.hemaapp.xsm.model.Union;
import com.hemaapp.xsm.util.AMapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/3/23.
 */
public class MediaOnlyMapActivity extends JhActivity implements
        RadioGroup.OnCheckedChangeListener,
        View.OnClickListener,
        LocationSource,
        AMapLocationListener,
        AMap.OnMapClickListener,
        GeoFenceListener,
        AMap.InfoWindowAdapter,
        AMap.OnMarkerDragListener,
        AMap.OnMapLoadedListener
        , GeocodeSearch.OnGeocodeSearchListener
        , AMap.OnInfoWindowClickListener
        , AMap.OnMarkerClickListener {
    private MarkerOptions markerOption = null;
    private AMap aMap;
    private MapView mapView;
    private UiSettings mUiSettings;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mLocationChangeListener;
    private List<Marker> markerList = new ArrayList<Marker>();
    // 中心点坐标
    private LatLng centerLatLng = null;
    // 中心点marker
    private Marker centerMarker;
    private BitmapDescriptor ICON_YELLOW = BitmapDescriptorFactory
            .defaultMarker(R.mipmap.loaction_img_dw);
    private AMapLocation location;
    private GeocodeSearch geocoderSearch;
    private String cityL;
    LatLonPoint latLonPoint;
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private String lng;
    private String lat;
    private ImageView go_to;
    private String keytype;
    private ArrayList<Media> medias;
    private ImageView go_show;
    private LinearLayout add_city_layout;
    private TextView company_name;
    private TextView city_name;
    private ArrayList<Union> unions = new ArrayList<>();
    private TextView search_input;
    private ShowSopen showView;
    private String kkkk;
    private Button next_button_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_media_only_map);
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        if ("2".equals(keytype)) {
            String token = JhctmApplication.getInstance().getUser().getToken();
            getNetWorker().mediaList(token, "4", "", "", "", "", "", "", "", "", "", "", "");
        } else if ("3".equals(keytype)) {
            String token = JhctmApplication.getInstance().getUser().getToken();
            getNetWorker().unionList(token, "1", "");
        } else {
            initMap();
            aMap.setLocationSource(this);// 设置定位监听
            mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
            aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
            markerOption = new MarkerOptions().draggable(true);
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(this);
        }
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            aMap.getUiSettings().setRotateGesturesEnabled(false);
            aMap.moveCamera(CameraUpdateFactory.zoomBy(6));
            setUpMap();
        }
//        mUiSettings.setZoomControlsEnabled(true);
//        aMap.setLocationSource(this);// 设置定位监听
//        mUiSettings.setMyLocationButtonEnabled(true); // 是否显示默认的定位按钮
//        aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));// 设置小蓝点的图标
//        myLocationStyle.strokeColor(Color.WHITE);// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
//        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//        myLocationStyle.strokeWidth(0.0f);// 设置圆形的边框粗细
//        aMap.setMyLocationStyle(myLocationStyle);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setOnMapClickListener(this);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        if (!"3".equals(keytype)) {
            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式}
        }
//        // 自定义系统定位蓝点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        // 自定义定位蓝点图标
//        myLocationStyle.myLocationIcon(
//                BitmapDescriptorFactory.fromResource(R.mipmap.loaction_img_dw));
//        // 自定义精度范围的圆形边框颜色
//        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
//        // 自定义精度范围的圆形边框宽度
//        myLocationStyle.strokeWidth(0);
//        // 设置圆形的填充颜色
//        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
//        // 将自定义的 myLocationStyle 对象添加到地图上
//        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        go_to = (ImageView) findViewById(R.id.go_to);
        go_show = (ImageView) findViewById(R.id.go_show);
        add_city_layout = (LinearLayout) findViewById(R.id.add_city_layout);
        company_name = (TextView) findViewById(R.id.company_name);
        city_name = (TextView) findViewById(R.id.city_name);
        search_input = (TextView) findViewById(R.id.search_input);
        next_button_text = (Button) findViewById(R.id.next_button_text);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
        //   medias = (ArrayList<Media>) mIntent.getSerializableExtra("medias");
        lng = mIntent.getStringExtra("lng");
        lat = mIntent.getStringExtra("lat");
        cityL = mIntent.getStringExtra("cityL");
        kkkk = mIntent.getStringExtra("kkkk");
    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if ("3".equals(keytype)) {
            title_text.setText("加盟城市");
            go_show.setVisibility(View.VISIBLE);
            go_to.setVisibility(View.GONE);
        } else
            title_text.setText("媒体定位地图");


        if (isNull(keytype))
            search_input.setVisibility(View.VISIBLE);
        else
            search_input.setVisibility(View.GONE);
        //判断显示确定
        if (!isNull(kkkk)) {
            next_button_text.setVisibility(View.VISIBLE);
            next_button_text.setText("确定");
        }
        next_button.setVisibility(View.INVISIBLE);
        //搜索
        search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaOnlyMapActivity.this, SearchActivity_Map.class);
                String lng = String.valueOf(centerMarker.getPosition().longitude);
                String lat = String.valueOf(centerMarker.getPosition().latitude);
                intent.putExtra("lng", lng);
                intent.putExtra("lat", lat);
                startActivityForResult(intent, 1);
            }
        });
        //确定
        next_button_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        //跳转导航
        go_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(location.getLatitude()).equals(lat) && String.valueOf(location.getLongitude()).equals(lng)) {
                    showTextDialog("起点终点相同");
                    return;
                }
                Intent intent = new Intent(MediaOnlyMapActivity.this, NavigationMapActivity.class);
                //  intent.putExtra("mStartPoint",new LatLng(location.getLatitude(), location.getLongitude()));
                intent.putExtra("startlat", String.valueOf(location.getLatitude()));
                intent.putExtra("startlng", String.valueOf(location.getLongitude()));
                intent.putExtra("lng", lng);
                intent.putExtra("lat", lat);
                startActivity(intent);
            }
        });
    }

    private class ShowSopen {
        EditText iphone_number;
        TextView close_pop;
        TextView yas_pop;
    }

    //距离
    private void show() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_loaction_scopen, null);
        showView = new ShowSopen();
        showView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        showView.iphone_number = (EditText) view.findViewById(R.id.iphone_number);
        showView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        showView.close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        showView.yas_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loactionopen = showView.iphone_number.getText().toString();
                if (isNull(loactionopen)) {
                    showTextDialog("请填写范围");
                    return;
                }
                if (Integer.valueOf(loactionopen)>10 || Integer.valueOf(loactionopen)<1)
                {
                    showTextDialog("公里数为1-10");
                    return;
                }
                setResult(RESULT_OK, mIntent);
                mIntent.putExtra("address", cityL);
                mIntent.putExtra("lng", lng);
                mIntent.putExtra("lat", lat);
                mIntent.putExtra("distance", loactionopen);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(showView.iphone_number.getWindowToken(), 0);
                finish();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new
                BitmapDrawable()
        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1: //相册
                cityL = data.getStringExtra("cityL");
                centerLatLng = new LatLng(data.getDoubleExtra("lat", 1.0), data.getDoubleExtra("lng", 1.0));
                centerMarker.setPosition(new LatLng(data.getDoubleExtra("lat", 1.0), data.getDoubleExtra("lng", 1.0)));
                getAddress(new LatLonPoint(data.getDoubleExtra("lat", 1.0), data.getDoubleExtra("lng", 1.0)));
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog("获取数据");
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult
            hemaBaseResult) {
        JhHttpInformation information = (JhHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MEDIA_LIST:
                HemaPageArrayResult<Media> result = (HemaPageArrayResult<Media>) hemaBaseResult;
                medias = result.getObjects();
                initMap();
                aMap.setLocationSource(this);// 设置定位监听
                mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
                aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
                markerOption = new MarkerOptions().draggable(true);
                geocoderSearch = new GeocodeSearch(this);
                geocoderSearch.setOnGeocodeSearchListener(this);
                aMap.setOnMarkerClickListener(this);
                break;
            case UNION_LIST:
                HemaArrayResult<Union> result1 = (HemaArrayResult<Union>) hemaBaseResult;
                unions = result1.getObjects();
                initMap();
                aMap.setLocationSource(this);// 设置定位监听
                mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
                aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
                markerOption = new MarkerOptions().draggable(true);
                geocoderSearch = new GeocodeSearch(this);
                geocoderSearch.setOnGeocodeSearchListener(this);
                aMap.setOnMarkerClickListener(this);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult
            hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showTextDialog("获取失败，请稍后重试");
        next_button.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        if (mLocationChangeListener != null && aLocation != null) {
            mlocationClient.stopLocation();//停止定位
            location = aLocation;
            //    mLocationChangeListener.onLocationChanged(aLocation);// 显示系统小蓝点
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aLocation.getLatitude(), aLocation.getLongitude()), 15));
            markerOption.icon(ICON_YELLOW);
            centerLatLng = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
            String address = aLocation.getAddress();
            log_i("++________" + centerLatLng);
            if ("1".equals(keytype)) {
                addCenterMarker(new LatLng(Double.valueOf(lat), Double.valueOf(lng)));
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(new LatLonPoint(Double.valueOf(lat), Double.valueOf(lng))), 15));
            } else if ("2".equals(keytype)) {

                for (int i = 0; i < medias.size(); i++) {
                    lat = medias.get(i).getLat();
                    lng = medias.get(i).getLng();
                    centerMarker = aMap.addMarker(new MarkerOptions()
                            .title(cityL)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.loaction_img_dw)
                            )
                            .draggable(true));
                    log_i("++________+++++++++" + centerLatLng + "++++++++++++" + location.getAddress());
//        centerMarker.setRotateAngle(90);// 设置marker旋转90度
                    centerMarker.setPositionByPixels(400, 400);
                    centerMarker.setTitle(medias.get(i).getName());
                    centerMarker.setSnippet(medias.get(i).getId());
                    centerMarker.showInfoWindow();// 设置默认显示一个infowinfow}
                    centerMarker.setPosition(new LatLng(Double.valueOf(medias.get(i).getLat()), Double.valueOf(medias.get(i).getLng())));
                    markerList.add(centerMarker);
                }
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(new LatLonPoint(Double.valueOf(lat), Double.valueOf(lng))), 10));
            } else if ("3".equals(keytype)) {
                for (int i = 0; i < unions.size(); i++) {
                    lat = unions.get(i).getLat();
                    lng = unions.get(i).getLng();
                    if (isNull(lat)) {
                        continue;
                    }
                    log_i("++++LLLLLLLLL+++" + i);
                    if ("1".equals(unions.get(i).getType()))
                        centerMarker = aMap.addMarker(new MarkerOptions()
                                .title(cityL)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.city_company_img)
                                )
                                .draggable(true));
                    else
                        centerMarker = aMap.addMarker(new MarkerOptions()
                                .title(cityL)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.want_add_img)
                                )
                                .draggable(true));
                    log_i("++________+++++++++" + centerLatLng + "++++++++++++" + location.getAddress());
//        centerMarker.setRotateAngle(90);// 设置marker旋转90度
                    centerMarker.setPositionByPixels(400, 400);
                    centerMarker.setTitle(unions.get(i).getName());
                    centerMarker.setSnippet(unions.get(i).getId());
                    //  centerMarker.showInfoWindow();// 设置默认显示一个infowinfow}
                    centerMarker.setPosition(new LatLng(Double.valueOf(unions.get(i).getLat()), Double.valueOf(unions.get(i).getLng())));
                    markerList.add(centerMarker);
                }
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(new LatLonPoint(Double.valueOf(lat), Double.valueOf(lng))), 4));
            } else {
                cityL = aLocation.getAddress();
                addCenterMarker(centerLatLng);
            }
            // addMarkersToMap(centerLatLng);
            LatLng latlng = new LatLng(aLocation.getLatitude() + 0.001, aLocation.getLongitude() + 0.001);
            LatLng latlng2 = new LatLng(aLocation.getLatitude() - 0.001, aLocation.getLongitude() - 0.001);
//            addMarkersToMap(latlng);
//            addMarkersToMap(latlng2);
            log_i("DDDDDDDDDDDD" + latlng + ",,," + latlng2);
        } else {
            //   Toast.makeText(mContext, "定位失败," + aLocation.getErrorCode() + ": " + aLocation.getErrorInfo(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mLocationChangeListener = listener;
        log_i("SSSSSSSSSSSSSSS+重新定位");
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mLocationChangeListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 在地图上添加marker
     */

    private void addMarkersToMap(LatLng latlng) {
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).position(latlng).draggable(true);
        aMap.addMarker(markerOption);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGeoFenceCreateFinished(List<GeoFence> list, int i, String s) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (isNull(keytype)) {
            markerOption.icon(ICON_YELLOW);
            centerLatLng = latLng;
            log_i("++________" + centerLatLng);
            // mlocationClient.startLocation();
            getAddress(new LatLonPoint(latLng.latitude, latLng.longitude));
            //    tvGuide.setBackgroundColor(getResources().getColor(R.color.gary));
        }
    }


    private void addCenterMarker(LatLng latlng) {
        if (null == centerMarker) {
//            centerMarker = aMap.addMarker(markerOption);
            if (!"1".equals(keytype))
                centerMarker = aMap.addMarker(new MarkerOptions()
                        .title(location.getAddress())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.loaction_img_dw)
                        )
                        .draggable(true));
            else
                centerMarker = aMap.addMarker(new MarkerOptions()
                        .title(cityL)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.loaction_img_dw)
                        )
                        .draggable(true));
        }
        log_i("++________+++++++++" + centerLatLng + "++++++++++++" + location.getAddress());
        if (!"1".equals(keytype)) {
            lng = String.valueOf(latlng.longitude);
            lat = String.valueOf(latlng.latitude);
        }
//        centerMarker.setRotateAngle(90);// 设置marker旋转90度
        centerMarker.setPositionByPixels(400, 400);

        centerMarker.showInfoWindow();// 设置默认显示一个infowinfow
        centerMarker.setPosition(latlng);
        //  markerList.add(centerMarker);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.custom_info_window, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    //写maker
    private void render(Marker marker, View view) {
        TextView textView = (TextView) view.findViewById(R.id.loaction_text);
        String title = marker.getTitle();
        if (title != null) {
//            SpannableString titleText = new SpannableString(title);
//            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
//                    titleText.length(), 0);
            textView.setTextSize(15);
            if ("2".equals(keytype))
                textView.setText(title);
            else
                textView.setText(cityL);

        } else {
            textView.setText("");
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if ("1".equals(keytype)) {

        } else if ("2".equals(keytype)) {
            Intent intent = new Intent(MediaOnlyMapActivity.this, MediaInformationActivity.class);
            intent.putExtra("mediaId", marker.getSnippet());
            startActivity(intent);
        } else {
            if (!isNull(kkkk))
                return;
            setResult(RESULT_OK, mIntent);
            mIntent.putExtra("address", cityL);
            mIntent.putExtra("lng", lng);
            mIntent.putExtra("lat", lat);
            finish();
        }
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        this.latLonPoint = latLonPoint;
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
        log_i("LLLLLLLLLLLLLLLL" + latLonPoint.getLongitude());
        log_i("OOOOOOOOOOOOOOOO" + latLonPoint.getLatitude());
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                if (!"3".equals(keytype)) {
                    cityL = result.getRegeocodeAddress().getFormatAddress();
                    addCenterMarker(centerLatLng);
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            AMapUtil.convertToLatLng(latLonPoint), 15));

                } else {
                    log_i("LLLLLLLLLLLLLLLL" + latLonPoint.getLongitude());
                    log_i("OOOOOOOOOOOOOOOO" + latLonPoint.getLatitude());
                    cityL = result.getRegeocodeAddress().getFormatAddress();
                    city_name.setText("所在城市:" + cityL);
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            AMapUtil.convertToLatLng(latLonPoint), 5));
                }
            }

//                addressName = result.getRegeocodeAddress().getFormatAddress()
//                        + "附近";
//                addressAd=result.getRegeocodeAddress().getPois().get(0).toString();

//                locationAddr=result.getRegeocodeAddress().getFormatAddress();
//                locationName=result.getRegeocodeAddress().getPois().get(0).toString();}

        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if ("2".equals(keytype)) {
            jumpPoint(marker);
            lng = String.valueOf(marker.getPosition().longitude);
            lat = String.valueOf(marker.getPosition().latitude);
        } else if ("3".equals(keytype)) {
            jumpPoint(marker);
            add_city_layout.setVisibility(View.VISIBLE);
            company_name.setText(marker.getTitle());
            log_i("LLLLLLLLLLLLLLLL" + marker.getPosition().longitude);
            log_i("OOOOOOOOOOOOOOOO" + marker.getPosition().latitude);

            getAddress(new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude));
            //跳转
            add_city_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaOnlyMapActivity.this, CompanyInformationActivity.class);
                    intent.putExtra("unionId", marker.getSnippet());
                    startActivity(intent);
                }
            });
        }
        return false;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


}
