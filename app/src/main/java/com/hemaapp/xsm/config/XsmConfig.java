package com.hemaapp.xsm.config;

/**
 * Created by lenovo on 2016/9/6.
 */
public class XsmConfig {
    /**
     * 是否打印信息开关
     */
    public static final boolean DEBUG = true;
    /**
     * 是否启用友盟统计
     */
    public static final boolean UMENG_ENABLE = false;
    /**
     * 后台服务接口根路径
     */
   public static final String SYS_ROOT = "http://124.128.23.74:8008/group15/hm_xmt/";

    //正式服务器接口初始化地址：
 //   public static final String SYS_ROOT = "http://118.190.49.204/";
    /**
     * 图片压缩的最大宽度
     */
    public static final int IMAGE_WIDTH = 640;
    /**
     * 图片压缩的最大高度
     */
    public static final int IMAGE_HEIGHT = 3000;
    /**
     * 图片压缩的失真率
     */
    public static final int IMAGE_QUALITY = 100;
    /**
     * 银联支付环境--"00"生产环境,"01"测试环境
     */
    public static final String UNIONPAY_TESTMODE = "00";


    public static final String PREFERENCES = "AlarmClock";
    /**
     * 微信appid
     */
    public static final String APPID_WEIXIN = "wx69385155534771a4";

}
