package com.example.schoolpa.action;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.schoolpa.domain.Invitation;
import com.example.schoolpa.lib.Callback.SPFileCallBack;
import com.example.schoolpa.lib.SPChatManager;
import com.example.schoolpa.receiver.PushReceiver;
import com.example.schoolpa.utils.DirUtil;
import com.example.schoolpa.db.InvitationDao;

import java.io.File;
import java.util.Map;

public class InvitationAction extends Action {

	@Override
	public String getAction() {
		return "invitation";
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

		Object nameObj = data.get("invitor_name");
		if (nameObj != null) {
			name = (String) nameObj;
		}

		Object iconObj = data.get("invitor_icon");
		if (iconObj != null) {
			icon = (String) iconObj;
		}

		// 存取数据
		InvitationDao invitationDao = new InvitationDao(context);
		Invitation invitation = invitationDao.queryInvitation(receiver, sender);
		if (invitation == null) {
			invitation = new Invitation();
			invitation.setUserId(sender);
			invitation.setOwner(receiver);
			invitation.setAgree(false);
			if (icon != null) {
				icon = DirUtil.getIconDir(context) + icon;
				invitation.setIcon(icon);
			}
			invitation.setName(name);
			invitationDao.addInvitation(invitation);
		} else {
			invitation.setAgree(false);
			if (icon != null) {
				icon = DirUtil.getIconDir(context) + icon;
				invitation.setIcon(icon);
			}
			invitationDao.updateInvitation(invitation);
		}

		String friendIcon = invitation.getIcon();
		if (!TextUtils.isEmpty(friendIcon)) {
			// 下载朋友icon
			File file = new File(friendIcon);
			SPChatManager.getInstance(context).downloadFile(icon, file,
					new SPFileCallBack() {

						@Override
						public void onSuccess(File file) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onProgress(long writen, long total) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(int error, String msg) {
							// TODO Auto-generated method stub

						}
					});
		}

		// 发送广播
		Intent intent = new Intent(PushReceiver.ACTION_INVATION);
		intent.putExtra(PushReceiver.KEY_FROM, sender);
		intent.putExtra(PushReceiver.KEY_TO, receiver);
		context.sendBroadcast(intent);
	}
}
