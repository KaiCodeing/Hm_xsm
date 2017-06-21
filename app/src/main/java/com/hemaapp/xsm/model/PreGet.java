package com.hemaapp.xsm.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/6/7.
 * 预定详情 列表
 */
public class PreGet extends XtomObject implements Serializable {
    private String id;
    private String name_str;
    private String up_date;
    private String down_date;
    private String status_text;
    private ArrayList<Media> point_r;
    private String regdate;
    private String custom_name;
    private String status;
    public PreGet(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name_str = get(jsonObject, "name_str");
                up_date = get(jsonObject, "up_date");
                down_date = get(jsonObject, "down_date");
                status_text = get(jsonObject, "status_text");
                status = get(jsonObject, "status");
                if (!jsonObject.isNull("point_r")
                        && !isNull(jsonObject.getString("point_r"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("point_r");
                    int size = jsonList.length();
                    point_r = new ArrayList<Media>();
                    for (int i = 0; i < size; i++)
                        point_r.add(new Media(jsonList.getJSONObject(i)));
                }
                regdate = get(jsonObject, "regdate");
                custom_name = get(jsonObject,"custom_name");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "PreGet{" +
                "custom_name='" + custom_name + '\'' +
                ", id='" + id + '\'' +
                ", name_str='" + name_str + '\'' +
                ", up_date='" + up_date + '\'' +
                ", down_date='" + down_date + '\'' +
                ", status_text='" + status_text + '\'' +
                ", point_r=" + point_r +
                ", regdate='" + regdate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getDown_date() {
        return down_date;
    }

    public String getId() {
        return id;
    }

    public String getName_str() {
        return name_str;
    }

    public ArrayList<Media> getPoint_r() {
        return point_r;
    }

    public String getStatus_text() {
        return status_text;
    }

    public String getUp_date() {
        return up_date;
    }
}
