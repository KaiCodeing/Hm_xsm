package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/21.
 * 模板
 */
public class Argument extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String modelurl;
    private String regdate;
    public Argument(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                modelurl = get(jsonObject, "modelurl");
                regdate = get(jsonObject, "regdate");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getModelurl() {
        return modelurl;
    }

    public String getName() {
        return name;
    }

    public String getRegdate() {
        return regdate;
    }

    @Override
    public String toString() {
        return "Argument{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", modelurl='" + modelurl + '\'' +
                ", regdate='" + regdate + '\'' +
                '}';
    }
}
