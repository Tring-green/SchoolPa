package com.example.schoolpa.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.schoolpa.Bean.Account;
import com.example.schoolpa.Bean.Message;
import com.example.schoolpa.ChatApplication;
import com.example.schoolpa.Lib.Callback.SPChatCallBack;
import com.example.schoolpa.Lib.Message.ChatMessage;
import com.example.schoolpa.Lib.Message.TextBody;
import com.example.schoolpa.Lib.SPChatManager;
import com.example.schoolpa.R;
import com.example.schoolpa.Receiver.PushReceiver;
import com.example.schoolpa.Utils.CommonUtil;
import com.example.schoolpa.Utils.ToastUtils;
import com.example.schoolpa.db.MessageDao;
import com.example.schoolpa.db.SPDB;

public class MessageActivity extends Activity implements View.OnClickListener, TextWatcher {

    private ListView listView;
    private MessageAdapter adapter;

    private Button btnSend;
    private EditText etContent;

    private String messager;

    private PushReceiver pushReceiver = new PushReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String from = intent.getStringExtra(PushReceiver.KEY_FROM);
            if (messager.equalsIgnoreCase(from)) {
                loadData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messager = getIntent().getStringExtra("messager");

        IntentFilter filter = new IntentFilter();
        filter.addAction(PushReceiver.ACTION_TEXT);
        registerReceiver(pushReceiver, filter);

        initView();
        initEvent();
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Account account = ((ChatApplication) getApplication())
                .getCurrentAccount();
        MessageDao dao = new MessageDao(this);
        dao.clearUnread(account.getUserId(), messager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(pushReceiver);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.message_list_view);

        btnSend = (Button) findViewById(R.id.message_btn_send);
        etContent = (EditText) findViewById(R.id.message_et_content);
        btnSend.setEnabled(false);

        adapter = new MessageAdapter(this, null);
        listView.setAdapter(adapter);
    }

    private void initEvent() {
        btnSend.setOnClickListener(this);
        etContent.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        clickSend();
    }


    private void loadData() {
        ChatApplication application = (ChatApplication) getApplication();
        Account account = application.getCurrentAccount();

        MessageDao dao = new MessageDao(this);
        final Cursor cursor = dao.queryMessage(account.getUserId(), messager);
        adapter.changeCursor(cursor);
        listView.post(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    listView.smoothScrollToPositionFromTop(cursor.getCount(), 0);
                } else {
                    listView.smoothScrollToPosition(cursor.getCount());
                }
            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            btnSend.setEnabled(false);
        } else {
            btnSend.setEnabled(true);
        }
    }

    // 发送消息
    private void clickSend() {
        String content = etContent.getText().toString().trim();
        final Account account = ((ChatApplication) getApplication())
                .getCurrentAccount();

        // 存储到本地
        final MessageDao dao = new MessageDao(this);
        final Message msg = new Message();
        msg.setUserId(messager);
        msg.setContent(content);
        msg.setCreateTime(System.currentTimeMillis());
        msg.setDirection(0);
        msg.setOwner(account.getUserId());
        msg.setRead(true);
        msg.setState(1);
        msg.setType(0);
        dao.addMessage(msg);
        // 更新ui
        loadData();

        etContent.setText("");

        // 网络调用
        ChatMessage message = ChatMessage.createMessage(ChatMessage.Type.TEXT);
        message.setAccount(account.getUserId());
        message.setToken(account.getToken());
        message.setReceiver(messager);
        message.setBody(new TextBody(content));
        SPChatManager.getInstance(this).sendMessage(message, new SPChatCallBack() {

            @Override
            public void onSuccess() {
                ToastUtils.showTestShort(getApplicationContext(), "发送成功");

                msg.setState(2);
                dao.updateMessage(msg);
                // 更新ui
                loadData();
            }

            @Override
            public void onProgress(int progress) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int error, String errorString) {
                ToastUtils.showTestShort(getApplicationContext(), "发送失败");

                msg.setState(3);
                dao.updateMessage(msg);
                // 更新ui
                loadData();
            }
        });
    }

    private class MessageAdapter extends CursorAdapter {

        public MessageAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return View.inflate(context, R.layout.activity_message_item, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvTime = (TextView) view
                    .findViewById(R.id.item_message_tv_time);
            View senderView = view.findViewById(R.id.item_message_sender);
            View receiverView = view.findViewById(R.id.item_message_receiver);

            int direction = cursor.getInt(cursor
                    .getColumnIndex(SPDB.Message.COLUMN_DIRECTION));
            long createTime = cursor.getLong(cursor
                    .getColumnIndex(SPDB.Message.COLUMN_CREATE_TIME));
            tvTime.setText(CommonUtil.getDateFormat(createTime));

            if (direction == 0) {
                // 发送
                senderView.setVisibility(View.VISIBLE);
                receiverView.setVisibility(View.GONE);

                ImageView senderIconView = (ImageView) view
                        .findViewById(R.id.item_message_sender_icon);
                TextView senderContentView = (TextView) view
                        .findViewById(R.id.item_message_sender_tv_content);
                ProgressBar pbLoading = (ProgressBar) view
                        .findViewById(R.id.item_message_sender_pb_state);
                ImageView faildView = (ImageView) view
                        .findViewById(R.id.item_message_sender_iv_faild);

                senderContentView.setText(cursor.getString(cursor
                        .getColumnIndex(SPDB.Message.COLUMN_CONTENT)));

                int state = cursor.getInt(cursor
                        .getColumnIndex(SPDB.Message.COLUMN_STATE));

                // 1.正在发送 2.已经成功发送 3.发送失败
                if (state == 1) {
                    pbLoading.setVisibility(View.VISIBLE);
                    faildView.setVisibility(View.GONE);
                } else if (state == 2) {
                    pbLoading.setVisibility(View.GONE);
                    faildView.setVisibility(View.GONE);
                } else {
                    pbLoading.setVisibility(View.GONE);
                    faildView.setVisibility(View.VISIBLE);
                }

            } else {
                // 接收
                senderView.setVisibility(View.GONE);
                receiverView.setVisibility(View.VISIBLE);

                ImageView receiverIconView = (ImageView) view
                        .findViewById(R.id.item_message_receiver_icon);
                TextView receiverContentView = (TextView) view
                        .findViewById(R.id.item_message_receiver_tv_content);

                receiverContentView.setText(cursor.getString(cursor
                        .getColumnIndex(SPDB.Message.COLUMN_CONTENT)));
            }

        }
    }
}
