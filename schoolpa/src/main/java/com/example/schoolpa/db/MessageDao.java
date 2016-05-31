package com.example.schoolpa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.schoolpa.Bean.Conversation;
import com.example.schoolpa.Bean.Message;


public class MessageDao {
	private SPDBOpenHelper helper;

	public MessageDao(Context context) {
		helper = SPDBOpenHelper.getInstance(context);
	}

	public void addMessage(Message message) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SPDB.Message.COLUMN_USERID, message.getUserId());
		values.put(SPDB.Message.COLUMN_CONTENT, message.getContent());
		values.put(SPDB.Message.COLUMN_CREATE_TIME, message.getCreateTime());
		values.put(SPDB.Message.COLUMN_DIRECTION, message.getDirection());
		values.put(SPDB.Message.COLUMN_OWNER, message.getOwner());
		values.put(SPDB.Message.COLUMN_STATE, message.getState());
		values.put(SPDB.Message.COLUMN_TYPE, message.getType());
		values.put(SPDB.Message.COLUMN_URL, message.getUrl());
		values.put(SPDB.Message.COLUMN_READ, message.isRead() ? 1 : 0);

		message.setId(db.insert(SPDB.Message.TABLE_NAME, null, values));

		String sql = "select * from " + SPDB.Conversation.TABLE_NAME
				+ " where " + SPDB.Conversation.COLUMN_USERID + "=? and "
				+ SPDB.Conversation.COLUMN_OWNER + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { message.getUserId(),
				message.getOwner() });
		if (cursor != null && cursor.moveToNext()) {
			// String account = cursor.getString(cursor
			// .getColumnIndex(SPDB.Conversation.COLUMN_USERID));
			// String content = cursor.getString(cursor
			// .getColumnIndex(SPDB.Conversation.COLUMN_CONTENT));
			// String icon = cursor.getString(cursor
			// .getColumnIndex(SPDB.Conversation.COLUMN_ICON));
			// String name = cursor.getString(cursor
			// .getColumnIndex(SPDB.Conversation.COLUMN_NAME));
			// String owner = cursor.getString(cursor
			// .getColumnIndex(SPDB.Conversation.COLUMN_OWNER));
			// int unread = cursor.getInt(cursor
			// .getColumnIndex(SPDB.Conversation.COLUMN_UNREAD));
			// long updateTime = cursor.getLong(cursor
			// .getColumnIndex(SPDB.Conversation.COLUMN_UPDATE_TIME));
			//

			// 关闭cursor
			cursor.close();
			cursor = null;

			int unread = 0;

			sql = "select count(_id) from " + SPDB.Message.TABLE_NAME
					+ " where " + SPDB.Message.COLUMN_READ + "=0 and "
					+ SPDB.Message.COLUMN_USERID + "=? and "
					+ SPDB.Message.COLUMN_OWNER + "=?";
			cursor = db.rawQuery(sql, new String[] { message.getUserId(),
					message.getOwner() });
			if (cursor != null && cursor.moveToNext()) {
				unread = cursor.getInt(0);
			}

			values = new ContentValues();
			values.put(SPDB.Conversation.COLUMN_USERID, message.getUserId());

			int type = message.getType();
			if (type == 0) {
				values.put(SPDB.Conversation.COLUMN_CONTENT,
						message.getContent());
			} else if (type == 1) {
				values.put(SPDB.Conversation.COLUMN_CONTENT, "图片");
			}
			// values.put(SPDB.Conversation.COLUMN_ICON,
			// conversation.getIcon());
			// values.put(SPDB.Conversation.COLUMN_NAME,
			// conversation.getName());
			values.put(SPDB.Conversation.COLUMN_OWNER, message.getOwner());
			values.put(SPDB.Conversation.COLUMN_UNREAD, unread);
			values.put(SPDB.Conversation.COLUMN_UPDATE_TIME,
					System.currentTimeMillis());

			String whereClause = SPDB.Conversation.COLUMN_OWNER + "=? and "
					+ SPDB.Conversation.COLUMN_USERID + "=?";
			String[] whereArgs = new String[] { message.getOwner(),
					message.getUserId() };

			db.update(SPDB.Conversation.TABLE_NAME, values, whereClause,
					whereArgs);

		} else {
			Conversation conversation = new Conversation();
			conversation.setAccount(message.getUserId());
			int type = message.getType();
			if (type == 0) {
				conversation.setContent(message.getContent());
			} else if (type == 1) {
				conversation.setContent("图片");
			}
			// conversation.setIcon(message.get);
			// conversation.setName(message.get);
			conversation.setOwner(message.getOwner());
			conversation.setUnread(message.isRead() ? 0 : 1);
			conversation.setUpdateTime(System.currentTimeMillis());

			values = new ContentValues();
			values.put(SPDB.Conversation.COLUMN_USERID,
					conversation.getAccount());
			values.put(SPDB.Conversation.COLUMN_CONTENT,
					conversation.getContent());
			values.put(SPDB.Conversation.COLUMN_ICON, conversation.getIcon());
			values.put(SPDB.Conversation.COLUMN_NAME, conversation.getName());
			values.put(SPDB.Conversation.COLUMN_OWNER, conversation.getOwner());
			values.put(SPDB.Conversation.COLUMN_UNREAD,
					conversation.getUnread());
			values.put(SPDB.Conversation.COLUMN_UPDATE_TIME,
					conversation.getUpdateTime());

			db.insert(SPDB.Conversation.TABLE_NAME, null, values);
		}
	}

	public void updateMessage(Message message) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SPDB.Message.COLUMN_CONTENT, message.getContent());
		values.put(SPDB.Message.COLUMN_CREATE_TIME, message.getCreateTime());
		values.put(SPDB.Message.COLUMN_DIRECTION, message.getDirection());
		values.put(SPDB.Message.COLUMN_STATE, message.getState());
		values.put(SPDB.Message.COLUMN_TYPE, message.getType());
		values.put(SPDB.Message.COLUMN_URL, message.getUrl());
		values.put(SPDB.Message.COLUMN_READ, message.isRead() ? 1 : 0);

		String whereClause = SPDB.Message.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { message.getId() + "" };
		db.update(SPDB.Message.TABLE_NAME, values, whereClause, whereArgs);
	}

	public Cursor queryMessage(String owner, String account) {
		String sql = "select * from " + SPDB.Message.TABLE_NAME + " where "
				+ SPDB.Message.COLUMN_OWNER + "=? and "
				+ SPDB.Message.COLUMN_USERID + "=? order by "
				+ SPDB.Message.COLUMN_CREATE_TIME + " asc";
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(sql, new String[] { owner, account });
	}

	public Cursor queryConversation(String owner) {
		String sql = "select * from " + SPDB.Conversation.TABLE_NAME
				+ " where " + SPDB.Conversation.COLUMN_OWNER + "=? order by "
				+ SPDB.Conversation.COLUMN_UPDATE_TIME + " desc";
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(sql, new String[] { owner });
	}

	public void clearUnread(String owner, String account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SPDB.Message.COLUMN_READ, 1);
		String whereClause = SPDB.Message.COLUMN_OWNER + "=? and "
				+ SPDB.Message.COLUMN_USERID + "=?";
		String[] whereArgs = new String[] { owner, account };
		db.update(SPDB.Message.TABLE_NAME, values, whereClause, whereArgs);

		values = new ContentValues();
		values.put(SPDB.Conversation.COLUMN_UNREAD, 0);
		whereClause = SPDB.Conversation.COLUMN_OWNER + "=? and "
				+ SPDB.Conversation.COLUMN_USERID + "=?";
		db.update(SPDB.Conversation.TABLE_NAME, values, whereClause, whereArgs);
	}

	public int getAllUnread(String owner) {
		String sql = "select sum(" + SPDB.Conversation.COLUMN_UNREAD
				+ ") from " + SPDB.Conversation.TABLE_NAME + " where "
				+ SPDB.Conversation.COLUMN_OWNER + "=?";

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] { owner });
		int sum = 0;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				sum = cursor.getInt(0);
			}
			cursor.close();
		}
		return sum;
	}
}
