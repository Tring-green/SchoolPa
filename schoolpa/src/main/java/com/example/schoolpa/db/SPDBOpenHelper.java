package com.example.schoolpa.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SPDBOpenHelper extends SQLiteOpenHelper {

	private SPDBOpenHelper(Context context) {
		super(context, SPDB.NAME, null, SPDB.VERSION);
	}

	private static SPDBOpenHelper instance;

	public static SPDBOpenHelper getInstance(Context context) {
		if (instance == null) {
			synchronized (SPDBOpenHelper.class) {
				if (instance == null) {
					instance = new SPDBOpenHelper(context);
				}
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SPDB.Account.SQL_CREATE_TABLE);
		db.execSQL(SPDB.Friend.SQL_CREATE_TABLE);
		db.execSQL(SPDB.Invitation.SQL_CREATE_TABLE);
		db.execSQL(SPDB.Message.SQL_CREATE_TABLE);
		db.execSQL(SPDB.Conversation.SQL_CREATE_TABLE);
		db.execSQL(SPDB.BackTask.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
