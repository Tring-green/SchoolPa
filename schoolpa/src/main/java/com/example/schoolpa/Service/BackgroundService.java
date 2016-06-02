package com.example.schoolpa.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import com.example.schoolpa.domain.Account;
import com.example.schoolpa.domain.NetTask;
import com.example.schoolpa.ChatApplication;
import com.example.schoolpa.lib.SPHttpManager;
import com.example.schoolpa.utils.SerializableUtil;
import com.example.schoolpa.db.BackTaskDao;
import com.example.schoolpa.db.SPDB;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/5/27.
 */
public class BackgroundService extends IntentService {

    private Account mAccount;

    public BackgroundService() {
        super("background");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mAccount = ((ChatApplication) getApplication()).getCurrentAccount();
        if (mAccount == null) {
            return;
        }
        final BackTaskDao dao = new BackTaskDao(this);
        Cursor cursor = dao.query(mAccount.getUserId(), 0);

        Map<Long, String> map = new HashMap<>();

        if (cursor != null) {

            while (cursor.moveToNext()) {
                final long id = cursor.getLong(cursor
                        .getColumnIndex(SPDB.BackTask.COLUMN_ID));
                String filePath = cursor.getString(cursor
                        .getColumnIndex(SPDB.BackTask.COLUMN_PATH));

                map.put(id, filePath);
            }
            cursor.close();
        }

        for (Map.Entry<Long, String> me : map.entrySet()) {
            try {
                final Long id = me.getKey();
                String filePath = me.getValue();

                NetTask task = (NetTask) SerializableUtil.read(filePath);

                // 改变状态值，正在发送
                dao.updateState(id, 1);

                int type = task.getType();
                if (type == NetTask.TYPE_NORMAL) {
                    doNormalTask(dao, id, task);
                } else if (type == NetTask.TYPE_UPLOAD) {
                    doUploadTask(dao, id, task);
                } else if (type == NetTask.TYPE_DOWNLOAD) {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void doNormalTask(final BackTaskDao dao, final Long id, NetTask task) {
        boolean result = SPHttpManager.getInstance().post(task.getPath(),
                task.getParams());

        if (result) {
            dao.updateState(id, 2);
        }
    }

    private void doUploadTask(final BackTaskDao dao, final Long id, NetTask task) {
        HashMap<String, String> files = task.getFiles();
        if (files != null) {
            SPHttpManager.getInstance().upload(task.getPath(), task.getParams(),
                    task.getFiles());
        }
    }
}
