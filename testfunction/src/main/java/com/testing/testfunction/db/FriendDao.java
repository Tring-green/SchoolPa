package com.testing.testfunction.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testing.testfunction.Domain.Friend;


public class FriendDao {
	private SPDBOpenHelper helper;

	public FriendDao(Context context) {
		helper = SPDBOpenHelper.getInstance(context);
	}

	public Cursor queryFriends(String owner) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "select * from " + SPDB.Friend.TABLE_NAME + " where "
				+ SPDB.Friend.COLUMN_OWNER + "=?";
		return db.rawQuery(sql, new String[] { owner });
	}

	public Friend queryFriendByAccount(String owner, String account) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "select * from " + SPDB.Friend.TABLE_NAME + " where "
				+ SPDB.Friend.COLUMN_OWNER + "=? and "
				+ SPDB.Friend.COLUMN_USERID + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { owner, account });
		if (cursor != null) {
			Friend friend = null;
			if (cursor.moveToNext()) {
				String alpha = cursor.getString(cursor
						.getColumnIndex(SPDB.Friend.COLUMN_ALPHA));
				String area = cursor.getString(cursor
						.getColumnIndex(SPDB.Friend.COLUMN_AREA));
				String icon = cursor.getString(cursor
						.getColumnIndex(SPDB.Friend.COLUMN_ICON));
				String name = cursor.getString(cursor
						.getColumnIndex(SPDB.Friend.COLUMN_NAME));
				String nickName = cursor.getString(cursor
						.getColumnIndex(SPDB.Friend.COLUMN_NICKNAME));
				int sex = cursor.getInt(cursor
						.getColumnIndex(SPDB.Friend.COLUMN_SEX));
				String sign = cursor.getString(cursor
						.getColumnIndex(SPDB.Friend.COLUMN_SIGN));
				int sort = cursor.getInt(cursor
						.getColumnIndex(SPDB.Friend.COLUMN_SORT));

				friend = new Friend();
				friend.setAccount(account);
				friend.setAlpha(alpha);
				friend.setArea(area);
				friend.setIcon(icon);
				friend.setName(name);
				friend.setNickName(nickName);
				friend.setOwner(owner);
				friend.setSex(sex);
				friend.setSign(sign);
				friend.setSort(sort);
			}
			cursor.close();
			return friend;
		}
		return null;
	}

	public void addFriend(Friend friend) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SPDB.Friend.COLUMN_USERID, friend.getAccount());
		values.put(SPDB.Friend.COLUMN_ALPHA, friend.getAlpha());
		values.put(SPDB.Friend.COLUMN_AREA, friend.getArea());
		values.put(SPDB.Friend.COLUMN_ICON, friend.getIcon());
		values.put(SPDB.Friend.COLUMN_NAME, friend.getName());
		values.put(SPDB.Friend.COLUMN_NICKNAME, friend.getNickName());
		values.put(SPDB.Friend.COLUMN_OWNER, friend.getOwner());
		values.put(SPDB.Friend.COLUMN_SEX, friend.getSex());
		values.put(SPDB.Friend.COLUMN_SIGN, friend.getSign());
		values.put(SPDB.Friend.COLUMN_SORT, friend.getSort());

		friend.setId(db.insert(SPDB.Friend.TABLE_NAME, null, values));
	}

	public void updateFriend(Friend friend) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SPDB.Friend.COLUMN_USERID, friend.getAccount());
		values.put(SPDB.Friend.COLUMN_ALPHA, friend.getAlpha());
		values.put(SPDB.Friend.COLUMN_AREA, friend.getArea());
		values.put(SPDB.Friend.COLUMN_ICON, friend.getIcon());
		values.put(SPDB.Friend.COLUMN_NAME, friend.getName());
		values.put(SPDB.Friend.COLUMN_NICKNAME, friend.getNickName());
		values.put(SPDB.Friend.COLUMN_OWNER, friend.getOwner());
		values.put(SPDB.Friend.COLUMN_SEX, friend.getSex());
		values.put(SPDB.Friend.COLUMN_SIGN, friend.getSign());
		values.put(SPDB.Friend.COLUMN_SORT, friend.getSort());

		String whereClause = SPDB.Friend.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { friend.getId() + ""};
		db.update(SPDB.Friend.TABLE_NAME, values, whereClause, whereArgs);
	}
}
