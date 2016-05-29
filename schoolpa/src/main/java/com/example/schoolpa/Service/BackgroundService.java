package com.example.schoolpa.Service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import com.example.schoolpa.Bean.Account;
import com.example.schoolpa.Bean.NetTask;
import com.example.schoolpa.ChatApplication;
import com.example.schoolpa.Lib.SPHttpClient;
import com.example.schoolpa.Lib.SPHttpParams;
import com.example.schoolpa.Utils.SerializableUtil;
import com.example.schoolpa.db.BackTaskDao;
import com.example.schoolpa.db.SPDB;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/5/27.
 */
public class BackgroundService extends IntentService {

    public BackgroundService() {
        super("background");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Account account = ((ChatApplication) getApplication()).getCurrentAccount();
        if (account == null) {
            return;
        }
        final BackTaskDao dao = new BackTaskDao(this);
        Cursor cursor = dao.query(account.getUserId(), 0);

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

                String url = task.getUrl();
                Map<String, String> headers = task.getHeaders();
                Map<String, String> paramaters = task.getParameters();
                SPHttpParams httpParams = new SPHttpParams(5000, 5000, true);
                SPHttpClient.getInstance(this).startConnectionNOThread(url, "POST", httpParams, headers, paramaters,
                        new SPHttpClient.OnVisitingListener() {

                            @Override
                            public void onSuccess(String result) {
                                if (result.contains("true")) {
                                    System.out.println("#########9");
                                    dao.updateState(id, 2);
                                } else {
                                    System.out.println("#########10");
                                    dao.updateState(id, 0);
                                }
                            }
                            @Override
                            public void onFailure(IOException e) {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
