package com.hemaapp.xsm;

import android.content.Context;

import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.xsm.nettask.AdListTask;
import com.hemaapp.xsm.nettask.ArgumentListTask;
import com.hemaapp.xsm.nettask.ClientAddTask;
import com.hemaapp.xsm.nettask.ClientLoginTask;
import com.hemaapp.xsm.nettask.ClientVerifyTask;
import com.hemaapp.xsm.nettask.CodeGetTask;
import com.hemaapp.xsm.nettask.CodeVerifyTask;
import com.hemaapp.xsm.nettask.DemoGetTask;
import com.hemaapp.xsm.nettask.DemoListTask;
import com.hemaapp.xsm.nettask.DistrictAllListTask;
import com.hemaapp.xsm.nettask.DistrictListTask;
import com.hemaapp.xsm.nettask.FileUploadTask;
import com.hemaapp.xsm.nettask.IndustryListTask;
import com.hemaapp.xsm.nettask.InitTask;
import com.hemaapp.xsm.nettask.MediaGetTask;
import com.hemaapp.xsm.nettask.MediaListTask;
import com.hemaapp.xsm.nettask.MedieSaveTask;
import com.hemaapp.xsm.nettask.NoticeListTask;
import com.hemaapp.xsm.nettask.NoticeUnreadTask;
import com.hemaapp.xsm.nettask.PasswordResetTask;
import com.hemaapp.xsm.nettask.PreListTask;
import com.hemaapp.xsm.nettask.PreTask;
import com.hemaapp.xsm.nettask.PriceListTask;
import com.hemaapp.xsm.nettask.ReportGetTask;
import com.hemaapp.xsm.nettask.ReportListTask;
import com.hemaapp.xsm.nettask.ServiceListTask;
import com.hemaapp.xsm.nettask.Union2ListTask;
import com.hemaapp.xsm.nettask.UnionGetTask;
import com.hemaapp.xsm.nettask.UnionListTask;
import com.hemaapp.xsm.nettask.UpDateInforTask;

import java.util.HashMap;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/6.
 */
public class JhNetWorker extends HemaNetWorker {
    /**
     * 实例化网络请求工具类
     *
     * @param mContext
     */
    private Context mContext;

    public JhNetWorker(Context mContext) {

        super(mContext);
        this.mContext = mContext;


    }

    @Override
    public void clientLogin() {

        JhHttpInformation information = JhHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        String username = XtomSharedPreferencesUtil.get(mContext, "username");
        params.put("username", username);// 用户登录名 手机号或邮箱
        String password = XtomSharedPreferencesUtil.get(mContext, "password");
        params.put("password", password); // 登陆密码 服务器端存储的是32位的MD5加密串
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计

        JhNetTask task = new ClientLoginTask(information, params);
        executeTask(task);

    }

    @Override
    public boolean thirdSave() {
        return false;
    }

    /**
     * @param
     * @param
     * @方法名称: inIt
     * @功能描述: TODO初始化
     * @返回值: void
     */
    public void inIt() {
        JhHttpInformation information = JhHttpInformation.INIT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lastloginversion", HemaUtil.getAppVersionForSever(mContext));// 版本号
        params.put("devicetype", "2");
        JhNetTask netTask = new InitTask(information, params);
        executeTask(netTask);
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     */
    public void clientLogin(String username, String password) {
        JhHttpInformation information = JhHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);// 版本号
        params.put("password", password);
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计
        JhNetTask netTask = new ClientLoginTask(information, params);
        executeTask(netTask);
    }

    /**
     * 用户详情
     *
     * @param //username
     * @param //password
     */
    public void clientGet(String token, String id) {
        JhHttpInformation information = JhHttpInformation.CLIENT_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 版本号
        params.put("id", id);

        String version = HemaUtil.getAppVersionForSever(mContext);
        JhNetTask netTask = new ClientLoginTask(information, params);
        executeTask(netTask);
    }

