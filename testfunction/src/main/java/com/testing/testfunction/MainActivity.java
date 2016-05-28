package com.testing.testfunction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.testing.testfunction.Domain.Account;
import com.testing.testfunction.Domain.BackTask;
import com.testing.testfunction.Domain.NetTask;
import com.testing.testfunction.Lib.Callback.ObjectCallback;
import com.testing.testfunction.Lib.SPChatManager;
import com.testing.testfunction.Lib.SPError;
import com.testing.testfunction.Service.BackgroundService;
import com.testing.testfunction.Utils.CommonUtil;
import com.testing.testfunction.Utils.DirUtil;
import com.testing.testfunction.Utils.SPHttpClass;
import com.testing.testfunction.Utils.SerializableUtil;
import com.testing.testfunction.Utils.ThreadUtils;
import com.testing.testfunction.Utils.ToastUtils;
import com.testing.testfunction.Utils.UrlUtils;
import com.testing.testfunction.db.AccountDao;
import com.testing.testfunction.db.BackTaskDao;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String mUserId = "122";
    private String mPasswd = "123";
    private EditText mEt_userId;
    private EditText mEt_passwd;
    private SPHttpClass mHttpClass;
    private EditText mEt_name;
    private EditText mEt_content;
    private Socket mClient;
    private String dstName = "10.0.2.2";
    private int dstPort = 10000;
    private AccountDao mDao;
    private Account mAccount;
    private String mIconDir;
    private File mSdcardTempFile;
    private String TAG = "SignUpFra";

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        initView();
        initData();
        ThreadUtils.init(50);

    }

    private void initData() {
        mDao = new AccountDao(this);
        mAccount = mDao.getCurrentAccount();
        mIconDir = DirUtil.getIconDir(this);
        System.out.println(mIconDir);
        mSdcardTempFile = new File(mIconDir, CommonUtil.string2MD5(mAccount.getUserId()));

        if (!mSdcardTempFile.getParentFile().exists()) {
            mSdcardTempFile.getParentFile().mkdirs();
        }

        mAccount.setIcon(mSdcardTempFile.getAbsolutePath());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHttpClass != null) {
            mHttpClass.disconnect();
            mHttpClass = null;
        }
    }

    private void initView() {
        //mEt_content = (EditText) findViewById(R.id.content);
        mEt_userId = (EditText) findViewById(R.id.userId);
        mEt_passwd = (EditText) findViewById(R.id.passwd);
        mEt_name = (EditText) findViewById(R.id.name);
    }

    //public void clickConnect(View view) {
    //    new Thread(new Runnable() {
    //        @Override
    //        public void run() {
    //
    //            try {
    //                if (mClient == null || mClient.isClosed()) {
    //                    mClient = new Socket(dstName, dstPort);
    //                }
    //
    //                InputStream is = mClient.getInputStream();
    //                byte[] buffer = new byte[1024];
    //                int len;
    //                while ((len = is.read(buffer)) != -1) {
    //                    final String text = new String(buffer, 0, len);
    //                    runOnUiThread(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    //                        }
    //                    });
    //                }
    //
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //
    //    }).start();
    //}
    //
    //public void clickMessage(View view) {
    //    final String content = mEt_content.getText().toString();
    //    if (content == null && content.equals("")) {
    //        return;
    //    }
    //    OutputStream os = null;
    //    try {
    //        os = mClient.getOutputStream();
    //        os.write(content.getBytes());
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //}
    //
    //public void clickDisconnect(View view) {
    //    try {
    //        if (mClient != null && mClient.isClosed()) {
    //            mClient.close();
    //            mClient = null;
    //        }
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //
    //}

    public void fillinfo(View view) {
        String name = mEt_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showTestShort(getApplicationContext(), "名字不能为空");
        }

        mAccount.setName(name);
        mDao.updateAccount(mAccount);


        String url = UrlUtils.DATABASEURL + "user/nameChange";

        Map<String, String> headers = new HashMap<>();
        headers.put("userId", mAccount.getUserId());
        headers.put("token", mAccount.getToken());

        Map<String, String> parameters = new HashMap<>();

        parameters.put("name", name);

        NetTask request = new NetTask();
        request.setUrl(url);
        request.setMethod(0);
        request.setHeaders(headers);
        request.setParameters(parameters);

        String outPath = DirUtil.getTaskDir(this) + "/"
                + System.currentTimeMillis();
        try {
            SerializableUtil.write(request, outPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BackTask task = new BackTask();
        task.setOwner(mAccount.getUserId());
        task.setPath(outPath);
        task.setState(0);

        new BackTaskDao(this).addTask(task);
        startService(new Intent(this, BackgroundService.class));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    public void register(View view) {
        mUserId = mEt_userId.getText().toString();
        mPasswd = mEt_passwd.getText().toString();
        String url = UrlUtils.DATABASEURL + "register";
        Map<String, String> parameter = new HashMap<>();
        parameter.put("userId", mUserId);
        parameter.put("passwd", mPasswd);

        mHttpClass = SPChatManager.getInstance(this).sendRequest(url, parameter, new
                ObjectCallback<Account>() {
                    @Override
                    public void onSuccess(Account data) {
                        Log.d("onSuccess", data.toString());
                        AccountDao dao = new AccountDao(MainActivity.this);
                        data.transcoding();
                        data.setCurrent(true);
                        Account localAccount = dao.getByAccount(data.getUserId());
                        if (localAccount != null) {
                            dao.updateAccount(data);
                        } else {
                            dao.addAccount(data);
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMessage) {
                        switch (errorCode) {
                            case SPError.ERROR_CLIENT_NET:
                                Log.d(TAG, "客户端网络异常");
                                ToastUtils.showTestShort(MainActivity.this, "客户端网络异常");
                                break;
                            case SPError.ERROR_SERVER:
                                Log.d(TAG, "服务器异常");
                                ToastUtils.showTestShort(MainActivity.this, "服务器异常");
                                break;
                            case SPError.Register.ACCOUNT_EXIST:
                                Log.d(TAG, "用户已经存在");
                                ToastUtils.showTestShort(MainActivity.this, "用户已经存在");
                                break;
                            default:
                                break;
                        }
                    }
                });


    }
}
