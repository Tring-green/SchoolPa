package com.example.schoolpa.action;

import android.content.Context;
import android.content.Intent;

import com.example.schoolpa.domain.Friend;
import com.example.schoolpa.receiver.PushReceiver;
import com.example.schoolpa.db.FriendDao;

import java.util.Map;

public class NameChangeAction extends Action {

	@Override
	public String getAction() {
		return "nameChange";
	}

	@Override
	public void doAction(Context context, Map<String, Object> data) {
		if (data == null) {
			return;
		}

		String receiver = data.get("receiver").toString();
		String sender = data.get("sender").toString();
		String name = data.get("name").toString();

		// 数据存储
		FriendDao friendDao = new FriendDao(context);
		Friend friend = friendDao.queryFriendByAccount(receiver, sender);
		if (friend == null) {
			return;
		}
		friend.setName(name);
		friendDao.updateFriend(friend);

		// 发送广播
		Intent intent = new Intent(PushReceiver.ACTION_NAME_CHANGE);
		intent.putExtra(PushReceiver.KEY_FROM, sender);
		intent.putExtra(PushReceiver.KEY_TO, receiver);
		context.sendBroadcast(intent);
	}

}
