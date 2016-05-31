package com.example.schoolpa.Adapter.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolpa.Base.BaseAdapter;
import com.example.schoolpa.Base.BaseViewHolder;
import com.example.schoolpa.Fragment.Data.OfficialDocumentData;
import com.example.schoolpa.R;

/**
 * Created by admin on 2016/5/30.
 */
public class ConversationAdapter extends BaseAdapter {

    private View view;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat_item, parent, false);
        ConversationViewHolder holder = new ConversationViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ConversationViewHolder extends BaseViewHolder {
        public final View mView;
        public OfficialDocumentData.OfficialDocumentItem mItem;
        private final TextView mTvUnread;
        private final TextView mTvName;
        private final TextView mTvContent;
        //private final String mName;
        //private final String mContent;

        public ConversationViewHolder(View view) {
            super(view);
            mView = view;
            mTvUnread = (TextView) view
                    .findViewById(R.id.item_converation_tv_unread);
            mTvName = (TextView) view
                    .findViewById(R.id.item_converation_name);
            mTvContent = (TextView) view
                    .findViewById(R.id.item_converation_content);

            //mName = cursor.getString(cursor
            //        .getColumnIndex(HMDB.Conversation.COLUMN_ACCOUNT));
            //mContent = cursor.getString(cursor
            //        .getColumnIndex(HMDB.Conversation.COLUMN_CONTENT));
        }

    }
}
