package com.example.schoolpa.action;

import android.content.Context;
import android.content.Intent;

import com.example.schoolpa.domain.Friend;
import com.example.schoolpa.receiver.PushReceiver;
import com.example.schoolpa.utils.CommonUtil;
import com.example.schoolpa.utils.DirUtil;
import com.example.schoolpa.db.FriendDao;

import java.util.Map;

public class ReinvitationAction extends Action {

	@Override
	public String getAction() {
		return "reinvitation";
	}

	@Override
	public void doAction(Context context, Map<String, Object> data) {
		if (data == null) {
			return;
		}

		String receiver = data.get("receiver").toString();
		String sender = data.get("sender").toString();

		String name = null;
		String icon = null;

		Object nameObj = data.get("name");
		if (nameObj != null) {
			name = (String) nameObj;
		}

		Object iconObj = data.get("icon");
		if (iconObj != null) {
			icon = (String) iconObj;
		}

		// 数据存储
		FriendDao friendDao = new FriendDao(context);
		Friend friend = friendDao.queryFriendByAccount(receiver, sender);
		if (friend == null) {
			friend = new Friend();
			friend.setAccount(sender);
			friend.setAlpha(CommonUtil.getFirstAlpha(name));
			if (icon != null) {
				icon = DirUtil.getIconDir(context) + icon;
				friend.setIcon(icon);
			}
			friend.setName(name);
			friend.setOwner(receiver);
			friend.setSort(0);

			friendDao.addFriend(friend);
		}

		Intent intent = new Intent(PushReceiver.ACTION_REINVATION);
		intent.putExtra(PushReceiver.KEY_FROM, sender);
		intent.putExtra(PushReceiver.KEY_TO, receiver);
		context.sendBroadcast(intent);
	}

}
