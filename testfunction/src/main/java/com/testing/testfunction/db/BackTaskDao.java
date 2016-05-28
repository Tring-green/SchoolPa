package com.testing.testfunction.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testing.testfunction.Domain.BackTask;


public class BackTaskDao {
	private SPDBOpenHelper helper;

	public BackTaskDao(Context context) {
		helper = SPDBOpenHelper.getInstance(context);
	}

	public void addTask(BackTask task) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SPDB.BackTask.COLUMN_OWNER, task.getOwner());
		values.put(SPDB.BackTask.COLUMN_PATH, task.getPath());
		values.put(SPDB.BackTask.COLUMN_STATE, task.getState());
		task.setId(db.insert(SPDB.BackTask.TABLE_NAME, null, values));
	}

	public void updateTask(BackTask task) {

		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SPDB.BackTask.COLUMN_OWNER, task.getOwner());
		values.put(SPDB.BackTask.COLUMN_PATH, task.getPath());
		values.put(SPDB.BackTask.COLUMN_STATE, task.getState());

		String whereClause = SPDB.BackTask.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { task.getId() + "" };
		db.update(SPDB.BackTask.TABLE_NAME, values, whereClause, whereArgs);
	}

	public void updateState(long id, int state) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SPDB.BackTask.COLUMN_STATE, state);
		String whereClause = SPDB.BackTask.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { id + "" };
		db.update(SPDB.BackTask.TABLE_NAME, values, whereClause, whereArgs);
	}

	public Cursor query(String owner) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + SPDB.BackTask.TABLE_NAME + " where "
				+ SPDB.BackTask.COLUMN_OWNER + "=?";
		return db.rawQuery(sql, new String[] { owner });
	}

	public Cursor query(String owner, int state) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + SPDB.BackTask.TABLE_NAME + " where "
				+ SPDB.BackTask.COLUMN_OWNER + "=? and "
				+ SPDB.BackTask.COLUMN_STATE + "=?";
		return db.rawQuery(sql, new String[] { owner, "0" });
	}
}
