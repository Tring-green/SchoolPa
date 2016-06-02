package com.example.schoolpa.fragment.Chat;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolpa.activity.MessageActivity;
import com.example.schoolpa.adapter.Chat.ConversationAdapter;
import com.example.schoolpa.base.BaseFragment;
import com.example.schoolpa.domain.Account;
import com.example.schoolpa.ChatApplication;
import com.example.schoolpa.R;
import com.example.schoolpa.receiver.PushReceiver;
import com.example.schoolpa.db.MessageDao;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment {

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

    private PushReceiver pushReceiver = new PushReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String to = intent.getStringExtra(PushReceiver.KEY_TO);
            Account account = ((ChatApplication) getActivity().getApplication())
                    .getCurrentAccount();

            if (account != null && account.getUserId().equalsIgnoreCase(to)) {
                loadData();
            }
        }
    };

    private ConversationAdapter mAdapter;
    private View mView;


    private void loadData() {
        if (getActivity() == null) {
            return;
        }
        ChatApplication application = (ChatApplication) getActivity()
                .getApplication();
        Account account = application.getCurrentAccount();

        if (account != null) {
            MessageDao dao = new MessageDao(getActivity());
            Cursor cursor = dao.queryConversation(account.getUserId());
            mAdapter = new ConversationAdapter(getActivity(), cursor, new ConversationAdapter.onItemClickListener() {
                @Override
                public void onClick(String name) {
                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    intent.putExtra("messager", name);
                    startActivity(intent);
                }
            });
        }
    }

    public ChatFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        verifyStoragePermissions(getActivity());
        mView = inflater.inflate(R.layout.fragment_chat, container, false);

        initView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushReceiver.ACTION_TEXT);
        filter.addAction(PushReceiver.ACTION_ICON_CHANGE);
        filter.addAction(PushReceiver.ACTION_NAME_CHANGE);
        getActivity().registerReceiver(pushReceiver, filter);

        return mView;
    }

    private void initView() {
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.rv_list);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(pushReceiver);
    }
}
