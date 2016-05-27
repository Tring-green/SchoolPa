package com.example.schoolpa.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.example.schoolpa.Base.HttpClass;
import com.example.schoolpa.Bean.WebBean;
import com.example.schoolpa.R;
import com.example.schoolpa.Utils.HttpUtils;
import com.example.schoolpa.Utils.LogUtils;
import com.example.schoolpa.Utils.SharedPreferenceUtils;
import com.example.schoolpa.Utils.StreamUtils;
import com.example.schoolpa.Utils.ThreadUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {


    private static final int CODE_ENTER_MAIN = 0;
    private String urlmaxid;
    public static List<String> urllist = new ArrayList<String>();
    private SharedPreferences sp;
    private static String downloadUrl;
    private static List<WebBean> webDetails;
    private HttpClass httpClass;
    private String TAG = "SplashActivity";
    private long mStartTime;

    public static String getDownloadUrl() {
        return downloadUrl;
    }

    private List<String> dirList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferenceUtils.delete(this);
        initView();
        enterMain();
//        initData();
    }


    private void initElectric() {
        HttpUtils.findBuildingName();
        LogUtils.d(TAG, "findBuildingName");
        HttpUtils.findRoomName();
        LogUtils.d(TAG, "findRoomName");
        long endTime = System.currentTimeMillis();
        long usedTIme = endTime - mStartTime;
        if (usedTIme < 2000) {
            SystemClock.sleep(2000 - usedTIme);
        }
    }


    private void initView() {
        //初始化线程池的数量为3
        ThreadUtils.init(3);
        setContentView(R.layout.activity_splash);
        RelativeLayout rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
        anim.setDuration(2000);
        rl_root.startAnimation(anim);
    }

    private void initData() {
        checkNewWebUrl();
        urlmaxid = SharedPreferenceUtils.getString(this, "urlmaxid");
        if(urlmaxid == null)
            urlmaxid = "0";
    }

    private void enterMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void checkNewWebUrl() {
        mStartTime = System.currentTimeMillis();
        ThreadUtils.execute(new ThreadUtils.onStartThreadListener() {
            @Override
            public void onStartThread() {
                final Message msg = Message.obtain();
                httpClass = new HttpClass();
                httpClass.isPost = false;
                httpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {
                    @Override
                    public void onSuccess(HttpURLConnection conn) {
                        InputStream inputStream;
                        try {
                            inputStream = conn.getInputStream();
                            String result = StreamUtils.readFromStream(inputStream);
                            JSONObject jo = new JSONObject(result);
                            String id = jo.getString("id");
                            downloadUrl = jo.getString("downloadUrl");
                            LogUtils.d(TAG, "获取下载链接");
                            if (id.compareTo(urlmaxid) > 0) {
                                //有新的网页
                                HttpUtils.downloadDetailInfo(SplashActivity.this, downloadUrl);
                            } else {
                                //没有新的网页
                                msg.what = CODE_ENTER_MAIN;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSetDetails(HttpURLConnection conn) {

                    }

                    @Override
                    public void onFail(IOException e) {
                        e.printStackTrace();
                    }
                }).startConnenction("http://10.0.2.2:8080/newweburl.json");

                //初始化电费
                initElectric();

                long endTime = System.currentTimeMillis();
                long usedTIme = endTime - mStartTime;
                if (usedTIme < 2000) {
                    SystemClock.sleep(2000 - usedTIme);
                }
                enterMain();
            }
        });
    }

    public static List<String> getUrllist() {
        return urllist;
    }

}
