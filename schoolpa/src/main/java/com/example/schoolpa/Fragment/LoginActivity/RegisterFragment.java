package com.example.schoolpa.Fragment.LoginActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.schoolpa.Bean.HttpRegister;
import com.example.schoolpa.Lib.Callback.ObjectCallback;
import com.example.schoolpa.Lib.SPChatManager;
import com.example.schoolpa.Lib.SPHttpClass;
import com.example.schoolpa.R;
import com.example.schoolpa.Utils.TextUtils;
import com.example.schoolpa.Utils.UrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    private View mView;
    private AutoCompleteTextView mTv_UserId;
    private AutoCompleteTextView mTv_Passwd;
    private String mUserId;
    private String mPasswd;
    private SPHttpClass mHttpClass;

    public RegisterFragment() {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHttpClass != null) {
            mHttpClass.disconnect();
            mHttpClass = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mTv_UserId = (AutoCompleteTextView) mView.findViewById(R.id.userId);
        mTv_Passwd = (AutoCompleteTextView) mView.findViewById(R.id.passwd);
        Button register = (Button) mView.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserId = mTv_UserId.getText().toString();
                Log.d("userId", mUserId);
                mPasswd = mTv_Passwd.getText().toString();
                View focusView = mTv_UserId;
                boolean shouldRegister = true;
                if (!TextUtils.isEffective(mUserId)) {
                    mTv_UserId.setError("用户名不能为空");
                    focusView = mTv_UserId;
                    shouldRegister = false;
                } else if (mUserId.length() != 10) {
                    mTv_UserId.setError("学号应为10位数字组成");
                    focusView = mTv_UserId;
                    shouldRegister = false;
                }
                if (!TextUtils.isEffective(mPasswd)) {
                    mTv_Passwd.setError("密码不能为空");
                    focusView = mTv_Passwd;
                    shouldRegister = false;
                }

                if (shouldRegister) {
                    String url = UrlUtils.DATABASEURL + "register";
                    Map<String, String> parameter = new HashMap<>();
                    parameter.put("userId", mUserId);
                    parameter.put("passwd", mPasswd);

                    mHttpClass = SPChatManager.getInstance(getActivity()).sendRequest(url, parameter, new
                            ObjectCallback<HttpRegister>() {
                                @Override
                                public void onSuccess(HttpRegister data) {
                                    Log.d("onSuccess", data.toString());
                                    TurnToFillInfo();
                                }

                                @Override
                                public void onFailure(int errorCode, String errorMessage) {
                                    Log.d("onFailure", errorCode + " :" + errorMessage);
                                }
                            });

                } else {
                    focusView.requestFocus();
                }
            }
        });
    }

    private void TurnToFillInfo() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        mView.setVisibility(View.GONE);
        transaction.replace(R.id.framelayout, new FillInfoFragment()).commit();
    }


}
