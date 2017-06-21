package com.hemaapp.xsm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2016/9/6.
 */
public class JhDbHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "xsm.db";

    /**
     * 系统初始化信息
     */
    protected static final String SYSINITINFO = "sysinfo";
    /**
     * 当前登录用户信息
     */
    protected static final String USER = "user";
    /**
     * 访问城市缓存信息
     */
    protected static final String VISIT_CITYS = "visit_citys";

    /**
     * 闹钟信息
     */
    protected static final String ALARM = "alarm";
    /**
     * 搜索词缓存
     */
    protected static final String SYS_CASCADE_SEARCH = "sys_cascade_search";

    /**
     * 账号
     * @param context
     */
    protected  static final String SELECTUSER ="selectuser";
    public JhDbHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // 创建系统初始化信息缓存表
        String sys = "sys_web_service text,sys_plugins text,start_img text,android_must_update text,"
                + "android_last_version text,sys_chat_ip text,sys_chat_port text,sys_pagesize text,"
                + "sys_service_phone text,android_update_url text,msg_invite text";
        String sysSQL = "create table " + SYSINITINFO
                + " (id integer primary key," + sys + ")";
        db.execSQL(sysSQL);
        // 创建当前登录用户信息缓存表
        String user = "id text,token text,username text,email text,nickname text,"
                + "mobile text,password text,charindex text,sex text,avatar text,"
                + "avatarbig text,district_name text,"
                + "lng text,lat text,regdate text,score text,"
                + "feeaccount text,birthday text,role text,company text,top_week text,top_year text,backimg text,noticecount text,"
                + "visitorcount text,fancount text,followcount text,blogcount text,followflag text,signinflag text,"
                + "level_exp text,level_name text,level_imgurl text,selfsign text,contisignintimes text,expiredflag text";
        String userSQL = "create table " + USER + " (" + user + ")";
        db.execSQL(userSQL);
        String citys = "id text primary key,name text,parentid text,nodepath text,"
                + "namepath text,charindex text,level text,orderby text";
        String citysSQL = "create table " + VISIT_CITYS + " (" + citys + ")";
        db.execSQL(citysSQL);

//        // 创建闹钟表
//        String alarm = "id text,content text,time text,rate text,state text,keytype text";
//        String alarmSQL = "create table " + ALARM + " (" + alarm + ")";
//        db.execSQL(alarmSQL);
        //搜索词
        String search = "searchname text";
        String searchSQL = "create table " + SYS_CASCADE_SEARCH + " (" + search + ")";
        db.execSQL(searchSQL);
//        //账号
//        String select = "username text,nickname text,password text,avatar text";
//        String selectSQL="create table "+ SELECTUSER +" (" + select + ")";
//        db.execSQL(selectSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }


}
