package com.example.schoolpa.Base;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by admin on 2016/5/30.
 */
public class CursorAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public CursorAdapter(Context context, Cursor cursor) {

    }

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
}
