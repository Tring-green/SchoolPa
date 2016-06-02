package com.example.schoolpa.action;

import android.content.Context;
import android.content.Intent;

import com.example.schoolpa.domain.Friend;
import com.example.schoolpa.lib.Callback.SPFileCallBack;
import com.example.schoolpa.lib.SPChatManager;
import com.example.schoolpa.receiver.PushReceiver;
import com.example.schoolpa.utils.DirUtil;
import com.example.schoolpa.db.FriendDao;

import java.io.File;
import java.util.Map;

public class IconChangeAction extends Action {

    @Override
    public String getAction() {
        return "nameChange";
    }

    @Override
    public void doAction(final Context context, Map<String, Object> data) {
        if (data == null) {
            return;
        }

        final String receiver = data.get("receiver").toString();
        final String sender = data.get("sender").toString();
        String iconPath = data.get("iconPath").toString();

        // 数据存储
        FriendDao friendDao = new FriendDao(context);
        Friend friend = friendDao.queryFriendByAccount(receiver, sender);
        if (friend == null) {
            return;
        }

        friend.setIcon(DirUtil.getIconDir(context) + iconPath);
        friendDao.updateFriend(friend);

        // 下载朋友的icon
        File file = new File(friend.getIcon());
        SPChatManager.getInstance().downloadFile(iconPath, file,
                new SPFileCallBack() {

                    @Override
                    public void onSuccess(File file) {
                        // 发送广播
                        Intent intent = new Intent(
                                PushReceiver.ACTION_ICON_CHANGE);
                        intent.putExtra(PushReceiver.KEY_FROM, sender);
                        intent.putExtra(PushReceiver.KEY_TO, receiver);
                        context.sendBroadcast(intent);
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
}
