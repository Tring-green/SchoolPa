package com.example.schoolpa.Fragment.LoginActivity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.schoolpa.Activity.MainActivity;
import com.example.schoolpa.Bean.Account;
import com.example.schoolpa.Bean.BackTask;
import com.example.schoolpa.Bean.NetTask;
import com.example.schoolpa.R;
import com.example.schoolpa.Service.BackgroundService;
import com.example.schoolpa.Utils.CommonUtil;
import com.example.schoolpa.Utils.DirUtil;
import com.example.schoolpa.Utils.SerializableUtil;
import com.example.schoolpa.Utils.ToastUtils;
import com.example.schoolpa.Utils.UrlUtils;
import com.example.schoolpa.db.AccountDao;
import com.example.schoolpa.db.BackTaskDao;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FillInfoFragment extends Fragment {

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

    private View mView;
    private EditText mEt_name;

    public FillInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fill_info, container, false);
        verifyStoragePermissions(getActivity());
        initView();
        initData();
        return mView;
    }
    private AccountDao mDao;
    private Account mAccount;
    private String mIconDir;
    private File mSdcardTempFile;
    private String TAG = "SignUpFra";

    private void initData() {
        mDao = new AccountDao(getActivity());
        mAccount = mDao.getCurrentAccount();
        if (mAccount != null) {
            mIconDir = DirUtil.getIconDir(getActivity());
            mSdcardTempFile = new File(mIconDir, CommonUtil.string2MD5(mAccount.getUserId()));
            if (!mSdcardTempFile.getParentFile().exists()) {
                mSdcardTempFile.getParentFile().mkdirs();
            }
            mAccount.setIcon(mSdcardTempFile.getAbsolutePath());
        }
    }



    private void initView() {
        mEt_name = (EditText) mView.findViewById(R.id.name);
    }

    public void fillinfo(View view) {
        String name = mEt_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showTestShort(getActivity(), "名字不能为空");
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

        String outPath = DirUtil.getTaskDir(getActivity()) + "/"
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

        new BackTaskDao(getActivity()).addTask(task);
        getActivity().startService(new Intent(getActivity(), BackgroundService.class));
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();

    }


}
