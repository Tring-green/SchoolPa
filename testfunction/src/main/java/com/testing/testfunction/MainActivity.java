package com.testing.testfunction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.testing.testfunction.Domain.HttpRegister;
import com.testing.testfunction.Domain.NetTask;
import com.testing.testfunction.Lib.Callback.ObjectCallback;
import com.testing.testfunction.Lib.SPChatManager;
import com.testing.testfunction.Utils.SPHttpClass;
import com.testing.testfunction.Utils.SerializableUtil;
import com.testing.testfunction.Utils.ThreadUtils;
import com.testing.testfunction.Utils.UrlUtils;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String mUserId = "122";
    private String mPasswd = "123";
    private EditText mEt_userId;
    private EditText mEt_passwd;
    private SPHttpClass mHttpClass;
    private EditText mEt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ThreadUtils.init(50);

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
        mEt_userId = (EditText) findViewById(R.id.userId);
        mEt_passwd = (EditText) findViewById(R.id.passwd);
        mEt_name = (EditText) findViewById(R.id.name);
    }

    public void fillinfo(View view) {
        String name = mEt_name.getText().toString();
        NetTask request = new NetTask();

        String url = UrlUtils.DATABASEURL + "user/nameChange";

        Map<String, String> headers = new HashMap<>();
        headers.put("account", "123456");
        headers.put("token", "123456");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        request.setUrl(url);
        request.setMethod(0);
        request.setHeaders(headers);
        request.setParameters(parameters);

        String outPath = null;
        try {
            SerializableUtil.write(request, outPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }




    public void register(View view) {
        mUserId = mEt_userId.getText().toString();
        mPasswd = mEt_passwd.getText().toString();
        String url = UrlUtils.DATABASEURL + "register";
        Map<String, String> parameter = new HashMap<>();
        parameter.put("userId", mUserId);
        parameter.put("passwd", mPasswd);

        mHttpClass = SPChatManager.getInstance(this).sendRequest(url, parameter, new
                ObjectCallback<HttpRegister>() {
            @Override
            public void onSuccess(HttpRegister data) {

                Log.d("onSuccess", data.toString());
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                Log.d("onFailure", errorCode + " :" + errorMessage);
            }
        });


    }
}
