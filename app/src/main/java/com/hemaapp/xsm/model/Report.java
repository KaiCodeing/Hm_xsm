package com.hemaapp.xsm.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2017/3/17.
 * 全国单
 */
public class Report extends XtomObject implements Serializable {
    private String id;
    private String area;
    private String district;
    private String putnum;
    private String startdate;
    private String enddate;
    private String industry_text;
    private String industry;
    private String name;
    private String linkname;
    private String linkpost;
    private String nickname;
    private String mobile;
    private String post;
    private String regdate;
    private String status;
    private String status_text;
    private String compony;
    private String iscollect;
    private ArrayList<ConReport> con;
    public Report(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                area = get(jsonObject, "area");
                district = get(jsonObject, "district");
                putnum = get(jsonObject, "putnum");
                startdate = get(jsonObject, "startdate");
                enddate = get(jsonObject, "enddate");
                industry_text = get(jsonObject, "industry_text");
                industry = get(jsonObject, "industry");
                compony = get(jsonObject, "compony");
                name = get(jsonObject, "name");
                linkname = get(jsonObject, "linkname");
                linkpost = get(jsonObject, "linkpost");
                compony = get(jsonObject, "compony");
                nickname = get(jsonObject, "nickname");
                mobile = get(jsonObject, "mobile");
                post = get(jsonObject, "post");
                regdate = get(jsonObject, "regdate");
                status = get(jsonObject, "status");
                status_text = get(jsonObject, "status_text");
                iscollect = get(jsonObject,"iscollect");
                if (!jsonObject.isNull("con")
                        && !isNull(jsonObject.getString("con"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("con");
                    int size = jsonList.length();
                    con = new ArrayList<ConReport>();
                    for (int i = 0; i < size; i++)
                        con.add(new ConReport(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public void setIscollect(String iscollect) {
        this.iscollect = iscollect;
    }

    public String getIscollect() {
        return iscollect;
    }

    public String getArea() {
        return area;
    }

    public String getCompony() {
        return compony;
    }

    public ArrayList<ConReport> getCon() {
        return con;
    }

    public String getDistrict() {
        return district;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getId() {
        return id;
    }

    public String getIndustry() {
        return industry;
    }

    public String getIndustry_text() {
        return industry_text;
    }

    public String getLinkname() {
        return linkname;
    }

    public String getLinkpost() {
        return linkpost;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPost() {
        return post;
    }

    public String getPutnum() {
        return putnum;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getStatus() {
        return status;
    }

    public String getStatus_text() {
        return status_text;
    }

    @Override
    public String toString() {
        return "Report{" +
                "area='" + area + '\'' +
                ", id='" + id + '\'' +
                ", district='" + district + '\'' +
                ", putnum='" + putnum + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", industry_text='" + industry_text + '\'' +
                ", industry='" + industry + '\'' +
                ", name='" + name + '\'' +
                ", linkname='" + linkname + '\'' +
                ", linkpost='" + linkpost + '\'' +
                ", nickname='" + nickname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", post='" + post + '\'' +
                ", regdate='" + regdate + '\'' +
                ", status='" + status + '\'' +
                ", status_text='" + status_text + '\'' +
                ", compony='" + compony + '\'' +
                ", iscollect='" + iscollect + '\'' +
                ", con=" + con +
                '}';
    }
}
