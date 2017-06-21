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
 * Created by lenovo on 2017/3/16.
 * 媒体详情
 */
public class MediaGet extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String status;
    private String up_date;
    private String down_date;
    private String mtype;
    private String address;
    private String district1;
    private String district2;
    private String district3;
    private String province;
    private String city;
    private String area;
    private String spec;
    private String clientname;
    private String clientmobile;
    private String iscollect;
    private String limitcontent;
    private String limitself;
    private String lng;
    private String lat;
    private String union;
    private String unionid;
    private String regdate;
    private ArrayList<Image> imgitem;
    private ArrayList<Image> imgcloseitem;

    private String  ingress;;
    private String house_type_text;
    private String house_price;
    private String people_num;
    private String park_num;
    private String custom_name;
    private String imgurl_d;
    private String imgurlbig_d;
    private String linknum;
    private String recuitnum;
    private String change_flag;

    public MediaGet(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                recuitnum = get(jsonObject, "recuitnum");
                change_flag = get(jsonObject, "change_flag");
                ingress = get(jsonObject, "ingress");
                house_type_text = get(jsonObject, "house_type_text");
                house_price = get(jsonObject, "house_price");
                people_num = get(jsonObject, "people_num");
                park_num = get(jsonObject, "park_num");
                custom_name = get(jsonObject, "custom_name");
                imgurl_d = get(jsonObject, "imgurl_d");
                imgurlbig_d = get(jsonObject, "imgurlbig_d");
                linknum = get(jsonObject, "linknum");
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                status = get(jsonObject, "status");
                up_date = get(jsonObject, "up_date");
                down_date = get(jsonObject, "down_date");
                mtype = get(jsonObject, "mtype");
                address = get(jsonObject, "address");
                district1 = get(jsonObject, "district1");

                district2 = get(jsonObject, "district2");
                district3 = get(jsonObject, "district3");
                province = get(jsonObject, "province");
                city = get(jsonObject, "city");
                area = get(jsonObject, "area");
                spec = get(jsonObject, "spec");
                clientname = get(jsonObject, "clientname");
                clientmobile = get(jsonObject, "clientmobile");
                iscollect = get(jsonObject, "iscollect");
                limitcontent = get(jsonObject, "limitcontent");
                limitself = get(jsonObject, "limitself");
                lng = get(jsonObject, "lng");
                lat = get(jsonObject, "lat");
                union = get(jsonObject, "union");
                unionid = get(jsonObject, "unionid");
                regdate = get(jsonObject, "regdate");
                if (!jsonObject.isNull("imgitem")
                        && !isNull(jsonObject.getString("imgitem"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("imgitem");
                    int size = jsonList.length();
                    imgitem = new ArrayList<Image>();
                    for (int i = 0; i < size; i++)
                        imgitem
                                .add(new Image(jsonList.getJSONObject(i)));
                }
                if (!jsonObject.isNull("imgcloseitem")
                        && !isNull(jsonObject.getString("imgcloseitem"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("imgcloseitem");
                    int size = jsonList.length();
                    imgcloseitem = new ArrayList<Image>();
                    for (int i = 0; i < size; i++)
                        imgcloseitem
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
        return "MediaGet{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", up_date='" + up_date + '\'' +
                ", down_date='" + down_date + '\'' +
                ", mtype='" + mtype + '\'' +
                ", district1='" + district1 + '\'' +
                ", district2='" + district2 + '\'' +
                ", district3='" + district3 + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", spec='" + spec + '\'' +
                ", clientname='" + clientname + '\'' +
                ", clientmobile='" + clientmobile + '\'' +
                ", iscollect='" + iscollect + '\'' +
                ", limitcontent='" + limitcontent + '\'' +
                ", limitself='" + limitself + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", union='" + union + '\'' +
                ", unionid='" + unionid + '\'' +
                ", regdate='" + regdate + '\'' +
                ", imgitem=" + imgitem +
                ", imgcloseitem=" + imgcloseitem +
                ", ingress='" + ingress + '\'' +
                ", house_type_text='" + house_type_text + '\'' +
                ", house_price='" + house_price + '\'' +
                ", people_num='" + people_num + '\'' +
                ", park_num='" + park_num + '\'' +
                ", custom_name='" + custom_name + '\'' +
                ", imgurl_d='" + imgurl_d + '\'' +
                ", imgurlbig_d='" + imgurlbig_d + '\'' +
                ", linknum='" + linknum + '\'' +
                ", recuitnum='" + recuitnum + '\'' +
                ", change_flag='" + change_flag + '\'' +
                '}';
    }

    public String getChange_flag() {
        return change_flag;
    }

    public String getRecuitnum() {
        return recuitnum;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public String getHouse_price() {
        return house_price;
    }

    public String getHouse_type_text() {
        return house_type_text;
    }

    public String getImgurl_d() {
        return imgurl_d;
    }

    public String getImgurlbig_d() {
        return imgurlbig_d;
    }

    public String getIngress() {
        return ingress;
    }

    public String getLinknum() {
        return linknum;
    }

    public String getPark_num() {
        return park_num;
    }

    public String getPeople_num() {
        return people_num;
    }

    public void setIscollect(String iscollect) {
        this.iscollect = iscollect;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getClientmobile() {
        return clientmobile;
    }

    public String getClientname() {
        return clientname;
    }

    public String getDistrict1() {
        return district1;
    }

    public String getDistrict2() {
        return district2;
    }

    public String getDistrict3() {
        return district3;
    }

    public String getDown_date() {
        return down_date;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Image> getImgcloseitem() {
        return imgcloseitem;
    }

    public ArrayList<Image> getImgitem() {
        return imgitem;
    }

    public String getIscollect() {
        return iscollect;
    }

    public String getLat() {
        return lat;
    }

    public String getLimitcontent() {
        return limitcontent;
    }

    public String getLimitself() {
        return limitself;
    }

    public String getLng() {
        return lng;
    }

    public String getMtype() {
        return mtype;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getSpec() {
        return spec;
    }

    public String getStatus() {
        return status;
    }

    public String getUnion() {
        return union;
    }

    public String getUnionid() {
        return unionid;
    }

    public String getUp_date() {
        return up_date;
    }
}
