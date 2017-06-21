package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/17.
 * 媒体价格
 *
 */
public class Price extends XtomObject implements Serializable {
    private String id;
    private String edit;
    private String city;
    private String unionprice;
    private String dealprice1;
    private String dealprice2;
    private String demoprice;
    private String regdate;
    public Price(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                edit = get(jsonObject, "edit");
                city = get(jsonObject, "city");
                unionprice = get(jsonObject, "unionprice");
                dealprice1 = get(jsonObject, "dealprice1");
                dealprice2 = get(jsonObject, "dealprice2");
                demoprice = get(jsonObject, "demoprice");
                regdate = get(jsonObject, "regdate");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Price{" +
                "city='" + city + '\'' +
                ", id='" + id + '\'' +
                ", edit='" + edit + '\'' +
                ", unionprice='" + unionprice + '\'' +
                ", dealprice1='" + dealprice1 + '\'' +
                ", dealprice2='" + dealprice2 + '\'' +
                ", demoprice='" + demoprice + '\'' +
                ", regdate='" + regdate + '\'' +
                '}';
    }

    public String getCity() {
        return city;
    }

    public String getDealprice1() {
        return dealprice1;
    }

    public String getDealprice2() {
        return dealprice2;
    }

    public String getDemoprcie() {
        return demoprice;
    }

    public String getEdit() {
        return edit;
    }

    public String getId() {
        return id;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getUnionprice() {
        return unionprice;
    }
}
