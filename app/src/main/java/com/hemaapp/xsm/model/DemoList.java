package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 案例列表
 * Created by lenovo on 2017/3/20.
 */
public class DemoList extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String industry;
    private String industry_text;
    private String address;
    private String villige;
    private String imgurl;
    private String imgurlbig;
    private String regdate;
    public DemoList(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                industry = get(jsonObject, "industry");
                industry_text = get(jsonObject, "industry_text");
                address = get(jsonObject, "address");
                villige = get(jsonObject, "villige");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                regdate = get(jsonObject, "regdate");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "DemoList{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", industry='" + industry + '\'' +
                ", industry_text='" + industry_text + '\'' +
                ", villige='" + villige + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", regdate='" + regdate + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getIndustry() {
        return industry;
    }

    public String getIndustry_text() {
        return industry_text;
    }

    public String getName() {
        return name;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getVillige() {
        return villige;
    }
}
