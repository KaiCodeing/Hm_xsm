package com.hemaapp.xsm.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hemaapp.xsm.model.SysInitInfo;


/**
 * Created by lenovo on 2016/9/6.
 */
public class SysInfoDBHelper extends JhDbHelper {

    String tableName = SYSINITINFO;
    String columns = "sys_web_service,sys_plugins,start_img,android_must_update,"
            + "android_last_version,sys_chat_ip,sys_chat_port,sys_pagesize,"
            + "sys_service_phone,android_update_url,msg_invite";

    String updateColumns = "sys_web_service=?,sys_plugins=?,start_img=?,android_must_update=?,"
            + "android_last_version=?,sys_chat_ip=?,sys_chat_port=?,sys_pagesize=?,"
            + "sys_service_phone=?,android_update_url=?,msg_invite=?";

    public SysInfoDBHelper(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param info
     * @return
     * @方法名称: insert
     * @功能描述: 插入一条数据
     * @返回值: boolean
     */
    public boolean insert(SysInitInfo info) {
        String sql = "insert into " + tableName + " (" + columns
                + ") values (?,?,?,?,?,?,?,?,?,?,?)";
        Object[] bindArgs = new Object[]{info.getSys_web_service(),
                info.getSys_plugins(), info.getStart_img(), info.getAndroid_must_update(),
                info.getAndroid_last_version(), info.getSys_chat_ip(), info.getSys_chat_port(),
                info.getSys_pagesize(), info.getSys_service_phone(),
                info.getAndroid_update_url(), info.getMsg_invite()};
        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL(sql, bindArgs);
        } catch (Exception e) {
            // TODO: handle exception
            success = false;
        }
        db.close();

        return success;

    }

    /**
     * @param info
     * @return
     * @方法名称: updata
     * @功能描述: 更新一条数据
     * @返回值: boolean
     */
    public boolean update(SysInitInfo info) {
        int id = 1;
        String conditions = " where id=" + id;
        String sql = "update " + tableName + " set " + updateColumns
                + conditions;
        Object[] bindArgs = new Object[]{info.getSys_web_service(),
                info.getSys_plugins(), info.getStart_img(), info.getAndroid_must_update(),
                info.getAndroid_last_version(), info.getSys_chat_ip(), info.getSys_chat_port(),
                info.getSys_pagesize(), info.getSys_service_phone(),
                info.getAndroid_update_url(), info.getMsg_invite()};
        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL(sql, bindArgs);
        } catch (Exception e) {
            // TODO: handle exception
            success = false;
        }
        db.close();
        return success;
    }

    /**
     * @return
     * @方法名称: select
     * @功能描述: 初始化信息
     * @返回值: SysInitInfo
     */
    public SysInitInfo select() {
        int id = 1;
        String conditions = " where id=" + id;
        String sql = "select " + columns + " from " + tableName + conditions;
        SQLiteDatabase db = getWritableDatabase();
        SysInitInfo info = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            info = new SysInitInfo(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getInt(7),
                    cursor.getString(8), cursor.getString(9),
                    cursor.getString(10));
        }
        cursor.close();
        db.close();
        return info;
    }

    /**
     * @方法名称: clear
     * @功能描述: 清空初始化表
     * @返回值: void
     */
    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + tableName);
        db.close();
    }

    /**
     * @return
     * @方法名称: isEmpty
     * @功能描述: 判断是否为空
     * @返回值: boolean
     */
    public boolean isEmpty() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableName, null);
        boolean empty = 0 == cursor.getCount();
        cursor.close();
        db.close();
        return empty;
    }

    /**
     * @return
     * @方法名称: isExist
     * @功能描述: 判断是否存在
     * @返回值: boolean
     */
    public boolean isExist() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableName + " where id = 1", null);
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exist;
    }

    /**
     * @param info
     * @return
     * @方法名称: insertOrUpdate
     * @功能描述: 判断是删除还是插入
     * @返回值: boolean
     */
    public boolean insertOrUpdate(SysInitInfo info) {
        if (isExist()) {
            return update(info);
        } else {
            return insert(info);
        }
    }

}
