package com.hemaapp.xsm.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.xsm.R;

import java.io.File;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;

public class JhctmImageWay extends XtomObject {
	private Activity mContext;// 上下文对象
	private Fragment mFragment;// 上下文对象
	private PopupWindow mWindow;
	private View mView;
	protected int albumRequestCode;//  相册选择时startActivityForResult方法的requestCodeֵ
	protected int cameraRequestCode;//拍照选择时startActivityForResult方法的requestCodeֵ
	private static final String IMAGE_TYPE = ".jpg";// 图片名后缀
	private String imagePathByCamera;// 拍照时图片保存路径
	
	public JhctmImageWay(Activity mContext, int albumRequestCode,
						 int cameraRequestCode) {
		this.mContext = mContext;
		this.albumRequestCode = albumRequestCode;
		this.cameraRequestCode = cameraRequestCode;
	}
	
	public JhctmImageWay(Fragment mFragment, int albumRequestCode,
						 int cameraRequestCode) {
		this.mFragment = mFragment;
		this.albumRequestCode = albumRequestCode;
		this.cameraRequestCode = cameraRequestCode;
	}
	
	private void initPop() {
		Context context = mContext == null ? mFragment.getActivity() : mContext;
		
		mWindow = new PopupWindow(context);
		mWindow.setWidth(LayoutParams.MATCH_PARENT);
		mWindow.setHeight(LayoutParams.MATCH_PARENT);
		mWindow.setBackgroundDrawable(new BitmapDrawable());
		mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mWindow.setFocusable(true);
		mWindow.setAnimationStyle(R.style.PopupAnimation);
		mView = LayoutInflater.from(context).inflate(R.layout.popwindo_camera, null);
		
		View top = mView.findViewById(R.id.imageway_pop_top);
		TextView cameraTxt = (TextView) mView.findViewById(R.id.camera_text);
		TextView albumTxt = (TextView) mView.findViewById(R.id.album_text);
		TextView cancelTxt = (TextView) mView.findViewById(R.id.textView1_camera);
		LinearLayout close_ly_co = (LinearLayout) mView.findViewById(R.id.close1);
		close_ly_co.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWindow.dismiss();
			}
		});
		top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWindow.dismiss();
			}
		});
		cameraTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				camera();
				mWindow.dismiss();
			}
		});
		albumTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				album();
				mWindow.dismiss();
			}
		});
		cancelTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWindow.dismiss();
			}
		});
		
		mWindow.setContentView(mView);
	}
	
	public void show() {
		if (mWindow == null) 
			initPop();
		mWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
	}
	
	public void album() {
		Intent it1 = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		if (mContext != null)
			mContext.startActivityForResult(it1, albumRequestCode);
		else
			mFragment.startActivityForResult(it1, albumRequestCode);
	}

	public void camera() {
		String imageName = XtomBaseUtil.getFileName() + IMAGE_TYPE;
		Intent it3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String imageDir = XtomFileUtil
				.getTempFileDir(mContext == null ? mFragment.getActivity()
						: mContext);
		imagePathByCamera = imageDir + imageName;
		File file = new File(imageDir);
		if (!file.exists())
			file.mkdir();
		// 设置图片保存路径
		File out = new File(file, imageName);
		Uri uri = Uri.fromFile(out);
		it3.putExtra(MediaStore.EXTRA_OUTPUT, uri);

		if (mContext != null)
			if (Build.VERSION.SDK_INT >= 23) {
				int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
				if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
					ActivityCompat.requestPermissions(mContext,new String[]{Manifest.permission.CAMERA},222);
					return;
				}else{

					mContext.startActivityForResult(it3, cameraRequestCode);
				}
			} else {

				mContext.startActivityForResult(it3, cameraRequestCode);
			}

		else
		if (Build.VERSION.SDK_INT >= 23) {
			int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
			if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
				ActivityCompat.requestPermissions(mContext,new String[]{Manifest.permission.CAMERA},222);
				return;
			}else{
				mFragment.startActivityForResult(it3, cameraRequestCode);
			}
		} else {
			mFragment.startActivityForResult(it3, cameraRequestCode);
		}


	}

	/**
	 * 获取拍照图片路径
	 * 
	 * @return 图片路径
	 */
	public String getCameraImage() {
		return imagePathByCamera;
	}
}
