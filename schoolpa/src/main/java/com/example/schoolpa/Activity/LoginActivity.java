package com.example.schoolpa.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.example.schoolpa.R;
import com.example.schoolpa.db.AccountDao;
import com.example.schoolpa.domain.Account;
import com.example.schoolpa.fragment.LoginActivity.FillInfoFragment;
import com.example.schoolpa.fragment.LoginActivity.LogoFragment;
import com.example.schoolpa.fragment.LoginActivity.SettingFragment;
import com.example.schoolpa.fragment.LoginActivity.SignInFragment;
import com.example.schoolpa.fragment.LoginActivity.SignUpFragment;

public class LoginActivity extends FragmentActivity {
    public static final String TAG_LOGO = "logo";
    public static final String TAG_SIGN_IN = "sign_in";
    public static final String TAG_SIGN_UP = "sign_up";
    public static final String TAG_FILL_INFO = "fill_info";
    private static final String TAG_SETTING = "setting";

    public static final String ENTER_KEY = "enter";
    public static final int ENTER_FIRST = 0;
    public static final int ENTER_LOGINED = 1;
    public static final int ENTER_SIGN_IN = 2;
    public static final int ENTER_SIGN_UP = 3;
    public static final int ENTER_FILL_INFO = 4;

    private Fragment currentFra;
    private String currentTag;
    private FragmentManager fm;

    private int enterFlag = 0;

    private AccountDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fm = getSupportFragmentManager();
        enterFlag = getIntent().getIntExtra(ENTER_KEY, ENTER_FIRST);

        dao = new AccountDao(this);
        Account account = dao.getCurrentAccount();
        if (account != null && !TextUtils.isEmpty(account.getName())) {
            enterFlag = ENTER_LOGINED;
        } else if (account != null) {
            enterFlag = ENTER_FILL_INFO;
        }
        if (enterFlag == ENTER_FIRST) {
            // 第一次登录
            currentFra = new LogoFragment();
            Bundle args = new Bundle();
            args.putInt(LogoFragment.ARG_KEY, LogoFragment.ARG_TYPE_FIRST);
            currentFra.setArguments(args);
            currentTag = TAG_LOGO;
        } else if (enterFlag == ENTER_LOGINED) {
            // 用户已经登录
            currentFra = new SettingFragment();
            currentTag = TAG_SETTING;
        } else if (enterFlag == ENTER_SIGN_IN) {
            currentFra = new SignInFragment();
            currentTag = TAG_SIGN_IN;
        } else if (enterFlag == ENTER_FILL_INFO) {
            currentFra = new FillInfoFragment();
            currentTag = TAG_FILL_INFO;
        }
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.contanier_login, currentFra, currentTag);
        transaction.addToBackStack(currentTag);
        transaction.commit();
    }


    public void go2FillInfo() {

        Fragment fragment = fm.findFragmentByTag(TAG_FILL_INFO);
        int count = fm.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            fm.popBackStack();
        }


        if (fragment == null) {
            fragment = new FillInfoFragment();
        }

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.contanier_login, fragment, TAG_FILL_INFO);
        ft.addToBackStack(TAG_FILL_INFO);
        ft.commit();

    }
    public void go2SignIn() {
        Fragment fragment = fm.findFragmentByTag(TAG_SIGN_IN);
        if (fragment == null) {
            fragment = new SignInFragment();
        }


        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.contanier_login, fragment, TAG_SIGN_IN);
        ft.addToBackStack(TAG_SIGN_IN);
        ft.commit();
    }

    public void go2SignUp() {
        Fragment fragment = fm.findFragmentByTag(TAG_SIGN_UP);
        if (fragment == null) {
            fragment = new SignUpFragment();
        }


        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.contanier_login, fragment, TAG_SIGN_UP);
        ft.addToBackStack(TAG_SIGN_UP);
        ft.commit();
    }
}
