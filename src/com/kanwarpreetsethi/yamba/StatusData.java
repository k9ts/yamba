package com.kanwarpreetsethi.yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class StatusData {
	
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "timeline.db";
	static final String TAG = "DBHelper";
	static final String TABLE = "timeline";
	static final String C_ID = BaseColumns._ID;
	static final String C_CREATED_AT = "created_at";
	static final String C_SOURCE = "source";
	static final String C_TEXT = "txt";
	static final String C_USER = "user";

	private static final String GET_ALL_ORDER_BY = C_CREATED_AT + " DESC";
	private static final String[] MAX_CREATED_AT_COLUMNS = { "max(" + StatusData.C_CREATED_AT + ")" };
	private static final String[] DB_TEXT_COLUMNS = { C_TEXT };
	
	private final DBHelper dbHelper;
	
	public StatusData(Context context) {
		this.dbHelper = new DBHelper(context);
	}
	
	public void close() {
		this.dbHelper.close();
	}
	
	public void insertOrIgnore(ContentValues values) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}
	}
	
	public Cursor getStatusUpdates() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(TABLE, null, null, null, null, null, GET_ALL_ORDER_BY);
	}
	
	public long getLatestStatusCreatedAtTime() { 
		SQLiteDatabase db = this.dbHelper.getReadableDatabase(); 
		try {
			Cursor cursor = db.query(TABLE, MAX_CREATED_AT_COLUMNS, null, null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
			} finally {
				cursor.close(); 
			}
		} finally {
			db.close(); 
		}
	}
	
	public String getStatusTextById(long id) {  
		SQLiteDatabase db = this.dbHelper.getReadableDatabase(); 
		try {
			Cursor cursor = db.query(TABLE, DB_TEXT_COLUMNS, C_ID + "=" + id, null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getString(0) : null;
			} finally {
				cursor.close(); 
			}
		} finally {
			db.close(); 
		}
	}
	
	private class DBHelper extends SQLiteOpenHelper {
	
		public DBHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			String create_sql = "create table " + TABLE + " (" + C_ID + " int primary key, " + C_CREATED_AT + " int, " 
							+ C_USER + " text, " + C_TEXT + " text)";
			db.execSQL(create_sql);
			Log.d(TAG, "OnCreated sql:" + create_sql);
			
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			db.execSQL("drop table if exists " + TABLE);
			Log.d(TAG, "onUpdated");
			onCreate(db);
		}
	
		
		
	}
}
