package com.example.schoolpa.fragment.LoginActivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.schoolpa.R;
import com.example.schoolpa.activity.LoginActivity;
import com.example.schoolpa.db.AccountDao;
import com.example.schoolpa.db.FriendDao;
import com.example.schoolpa.db.MessageDao;
import com.example.schoolpa.domain.Account;
import com.example.schoolpa.domain.Friend;
import com.example.schoolpa.domain.Message;
import com.example.schoolpa.domain.SPError;
import com.example.schoolpa.lib.Callback.SPObjectCallBack;
import com.example.schoolpa.lib.SPChatManager;
import com.example.schoolpa.lib.SPHttpClient;
import com.example.schoolpa.service.ChatCoreService;
import com.example.schoolpa.utils.CommonUtil;
import com.example.schoolpa.utils.TextUtils;
import com.example.schoolpa.utils.UrlUtils;
import com.example.schoolpa.widget.DialogLoading;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    private static final String TAG = "REGISTERFRAGMENT";
    private View mView;
    private AutoCompleteTextView mTv_UserId;
    private AutoCompleteTextView mTv_Passwd;
    private String mUserId;
    private String mPasswd;
    private SPHttpClient mHttpClass;

    public SignUpFragment() {

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

                    final DialogLoading dialog = new DialogLoading(getActivity());
                    dialog.show();
                    SPChatManager.getInstance().register(mUserId, mPasswd,
                            new SPObjectCallBack<Account>() {

                                @Override
                                public void onSuccess(Account account) {
                                    Log.d(TAG, "注册成功!!!");
                                    dialog.dismiss();

                                    // 初始化用户连接安全信息
                                    SPChatManager.getInstance().initAccount(
                                            account.getUserId(), account.getToken());

                                    // 存储用户
                                    AccountDao dao = new AccountDao(getActivity());
                                    account.setCurrent(true);

                                    Account localAccount = dao.getByUserId(account
                                            .getUserId());
                                    if (localAccount != null) {
                                        dao.updateAccount(account);
                                    } else {
                                        dao.addAccount(account);
                                    }

                                    // 开启服务
                                    if (!CommonUtil.isServiceRunning(getActivity(),
                                            ChatCoreService.class)) {
                                        getActivity().startService(
                                                new Intent(getActivity(),
                                                        ChatCoreService.class));
                                    }

                                    FriendDao friendDao = new FriendDao(getActivity());
                                    Friend friend = friendDao.queryFriendByAccount(
                                            account.getUserId(), "HMChat");
                                    if (friend == null) {
                                        // 初始化通讯录
                                        friend = new Friend();
                                        friend.setOwner(account.getUserId());
                                        friend.setAccount("SPChat");
                                        friend.setAlpha("S");
                                        friend.setArea("");
                                        friend.setIcon("");
                                        friend.setName("深大校园通");
                                        friend.setNickName("");
                                        friend.setSort(1000);

                                        friendDao.addFriend(friend);

                                        MessageDao messageDao = new MessageDao(
                                                getActivity());
                                        Message message = new Message();
                                        message.setUserId("SPChat");
                                        message.setContent("欢迎使用深大校园通，深大校园通会给你带来更多精彩");
                                        message.setCreateTime(System.currentTimeMillis());
                                        message.setDirection(1);
                                        message.setOwner(account.getUserId());
                                        message.setRead(false);
                                        messageDao.addMessage(message);
                                    }

                                    ((LoginActivity) getActivity()).go2FillInfo();
                                }

                                @Override
                                public void onError(int error, String msg) {
                                    dialog.dismiss();

                                    switch (error) {
                                        case SPError.ERROR_CLIENT_NET:
                                            Log.d(TAG, "客户端网络异常");
                                            break;
                                        case SPError.ERROR_SERVER:
                                            Log.d(TAG, "服务器异常");
                                            break;
                                        case SPError.Register.ACCOUNT_EXIST:
                                            Log.d(TAG, "用户已经存在");
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });

                } else {
                    focusView.requestFocus();
                }
            }
        });
    }



}
