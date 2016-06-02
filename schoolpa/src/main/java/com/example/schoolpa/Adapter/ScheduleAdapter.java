package com.example.schoolpa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolpa.base.BaseViewHolder;
import com.example.schoolpa.base.DefaultAdapter;
import com.example.schoolpa.fragment.Data.ScheduleData;
import com.example.schoolpa.fragment.ScheduleFragment.OnListFragmentInteractionListener;
import com.example.schoolpa.R;

import java.util.List;


public class ScheduleAdapter extends DefaultAdapter {

    private final List<ScheduleData.ScheduleItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ScheduleAdapter(List<ScheduleData.ScheduleItem> items, OnListFragmentInteractionListener
            listener) {
        mValues = items;
        mListener = listener;
    }

    public void setValues(List<ScheduleData.ScheduleItem> scheduleItems) {
        for (ScheduleData.ScheduleItem scheduleItem : scheduleItems) {
            int pos = scheduleItem.pos;
            mValues.remove(pos);
            mValues.add(pos, scheduleItem);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_schedule_item_common, parent, false);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof CommonViewHolder) {
            final CommonViewHolder commonViewHolder = (CommonViewHolder) holder;
            commonViewHolder.mItem = mValues.get(position);
            commonViewHolder.mContentView.setText(mValues.get(position).couName);

            commonViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(commonViewHolder.mItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void loadMore() {

    }

    public class CommonViewHolder extends BaseViewHolder {
        public final View mView;
        public final TextView mContentView;
        public ScheduleData.ScheduleItem mItem;

        public CommonViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
