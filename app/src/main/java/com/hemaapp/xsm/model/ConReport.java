package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/17.
 * 全国单的content
 */
public class ConReport extends XtomObject implements Serializable {
    private String content;
    private String regtime;
    public ConReport(JSONObject jsonObject) throws DataParseException {
        if(jsonObject!=null){
            try {
                content = get(jsonObject, "content");
                regtime = get(jsonObject, "regtime");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "ConReport{" +
                "content='" + content + '\'' +
                ", regtime='" + regtime + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public String getRegtime() {
        return regtime;
    }
}
