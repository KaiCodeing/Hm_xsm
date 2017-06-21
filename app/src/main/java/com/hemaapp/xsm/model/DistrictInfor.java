package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 *
 * */
public class DistrictInfor extends XtomObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id; //主键id
	private String name; //地区名称
	private String parentid; //父级别主键
	private String nodepath; //节点主键路径串
	private String namepath; //节点名称路径串
	private String charindex; //名称拼音首字母索引
	private String level; //节点层级
	private String orderby; //排序优先级
	private String imgurl;
	private String checkflag; //0否1是
	private boolean check;
	public DistrictInfor(JSONObject jsonObject) throws DataParseException {
		if(jsonObject!=null){
			try {
				id = get(jsonObject, "id");
				name = get(jsonObject, "name");
				parentid = get(jsonObject, "parentid");
				nodepath = get(jsonObject, "nodepath");
				namepath = get(jsonObject, "namepath");
				charindex = get(jsonObject, "charindex");
				level = get(jsonObject, "level");
				orderby = get(jsonObject, "orderby");
				checkflag = get(jsonObject, "checkflag");
				imgurl = get(jsonObject,"imgurl");
				check = false;
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	public String getImgurl() {
		return imgurl;
	}

	@Override
	public String toString() {
		return "DistrictInfor{" +
				"charindex='" + charindex + '\'' +
				", id='" + id + '\'' +
				", name='" + name + '\'' +
				", parentid='" + parentid + '\'' +
				", nodepath='" + nodepath + '\'' +
				", namepath='" + namepath + '\'' +
				", level='" + level + '\'' +
				", orderby='" + orderby + '\'' +
				", imgurl='" + imgurl + '\'' +
				", checkflag='" + checkflag + '\'' +
				", check=" + check +
				'}';
	}

	public String getCheckflag() {
		return checkflag;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public DistrictInfor(String id, String name, String parentid, String nodepath,
						 String namepath, String charindex, String level, String orderby) {
		this.id = id;
		this.name = name;
		this.parentid = parentid;
		this.nodepath = nodepath;
		this.namepath = namepath;
		this.charindex = charindex;
		this.level = level;
		this.orderby = orderby;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getParentid() {
		return parentid;
	}

	public String getNodepath() {
		return nodepath;
	}

	public String getNamepath() {
		return namepath;
	}

	public String getCharindex() {
		return charindex;
	}

	public String getLevel() {
		return level;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}
	
}
