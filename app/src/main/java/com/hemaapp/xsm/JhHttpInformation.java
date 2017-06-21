package com.hemaapp.xsm;

import com.hemaapp.HemaConfig;
import com.hemaapp.hm_FrameWork.HemaHttpInfomation;
import com.hemaapp.xsm.config.XsmConfig;
import com.hemaapp.xsm.model.SysInitInfo;

/**
 * Created by lenovo on 2016/9/6.
 */
public  enum JhHttpInformation implements HemaHttpInfomation {
    /**
     * 登录
     */
    CLIENT_LOGIN(HemaConfig.ID_LOGIN, "client_login", "登录", false),
    // 注意登录接口id必须为HemaConfig.ID_LOGIN

    /**
     * 后台服务接口根路径
     */
    SYS_ROOT(0, XsmConfig.SYS_ROOT, "后台服务接口根路径", true),
    /**
     * 初始化
     */
    INIT(1, "index.php/webservice/index/init", "初始化", false),
    /**
     * code_get发送验证码
     */
    CODE_GET(2, "code_get", "发送验证码", false),
    /**
     * code_verify验证验证码
     */
    CODE_VERIFY(3, "code_verify", "验证验证码", false),
    /**
     * client_verify验证用户是否注册
     */
    CLIENT_VERIFY(4, "client_verify", "验证用户是否注册", false),
    /**
     * 密码重设password_reset
     */
    PASSWORD_RESET(5, "password_reset", "密码重设", false),
    /**
     * 支付宝alipay
     */
    ALIPAY(7,"OnlinePay/Alipay/alipaysign_get.php","支付宝获取串口号",false),
    /**
     * 银联支付unionpay
     */
    UNIONPAY(8,"OnlinePay/Unionpay/unionpay_get.php","银联获取串口号",false),
    /**
     * 微信支付wxpayment
     */
    WXPAYMENT(9,"OnlinePay/Weixinpay/weixinpay_get.php","微信获取串口号",false),

    /**
     * 城市列表district_list
     * 0：表示获取第一级别（省份或直辖市或自治区）
     -1：表示获取所有地级以上级别城市（含地级）
     */
    DISTRICT_LIST(10,"district_list","地区列表",false),

