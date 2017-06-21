package com.hemaapp.xsm;

import android.content.Context;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2016/9/6.
 */
public class JhUtil {

        /**
         * 根据手机分辨率从dp转成px
         *
         * @param context
         * @param dpValue
         * @return
         */
        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        public static int dimen2px(Context context, int dimen) {
            return (int) Math.ceil(context.getResources().getDimension(dimen));
        }
        /**
         * 退出软件
         *
         * @param context
         */
        public static void exit(Context context) {
            XtomActivityManager.finishAll();
        }

        /**
         *
         * @方法名称: px2dip
         * @功能描述: TODO像素转dp
         * @param context
         * @param pxValue
         * @return
         * @返回值: int
         */
        public static int px2dip(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }


        /**
         * 将sp转换为px
         *
         * @param context
         * @param spValue
         * @return
         */
        public static int sp2px(Context context, float spValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (spValue * fontScale + 0.5f);
        }

        /**
         * 转换时间显示形式(与当前系统时间比较),在显示即时聊天的时间时使用
         *
         * @param time
         *            时间字符串
         * @return String
         */
        public static String transTimeChat(String time) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault());
                String current = XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
                String dian24 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                        + " 24:00:00";
                String dian00 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                        + " 00:00:00";
                Date now = null;
                Date date = null;
                Date d24 = null;
                Date d00 = null;
                try {
                    now = sdf.parse(current); // 将当前时间转化为日期
                    date = sdf.parse(time); // 将传入的时间参数转化为日期
                    d24 = sdf.parse(dian24);
                    d00 = sdf.parse(dian00);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diff = now.getTime() - date.getTime(); // 获取二者之间的时间差值
                long min = diff / (60 * 1000);
                if (min <= 5)
                    return "刚刚";
                if (min < 60)
                    return min + "分钟前";

                if (now.getTime() <= d24.getTime()
                        && date.getTime() >= d00.getTime())
                    return "今天" + XtomTimeUtil.TransTime(time, "HH:mm");

                int sendYear = Integer
                        .valueOf(XtomTimeUtil.TransTime(time, "yyyy"));
                int nowYear = Integer.valueOf(XtomTimeUtil.TransTime(current,
                        "yyyy"));
                if (sendYear < nowYear)
                    return XtomTimeUtil.TransTime(time, "yyyy-MM-dd HH:mm");
                else
                    return XtomTimeUtil.TransTime(time, "MM-dd HH:mm");
            } catch (Exception e) {
                return null;
            }

        }

        public static String transDuration(long duration) {
            String ds = "";
            long min = duration / 60;
            if (min < 60) {
                ds += (min + "分钟");
            } else {
                long hour = min / 60;
                long rm = min % 60;
                if (rm > 0)
                    ds += (hour + "小时" + rm + "分钟");
                else
                    ds += (hour + "小时");
            }
            return ds;
        }

        public static String transDistance(float distance) {
            String ds = "";
            if (distance < 1000) {
                ds += (distance + "米");
            } else {
                float km = distance / 1000;
                ds += (String.format(Locale.getDefault(), "%.2f", km) + "千米");
            }
            return ds;
        }

        /**
         *
         * @方法名称: ToDBC
         * @功能描述: TODO半角转全角
         * @param input
         * @return
         * @返回值: String
         */
        public static String ToDBC(String input) {
            char[] c = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == 12288) {
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375)
                    c[i] = (char) (c[i] - 65248);
            }
            return new String(c);
        }

        /**
         *
         * @方法名称: Distance
         * @功能描述: TODO计算距离
         * @param long1
         * @param lat1
         * @param long2
         * @param lat2
         * @return
         * @返回值: double
         */
        public static String Distance(double long1, double lat1, double long2,
                                      double lat2) {
            double a, b, R;
            R = 6378137; // 地球半径
            lat1 = lat1 * Math.PI / 180.0;
            lat2 = lat2 * Math.PI / 180.0;
            a = lat1 - lat2;
            b = (long1 - long2) * Math.PI / 180.0;
            double d;
            double sa2, sb2;
            sa2 = Math.sin(a / 2.0);
            sb2 = Math.sin(b / 2.0);
            d = 2
                    * R
                    * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                    * Math.cos(lat2) * sb2 * sb2));
            DecimalFormat df   = new DecimalFormat("######0.00");
            return df.format(d/1000);
        }
    public static int getScreenWidth(Context context) {
        // 取得窗口属性
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        // 窗口的宽度
        return wm.getDefaultDisplay().getWidth();
    }
    public static void initWebview(WebView webView) {
        // 支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        // 扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        // 取消显示滚动条
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setGeolocationDatabasePath("/data/data/org.itri.html5webView/databases/");     // enable Web Storage: localStorage, sessionStorage
        webView.getSettings().setDomStorageEnabled(true);
        webView.requestFocus();
//        webView.setScrollBarStyle(0);
    }


}
