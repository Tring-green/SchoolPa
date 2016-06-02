package com.example.schoolpa.adapter.Chat;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolpa.base.BaseAdapter;
import com.example.schoolpa.base.BaseViewHolder;
import com.example.schoolpa.fragment.Data.OfficialDocumentData;
import com.example.schoolpa.R;
import com.example.schoolpa.db.SPDB;

/**
 * Created by admin on 2016/5/30.
 */
public class ConversationAdapter extends BaseAdapter {

    private View view;
    private Context mContext;
    private Cursor mCursor;
    private onItemClickListener mListener;
    public ConversationAdapter(Context context, Cursor cursor, onItemClickListener listener) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat_item, parent, false);
        ConversationViewHolder holder = new ConversationViewHolder(view);
        return holder;
    }

    public interface onItemClickListener{
        void onClick(String name);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof ConversationViewHolder) {
            final ConversationViewHolder conversationViewHolder = (ConversationViewHolder) holder;
            conversationViewHolder.mName = mCursor.getString(mCursor
                    .getColumnIndex(SPDB.Conversation.COLUMN_USERID));
            conversationViewHolder.mContent = mCursor.getString(mCursor
                    .getColumnIndex(SPDB.Conversation.COLUMN_CONTENT));

            int unread = mCursor.getInt(mCursor
                    .getColumnIndex(SPDB.Conversation.COLUMN_UNREAD));
            if (unread <= 0) {
                conversationViewHolder.mTvUnread.setVisibility(View.GONE);
                conversationViewHolder.mTvUnread.setText("");
            } else {
                if (unread >= 99) {
                    conversationViewHolder.mTvUnread.setText("99");
                } else {
                    conversationViewHolder.mTvUnread.setText("" + unread);
                }
                conversationViewHolder.mTvUnread.setVisibility(View.VISIBLE);
            }
            conversationViewHolder.mTvName.setText(conversationViewHolder.mName);
            conversationViewHolder.mTvContent.setText(conversationViewHolder.mContent);
            conversationViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(conversationViewHolder.mName);
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ConversationViewHolder extends BaseViewHolder {
        public final View mView;
        public OfficialDocumentData.OfficialDocumentItem mItem;
        public final TextView mTvUnread;
        public final TextView mTvName;
        public final TextView mTvContent;
        public String mName;
        public String mContent;

        public ConversationViewHolder(View view) {
            super(view);
            mView = view;
            mTvUnread = (TextView) view
                    .findViewById(R.id.item_converation_tv_unread);
            mTvName = (TextView) view
                    .findViewById(R.id.item_converation_name);
            mTvContent = (TextView) view
                    .findViewById(R.id.item_converation_content);

        }

    }
}
