package com.example.schoolpa.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by admin on 2016/2/20.
 */
public abstract class DefaultAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public boolean hasMore;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public abstract void loadMore();
}
