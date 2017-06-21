package com.hemaapp.xsm;

import com.hemaapp.HemaConfig;
import com.hemaapp.hm_FrameWork.HemaApplication;
import com.hemaapp.xsm.config.XsmConfig;
import com.hemaapp.xsm.db.SysInfoDBHelper;
import com.hemaapp.xsm.db.UserDBHelper;
import com.hemaapp.xsm.model.CitySan;
import com.hemaapp.xsm.model.SysInitInfo;
import com.hemaapp.xsm.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;

import xtom.frame.XtomConfig;
import xtom.frame.util.XtomLogger;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/6.
 */
public class JhctmApplication extends HemaApplication {

    private static final String TAG = JhctmApplication.class.getSimpleName();
    private SysInitInfo sysInitInfo;
    private User user;
    private CitySan cityInfo;
    private static JhctmApplication application;
    //    private Select select;
    public static JhctmApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        application = this;
        XtomConfig.LOG = XsmConfig.DEBUG;
        HemaConfig.UMENG_ENABLE = XsmConfig.UMENG_ENABLE;
        String iow = XtomSharedPreferencesUtil.get(this, "imageload_onlywifi");
        XtomConfig.IMAGELOAD_ONLYWIFI = "true".equals(iow);
        XtomConfig.MAX_IMAGE_SIZE = 200;
        XtomConfig.DIGITAL_CHECK = true;
        XtomConfig.DATAKEY = "yrSFqyE8tQBXrPrt";
        XtomLogger.i(TAG, "onCreate");
        initImageLoader();
        super.onCreate();
        /**
         * 百度推�?
         */
        // FrontiaApplication.initFrontiaApplication(this);
    }

    /**
     * @return
     * @方法名称: getCityInfo
     * @功能描述: TODO三级城市保存
     * @返回值: CitySan
     */
    public CitySan getCityInfo() {
        return cityInfo;
    }

      public void setCityInfo(CitySan cityInfo) {
        this.cityInfo = cityInfo;
    }
    public void initImageLoader() {
        L.writeLogs(false);
/*
File cacheDir = StorageUtils.getCacheDirectory(this);
ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽

.threadPoolSize(3)//线程池内加载的数量
.threadPriority(Thread.NORM_PRIORITY - 2)
.denyCacheImageMultipleSizesInMemory()
.memoryCache(new UsingFreqLimitedMemoryCache(1 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
.memoryCacheSize(256 * 1024)
.discCacheSize(50 * 1024 * 1024)
.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
.tasksProcessingOrder(QueueProcessingType.LIFO)
.discCacheFileCount(100) //缓存的文件数量
.discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
.imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
.writeDebugLogs() // Remove for release app
.build();//开始构建
*/
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(config);
    }

//	@SuppressWarnings("deprecation")
//	public DisplayImageOptions getOptions(int drawableId){
//		return new DisplayImageOptions.Builder()
//		.showImageOnLoading(drawableId)
//		.showImageForEmptyUri(drawableId)
//		.showImageOnFail(drawableId)
//		.resetViewBeforeLoading(true)
//		.cacheInMemory(true)
//		.cacheOnDisc(true)
//		.imageScaleType(ImageScaleType.EXACTLY)
//		.bitmapConfig(Config.RGB_565)
//		.build();
//	}

    // 获取信息
    public SysInitInfo getSysInitInfo() {
        if (sysInitInfo == null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);
            sysInitInfo = helper.select();
            helper.close();
        }
        return sysInitInfo;
    }

    /**
     * @param sysInitInfo
     * @方法名称: setSysInitInfo
     * @功能描述: 将信息写入数据库
     * @返回�? void
     */
    public void setSysInitInfo(SysInitInfo sysInitInfo) {
        this.sysInitInfo = sysInitInfo;
        if (sysInitInfo != null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);

            helper.insertOrUpdate(sysInitInfo);
            helper.close();
        }
    }


    /**
     * @param user
     * @方法名称: setUser
     * @功能描述: 存入用户信息
     * @返回�? void
     */
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            UserDBHelper helper = new UserDBHelper(this);
            helper.insertOrUpdate(user);
            helper.close();
        }
    }

//    /**
//     * 存入用户
//     * @param select
//     */
//    public void setSelect(Select select)
//    {
//        this.select = select;
//        if (select!=null)
//        {
//            SelectDBHelper helper = new SelectDBHelper(this);
//            helper.insertOrUpdate(select);
//            helper.close();
//        }
//    }

    /**
     * @return
     * @方法名称: getUser
     * @功能描述: 获得用户信息
     * @返回�? User
     */
    public User getUser() {
        if (user == null) {
            UserDBHelper helper = new UserDBHelper(this);
            String username = XtomSharedPreferencesUtil.get(this, "username");
            user = helper.select(username);
            helper.close();
        }
        return user;
    }

}