    CLIENT_ADD(14,"client_add","注册",false),
    /**
     * file_upload上传文件
     */
    FILE_UPLOAD(15,"file_upload","上传文件",false),
    /**
     * device_save硬件保存
     */
    DEVICE_SAVE(16,"device_save","硬件保存",false),
    /**
     * client_get 获取用户详情
     */
    CLIENT_GET(17,"client_get","用户详情",false),
    /**
     * media_list 媒体列表
     */
    MEDIA_LIST(18,"media_list","媒体列表",false),
    /**
     * media_save 媒体发布修改
     */
    MEDIA_SAVE(19,"media_save","媒体发布修改",false),
    /**
     * file_upload_return_url上传图片返回url
     */
    FILE_UPLOAD_RETURN_URL(20,"file_upload_return_url","上传图片返回url",false),
    /**
     * media_edit 状态修改
     */
    MEDIA_EDIT(21,"media_edit","媒体状态修改",false),
    /**
     * media_del 删除媒体
     *
     */
    MEDIA_DEL(22,"media_del","删除媒体",false),
    /**
     * media_get媒体详情
     */
    MEDIA_GET(23,"media_get","媒体详情",false),
    /**
     * collect_save 收藏操作
     */
    COLLECT_SAVE(24,"collect_save","收藏操作",false),
    /**
     * ad_list 广告页
     */
    AD_LIST(25,"ad_list","广告页",false),
    /**
     * price_list 媒体列表
     */
    PRICE_LIST(26,"price_list","媒体列表",false),
    /**
     * price_save 修改价格
     */
    PRICE_SAVE(27,"price_save","修改价格",false),
    /**
     * 全国单列表report_list
     */
    REPORT_LIST(28,"report_list","全国单列表",false),
    /**
     * report_save 修改保存全国单
     */
    REPORT_SAVE(29,"report_save","修改保存全国单",false),
    /**
     * industry_list 行业列表
     */
    INDUSTRY_LIST(30,"industry_list","行业列表",false),
    /**
     * report_get全国单详情
     */
    REPORT_GET(31,"report_get","全国单详情",false),
    /**
     * report_rm全国单删除
     */
    REPORT_RM(32,"report_rm","全国单删除",false),
    /**
     * status_save 输入跟进状态
     */
    STATUS_SAVE(33,"status_save","输入跟进状态",false),
    /**
     * deal_save 全国单状态修改
     */
    DEAL_SAVE(34,"deal_save","全国单状态修改",false),
    /**
     * demo_list 案例列表
     */
    DEMO_LIST(35,"demo_list","案例列表",false),
    /**
     * 案例详情demo_get
     */
    DEMO_GET(36,"demo_get","案例详情",false),
    /**
     * notice_list 消息列表
     */
    NOTICE_LIST(37,"notice_list","消息列表",false),
    /**
     * notice_operate 消息操作
     */
    NOTICE_OPERATE(38,"notice_operate","消息操作",false),
    /**
     * notice_unread 未读通知条数
     */
    NOTICE_UNREAD(39,"notice_unread","未读通知条数",false),
    /**
     * union_service_list 客服列表
     */
    UNION_SERVICE_LIST(40,"union_service_list","客服列表",false),
    /**
     * argument_list 合同模板
     */
    ARGUMENT_LIST(41,"argument_list","合同模板",false),
    /**
     * 修改密码password_save
     */
    PASSWORD_SAVE(42,"password_save","修改密码",false),
    /**
     * advise_add 意见反馈
     */
    ADVISE_ADD(43,"advise_add","意见反馈",false),
    /**
     * sys_set 接收推送
     */
    SYS_SET(44,"sys_set","接收推送设置",false),
    /**
     * 退出登录client_loginout
     */
    CLIENT_LOGINOUT(45,"client_loginout","退出登录",false),
    /**
     * client_save  个人信息修改保存
     */
    CLIENT_SAVE(46,"client_save","个人信息修改",false),
    /**
     * union_list 联盟公司
     */
    UNION_LIST(47,"union_list","联盟公司",false),
    /**
     * union_get 联盟公司详情
     */
    UNION_GET(48,"union_get","联盟公司详情",false),
    /**
     * update_info 更新内容
     */
    UPDATE_INFO(49,"update_info","更新内容",false),
    /**
     * 点位列表point_list
     */
    POINT_LIST(50,"point_list","点位列表",false),
    /**
     * 点位预定point_pre
     */
    POINT_PRE(51,"point_pre","点位预定",false),
    /**
     *pre_list 预定列表
     */
    PRE_LIST(52,"pre_list","预定列表",false),
    /**
     * 预定详情pre_get
     */
    PRE_GET(53,"pre_get","预定详情",false),
    /**
     * pre_delete预定删除
     */
    PRE_DELETE(54,"pre_delete","预定删除",false);
    private int id;// 对应NetTask的id
    private String urlPath;// 请求地址
    private String description;// 请求描述
    private boolean isRootPath;// 是否是根路径

    private JhHttpInformation(int id, String urlPath, String description,
                                boolean isRootPath) {
        this.id = id;
        this.urlPath = urlPath;
        this.description = description;
        this.isRootPath = isRootPath;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getUrlPath() {
        if (isRootPath)
            return urlPath;

        String path = SYS_ROOT.urlPath + urlPath;

        if (this.equals(INIT))
            return path;

        JhctmApplication application = JhctmApplication.getInstance();
        SysInitInfo info = application.getSysInitInfo();
        path = info.getSys_web_service() + urlPath;
         if (this.equals(ALIPAY))
         path = info.getSys_plugins() + urlPath;
         if (this.equals(UNIONPAY))
         path = info.getSys_plugins() + urlPath;
        if (this.equals(WXPAYMENT))
            path = info.getSys_plugins() + urlPath;
        return path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isRootPath() {
        return isRootPath;
    }

}
