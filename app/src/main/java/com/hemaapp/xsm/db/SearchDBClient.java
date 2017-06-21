package com.hemaapp.xsm.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

public class SearchDBClient extends JhDbHelper {
	
	private static SearchDBClient mClient;

	private String tableName = SYS_CASCADE_SEARCH;
	
	public SearchDBClient(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static SearchDBClient get(Context context) {
		return mClient == null ? mClient = new SearchDBClient(context)
				: mClient;
	}
	
	/**
	 * 插入一条记录
	 * 
	 * @param //count
	 * @return
	 */
	public boolean insert(String search) {
		SQLiteDatabase db = getWritableDatabase();
		boolean success = true;
		try {
			db.execSQL(
					("insert into " + tableName + "(searchname) " + "values (?)"),
					new Object[] { search });
		} catch (SQLException e) {
			success = false;
			Log.w("insert", "insert e=" + e);
		}
		db.close();
		return success;
	}

	/**
	 * 清空
	 */
	public void clear() {
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.execSQL("delete from " + tableName);
		} catch (SQLiteException e) {
		} catch (SQLException e) {
		}
		db.close();
	}

	/**
	 * 判断表是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + tableName, null);
		int num = cursor.getCount();
		cursor.close();
		db.close();
		return 0 == num;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public ArrayList<String> select() {
		ArrayList<String> counts = null;
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from " + tableName, null);
			if (cursor != null && cursor.getCount() > 0) {
				counts = new ArrayList<String>();
				cursor.moveToFirst();
				String count;
				for (int i = 0; i < cursor.getCount(); i++) {
					count = cursor.getString(0);
					counts.add(0, count);
					cursor.moveToNext();
				}
			}
		} catch (Exception e) {
			Log.w("select", "select e=" + e);
		}
		if (cursor != null)
			cursor.close();
		db.close();
		return counts;
	}
}
