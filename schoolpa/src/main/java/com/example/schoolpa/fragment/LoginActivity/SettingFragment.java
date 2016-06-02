package com.example.schoolpa.fragment.LoginActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.schoolpa.ChatApplication;
import com.example.schoolpa.R;
import com.example.schoolpa.activity.LoginActivity;
import com.example.schoolpa.db.AccountDao;
import com.example.schoolpa.domain.Account;
import com.example.schoolpa.widget.DialogLogout;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private Button mBtnLogout;
    private Activity mActivity;
    private View mView;


    private void initView(View view) {
        mBtnLogout = (Button) view.findViewById(R.id.setting_logout);
    }

    private void initEvent() {
        mBtnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnLogout) {
            clickLogout();
        }
    }

    private void clickLogout() {
        final DialogLogout dialog = new DialogLogout(mActivity);
        dialog.show();
        dialog.setClickLogoutListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                logout();
            }
        });
    }

    private void logout() {
        AccountDao dao = new AccountDao(mActivity);
        Account account = dao.getCurrentAccount();
        account.setCurrent(false);
        account.setToken(null);
        dao.updateAccount(account);

        ((ChatApplication) mActivity.getApplication()).closeApplication();

        Intent intent = new Intent(mActivity.getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(LoginActivity.ENTER_KEY, LoginActivity.ENTER_SIGN_IN);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        mView = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(mView);
        initEvent();
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.finish();
        }
    }
}
