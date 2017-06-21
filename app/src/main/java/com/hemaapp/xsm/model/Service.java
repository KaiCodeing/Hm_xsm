package com.hemaapp.xsm.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/21.
 * 客服列表
 */
public class Service extends XtomObject implements Serializable {
    private String province;
    private ArrayList<ServiceItem> price;
    public Service(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                province = get(jsonObject, "province");

                if (!jsonObject.isNull("price")
                        && !isNull(jsonObject.getString("price"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("price");
                    int size = jsonList.length();
                    price = new ArrayList<ServiceItem>();
                    for (int i = 0; i < size; i++)
                        price.add(new ServiceItem(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public ArrayList<ServiceItem> getPrice() {
        return price;
    }

    public String getProvince() {
        return province;
    }

    @Override
    public String toString() {
        return "Service{" +
                "price=" + price +
                ", province='" + province + '\'' +
                '}';
    }
}
