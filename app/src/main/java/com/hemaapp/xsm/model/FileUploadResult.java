package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 文件上传结果
 */
public class FileUploadResult extends XtomObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String item1;// 请查看标准化api文档
	private String item2;// 请查看标准化api文档
	private String imgwidth;//图片宽度
	private String imgheight;//图片高度
	public FileUploadResult(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				item1 = get(jsonObject, "item1");
				item2 = get(jsonObject, "item2");
				imgwidth = get(jsonObject, "imgwidth");
				imgheight = get(jsonObject, "imgheight");
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getImgwidth() {
		return imgwidth;
	}



	public String getImgheight() {
		return imgheight;
	}



	@Override
	public String toString() {
		return "FileUploadResult [item1=" + item1 + ", item2=" + item2
				+ ", imgwidth=" + imgwidth + ", imgheight=" + imgheight + "]";
	}



	/**
	 * @return the item1
	 */
	public String getItem1() {
		return item1;
	}

	/**
	 * @return the item2
	 */
	public String getItem2() {
		return item2;
	}

}
