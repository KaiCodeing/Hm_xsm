package com.hemaapp.xsm.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomTimeUtil;
import xtom.frame.util.XtomToastUtil;


public class BaseUtil {
	private static double EARTH_RADIUS = 6378.137;// 地球半径
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
	/**
	 * 转换时间显示形式
	 * 
	 * @param time
	 *            时间字符串yyyy-MM-dd HH:mm:ss
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String TransTime(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		try {
			Date date1 = sdf.parse(time);
			SimpleDateFormat dateFormat = new SimpleDateFormat(format,
					Locale.getDefault());// "yyyy年MM月dd HH:mm"
			return dateFormat.format(date1);
		} catch (Exception e) {
			return null;
		}
	}
	public static String get2double(Double s)
	{
		DecimalFormat df = new DecimalFormat("#.00");
		return String.valueOf(df.format((s * 100)));
	}
	/**
	 * 获取系统当前时间
	 * 
	 * @param format
	 *            时间格式yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String getCurrentTime(String format) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(format,
				Locale.getDefault());
		return dateFormat.format(date);
	}
	/**
	 * 转换时间显示形式(与当前系统时间比�?,在显示即时聊天的时间时使�?
	 * 
	 * @param time
	 *            时间字符�?
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
			long diff = now.getTime() - date.getTime(); // 获取二�?之间的时间差�?
			long min = diff / (60 * 1000);
			if (min <= 5)
				return "刚刚";
			if (min < 60)
				return min + "分钟";

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
	public static String transLength(long length) {
		long k = length / 1024;
		if (k < 1024)
			return k + "K";
		else {
			double m = (double) k / (double) 1024;
			if (m < 1024) {
				String ms = String.format(Locale.getDefault(), "%.2f", m);
				return ms + "M";
			} else {
				double g = (double) m / (double) 1024;
				if (g < 1024) {
					String gs = String.format(Locale.getDefault(), "%.2f", g);
					return gs + "G";
				} else {
					return ">1T";
				}
			}
		}
	}
	/**
	 * 计算两点间的距离
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static int GetDistance(double lat1, double lng1, double lat2,
			double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		return (int) Math.round(s * 1000/1000.0);
	}
	
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
	 * double保留2位小数，转成String
	 * 
	 * @param d
	 * @return
	 */
	public static String format(double d) {
		return String.format("%.2f", d);
	}
	public static String copy(Context context, String urlPath) {
		String savePath ="";
		try {
			
			if (!XtomFileUtil.isExternalMemoryAvailable()) {
				XtomToastUtil.showShortToast(context, "没有SD卡，不能复制");
				return "";
			}
			String imgPath;
				imgPath = XtomImageCache.getInstance(context)
						.getPathAtLoacal(urlPath);
			String saveDir = XtomFileUtil.getExternalMemoryPath();
			String pakage = context.getPackageName();
			String folder = "images";
			int dot = pakage.lastIndexOf('.');
			if (dot != -1) {
				folder = pakage.substring(dot + 1);
			}
			saveDir += ("/hemaapp/" + folder + "/");
			String fileName = XtomTimeUtil
					.getCurrentTime("yyyy-MM-dd_HH-mm-ss") + ".jpg";
			savePath = saveDir + fileName;
			if (XtomFileUtil.copy(imgPath, savePath)) {
//				XtomToastUtil.showShortToast(context, "图片已保存至" + saveDir);
				Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri uri = Uri.fromFile(new File(savePath));
				intent.setData(uri);
				context.sendBroadcast(intent);//
			} else {
				XtomToastUtil.showShortToast(context, "图片保存失败");
			}
		} catch (Exception e) {
			XtomToastUtil.showShortToast(context, "图片保存失败");
		}
		return savePath;
	}
	/*
	 * 隐藏手机号 
	 *
	 */
	public static String hide(String old, String keytype) {
		try {
			if ("1".equals(keytype))
				return old.substring(0, 3) + "****" + old.substring(7, 11);
			else {
				StringBuilder sb = new StringBuilder();
				String[] s = old.split("@");
				int l = s[0].length();
				int z = l / 3;
				sb.append(s[0].substring(0, z));
				int y = l % 3;
				for (int i = 0; i < z + y; i++)
					sb.append("*");
				sb.append(s[0].substring(z * 2 + y, l));
				sb.append("@");
				if (s[1] == null) {

				}
				sb.append(s[1]);
				return sb.toString();
			}
		} catch (Exception e) {
			return "";
		}

	}
	    //获取周几
	    public static String getWeek(){
	    	String week ="周一";
			final Calendar c = Calendar.getInstance();
			switch (c.get(Calendar.DAY_OF_WEEK)) {
			case 1:
				week="周日";
				break;
			case 2:
				week="周一";
				break;
			case 3:
				week="周二";
				break;
			case 4:
				week="周三";
				break;
			case 5:
				week="周四";
				break;
			case 6:
				week="周五";
				break;
			case 7:
				week="周六";
				break;
			}
			return week;
	    }
	    //将字符串转成整形
	    public static int getIntPoint(String point)
	    {
	    	int i;
	    	if(point==null||point=="")
	    	{
	    		i=0;
	    	}else
	    		{
	    		i= Integer.parseInt(point);
	    		}
	    	return i;
	    }
		/** 首先默认个文件保存路径 */
		private static final String SAVE_PIC_PATH = Environment
				.getExternalStorageState().equalsIgnoreCase(
						Environment.MEDIA_MOUNTED) ? Environment
				.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";// 保存到SD卡
		private static final String SAVE_REAL_PATH = SAVE_PIC_PATH
				+ "/good/savePic/";// 保存的确切位置

		public static String saveFile(Bitmap bm){
			String fileName = XtomBaseUtil.getFileName() + ".jpg";
			String subForder = SAVE_REAL_PATH ;
			String path=subForder+fileName;
			File foder = new File(subForder);
			if (!foder.exists()) {
				foder.mkdirs();
			}
			File myCaptureFile = new File(subForder, fileName);
			if (!myCaptureFile.exists()) {
				try {
					myCaptureFile.createNewFile();
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(myCaptureFile));
					bm.compress(Bitmap.CompressFormat.JPEG,80, bos);
					bos.flush();
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return path;
		}

	public static String getSpecifiedDayBefore(String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day-1);

		String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}
	//获取大前一天
	public static String getSpecifiedDayBefore1(String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day-2);

		String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}
	/**
	 * 获得指定日期的后一天
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day+1);
		String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

}

