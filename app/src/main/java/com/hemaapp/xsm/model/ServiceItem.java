package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/21.
 * 客服item
 */
public class ServiceItem extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String city;
    private String type;
    private String linknum;
    public ServiceItem(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                city = get(jsonObject, "city");
                type = get(jsonObject, "type");
                linknum = get(jsonObject, "linknum");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "ServiceItem{" +
                "city='" + city + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", linknum='" + linknum + '\'' +
                '}';
    }

    public String getCity() {
        return city;
    }

    public String getId() {
        return id;
    }

    public String getLinknum() {
        return linknum;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
