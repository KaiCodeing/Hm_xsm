package com.hemaapp.xsm.model;

import com.hemaapp.hm_FrameWork.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/20.
 * 案例详情
 *
 */
public class DemoGet extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String regdate;
    private String industry_text;
    private String villige;
    private String address;
    private String iscollect;
    private String lng;
    private String lat;
    private String mvimgurl;
    private String mvimgurlbig;
    private String mvurl;
    private ArrayList<Image> imgitem;
    public DemoGet(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                iscollect = get(jsonObject, "iscollect");
                industry_text = get(jsonObject, "industry_text");
                address = get(jsonObject, "address");
                villige = get(jsonObject, "villige");
                lng = get(jsonObject, "lng");
                lat = get(jsonObject, "lat");
                regdate = get(jsonObject, "regdate");
                mvimgurl = get(jsonObject, "mvimgurl");
                mvimgurlbig = get(jsonObject, "mvimgurlbig");
                mvurl = get(jsonObject, "mvurl");
                if (!jsonObject.isNull("imgitem")
                        && !isNull(jsonObject.getString("imgitem"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("imgitem");
                    int size = jsonList.length();
                    imgitem = new ArrayList<Image>();
                    for (int i = 0; i < size; i++)
                        imgitem
                                .add(new Image(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "DemoGet{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", regdate='" + regdate + '\'' +
                ", industry_text='" + industry_text + '\'' +
                ", villige='" + villige + '\'' +
                ", iscollect='" + iscollect + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", mvimgurl='" + mvimgurl + '\'' +
                ", mvimgurlbig='" + mvimgurlbig + '\'' +
                ", mvurl='" + mvurl + '\'' +
                ", imgitem=" + imgitem +
                '}';
    }

    public void setIscollect(String iscollect) {
        this.iscollect = iscollect;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Image> getImgitem() {
        return imgitem;
    }

    public String getIndustry_text() {
        return industry_text;
    }

    public String getIscollect() {
        return iscollect;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getMvimgurl() {
        return mvimgurl;
    }

    public String getMvimgurlbig() {
        return mvimgurlbig;
    }

    public String getMvurl() {
        return mvurl;
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
