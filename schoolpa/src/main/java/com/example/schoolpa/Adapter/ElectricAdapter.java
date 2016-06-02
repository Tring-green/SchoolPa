package com.example.schoolpa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolpa.fragment.Data.ElectricData.ElectricItem;
import com.example.schoolpa.fragment.ElectricFragment.OnListFragmentInteractionListener;
import com.example.schoolpa.R;

import java.util.List;

public class ElectricAdapter extends RecyclerView.Adapter<ElectricAdapter.ViewHolder> {

    private final List<ElectricItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ElectricAdapter(List<ElectricItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_electric_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(holder.mItem.buildingName);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public ElectricItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.tv_buildingname);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mView=" + mView +
                    ", mNameView=" + mNameView +
                    ", mItem=" + mItem +
                    '}';
        }
    }
}
