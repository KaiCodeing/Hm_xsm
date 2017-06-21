package com.hemaapp.xsm.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * ���������б���ر������
 * @author lenovo
 *
 */
public class CitySan extends XtomObject implements Serializable {

	private static final long serialVersionUID = 1265819772736012294L;
	private ArrayList<CityChildren> children=new ArrayList<CityChildren>();

	public CitySan(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				if (!jsonObject.isNull("children")
						&& !isNull(jsonObject.getString("children"))) {
					JSONArray jsonList = jsonObject.getJSONArray("children");
					int size = jsonList.length();
					children = new ArrayList<CityChildren>();
					for (int i = 0; i < size; i++) {
						children.add(new CityChildren(jsonList.getJSONObject(i)));
					}
				}
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	public CitySan() {
		super();
		this.children = children;
	}

	public ArrayList<CityChildren> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<CityChildren> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "City [children=" + children + "]";
	}

}
