package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/17.
 * 行业列表
 */
public class Industry extends XtomObject implements Serializable {
    private String id;
    private String name;
    private boolean check;
    public Industry(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                check =false;
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }
    public Industry(String id,String name,boolean check)
    {
        this.id = id;
        this.name = name;
        this.check = check;
    }
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Industry{" +
                "check=" + check +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
