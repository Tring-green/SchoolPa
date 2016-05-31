package com.example.schoolpa.Fragment.Chat;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.schoolpa.Activity.MessageActivity;
import com.example.schoolpa.Base.BaseFragment;
import com.example.schoolpa.Bean.Account;
import com.example.schoolpa.ChatApplication;
import com.example.schoolpa.R;
import com.example.schoolpa.Receiver.PushReceiver;
import com.example.schoolpa.db.MessageDao;
import com.example.schoolpa.db.SPDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView listView;

    private PushReceiver pushReceiver = new PushReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String to = intent.getStringExtra(PushReceiver.KEY_TO);
            Account account = ((ChatApplication) getActivity().getApplication())
                    .getCurrentAccount();
            if (account.getUserId().equalsIgnoreCase(to)) {
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

        MessageDao dao = new MessageDao(getActivity());
        Cursor cursor = dao.queryConversation(account.getUserId());

        mAdapter = new ConversationAdapter(getActivity(), cursor);
        listView.setAdapter(mAdapter);

    }

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(mView);
        initEvent();

        IntentFilter filter = new IntentFilter();
        filter.addAction(PushReceiver.ACTION_TEXT);
        filter.addAction(PushReceiver.ACTION_ICON_CHANGE);
        filter.addAction(PushReceiver.ACTION_NAME_CHANGE);
        getActivity().registerReceiver(pushReceiver, filter);

        return mView;
    }

    private void initEvent() {
        listView.setOnItemClickListener(this);
    }
    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.chat_list_view);

    }

    private class ConversationAdapter extends CursorAdapter {

        public ConversationAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return View.inflate(context, R.layout.fragment_chat_item, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvUnread = (TextView) view
                    .findViewById(R.id.item_converation_tv_unread);
            TextView tvName = (TextView) view
                    .findViewById(R.id.item_converation_name);
            TextView tvContent = (TextView) view
                    .findViewById(R.id.item_converation_content);
            String name = cursor.getString(cursor
                    .getColumnIndex(SPDB.Conversation.COLUMN_USERID));
            String content = cursor.getString(cursor
                    .getColumnIndex(SPDB.Conversation.COLUMN_CONTENT));
            int unread = cursor.getInt(cursor
                    .getColumnIndex(SPDB.Conversation.COLUMN_UNREAD));

            if (unread <= 0) {
                tvUnread.setVisibility(View.GONE);
                tvUnread.setText("");
            } else {
                if (unread >= 99) {
                    tvUnread.setText("99");
                } else {
                    tvUnread.setText("" + unread);
                }
                tvUnread.setVisibility(View.VISIBLE);
            }
            tvName.setText(name);
            tvContent.setText(content);

            view.setTag(name);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("messager", (String) view.getTag());
        startActivity(intent);
    }
}
