package com.hemaapp.xsm.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/14.
 * 媒体
 */
public class Media  extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String status;
    private String up_date;
    private String down_date;
    private String mtype;
    private String imgurl;
    private String imgurlbig;
    private String lng;
    private String lat;
    private String isedit;
    private String change_flag;
    private String ingress;
    private String spec;
    private boolean check;
    private String imgurl_d;
    private String  imgurlbig_d;
    public Media(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                ingress = get(jsonObject, "ingress");
                spec = get(jsonObject, "spec");
                status = get(jsonObject, "status");
                up_date = get(jsonObject, "up_date");
                down_date = get(jsonObject, "down_date");
                mtype = get(jsonObject, "mtype");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                lng = get(jsonObject, "lng");
                lat = get(jsonObject, "lat");
                isedit = get(jsonObject, "isedit");
                change_flag = get(jsonObject, "change_flag");
                check = false;
                imgurl_d = get(jsonObject, "imgurl_d");
                imgurlbig_d = get(jsonObject, "imgurlbig_d");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Media{" +
                "change_flag='" + change_flag + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", up_date='" + up_date + '\'' +
                ", down_date='" + down_date + '\'' +
                ", mtype='" + mtype + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", isedit='" + isedit + '\'' +
                ", ingress='" + ingress + '\'' +
                ", spec='" + spec + '\'' +
                ", check=" + check +
                ", imgurl_d='" + imgurl_d + '\'' +
                ", imgurlbig_d='" + imgurlbig_d + '\'' +
                '}';
    }

    public String getImgurl_d() {
        return imgurl_d;
    }

    public String getImgurlbig_d() {
        return imgurlbig_d;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    public boolean isCheck() {
        return check;
    }

    public String getIngress() {
        return ingress;
    }

    public String getSpec() {
        return spec;
    }

    public void setIsedit(String isedit) {
        this.isedit = isedit;
    }

    public String getChange_flag() {
        return change_flag;
    }

    public String getIsedit() {
        return isedit;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getDown_date() {
        return down_date;
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

    public String getMtype() {
        return mtype;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getUp_date() {
        return up_date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUp_date(String up_date) {
        this.up_date = up_date;
    }

    public void setDown_date(String down_date) {
        this.down_date = down_date;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }
}