    /**
     * @param username
     * @param code
     * @方法名称: codeVerify
     * @功能描述: TODO验证验证码
     * @返回值: void
     */
    public void codeVerify(String username, String code) {
        JhHttpInformation information = JhHttpInformation.CODE_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("code", code);
        JhNetTask task = new CodeVerifyTask(information, params);
        executeTask(task);
    }

    /**
     * @param username
     * @方法名称: clientVerify
     * @功能描述: TODO验证用户是否注册过
     * @返回值: void
     */
    public void clientVerify(String username) {
        JhHttpInformation information = JhHttpInformation.CLIENT_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);

        JhNetTask task = new ClientVerifyTask(information, params);
        executeTask(task);
    }

    /**
     * @param username
     * @方法名称: codeGet
     * @功能描述: TODO发送验证码
     * @返回值: void
     */
    public void codeGet(String username, String keytype) {
        JhHttpInformation information = JhHttpInformation.CODE_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("keytype", keytype);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * @param temp_token
     * @param keytype
     * @param new_password
     * @方法名称: passwordReset
     * @功能描述: TODO修改密码
     * @返回值: void
     */
    public void passwordReset(String temp_token, String username, String keytype,
                              String new_password) {
        JhHttpInformation information = JhHttpInformation.PASSWORD_RESET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("keytype", keytype);
        params.put("username", username);
        params.put("new_password", new_password);
        JhNetTask task = new PasswordResetTask(information, params);
        executeTask(task);
    }

    /**
     * @param token
     * @param deviceid
     * @param devicetype
     * @param channelid
     * @方法名称: deviceSave
     * @功能描述: TODO硬件保存
     * @返回值: void
     */
    public void deviceSave(String token, String deviceid, String devicetype,
                           String channelid) {
        JhHttpInformation information = JhHttpInformation.DEVICE_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("deviceid", deviceid);
        params.put("devicetype", devicetype);
        params.put("channelid", channelid);

        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * @param temp_token
     * @param username
     * @param password
     * @方法名称: clientAdd
     * @功能描述: TODO 用户注册
     * @返回值: void
     */
    public void clientAdd(String temp_token, String username, String password,
                          String nickname, String sex, String post, String compony, String mobile) {
        JhHttpInformation information = JhHttpInformation.CLIENT_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("username", username);
        params.put("password", password);
        params.put("nickname", nickname);
        params.put("sex", sex);
        params.put("post", post);
        params.put("compony", compony);
        params.put("mobile", mobile);
        JhNetTask task = new ClientAddTask(information, params);
        executeTask(task);
    }

    /**
     * @param token
     * @param keytype
     * @param keyid
     * @param orderby
     * @param temp_file
     * @方法名称: fileUpload
     * @功能描述: TODO上传文件
     * @返回值: void
     */
    public void fileUpload(String token, String keytype, String keyid,String orderby,
                           String duration,  String content, String temp_file) {
        JhHttpInformation information = JhHttpInformation.FILE_UPLOAD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("orderby", orderby);
        params.put("duration", duration);
        params.put("content", content);
        HashMap<String, String> files = new HashMap<String, String>();
        files.put("temp_file", temp_file);
        JhNetTask task = new FileUploadTask(information, params, files);
        executeTask(task);
    }

    /**
     * 地区列表district_list
     */
    public void districtList(String keytype, String keyword) {
        JhHttpInformation information = JhHttpInformation.DISTRICT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", keytype);
        params.put("keyword", keyword);
        JhNetTask netTask = new DistrictListTask(information, params);
        executeTask(netTask);
    }
    /**
     * 地区列表district_list
     */
    public void districtALLList() {
        JhHttpInformation information = JhHttpInformation.DISTRICT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", "3");
        params.put("keyword", "0");
        JhNetTask netTask = new DistrictAllListTask(information, params);
        executeTask(netTask);
    }
    /**
     * 媒体列表district_list
     */
    public void mediaList(String token, String keytype,String keyword, String status, String district1,
                          String district2,
                          String district3,
                          String limitcontent, String up_date1, String up_date2, String down_date1,
                          String down_date2,
                          String page) {
        JhHttpInformation information = JhHttpInformation.MEDIA_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyword", keyword);
        params.put("status", status);
        params.put("district1", district1);
        params.put("district2", district2);
        params.put("district3", district3);
        params.put("limitcontent", limitcontent);
        params.put("up_date1", up_date1);
        params.put("up_date2", up_date2);
        params.put("down_date1", down_date1);
        params.put("down_date2", down_date2);
        params.put("page", page);
        JhNetTask netTask = new MediaListTask(information, params);
        executeTask(netTask);
    }
    /**
     * media_save 媒体发布，修改
     */
    public void mediaSave(String token, String id,String name,String address,String district1,String district2,String district3,
                          String province,String city,String area,String spec,String up_date,String down_date,String mtype,
                          String status,
                          String limitcontent,String limitself,String lng,String lat,String imgurlstr,String imgcloseurlstr) {
        JhHttpInformation information = JhHttpInformation.MEDIA_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("name", name);
        params.put("address", address);
        params.put("district1", district1);
        params.put("district2", district2);
        params.put("district3", district3);
        params.put("province", province);
        params.put("city", city);
        params.put("area", area);
        params.put("spec", spec);
        params.put("up_date", up_date);
        params.put("down_date", down_date);
        params.put("mtype", mtype);
        params.put("status", status);
        params.put("limitcontent", limitcontent);
        params.put("limitself", limitself);
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("imgurlstr", imgurlstr);
        params.put("imgcloseurlstr", imgcloseurlstr);
        JhNetTask netTask = new MedieSaveTask(information, params);
        executeTask(netTask);
    }
    /**
     * media_save 媒体发布，修改
     */
    public void mediaSave(String token,String status,String change_flag,String id,String up_date,String down_date,
                          String custom_name,String imgurlstr,String imgcloseurlstr) {
        JhHttpInformation information = JhHttpInformation.MEDIA_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("change_flag", change_flag);
        params.put("custom_name", custom_name);
        params.put("up_date", up_date);
        params.put("down_date", down_date);
        params.put("status", status);
        params.put("imgurlstr", imgurlstr);
        params.put("imgcloseurlstr", imgcloseurlstr);
        JhNetTask netTask = new MedieSaveTask(information, params);
        executeTask(netTask);
    }
    /**
     * media_save 媒体发布，修改
     */
    public void mediaSave(String token, String id,String up_date,String down_date,
                          String custom_name,String imgurlstr,String imgcloseurlstr) {
        JhHttpInformation information = JhHttpInformation.MEDIA_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("up_date", up_date);
        params.put("down_date", down_date);
        params.put("custom_name", custom_name);
        params.put("imgurlstr", imgurlstr);
        params.put("imgcloseurlstr", imgcloseurlstr);
        JhNetTask netTask = new MedieSaveTask(information, params);
        executeTask(netTask);
    }

    /**
     * file_upload_return_url 图片返回url
     */
    public void fileUploadReturnUrl(String token,String keytype,String temp_file) {
        JhHttpInformation information = JhHttpInformation.FILE_UPLOAD_RETURN_URL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        HashMap<String, String> files = new HashMap<String, String>();
        files.put("temp_file", temp_file);
        JhNetTask task = new FileUploadTask(information, params, files);
        executeTask(task);
    }

    /**
     * 修改媒体状态
     * @param token
     * @param id
     * @param status
     */
    public void mediaEdit(String token, String id, String status) {
        JhHttpInformation information = JhHttpInformation.MEDIA_EDIT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("status", status);

        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 删除媒体
     * @param token
     * @param id
     */
    public void mediaDel(String token, String id) {
        JhHttpInformation information = JhHttpInformation.MEDIA_DEL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);

        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 获取媒体详情
     * @param token
     * @param id
     */
    public void mediaGet(String token, String id) {
        JhHttpInformation information = JhHttpInformation.MEDIA_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);

        JhNetTask task = new MediaGetTask(information, params);
        executeTask(task);
    }
    /**
     * collect_save 收藏操作
     * @param token
     * @param
     */
    public void collectSave(String token, String flag,String keytype,String keyid) {
        JhHttpInformation information = JhHttpInformation.COLLECT_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("flag", flag);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 广告页
     */
    public void adList() {
        JhHttpInformation information = JhHttpInformation.AD_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype","5");
        JhNetTask task = new AdListTask(information, params);
        executeTask(task);
    }
    /**
     * 媒体价格
     */
    public void priceList(String token) {
        JhHttpInformation information = JhHttpInformation.PRICE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token",token);
        JhNetTask task = new PriceListTask(information, params);
        executeTask(task);
    }

    /**
     * collect_save 修改价格
     * @param token
     * @param
     */
    public void priceSave(String token, String id,String dealprice1,String dealprice2) {
        JhHttpInformation information = JhHttpInformation.PRICE_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("dealprice1", dealprice1);
        params.put("dealprice2", dealprice2);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 全国单列表
     */
    public void reportList(String token,String keytype,String keyword,String status,String district,
                           String industry,String startdate1,String startdate2,String enddate1,String enddate2,
                           String page) {
        JhHttpInformation information = JhHttpInformation.REPORT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token",token);
        params.put("keytype",keytype);
        params.put("keyword",keyword);
        params.put("status",status);
        params.put("district",district);
        params.put("industry",industry);
        params.put("startdate1",startdate1);
        params.put("startdate2",startdate2);
        params.put("enddate1",enddate1);
        params.put("enddate2",enddate2);
        params.put("page",page);
        JhNetTask task = new ReportListTask(information, params);
        executeTask(task);
    }

    /**
     * 全国单添加修改
     * report_save
     */
    public void reportSave(String token,String name,String id,String district,String industry,
                           String industry_text,String putnum,String area,String linkname,String linkpost,
                           String startdate,String enddate,String status) {
        JhHttpInformation information = JhHttpInformation.REPORT_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token",token);
        params.put("name",name);
        params.put("id",id);
        params.put("status",status);
        params.put("district",district);
        params.put("industry",industry);
        params.put("industry_text",industry_text);
        params.put("putnum",putnum);
        params.put("area",area);
        params.put("linkname",linkname);
        params.put("linkpost",linkpost);
        params.put("startdate",startdate);
        params.put("enddate",enddate);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 行业列表
     */
    public void industryList(String token) {
        JhHttpInformation information = JhHttpInformation.INDUSTRY_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token",token);
        JhNetTask task = new IndustryListTask(information, params);
        executeTask(task);
    }
    /**
     * 全国单详情
     */
    public void reportGet(String token,String id) {
        JhHttpInformation information = JhHttpInformation.REPORT_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token",token);
        params.put("id",id);
        JhNetTask task = new ReportGetTask(information, params);
        executeTask(task);
    }
    /**
     * 删除全国单
     * @param token
     * @param id
     */
    public void reportRm(String token, String id) {
        JhHttpInformation information = JhHttpInformation.REPORT_RM;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 输入跟进状态
     * @param token
     * @param id
     */
    public void statusSave(String token, String id,String content) {
        JhHttpInformation information = JhHttpInformation.STATUS_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("content", content);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 修改全国单状态
     * @param token
     * @param id
     */
    public void dealSave(String token, String id,String status) {
        JhHttpInformation information = JhHttpInformation.DEAL_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("status", status);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 案例列表
     * @param token
     * @param
     */
    public void demoList(String token, String industry,String keyword,String keytype,String page) {
        JhHttpInformation information = JhHttpInformation.DEMO_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("industry", industry);
        params.put("keyword", keyword);
        params.put("keytype", keytype);
        params.put("page", page);
        JhNetTask task = new DemoListTask(information, params);
        executeTask(task);
    }
    /**
     * 案例详情
     * @param token
     * @param
     */
    public void demoGet(String token, String id) {
        JhHttpInformation information = JhHttpInformation.DEMO_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        JhNetTask task = new DemoGetTask(information, params);
        executeTask(task);
    }
    /**
     * 获取用户通知列表
     */
    public void noticeList(String token, String keytype, String page) {
        JhHttpInformation information = JhHttpInformation.NOTICE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);// 登陆令牌
        params.put("keytype", keytype);// /获取类型 1：系统通知2：评论回复3: 聊天人员列表页
        params.put("page", page);// 当前列表翻页索引 第一页时请传递page=0，翻页时依次递增。
        JhNetTask task = new NoticeListTask(information, params);
        executeTask(task);
    }
    /**
     * 通知操作
     * @param token
     * @param id
     */
    public void noticeOperate(String token, String operate,String id) {
        JhHttpInformation information = JhHttpInformation.NOTICE_OPERATE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("operate", operate);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 未读通知数
     * @param token
     * @param
     */
    public void noticeUnread(String token) {
        JhHttpInformation information = JhHttpInformation.NOTICE_UNREAD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new NoticeUnreadTask(information, params);
        executeTask(task);
    }
    /**
     * 客服列表
     * @param token
     * @param
     */
    public void serviceList(String token,String page) {
        JhHttpInformation information = JhHttpInformation.UNION_SERVICE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new ServiceListTask(information, params);
        executeTask(task);
    }
    /**
     * 模板列表
     * @param token
     * @param
     */
    public void argumentList(String token,String page) {
        JhHttpInformation information = JhHttpInformation.ARGUMENT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        JhNetTask task = new ArgumentListTask(information, params);
        executeTask(task);
    }
    /**
     * 修改密码
     * @param token
     * @param
     */
    public void passwordSave(String token,String old_password,String new_password) {
        JhHttpInformation information = JhHttpInformation.PASSWORD_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", "1");
        params.put("old_password", old_password);
        params.put("new_password", new_password);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 意见反馈
     * @param
     * @param
     */
    public void adviseAdd(String token,String content) {
        JhHttpInformation information = JhHttpInformation.ADVISE_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("content", content);

        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 设置推送接口
     * @param
     * @param
     */
    public void sysSet(String token,String value) {
        JhHttpInformation information = JhHttpInformation.SYS_SET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("value", value);

        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 退出登录
     * @param
     * @param
     */
    public void clientLoginout(String token) {
        JhHttpInformation information = JhHttpInformation.CLIENT_LOGINOUT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 修改用户信息client_save
     * @param
     * @param
     */
    public void clientSave(String token,String nickname,String sex,String post,String compony,String mobile,String temp_token) {
        JhHttpInformation information = JhHttpInformation.CLIENT_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("nickname", nickname);
        params.put("sex", sex);
        params.put("post", post);
        params.put("compony", compony);
        params.put("mobile", mobile);
        params.put("temp_token", temp_token);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 联盟城市
     * @param
     * @param
     */
    public void unionList(String token,String keytype,String page) {
        JhHttpInformation information = JhHttpInformation.UNION_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("page", page);
        JhNetTask task;
        if ("2".equals(keytype))
         task = new UnionListTask(information, params);
        else
            task = new Union2ListTask(information, params);
        executeTask(task);
    }
    /**
     * 联盟城市详情
     * @param
     * @param
     */
    public void unionGet(String token,String id) {
        JhHttpInformation information = JhHttpInformation.UNION_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        JhNetTask task = new UnionGetTask(information, params);
        executeTask(task);
    }
    /**
     * update_info 更新内容
     * @paramupdate_info
     * @param
     */
    public void upDateInfor() {
        JhHttpInformation information = JhHttpInformation.UPDATE_INFO;
        HashMap<String, String> params = new HashMap<String, String>();
        JhNetTask task = new UpDateInforTask(information, params);
        executeTask(task);
    }
    /**
     * 媒体列表二期
     */
    public void mediaList(String token, String keytype,String keyword,String id, String status, String district1,
                          String district2,
                          String district3,
                          String limitcontent, String change_flag, String house_type, String time_out,
                          String house_pirce_min, String house_pirce_max, String people_num_min, String people_num_max,
                          String park_num_min, String park_num_max,
                          String lng, String lat,String range_val,
                          String up_date1,String up_date2,String down_date1,String down_date2,
                          String cus_industry,
                          String page) {
        JhHttpInformation information = JhHttpInformation.MEDIA_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("up_date1", up_date1);
        params.put("cus_industry", cus_industry);
        params.put("up_date2", up_date2);
        params.put("down_date1", down_date1);
        params.put("down_date2", down_date2);
        params.put("keytype", keytype);
        params.put("keyword", keyword);
        params.put("status", status);
        params.put("district1", district1);
        params.put("district2", district2);
        params.put("district3", district3);
        params.put("limitcontent", limitcontent);
        params.put("id", id);
        params.put("change_flag", change_flag);
        params.put("house_type", house_type);
        params.put("time_out", time_out);
        params.put("house_pirce_min", house_pirce_min);
        params.put("house_pirce_max", house_pirce_max);
        params.put("people_num_min", people_num_min);
        params.put("people_num_max", people_num_max);
        params.put("park_num_min", park_num_min);
        params.put("park_num_max", park_num_max);
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("range_val", range_val);
        params.put("page", page);
        JhNetTask netTask = new MediaListTask(information, params);
        executeTask(netTask);
    }
//    /**
//     * 点位列表
//     */
//    public void pointList(String token, String keytype,String keyword,String status, String district1,
//                          String district2,
//                          String district3,
//                          String limitcontent, String change_flag, String house_type, String time_out,
//                          String house_pirce_min, String house_pirce_max, String people_num_min, String people_num_max,
//                          String park_num_min, String park_num_max,
//                          String lng, String lat,String range_val,
//                          String up_date1,String up_date2,String down_date1,String down_date2,
//                          String cus_industry,
//                          String page) {
//        JhHttpInformation information = JhHttpInformation.MEDIA_LIST;
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("token", token);
//        params.put("up_date1", up_date1);
//        params.put("cus_industry", cus_industry);
//        params.put("up_date2", up_date2);
//        params.put("down_date1", down_date1);
//        params.put("down_date2", down_date2);
//        params.put("keytype", keytype);
//        params.put("keyword", keyword);
//        params.put("status", status);
//        params.put("district1", district1);
//        params.put("district2", district2);
//        params.put("district3", district3);
//        params.put("limitcontent", limitcontent);
//        params.put("change_flag", change_flag);
//        params.put("house_type", house_type);
//        params.put("time_out", time_out);
//        params.put("house_pirce_min", house_pirce_min);
//        params.put("house_pirce_max", house_pirce_max);
//        params.put("people_num_min", people_num_min);
//        params.put("people_num_max", people_num_max);
//        params.put("park_num_min", park_num_min);
//        params.put("park_num_max", park_num_max);
//        params.put("lng", lng);
//        params.put("lat", lat);
//        params.put("range_val", range_val);
//        params.put("page", page);
//        JhNetTask netTask = new MediaListTask(information, params);
//        executeTask(netTask);
//    }
    /**
     * 点位预定
     * @param
     * @param
     */
    public void pointPre(String token,String id,String up_date,String down_date,String custom_name) {
        JhHttpInformation information = JhHttpInformation.POINT_PRE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("up_date", up_date);
        params.put("down_date", down_date);
        params.put("custom_name", custom_name);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
    /**
     * 预定列表
     */
    public void preList(String token,String status,String page)
    {
        JhHttpInformation information = JhHttpInformation.PRE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("status", status);
        params.put("page", page);

        JhNetTask task = new PreListTask(information, params);
        executeTask(task);
    }
    /**
     * 预定详情
     */
    public void preGet(String token,String id)
    {
        JhHttpInformation information = JhHttpInformation.PRE_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        JhNetTask task = new PreTask(information, params);
        executeTask(task);
    }
    /**
     * 点位删除
     * @param
     * @param
     */
    public void preDelete(String token,String id) {
        JhHttpInformation information = JhHttpInformation.PRE_DELETE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        JhNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }
}
