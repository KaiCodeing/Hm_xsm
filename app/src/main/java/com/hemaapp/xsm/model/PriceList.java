package com.hemaapp.xsm.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/17.
 * 媒体价格列表
 */
public class PriceList extends XtomObject implements Serializable {
    private String name;
    private ArrayList<Price> price;
    public PriceList(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {

                name = get(jsonObject, "name");
                if (!jsonObject.isNull("price")
                        && !isNull(jsonObject.getString("price"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("price");
                    int size = jsonList.length();
                    price = new ArrayList<Price>();
                    for (int i = 0; i < size; i++)
                        price.add(new Price(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "PriceList{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public String getName() {
        return name;
    }

    public ArrayList<Price> getPrice() {
        return price;
    }
}
