package com.hemaapp.xsm.model;

import com.amap.api.services.core.LatLonPoint;

import xtom.frame.XtomObject;

public class MyPoi extends XtomObject {
	private String name;
	private String address;
	private LatLonPoint ll;
	private boolean check;

	public MyPoi(String name, String address, LatLonPoint ll, boolean check) {
		super();
		this.name = name;
		this.address = address;
		this.ll = ll;
		this.check = check;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public LatLonPoint getLl() {
		return ll;
	}

	public boolean isCheck() {
		return check;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

}
