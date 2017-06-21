package com.hemaapp.xsm.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hemaapp.xsm.model.User;


/**
 * Created by lenovo on 2016/9/6.
 */
public class UserDBHelper extends JhDbHelper {

    String tableName = USER;
    String columns = "id,token,username,email,nickname,mobile,password,"
            + "charindex,sex,avatar,avatarbig,district_name,lng,"
            + "lat,regdate,score,feeaccount,birthday,role,company,top_week,top_year,backimg,noticecount,"
            + "visitorcount,fancount,followcount,blogcount,followflag,signinflag,level_exp,level_name,"
            + "level_imgurl,selfsign,contisignintimes,expiredflag";
    String updateColumns = "id=?,token=?,username=?,email=?,nickname=?,mobile=?,password=?,charindex=?,"
            + "sex=?,avatar=?,avatarbig=?,district_name=?,lng=?,lat=?,"
            + "regdate=?,score=?,feeaccount=?,birthday=?,role=?,company=?,top_week=?,top_year=?,backimg=?,noticecount=?"
            + ",visitorcount=?,fancount=?,followcount=?,blogcount=?,followflag=?,signinflag=?,level_exp=?,"
            + "level_name=?,level_imgurl=?,selfsign=?,contisignintimes=?,expiredflag=?";

    public UserDBHelper(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @方法名称: insert
     * @功能描述: 插入一天用户数据
     * @return
     * @返回值: boolean
     */
    public boolean insert(User user) {

        String sql = "insert into "
                + tableName
                + " ("
                + columns
                + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = getWritableDatabase();
        Object[] bindArgs = new Object[] { user.getId(), user.getToken(),
                user.getUsername(), user.getEmail(), user.getNickname(),
                user.getMobile(), user.getPassword(), user.getCharindex(),
                user.getSex(), user.getAvatar(), user.getAvatarbig(),
                user.getDistrict_name(), user.getLng(), user.getLat(),
                user.getRegdate(), user.getScore(), user.getFeeaccount(),
                user.getBirthday(), user.getRole(), user.getCompany(),
                user.getTop_week(), user.getTop_year(), user.getBackimg(),
                user.getNoticecount(), user.getVisitorcount(),
                user.getFancount(), user.getFollowcount(), user.getBlogcount(),
                user.getFollowflag(), user.getSigninflag(),
                user.getLevel_exp(), user.getLevel_name(),
                user.getLevel_imgurl(), user.getSelfsign(),
                user.getContisignintimes(), user.getExpiredflag() };
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
     *
     * @方法名称: update
     * @功能描述: 更新用户表
     * @param user
     * @return
     * @返回值: boolean
     */
    public boolean update(User user) {
        String conditions = " where id=" + user.getId();
        String sql = "update " + tableName + " set " + updateColumns
                + conditions;
        Object[] bindArgs = new Object[] { user.getId(), user.getToken(),
                user.getUsername(), user.getEmail(), user.getNickname(),
                user.getMobile(), user.getPassword(), user.getCharindex(),
                user.getSex(), user.getAvatar(), user.getAvatarbig(),
                user.getDistrict_name(), user.getLng(), user.getLat(),
                user.getRegdate(), user.getScore(), user.getFeeaccount(),
                user.getBirthday(), user.getRole(), user.getCompany(),
                user.getTop_week(), user.getTop_year(), user.getBackimg(),
                user.getNoticecount(), user.getVisitorcount(),
                user.getFancount(), user.getFollowcount(), user.getBlogcount(),
                user.getFollowflag(), user.getSigninflag(),
                user.getLevel_exp(), user.getLevel_name(),
                user.getLevel_imgurl(), user.getSelfsign(),
                user.getContisignintimes(), user.getExpiredflag() };
        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL(sql, bindArgs);
        } catch (SQLException e) {
            success = false;
        }
        db.close();
        return success;
    }

    /**
     *
     * @方法名称: select
     * @功能描述: 初始化用户表
     * @return
     * @返回值: boolean
     */
    public User select(String username) {
        String conditions = " where username='" + username + "'";
        String sql = "select " + columns + " from " + tableName + conditions;

        SQLiteDatabase db = getWritableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9),
                    cursor.getString(10), cursor.getString(11),
                    cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15),
                    cursor.getString(16), cursor.getString(17),
                    cursor.getString(18), cursor.getString(19),
                    cursor.getString(20), cursor.getString(21),
                    cursor.getString(22), cursor.getString(23),
                    cursor.getString(24), cursor.getString(25),
                    cursor.getString(26), cursor.getString(27),
                    cursor.getString(28), cursor.getString(29),
                    cursor.getString(30), cursor.getString(31),
                    cursor.getString(32), cursor.getString(33),
                    cursor.getString(34), cursor.getString(35));
        }
        cursor.close();
        db.close();
        return user;
    }

    /**
     * 判断表是否为空
     *
     * @return
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
     * 清空
     */
    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + tableName);
        db.close();
    }

    /**
     *
     * @方法名称: isExist
     * @功能描述: 判断是否存在
     * @param user
     * @return
     * @返回值: boolean
     */
    public boolean isExist(User user) {
        String id = user.getId();
        String sql = ("select * from " + tableName + " where id=" + id);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exist;
    }

    /**
     *
     * @方法名称: insertOrUpdate
     * @功能描述: 判断是更新还是插入
     * @param user
     * @return
     * @返回值: boolean
     */
    public boolean insertOrUpdate(User user) {
        if (isExist(user)) {
            return update(user);
        } else {
            return insert(user);
        }
    }


}
