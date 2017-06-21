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
 * Created by lenovo on 2017/3/22.
 * 联盟公司
 */
public class Union extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String linkman;
    private String linknum;
    private String address;
    private String type;
    private String lng;
    private String lat;
    private String iscollect;
    private ArrayList<Image> imgitem;
    public Union(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                linkman = get(jsonObject, "linkman");
                type = get(jsonObject, "type");
                linknum = get(jsonObject, "linknum");
                address = get(jsonObject, "address");
                lng = get(jsonObject, "lng");
                lat = get(jsonObject, "lat");
                iscollect = get(jsonObject, "iscollect");
                if (!jsonObject.isNull("imgitem")
                        && !isNull(jsonObject.getString("imgitem"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("imgitem");
                    int size = jsonList.length();
                    imgitem = new ArrayList<Image>();
                    for (int i = 0; i < size; i++)
                        imgitem.add(new Image(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Union{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", linkman='" + linkman + '\'' +
                ", linknum='" + linknum + '\'' +
                ", type='" + type + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", iscollect='" + iscollect + '\'' +
                ", imgitem=" + imgitem +
                '}';
    }

    public void setIscollect(String iscollect) {
        this.iscollect = iscollect;
    }

    public ArrayList<Image> getImgitem() {
        return imgitem;
    }

    public String getIscollect() {
        return iscollect;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getLinkman() {
        return linkman;
    }

    public String getLinknum() {
        return linknum;
    }

    public String getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
