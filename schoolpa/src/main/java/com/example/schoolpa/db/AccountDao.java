package com.example.schoolpa.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.schoolpa.domain.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {
	private SPDBOpenHelper helper;

	public AccountDao(Context context) {
		helper = SPDBOpenHelper.getInstance(context);
	}

	public List<Account> getAllAccount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + SPDB.Account.TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);

		List<Account> list = null;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				if (list == null) {
					list = new ArrayList<Account>();
				}
				Account account = new Account();

				account.setUserId(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_USERID)));
				account.setArea(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_AREA)));
				account.setCurrent(cursor.getInt(cursor
						.getColumnIndex(SPDB.Account.COLUMN_CURRENT)) == 1);
				account.setIcon(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_ICON)));
				account.setName(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_NAME)));
				account.setSex(cursor.getInt(cursor
						.getColumnIndex(SPDB.Account.COLUMN_SEX)));
				account.setSign(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_SIGN)));
				account.setToken(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_TOKEN)));
				list.add(account);
			}
		}
		return list;
	}

	public Account getCurrentAccount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + SPDB.Account.TABLE_NAME + " where "
				+ SPDB.Account.COLUMN_CURRENT + "=1";
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				Account account = new Account();

				account.setUserId(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_USERID)));
				account.setArea(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_AREA)));
				account.setCurrent(cursor.getInt(cursor
						.getColumnIndex(SPDB.Account.COLUMN_CURRENT)) == 1);
				account.setIcon(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_ICON)));
				account.setName(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_NAME)));
				account.setSex(cursor.getInt(cursor
						.getColumnIndex(SPDB.Account.COLUMN_SEX)));
				account.setSign(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_SIGN)));
				account.setToken(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_TOKEN)));
				return account;
			}
		}
		return null;
	}

	public Account getByUserId(String userId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + SPDB.Account.TABLE_NAME + " where "
				+ SPDB.Account.COLUMN_USERID + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { userId });

		if (cursor != null) {
			while (cursor.moveToNext()) {
				Account a = new Account();

				a.setUserId(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_USERID)));
				a.setArea(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_AREA)));
				a.setCurrent(cursor.getInt(cursor
						.getColumnIndex(SPDB.Account.COLUMN_CURRENT)) == 1);
				a.setIcon(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_ICON)));
				a.setName(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_NAME)));
				a.setSex(cursor.getInt(cursor
						.getColumnIndex(SPDB.Account.COLUMN_SEX)));
				a.setSign(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_SIGN)));
				a.setToken(cursor.getString(cursor
						.getColumnIndex(SPDB.Account.COLUMN_TOKEN)));
				return a;
			}
		}
		return null;
	}

	public void addAccount(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SPDB.Account.COLUMN_USERID, account.getUserId());
		values.put(SPDB.Account.COLUMN_AREA, account.getArea());
		values.put(SPDB.Account.COLUMN_ICON, account.getIcon());
		values.put(SPDB.Account.COLUMN_NAME, account.getName());
		values.put(SPDB.Account.COLUMN_SEX, account.getSex());
		values.put(SPDB.Account.COLUMN_SIGN, account.getSign());
		values.put(SPDB.Account.COLUMN_TOKEN, account.getToken());
		values.put(SPDB.Account.COLUMN_CURRENT, account.isCurrent() ? 1 : 0);

		db.insert(SPDB.Account.TABLE_NAME, null, values);
	}

	public void updateAccount(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SPDB.Account.COLUMN_AREA, account.getArea());
		values.put(SPDB.Account.COLUMN_ICON, account.getIcon());
		values.put(SPDB.Account.COLUMN_NAME, account.getName());
		values.put(SPDB.Account.COLUMN_SEX, account.getSex());
		values.put(SPDB.Account.COLUMN_SIGN, account.getSign());
		values.put(SPDB.Account.COLUMN_TOKEN, account.getToken());
		values.put(SPDB.Account.COLUMN_CURRENT, account.isCurrent() ? 1 : 0);

		String whereClause = SPDB.Account.COLUMN_USERID + "=?";
		String[] whereArgs = new String[] { account.getUserId() };
		db.update(SPDB.Account.TABLE_NAME, values, whereClause, whereArgs);
	}

	public void deleteAccount(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String whereClause = SPDB.Account.COLUMN_USERID + "=?";
		String[] whereArgs = new String[] { account.getUserId() };
		db.delete(SPDB.Account.TABLE_NAME, whereClause, whereArgs);
	}
}
