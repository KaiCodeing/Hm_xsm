package com.hemaapp.xsm.model;

import com.hemaapp.hm_FrameWork.HemaUser;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/9/6.
 */
public class User extends HemaUser {

    private String id;// 用户主键
    private String username;// 用户名
    private String email;// 用户邮箱
    private String nickname;// 用户昵称
    private String mobile;// 手机号码
    private String password;// 登录密码
    private String charindex;// 用户昵称的汉语拼音首字母索引
    private String sex;// 用户性别
    private String avatar;// 个人主页头像图片（小）
    private String avatarbig;// 个人主页头像图片（大）
    private String district_name;// 注册地区
    private String lng;// 经度
    private String lat;// 纬度
    private String regdate;// 用户注册时间
    private String score;// 用户积分
    private String feeaccount;// 用户余额
    private String birthday;//生日
    private String role;//角色  1:普通用户;2:vip用户
    private String company;//单位
    private String top_week;//每周新教师标志 0否;1是
    private String top_year;//年度教师标志 0否;1是
    private String backimg;//个人中心背景
    private String noticecount;//未读通知数
    private String visitorcount;//访客数量
    private String fancount;//粉丝数量
    private String followcount;//关注数量
    private String blogcount;//发布数
    private String followflag;//是否关注标记 0否;1是//银行银行开户
    private String signinflag;//今日是否已签到 0否;1是//银行卡号
    private String level_exp;//当前等级所需积分//支付宝
    private String level_name;//当前等级名称//银行卡
    private String level_imgurl;//当前等级图片//银行真实姓名
    private String selfsign;//职位
    private String contisignintimes;//用户类型对应文字
    private String expiredflag;//1普通用户2联盟公司ceo3联盟公司媒介4其他

    public User(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");

                username = get(jsonObject, "username");
                email = get(jsonObject, "email");
                nickname = get(jsonObject, "nickname");
                mobile = get(jsonObject, "mobile");
                password = get(jsonObject, "password");
                charindex = get(jsonObject, "charindex");
                sex = get(jsonObject, "sex");
                avatar = get(jsonObject, "avatar");
                avatarbig = get(jsonObject, "avatarbig");
                district_name = get(jsonObject, "location");
                lng = get(jsonObject, "lng");
                lat = get(jsonObject, "lat");
                regdate = get(jsonObject, "regdate");
                score = get(jsonObject, "score");
                feeaccount = get(jsonObject, "collectcount");
                birthday = get(jsonObject,"birthday");
                role = get(jsonObject,"role");
                company = get(jsonObject,"compony");
                top_week = get(jsonObject,"top_week");
                top_year = get(jsonObject,"top_year");
                backimg = get(jsonObject,"backimg");
                noticecount = get(jsonObject, "noticecount");
                visitorcount = get(jsonObject, "visitorcount");
                fancount = get(jsonObject, "fancount");
                followcount = get(jsonObject, "followcount");
                blogcount = get(jsonObject, "blogcount");
                followflag = get(jsonObject, "bank");
                signinflag = get(jsonObject, "bankcard");
                level_exp = get(jsonObject, "alipayname");
                level_name = get(jsonObject, "bankname");
                level_imgurl = get(jsonObject, "bankuser");
                selfsign = get(jsonObject, "post");
                contisignintimes = get(jsonObject, "client_text");
                expiredflag = get(jsonObject, "notice_switch");
                score = get(jsonObject, "score");
                //
                feeaccount = get(jsonObject, "client_type");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public void setExpiredflag(String expiredflag) {
        this.expiredflag = expiredflag;
    }

    public User(String id, String token, String username, String email,
                String nickname, String mobile, String password, String charindex,
                String sex, String avatar, String avatarbig, String district_name,
                String lng, String lat, String regdate, String score,
                String feeaccount, String birthday, String role, String company, String top_week,
                String top_year, String backimg, String noticecount, String visitorcount,
                String fancount, String followcount, String blogcount, String followflag,
                String signinflag, String level_exp, String level_name, String level_imgurl,
                String selfsign, String contisignintimes, String expiredflag) {
        super(token);
        this.id = id;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.mobile = mobile;
        this.password = password;
        this.charindex = charindex;
        this.sex = sex;
        this.avatar = avatar;
        this.avatarbig = avatarbig;
        this.district_name = district_name;
        this.lng = lng;
        this.lat = lat;
        this.regdate = regdate;
        this.score = score;
        this.feeaccount = feeaccount;
        this.birthday = birthday;

        this.role = role;
        this.company = company;
        this.top_week = top_week;
        this.top_year = top_year;
        this.backimg = backimg;
        this.noticecount = noticecount;

        this.visitorcount = visitorcount;
        this.fancount = fancount;
        this.followcount = followcount;
        this.blogcount = blogcount;
        this.followflag = followflag;
        this.signinflag = signinflag;
        this.level_exp = level_exp;
        this.level_name = level_name;
        this.level_imgurl = level_imgurl;
        this.selfsign = selfsign;
        this.contisignintimes = contisignintimes;
        this.expiredflag = expiredflag;
    }





    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email
                + ", nickname=" + nickname + ", mobile=" + mobile
                + ", password=" + password + ", charindex=" + charindex
                + ", sex=" + sex + ", avatar=" + avatar + ", avatarbig="
                + avatarbig + ", district_name=" + district_name + ", lng="
                + lng + ", lat=" + lat + ", regdate=" + regdate + ", score="
                + score + ", feeaccount=" + feeaccount + ", birthday="
                + birthday + ", role=" + role + ", company=" + company
                + ", top_week=" + top_week + ", top_year=" + top_year
                + ", backimg=" + backimg + ", noticecount=" + noticecount
                + ", visitorcount=" + visitorcount + ", fancount=" + fancount
                + ", followcount=" + followcount + ", blogcount=" + blogcount
                + ", followflag=" + followflag + ", signinflag=" + signinflag
                + ", level_exp=" + level_exp + ", level_name=" + level_name
                + ", level_imgurl=" + level_imgurl + ", selfsign=" + selfsign
                + ", contisignintimes=" + contisignintimes + ", expiredflag="
                + expiredflag + "]";
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    public String getExpiredflag() {
        return expiredflag;
    }

    public String getContisignintimes() {
        return contisignintimes;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getCharindex() {
        return charindex;
    }

    public String getSex() {
        return sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarbig() {
        return avatarbig;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getScore() {
        return score;
    }

    public String getFeeaccount() {
        return feeaccount;
    }

    public String getRole() {
        return role;
    }

    public String getCompany() {
        return company;
    }

    public String getTop_week() {
        return top_week;
    }

    public String getTop_year() {
        return top_year;
    }

    public String getBackimg() {
        return backimg;
    }
    public String getNoticecount() {
        return noticecount;
    }

    public void setNoticecount(String noticecount) {
        this.noticecount = noticecount;
    }

    public String getVisitorcount() {
        return visitorcount;
    }

    public String getFancount() {
        return fancount;
    }

    public String getFollowcount() {
        return followcount;
    }

    public String getBlogcount() {
        return blogcount;
    }

    public String getFollowflag() {
        return followflag;
    }

    public String getSigninflag() {
        return signinflag;
    }

    public String getLevel_exp() {
        return level_exp;
    }

    public String getLevel_name() {
        return level_name;
    }

    public String getLevel_imgurl() {
        return level_imgurl;
    }

    public String getSelfsign() {
        return selfsign;
    }


}
